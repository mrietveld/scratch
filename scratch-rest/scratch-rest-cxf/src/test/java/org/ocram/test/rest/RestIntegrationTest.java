package org.ocram.test.rest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunAsClient
@RunWith(Arquillian.class)
public class RestIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(RestIntegrationTest.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @Deployment(testable = false, name="async-rest")
    public static Archive<?> createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(TestResource.class)
                .setWebXML("WEB-INF/web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        
        File [] restEasyDeps = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.apache.cxf:cxf-rt-frontend-jaxrs",
                         "org.apache.cxf:cxf-rt-rs-extension-providers",
                         "org.apache.cxf:cxf-api")
                .withTransitivity()
                .asFile();
        war.addAsLibraries(restEasyDeps);
        
        return war;
    }

    @ArquillianResource
    URL deploymentUrl;
    
    
    @Test
    public void testRestPing() throws Exception {
        // normal
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/ping").toExternalForm();
        System.out.println( ">>> " + urlString);
        Response resp = Request.Get(urlString).execute();
        int status = resp.returnResponse().getStatusLine().getStatusCode();
        System.out.println( "<<< " + status );
        assertEquals(204, status);
        
        Thread.sleep(3000);
    }
}