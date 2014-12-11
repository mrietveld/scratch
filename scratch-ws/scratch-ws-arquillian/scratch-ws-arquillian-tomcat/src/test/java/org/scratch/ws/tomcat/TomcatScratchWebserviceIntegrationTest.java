/*
 * JBoss, Home of Professional Open Source
 * 
 * Copyright 2012, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.scratch.ws.tomcat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.scratch.ws.tomcat.ScratchWsWarTomcatDeploy.createTestWar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jsoup.Connection.Request;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.scratch.ws.AbstractPingWebServiceImpl;
import org.scratch.ws.generated.PingRequest;
import org.scratch.ws.generated.PingResponse;
import org.scratch.ws.generated.PingServiceClient;
import org.scratch.ws.generated.PingWebService;
import org.scratch.ws.generated.PingWebServiceException;

@RunAsClient
@RunWith(Arquillian.class)
public class TomcatScratchWebserviceIntegrationTest {

    protected static QName serviceName;
    protected static QName portName;

    private final static Random random = new Random();
    private final static AtomicLong idGen = new AtomicLong(random.nextInt(10000));

    static {
        serviceName = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingService");
        portName = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServicePlainTextPort");
    }

    @Deployment(testable = false, name = "tomcat")
    public static Archive<?> createWar() {
        return createTestWar();
    }

    @ArquillianResource
    URL deploymentUrl;

    private PingRequest createRequest() { 
        String name = UUID.randomUUID().toString();
        long id = idGen.getAndIncrement();
        PingRequest req = new PingRequest();
        req.setId(id);
        req.setRequestName(name);
        req.setRequestSize(name.getBytes().length);
        return req;
    }
    
    @Test
    public void webserviceTest() throws PingWebServiceException, MalformedURLException {
        URL wsdlURL = new URL(deploymentUrl, "ws/PingService?wsdl");
        PingServiceClient tsc = new PingServiceClient(wsdlURL, serviceName);
        PingWebService tws = tsc.getPingServicePlainTextPort();

        PingRequest req = createRequest();
        PingResponse resp = tws.ping(req);

        assertNotNull("Null ping response", resp);
        assertEquals("Ping name", req.getRequestName(), resp.getRequestName());
        assertNotNull("Ping request id", resp.getRequestId());
        assertEquals("Ping name", req.getId(), resp.getRequestId().longValue());
    }

}