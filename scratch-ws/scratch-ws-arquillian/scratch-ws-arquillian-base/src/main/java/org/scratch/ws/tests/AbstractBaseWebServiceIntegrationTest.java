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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.SecurityConstants;
import org.scratch.ws.AbstractPingWebServiceImpl;
import org.scratch.ws.generated.PingPlainTextServiceClient;
import org.scratch.ws.generated.PingRequest;
import org.scratch.ws.generated.PingResponse;
import org.scratch.ws.generated.PingSslServiceClient;
import org.scratch.ws.generated.PingWebService;
import org.scratch.ws.generated.PingWebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractBaseWebServiceIntegrationTest {

    private final static Logger logger = LoggerFactory.getLogger(AbstractBaseWebServiceIntegrationTest.class);

    protected static QName [] serviceName = new QName[2];
    protected static QName [] portName = new QName[2];

    private final static Random random = new Random();
    private final static AtomicLong idGen = new AtomicLong(random.nextInt(10000));

    static {
        serviceName[0] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingPlainTextService");
        portName[0] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServicePlainTextPort");
        serviceName[1] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingSslService");
        portName[1] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServiceSslPort");
    }

    private static final String CLIENT_KEYSTORE_PASSWORD = "CLIENT_KEYSTORE_PASSWORD";

    private static void setupTLS(Object port) throws FileNotFoundException, IOException, GeneralSecurityException {
            String keyStoreLoc = "src/main/resources/ssl/client_keystore.jks";
            HTTPConduit httpConduit = (HTTPConduit) ClientProxy.getClient(port).getConduit();

            TLSClientParameters tlsCP = new TLSClientParameters();
            KeyStore keyStore = KeyStore.getInstance("JKS");
            keyStore.load(new FileInputStream(keyStoreLoc), null);
            KeyManager[] myKeyManagers = getKeyManagers(keyStore, CLIENT_KEYSTORE_PASSWORD);
            tlsCP.setKeyManagers(myKeyManagers);

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(keyStoreLoc), null);
            TrustManager[] myTrustStoreKeyManagers = getTrustManagers(trustStore);
            tlsCP.setTrustManagers(myTrustStoreKeyManagers);

            httpConduit.setTlsClientParameters(tlsCP);
        }

        private static TrustManager[] getTrustManagers(KeyStore trustStore) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory fac = TrustManagerFactory.getInstance("RSA");
            fac.init(trustStore);
            return fac.getTrustManagers();
        }

        private static KeyManager[] getKeyManagers(KeyStore keyStore, String keyPassword) throws GeneralSecurityException, IOException {
            KeyManagerFactory fac = KeyManagerFactory.getInstance("RSA");
            fac.init(keyStore, null);
            return fac.getKeyManagers();
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

    public void pingServiceTest( URL deploymentUrl ) throws PingWebServiceException, FileNotFoundException, IOException, GeneralSecurityException {
        URL wsdlURL = new URL(deploymentUrl, "ws/PingService?wsdl");
        PingPlainTextServiceClient pptsc = new PingPlainTextServiceClient(wsdlURL, serviceName[0]);
        PingWebService pws = pptsc.getPingServicePlainTextPort();

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
        URL sslWsdlUrl = new URL( "https", deploymentUrl.getHost(), 8443, wsdlURL.getFile() + "ws/ssl/PingService?wsdl" );
        PingSslServiceClient pssc = new PingSslServiceClient(sslWsdlUrl, serviceName[1]);
        pws = pssc.getPingServiceSslPort();
        setupTLS(pws);

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