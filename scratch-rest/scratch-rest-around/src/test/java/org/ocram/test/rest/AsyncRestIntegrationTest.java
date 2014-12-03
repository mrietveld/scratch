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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocram.test.rest.around.AsyncJobRequest;
import org.ocram.test.rest.around.AsyncTestResource;
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
                .addClasses(AsyncTestResource.class, AsyncJobRequest.class)
                .setWebXML("WEB-INF/web.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
        
        File [] restEasyDeps = Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve("org.jboss.resteasy:jaxrs-api",
                         "org.jboss.resteasy:resteasy-jaxrs")
                .withTransitivity()
                .asFile();
        war.addAsLibraries(restEasyDeps);
        
        return war;
    }

    @ArquillianResource
    URL deploymentUrl;
    
    
    @Test
    public void testAsynchronousRestFramework() throws Exception {
        // normal
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/async/ping").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        System.out.println( ">>> " + request.getUri());
        logger.info("BEF: " + sdf.format(new Date(System.currentTimeMillis())));
        ClientResponse<String> responseObj = request.get();
        logger.info("AFT: " + sdf.format(new Date(System.currentTimeMillis())));
        System.out.println( "<<< " + responseObj.getStatus() );
        assertEquals(202, responseObj.getStatus());
        
        Thread.sleep(3000);
    }
}