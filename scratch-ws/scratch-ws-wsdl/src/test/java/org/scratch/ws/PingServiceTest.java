package org.scratch.ws;

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
import org.scratch.ws.generated.PingRequest;
import org.scratch.ws.generated.PingResponse;
import org.scratch.ws.generated.PingServiceClient;
import org.scratch.ws.generated.PingWebService;

public class PingServiceTest {

        protected static URL wsdlURL;
        protected static QName serviceName;
        protected static QName portName;

        private final static Random random = new Random();
        private final static AtomicLong idGen = new AtomicLong(random.nextInt(10000));
        
        static {
           serviceName = new QName(PingWebServiceImpl.NAMESPACE, "PingService");
           portName = new QName(PingWebServiceImpl.NAMESPACE, "PingServicePort");
        }

        protected static Endpoint ep;
        protected static String address;

        @BeforeClass
        public static void setUp() throws Exception {
           address = "http://localhost:9000/services/PingService";
           wsdlURL = new URL(address + "?wsdl");
           ep = Endpoint.publish(address, new PingWebServiceImpl());
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
        public void testPingWebServiceImpl() throws Exception {
           PingServiceClient tsc = new PingServiceClient(wsdlURL, serviceName);
           PingWebService tws = tsc.getPingServicePort();
           
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
