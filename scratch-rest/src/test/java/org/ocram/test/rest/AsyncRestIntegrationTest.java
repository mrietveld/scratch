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
import org.ocram.test.rest.async.AsyncJobObserverExecutor;
import org.ocram.test.rest.async.AsyncJobRequest;
import org.ocram.test.rest.async.AsyncTestResource;
import org.ocram.test.rest.context.ContextResource;
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
                .addClasses(AsyncJobRequest.class, AsyncJobObserverExecutor.class)
                .setWebXML("WEB-INF/web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @ArquillianResource
    URL deploymentUrl;
    
    
    @Test
    public void testAsynchronousRestFramework() throws Exception {
        // normal
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/async/test").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        System.out.println( ">>> " + request.getUri());
        logger.info("BEF: " + sdf.format(new Date(System.currentTimeMillis())));
        ClientResponse<String> responseObj = request.post();
        logger.info("AFT: " + sdf.format(new Date(System.currentTimeMillis())));
        System.out.println( "<<< " + responseObj.getStatus() );
        assertEquals(202, responseObj.getStatus());
        
        Thread.sleep(3000);
    }
}