/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jbpm.designer.assets.servlet;

import static junit.framework.Assert.*;
import static org.jbpm.designer.assets.test.TestUtil.*;

import java.io.*;
import java.net.URL;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.jbpm.designer.assets.exception.AssetProcessingException;
import org.jbpm.designer.assets.persistence.AssetEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class AssetResourceServletDeploymentTest {

    @Deployment(testable = false)
    public static WebArchive getTestArchive() {
        Properties properties = new Properties();
        try {
            InputStream arquillianLaunchFile = AssetResourceServletDeploymentTest.class.getResourceAsStream("/arquillian.launch");
            properties.load(arquillianLaunchFile);
        } catch (IOException e) {
            // do nada
        }

        MavenDependencyResolver resolver = DependencyResolvers.use(MavenDependencyResolver.class).loadMetadataFromPom("pom.xml");

        WebArchive war = null;
        if (properties.containsKey("glassfish-embedded")) {
            war = ShrinkWrap.create(WebArchive.class).addPackage(AssetProcessingException.class.getPackage()) // exception
                    .addPackages(true, AssetEntity.class.getPackage()) // persistence
                    .addPackage(AssetResourceServlet.class.getPackage()) // servlet
                    .addAsResource("persistence.xml", "META-INF/persistence.xml").addAsWebInfResource("WEB-INF/web.xml", "web.xml");
        } else if (properties.containsKey("jbossas-7")) {
            war = ShrinkWrap.create(WebArchive.class).addPackage(AssetProcessingException.class.getPackage())
                    // exception
                    .addPackages(true, AssetEntity.class.getPackage())
                    // persistence
                    .addPackage(AssetResourceServlet.class.getPackage())
                    // servlet
                    .addAsResource("persistence.xml", "META-INF/persistence.xml").addAsWebInfResource("WEB-INF/web.xml", "web.xml")
                    // jboss specific (because it's not embedded.. :( )
                    .addAsWebInfResource("jbossas-ds.xml")
                    .addAsLibraries(resolver.artifacts("commons-fileupload:commons-fileupload:1.2.2").resolveAsFiles());
        } else {
            properties.list(System.out);
            throw new RuntimeException("No property specifying which arquillian container to start.");
        }

        return war;
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testPostMultiPart() throws Exception {
        URL url = this.getClass().getResource("/all_seeing_eye.png");
        assertNotNull("URL is null", url);
        File contentFile = new File(url.getPath());

        postMultiPart("23", "image", "{ ninja, cowboy, alien }", contentFile);
    }

    private long postMultiPart(String processId, String type, String tags, File contentFile) throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(deploymentUrl + "/asset");

        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        reqEntity.addPart("processId", new StringBody(processId));
        reqEntity.addPart("type", new StringBody(type));
        reqEntity.addPart("tags", new StringBody(tags));
        reqEntity.addPart("content", new FileBody(contentFile));
        post.setEntity(reqEntity);

        HttpEntity responseEntity = client.execute(post).getEntity();
        assertNotNull("Response entity is null.", responseEntity);
        String assetIdString = convertToStringAndClose(responseEntity.getContent());

        long assetId = 0;
        try {
            assetId = Long.parseLong(assetIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            fail(assetIdString);
        }
        assertTrue("Asset id not greater than 0: " + assetIdString, assetId > 0);
        return assetId;
    }

    @Test
    public void testPostMultiPartAndGet() throws Exception {
        URL url = this.getClass().getResource("/all_seeing_eye.png");
        assertNotNull("URL is null", url);
        File contentFile = new File(url.getPath());

        String processId = "24";
        String type = "IMAGE";
        String tags = "{ ninja, samurai, monk }";

        long assetId = postMultiPart(processId, type, tags, contentFile);
        
        AssetEntity asset = retrieveAssetViaGet(assetId);
        
        checkReceivedAsset(processId, type, tags, contentFile, asset);
    }
    
    @Test
    public void testPostBadOrderMultiPartAndGet() throws Exception {
        URL url = this.getClass().getResource("/all_seeing_eye.png");
        assertNotNull("URL is null", url);
        File contentFile = new File(url.getPath());

        String processId = "24";
        String type = "IMAGE";
        String tags = "{ ninja, samurai, monk }";

        // Post bad order
        // ---
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(deploymentUrl + "/asset");

        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        // content is not last
        reqEntity.addPart("content", new FileBody(contentFile));
        reqEntity.addPart("processId", new StringBody(processId));
        reqEntity.addPart("type", new StringBody(type));
        reqEntity.addPart("tags", new StringBody(tags));
        post.setEntity(reqEntity);

        HttpEntity responseEntity = client.execute(post).getEntity();
        assertNotNull("Response entity is null.", responseEntity);
        String assetIdString = convertToStringAndClose(responseEntity.getContent());

        long assetId = 0;
        try {
            assetId = Long.parseLong(assetIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
            fail(assetIdString);
        }
        assertTrue("Asset id not greater than 0: " + assetIdString, assetId > 0);
        // -- end post
        
        AssetEntity asset = retrieveAssetViaGet(assetId);
        
        checkReceivedAsset(processId, type, tags, contentFile, asset);
    }
    
    private void checkReceivedAsset(String processId, String type, String tags, File contentFile, AssetEntity asset) throws Exception { 
        assertEquals(processId, asset.getProcessId());
        assertEquals(type, asset.getType());
        List<String> inputTags = parseJsonTagString(tags);
        Iterator<String> iter = inputTags.iterator();
        while(iter.hasNext()) { 
            String tag = iter.next();
           assertTrue("'" + tag + "' not present in tagStrings.", asset.getTagStrings().contains(tag));
           iter.remove();
        }
        assertEquals(0, inputTags.size());
        
        byte [] origContent = convertFileToByteArray(contentFile);
        assertTrue("Retrieved content is empty.", asset.getContent().length > 0 );
        assertTrue("Content of original and retrieved is not identical", Arrays.equals(origContent, asset.getContent()));
    }
    
    private AssetEntity retrieveAssetViaGet(long assetId) throws Exception { 
        HttpClient client = new DefaultHttpClient();
        
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http")
            .setHost(deploymentUrl.getHost())
            .setPort(deploymentUrl.getPort())
            .setPath(deploymentUrl.getPath() + "asset")
            .setParameter("assetId", Long.toString(assetId));
       
        HttpGet get = new HttpGet(builder.build());
        System.out.println( "builder: " + get.getURI());
        System.out.println( "deployment: " + deploymentUrl);
       
        HttpResponse response = client.execute(get);
        assertNotNull("Response is null.", response);
        HttpEntity responseEntity = response.getEntity();
        assertNotNull("Response entity is null.", responseEntity);
        
        Header [] header = response.getAllHeaders();
        HashMap<String, String> headerFields = new HashMap<String, String>();
        for( int i = 0; i < header.length; ++i ) { 
            headerFields.put(header[i].getName(), header[i].getValue());
        }
        
        AssetEntity asset = new AssetEntity();
        asset.setProcessId(headerFields.get("processId"));
        asset.setType(headerFields.get("type"));
        asset.setType(headerFields.get("type"));
        String jsonTagString = headerFields.get("tags");
        assertNotNull(jsonTagString);
        asset.getTagStrings().addAll(parseJsonTagString(jsonTagString));
       
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        response.getEntity().writeTo(baos);
        asset.setContent(baos.toByteArray());
        
        return asset;
    }
    
    private List<String> parseJsonTagString(String jsonTagString) { 
        List<String> tags = new ArrayList<String>();
        Scanner scan = new Scanner(jsonTagString).useDelimiter("\\s*[{},]\\s*");
        while (scan.hasNext("\\S+")) { // avoids empty sets
            tags.add(scan.next());
        }
        return tags;
    }
}
