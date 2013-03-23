package org.ocram.rest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocram.rest.domain.InfoXml;
import org.ocram.rest.resource.BaseResource;
import org.ocram.rest.resource.InfoResource;
import org.ocram.rest.resource.SubResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Arquillian.class)
public class InfoResourceTest {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);

    @Deployment(testable = false)
    public static Archive<?> createDeployment() {
        
        File[] libs = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.jboss.resteasy:resteasy-cdi")
                .withTransitivity()
                .asFile(); 
        
        return ShrinkWrap.create(WebArchive.class)
                .addClasses(InfoXml.class, InfoResource.class, SubResource.class, BaseResource.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .setWebXML("WEB-INF/web.xml")
                .addAsLibraries(libs);
    }

    
    @Test
    public void testBasicRestDispatching(@ArquillianResource URL base) throws Exception {
        String testRestResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><info><id>23</id><what>last</what></info>";
       
        String urlString = new URL(base, "id23" + "/next/test").toExternalForm();
        
        ClientRequest request = new ClientRequest(urlString);
        request.setHttpMethod("GET");

        // we're expecting a String back
        ClientResponse<String> responseObj = request.get(String.class);

        assertEquals(200, responseObj.getStatus());
        String result = responseObj.getEntity();
        logger.info( "GET: " + result );
        assertEquals(testRestResult, result);
    }
    
    @Test
    public void testMoreRestDispatching(@ArquillianResource URL base) throws Exception {
        String testRestResult = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><info><id>23</id><what>id23:4839</what></info>";
       
        String urlString = new URL(base, "id23" + "/next/id/" + "4839" + "/test").toExternalForm();
        
        ClientRequest request = new ClientRequest(urlString);
        request.setHttpMethod("GET");

        // we're expecting a String back
        ClientResponse<String> responseObj = request.get(String.class);

        assertEquals(200, responseObj.getStatus());
        String result = responseObj.getEntity();
        logger.info( "GET: " + result );
        assertEquals(testRestResult, result);
    }
    
}