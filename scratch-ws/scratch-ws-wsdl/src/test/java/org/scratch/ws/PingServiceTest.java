package org.scratch.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.scratch.ws.config.ScratchWsCxfServlet.setupPingServiceEndpoint;

import java.net.URL;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Endpoint;

import org.apache.cxf.ws.security.SecurityConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.scratch.ws.generated.PingRequest;
import org.scratch.ws.generated.PingResponse;
import org.scratch.ws.generated.PingServiceClient;
import org.scratch.ws.generated.PingWebService;

public class PingServiceTest {

        protected static URL [] wsdlURL = new URL[2];
        protected static QName serviceName;
        protected static QName [] portName = new QName[2];

        private final static Random random = new Random();
        private final static AtomicLong idGen = new AtomicLong(random.nextInt(10000));
        
        static {
           serviceName = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingService");
           portName[0] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServicePlainTextPort");
           portName[1] = new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingServiceSslPort");
        }

        protected static Endpoint [] eps = new Endpoint [2];

        @BeforeClass
        public static void setUp() throws Exception {
            { 
                String address = "http://localhost:9000/ws/PingService";
                URL url = PingServiceTest.class.getResource("/wsdl/PingService.wsdl");
                assertNotNull("Null URL for wsdl resource", url);
                PingWebService webServiceImpl = new PingWebServicePlainTextImpl();
                eps[0] = setupPingServiceEndpoint(url, address, "PingServicePlainTextPort", webServiceImpl);
                wsdlURL[0] = new URL(address + "?wsdl");
            }
            {
                String address = "http://localhost:9000/ws/ssl/PingService";
                URL url = PingServiceTest.class.getResource("/wsdl/PingService.wsdl");
                assertNotNull("Null URL for wsdl resource", url);
                PingWebService webServiceImpl = new PingWebServiceSimpleSslImpl();
                eps[1] = setupPingServiceEndpoint(url, address, "PingServiceSslPort", webServiceImpl);
                wsdlURL[1] = new URL(address + "?wsdl");
            }
        }

        
        @AfterClass
        public static void tearDown() {
            for( Endpoint ep : eps ) { 
                try {
                    ep.stop();
                } catch (Throwable t) {
                    System.out.println("Error thrown: " + t.getMessage());
                }
            }
        }

        @Test
        public void testPlainPingWebServiceImpl() throws Exception {
           PingServiceClient tsc = new PingServiceClient(wsdlURL[0], serviceName);
           PingWebService tws = tsc.getPingServicePlainTextPort();
           BindingProvider bindingProxy = (BindingProvider) tws;
           bindingProxy.getRequestContext().put(SecurityConstants.USERNAME, "mary");
           bindingProxy.getRequestContext().put(SecurityConstants.PASSWORD, "mary123@");
           
           String name = UUID.randomUUID().toString();
           long id = idGen.getAndIncrement();
           PingRequest req = new PingRequest();
           req.setId(id);
           req.setRequestName(name);
           req.setRequestSize(name.getBytes().length);
           
           PingResponse resp = tws.ping(req);
           
           assertNotNull( "Null ping response", resp );
           assertEquals("Ping name", req.getRequestName(), resp.getRequestName());
           assertNotNull("Ping request id", resp.getRequestId());
           assertEquals("Ping name", req.getId(), resp.getRequestId().longValue());
        }

        @Test
        public void testSslPingWebServiceImpl() throws Exception {
           PingServiceClient tsc = new PingServiceClient(wsdlURL[1], serviceName);
           PingWebService tws = tsc.getPingServiceSslPort();
           
           String name = UUID.randomUUID().toString();
           long id = idGen.getAndIncrement();
           PingRequest req = new PingRequest();
           req.setId(id);
           req.setRequestName(name);
           req.setRequestSize(name.getBytes().length);
           
           PingResponse resp = tws.ping(req);
           
           assertNotNull( "Null ping response", resp );
           assertEquals("Ping name", req.getRequestName(), resp.getRequestName());
           assertNotNull("Ping request id", resp.getRequestId());
           assertEquals("Ping name", req.getId(), resp.getRequestId().longValue());
        }
}