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
package org.scratch.ws.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.ws.security.SecurityConstants;
import org.scratch.ws.AbstractPingWebServiceImpl;
import org.scratch.ws.generated.PingRequest;
import org.scratch.ws.generated.PingResponse;
import org.scratch.ws.generated.PingServiceClient;
import org.scratch.ws.generated.PingWebService;
import org.scratch.ws.generated.PingWebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractBaseWebServiceIntegrationTest {

    private final static Logger logger = LoggerFactory.getLogger(AbstractBaseWebServiceIntegrationTest.class);
    
    protected static QName serviceName;
    protected static QName portName;

    private final static Random random = new Random();
    private final static AtomicLong idGen = new AtomicLong(random.nextInt(10000));

    static {
        serviceName = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingService");
        portName = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServicePlainTextPort");
    }

    private PingRequest createRequest() {
        String name = UUID.randomUUID().toString();
        long id = idGen.getAndIncrement();
        PingRequest req = new PingRequest();
        req.setId(id);
        req.setRequestName(name);
        req.setRequestSize(name.getBytes().length);
        return req;
    }

    public void plainTextServiceTest( URL deploymentUrl ) throws PingWebServiceException, MalformedURLException {
        URL wsdlURL = new URL(deploymentUrl, "ws/PingService?wsdl");
        PingServiceClient psc = new PingServiceClient(wsdlURL, serviceName);
        PingWebService pws = psc.getPingServicePlainTextPort();

        PingRequest req = createRequest();
        PingResponse resp = null;
        try {
            resp = pws.ping(req);
            fail("There should have been an authentication fault!");
        } catch( SOAPFaultException soapfe ) {
            logger.error( "SOAP fault thrown: " + soapfe.getMessage(), soapfe );
            assertTrue( "Incorrect exception message: " + soapfe.getMessage(), soapfe.getMessage().contains("username"));
            // do nothing
        }

        // setup auth
        Map<String, Object> reqCtx = ((BindingProvider) pws).getRequestContext();
        reqCtx.put(SecurityConstants.USERNAME, "mary");
        reqCtx.put(SecurityConstants.PASSWORD, "mary123@");

        resp = pws.ping(req);

        assertNotNull("Null ping response", resp);
        assertEquals("Ping name", req.getRequestName(), resp.getRequestName());
        assertNotNull("Ping request id", resp.getRequestId());
        assertEquals("Ping name", req.getId(), resp.getRequestId().longValue());

        // SSL test
        pws = psc.getPingServiceSslPort();

        req = createRequest();

        reqCtx = ((BindingProvider) pws).getRequestContext();
        reqCtx.put(SecurityConstants.USERNAME, "mary");
        reqCtx.put(SecurityConstants.PASSWORD, "mary123@");

        try {
            resp = pws.ping(req);
            fail("There should have been an authentication fault related to SSL!");
        } catch( SOAPFaultException soapfe ) {
            logger.error( "SOAP fault thrown: " + soapfe.getMessage(), soapfe );
            assertTrue( "Incorrect exception message: " + soapfe.getMessage(), soapfe.getMessage().toLowerCase().contains("https "));
            // do nothing
        }
    }

}