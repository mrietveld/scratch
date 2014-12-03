package org.ocram.test.rest;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocram.test.rest.async.AsyncTestResource;
import org.ocram.test.rest.async.JobRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunAsClient
@RunWith(Arquillian.class)
public class AsyncRestIntegrationTest {
    
    private static final Logger logger = LoggerFactory.getLogger(AsyncRestIntegrationTest.class);
    
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @Deployment(testable = false, name="async-rest")
    public static Archive<?> createDeployment() {
        WebArchive war = ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(AsyncTestResource.class, JobRequestProcessor.class)
                .setWebXML("WEB-INF/web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
       
        if( false ) { 
            File [] depsToAdd = Maven.resolver()
                    .loadPomFromFile("pom.xml")
                    .resolve("org.jboss.resteasy:resteasy-jaxb-provider")
                    .withoutTransitivity()
                    .asFile();
            war.addAsLibraries(depsToAdd);
        }
        
        return war;
    }

    @ArquillianResource
    URL deploymentUrl;
    
    
    @Test
    @Ignore
    public void testAsynchronousRestFramework() throws Exception {
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/async/ping").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        System.out.println( ">>> " + request.getUri());
        logger.info("BEF: " + sdf.format(new Date(System.currentTimeMillis())));
        request.formParameter("test", "ocram");
        request.queryParameter("other-test", "evil marco");
        ClientResponse<String> responseObj = request.post();
        assertEquals("Ping failed!", 200, responseObj.getStatus());

        // normal
        urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/async/test?comet").toExternalForm();
        request = new ClientRequest(urlString);
        System.out.println( sdf.format(new Date(System.currentTimeMillis())) + " TEST >>> " + request.getUri());
        responseObj = request.post();
        System.out.println( sdf.format(new Date(System.currentTimeMillis())) + " TEST <<< " + responseObj.getStatus());
        assertEquals(202, responseObj.getStatus());
        Thread.sleep(10*000);
        
        Thread.sleep(3000);
        request.formParameter("test", "ocram");
        request.queryParameter("other-test", "evil marco");
        responseObj = request.post();
        assertEquals("Ping failed!", 200, responseObj.getStatus());

        // normal
        urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/async/test?comet").toExternalForm();
        request = new ClientRequest(urlString);
        System.out.println( sdf.format(new Date(System.currentTimeMillis())) + " TEST >>> " + request.getUri());
        responseObj = request.post();
        System.out.println( sdf.format(new Date(System.currentTimeMillis())) + " TEST <<< " + responseObj.getStatus());
        assertEquals(202, responseObj.getStatus());
        Thread.sleep(10*000);
    }

}
