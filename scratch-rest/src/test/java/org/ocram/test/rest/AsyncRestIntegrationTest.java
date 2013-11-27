package org.ocram.test.rest;

import static junit.framework.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunAsClient
@RunWith(Arquillian.class)
public class AsyncRestIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(ContextResource.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @Deployment(testable = false, name="async-rest")
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AsyncTestResource.class)
                .addClass(AsyncPreProcessInterceptor.class)
                .setWebXML("WEB-INF/async-web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;
    
    @Test
    public void testSimpleTest() throws Exception {
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/test/").toExternalForm();
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
        
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/test/context?id=wakka").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        System.out.println( ">>> " + request.getUri());
        
        request.header("Accept", MediaType.APPLICATION_XML);
        String output = serialize(inputObject);
        request.body(MediaType.APPLICATION_XML, output);

        // we're expecting a String back
        ClientResponse<String> responseObj = request.post(String.class);
        System.out.println( "<<< " + responseObj.getStatus() );

        JaxbTestInput outputObject = deserialize(responseObj.getEntity());
        assertEquals(200, responseObj.getStatus());
        System.out.println("OUT: " + outputObject.getObjects().get(0).getClass().getName());
    }
    
    private String serialize(JaxbTestInput input) throws Exception {  
        JAXBContext jaxbContext = JAXBContext.newInstance(
                JaxbTestInput.class, 
                MyType.class
                );
        Marshaller marshaller = jaxbContext.createMarshaller();
        StringWriter stringWriter = new StringWriter();

        marshaller.marshal(input, stringWriter);
        String output = stringWriter.toString();

        return output;
    }
    
    private JaxbTestInput deserialize(String input) throws Exception {  
        JAXBContext jaxbContext = JAXBContext.newInstance(
                JaxbTestInput.class, 
                MyType.class
                );
        
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        ByteArrayInputStream xmlStrInputStream = new ByteArrayInputStream(input.getBytes(Charset.forName("UTF-8")));

        JaxbTestInput jaxbObj = (JaxbTestInput) unmarshaller.unmarshal(xmlStrInputStream);

        return jaxbObj;
    }
    
    @Test
    public void testAsynchronousRestFramework() throws Exception {
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/test/async?oneway=true").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        System.out.println( ">>> " + request.getUri());
        
        logger.info("BEF: " + sdf.format(new Date(System.currentTimeMillis())));
        ClientResponse<String> responseObj = request.post();
        logger.info("AFT: " + sdf.format(new Date(System.currentTimeMillis())));
        System.out.println( "<<< " + responseObj.getStatus() );

        assertEquals(202, responseObj.getStatus());
    }
}