package org.scratch.ws;

import static org.junit.Assert.*;
import static org.scratch.ws.config.ScratchWsCxfServlet.setupPingServiceEndpoint;

import java.net.URL;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPFaultException;

import org.apache.cxf.ws.security.SecurityConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scratch.ws.generated.PingPlainTextServiceClient;
import org.scratch.ws.generated.PingRequest;
import org.scratch.ws.generated.PingResponse;
import org.scratch.ws.generated.PingSslServiceClient;
import org.scratch.ws.generated.PingWebService;

public class PingServiceTest {

    protected static URL[] wsdlURL = new URL[2];
    protected static QName [] serviceName= new QName[2];
    protected static QName[] portName = new QName[2];

    private final static Random random = new Random();
    private final static AtomicLong idGen = new AtomicLong(random.nextInt(10000));

    static {
        serviceName[0] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingPlainTextService");
        serviceName[1] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingSslService");
        portName[0] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServicePlainTextPort");
        portName[1] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServiceSslPort");
    }

    protected static Endpoint[] eps = new Endpoint[2];

    @BeforeClass
    public static void setUp() throws Exception {
        {
            String address = "http://localhost:9000/ws/PingService";
            URL url = PingServiceTest.class.getResource("/wsdl/PingService.wsdl");
            assertNotNull("Null URL for wsdl resource", url);
            PingWebService webServiceImpl = new PingWebServicePlainTextImpl();
            eps[0] = setupPingServiceEndpoint(url, address, webServiceImpl);
            wsdlURL[0] = new URL(address + "?wsdl");
        }
        {
            String address = "http://localhost:9001/ws/PingService";
            URL url = PingServiceTest.class.getResource("/wsdl/PingService.wsdl");
            assertNotNull("Null URL for wsdl resource", url);
            PingWebService webServiceImpl = new PingWebServicePlainTextImpl();
            eps[1] = setupPingServiceEndpoint(url, address, webServiceImpl);
            wsdlURL[1] = new URL(address + "?wsdl");
        }
    }

    @AfterClass
    public static void tearDown() {
        for( Endpoint ep : eps ) {
            try {
                ep.stop();
            } catch( Throwable t ) {
                System.out.println("Error thrown: " + t.getMessage());
            }
        }
    }

    private PingPlainTextServiceClient getPingPlainTextServiceClient( URL wsdlURL ) { 
        return new PingPlainTextServiceClient(wsdlURL, serviceName[0]);
    }

    private PingSslServiceClient getPingSslServiceClient( ) {
        return new PingSslServiceClient(wsdlURL[1], serviceName[1]);
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

    @Test
    public void testPlainPingWebServiceImpl() throws Exception {
        // setup
        PingPlainTextServiceClient psc = getPingPlainTextServiceClient(wsdlURL[0]);
        PingWebService pws = psc.getPingServicePlainTextPort();
        BindingProvider bindingProxy = (BindingProvider) pws;
        
        // do request without auth
        PingRequest req = createRequest();
        PingResponse resp = null;
        try { 
            resp = pws.ping(req);
            fail("The WS call should not have succeeded without authentication");
        } catch( SOAPFaultException soapfe ) { 
            assertTrue( soapfe.getMessage().contains("No username") );
        }

        // setup auth
        bindingProxy.getRequestContext().put(SecurityConstants.USERNAME, "mary");
        bindingProxy.getRequestContext().put(SecurityConstants.PASSWORD, "mary123@");
       
        // request with auth
        resp = pws.ping(req);
       
        // test response
        assertNotNull("Null ping response", resp);
        assertEquals("Ping name", req.getRequestName(), resp.getRequestName());
        assertNotNull("Ping request id", resp.getRequestId());
        assertEquals("Ping name", req.getId(), resp.getRequestId().longValue());
    }

    @Test
    public void testSslPingWebServiceImpl() throws Exception {
        PingSslServiceClient tsc = getPingSslServiceClient();
        PingWebService tws = tsc.getPingServiceSslPort();

        PingRequest req = createRequest();
        try {
            PingResponse resp = tws.ping(req);
            fail("Ping should have failed due to no SSL authentication");
        } catch( SOAPFaultException soapfe ) {
            soapfe.printStackTrace();
            String msg = soapfe.getMessage();
            assertTrue(msg, msg.contains("Not an HTTPs connection"));
        }
    }
}