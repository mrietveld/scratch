package org.ocram.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ocram.ws.generated.PingRequest;
import org.ocram.ws.generated.PingResponse;
import org.ocram.ws.generated.TestRequest;
import org.ocram.ws.generated.TestResponse;
import org.ocram.ws.generated.TestServiceClient;
import org.ocram.ws.generated.TestWebService;

public class PingServiceTest {

        protected static URL wsdlURL;
        protected static QName serviceName;
        protected static QName portName;

        private final static Random random = new Random();
        private final static AtomicLong idGen = new AtomicLong(random.nextInt(10000));
        
        static {
           serviceName = new QName("http://services.ws.ocram.org/0.1.0/test", "TestService");
           portName = new QName("http://services.ws.ocram.org/0.1.0/test", "TestServicePort");
        }

        protected static Endpoint ep;
        protected static String address;

        @BeforeClass
        public static void setUp() throws Exception {
           address = "http://localhost:9000/services/TestService";
           wsdlURL = new URL(address + "?wsdl");
           ep = Endpoint.publish(address, new TestWebServiceImpl());
        }

        @AfterClass
        public static void tearDown() {
           try {
              ep.stop();
           } catch (Throwable t) {
              System.out.println("Error thrown: " + t.getMessage());
           }
        }

        /**
         * This test uses wsimport/wsdl2java generated artifacts, both service and
         * SEI
         */
        @Test
        public void testTestWebServiceImpl() throws Exception {
           TestServiceClient tsc = new TestServiceClient(wsdlURL, serviceName);
           TestWebService tws = tsc.getTestServicePort();
           
           String name = UUID.randomUUID().toString();
           long id = idGen.getAndIncrement();
           TestRequest req = new TestRequest();
           req.setId(id);
           req.setRequestName(name);
           req.setRequestSize(name.getBytes().length);
           
           PingRequest pingReq = new PingRequest();
           pingReq.setRequest(req);
           
           PingResponse pingResp = tws.ping(pingReq);
           TestResponse resp = pingResp.getReturn();
           
           assertNotNull( "Null test response", resp );
           assertEquals("Ping name", req.getRequestName(), resp.getRequestName());
           assertNotNull("Ping request id", resp.getRequestId());
           assertEquals("Ping name", req.getId(), resp.getRequestId().longValue());
        }

}
