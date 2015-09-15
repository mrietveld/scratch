package org.ocram.test.rest;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.text.SimpleDateFormat;

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

        return war;
    }

    @ArquillianResource
    URL deploymentUrl;


    @Test
    public void testRestResources() throws Exception {
        // normal
        String urlString = new URL(deploymentUrl,  deploymentUrl.getPath() + "rest/test/ping").toExternalForm();
        ClientRequest request = new ClientRequest(urlString);
        ClientResponse<String> responseObj = request.get();
        assertEquals(200, responseObj.getStatus());

        Thread.sleep(3000);
    }
}