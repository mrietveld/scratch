package org.jbpm.designer.assets.rest;

import static junit.framework.Assert.assertEquals;

import java.net.URL;

import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jbpm.designer.assets.domain.AssetXml;
import org.jbpm.designer.assets.persistence.AssetEntity;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AssetResourceBeanTest {
    

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {

        return ShrinkWrap.create(WebArchive.class)
                .addClasses(AssetXml.class, AssetEntity.class, AssetResource.class, AssetResourceBean.class)
                .addAsResource("META-INF/war-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsManifestResource("WEB-INF/context.xml", "context.xml")
                .setWebXML("WEB-INF/web.xml");
    }

    String testRestResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><asset><id>23</id><processId>illuminate</processId><type>test</type><content>aQ==</content></asset>";
    
    @Test
    public void testRestServiceTest(@ArquillianResource URL base) throws Exception {
       
        String urlString = new URL(base, "asset" + "/test").toExternalForm();
        
        ClientRequest request = new ClientRequest(urlString);
        request.setHttpMethod("GET");

        // we're expecting a String back
        ClientResponse<String> responseObj = request.get(String.class);

        assertEquals(200, responseObj.getStatus());
        String result = responseObj.getEntity();
        assertEquals(testRestResult, result);
    }
    
    @Test
    public void testCreateAndRetrieveAsset(@ArquillianResource URL base) throws Exception {
        
        byte [] content = "testContentForExampleAPictureOrAFormOrWhatever.".getBytes();
        AssetXml xml = new AssetXml();
        xml.setProcessId("23");
        xml.setContent(content);
        // id is set by server
        
        String urlString = new URL(base, "asset/").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        request.setHttpMethod("POST");
        request.header("Accept", MediaType.APPLICATION_XML);
        request.body(MediaType.APPLICATION_XML, xml);

        // we're expecting a String back
        ClientResponse<String> responseObj = request.execute();

        assertEquals(200, responseObj.getStatus());
        assertEquals(testRestResult, responseObj.getEntity());
    }
    
}