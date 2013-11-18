package org.ocram.test.rest;

import static junit.framework.Assert.assertEquals;

import java.net.URL;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.MediaType;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocram.test.domain.JaxbTestInput;

@RunAsClient
@RunWith(Arquillian.class)
public class ContextResolverIntegrationTest {
    
    @Deployment(testable = false, name="context-rest")
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(JaxbTestInput.class, TestResource.class)
                .setWebXML("WEB-INF/web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    public void testCreateAndRetrieveAsset() throws Exception {
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/test/ping").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        System.out.println( ">>> " + request.getUri());
        // we're expecting a String back
        ClientResponse responseObj = request.get();
        System.out.println( "<<< " + responseObj.getStatus() );
        
        JaxbTestInput inputObject = new JaxbTestInput();
        inputObject.setId(23l);
        inputObject.setName("test");
        
        urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/test/context").toExternalForm();
        request = new ClientRequest(urlString);
        System.out.println( ">>> " + request.getUri());
        
        request.header("Accept", MediaType.APPLICATION_XML);
        request.body(MediaType.APPLICATION_XML, inputObject);

        // we're expecting a String back
        responseObj = request.post(String.class);
        System.out.println( "<<< " + responseObj.getStatus() );

        assertEquals(200, responseObj.getStatus());
        System.out.println("OUT: " + responseObj.getEntity(String.class));
    }
    
}