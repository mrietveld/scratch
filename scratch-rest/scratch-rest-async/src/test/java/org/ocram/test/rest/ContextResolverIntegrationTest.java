package org.ocram.test.rest;

import static junit.framework.Assert.assertEquals;

import java.io.StringWriter;
import java.net.URL;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

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
import org.ocram.test.domain.MyType;
import org.ocram.test.rest.context.ContextResource;
import org.ocram.test.rest.context.DeploymentClassGatherer;
import org.ocram.test.rest.context.JaxbContextResolver;

@RunAsClient
@RunWith(Arquillian.class)
public class ContextResolverIntegrationTest {

    @Deployment(testable = false, name = "context-rest")
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(JaxbTestInput.class, ContextResource.class)
                .addClasses(JaxbContextResolver.class, MyType.class, DeploymentClassGatherer.class)
                .setWebXML("WEB-INF/web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;

    @Test
    public void testSimpleTest() throws Exception {
        String urlString = new URL(deploymentUrl, deploymentUrl.getPath() + "rest/context/ping").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        // we're expecting a String back
        ClientResponse responseObj = request.get();
        assertEquals("REST call status is incorrect.", 204, responseObj.getStatus());
    }

    @Test
    public void testCreateAndRetrieveAsset() throws Exception {

        JaxbTestInput inputObject = new JaxbTestInput();
        inputObject.setId(23l);
        inputObject.setName("test");
        inputObject.getObjects().add(new MyType("wakka", 49));

        String urlString = new URL(deploymentUrl, deploymentUrl.getPath() + "rest/context/test").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        System.out.println(">>> " + request.getUri());

        request.header("Accept", MediaType.APPLICATION_XML);
        String output = serialize(inputObject);
        request.body(MediaType.APPLICATION_XML, output);

        // we're expecting a String back
        ClientResponse<JaxbTestInput> responseObj = request.post(JaxbTestInput.class);
        System.out.println("<<< " + responseObj.getStatus());

        assertEquals(200, responseObj.getStatus());
        System.out.println("OUT: " + responseObj.getEntity(JaxbTestInput.class).getObjects().get(0).getClass().getName());
    }

    private String serialize(JaxbTestInput input) throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(JaxbTestInput.class, MyType.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();

        marshaller.marshal(input, stringWriter);
        String output = stringWriter.toString();

        return output;
    }

}