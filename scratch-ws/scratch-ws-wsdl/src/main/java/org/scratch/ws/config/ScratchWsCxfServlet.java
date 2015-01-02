package org.scratch.ws.config;

import java.net.URL;

import javax.servlet.ServletConfig;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.apache.cxf.ws.policy.PolicyEngine;
import org.apache.cxf.ws.security.SecurityConstants;
import org.jboss.wsf.stack.cxf.client.configuration.JBossWSNonSpringBusFactory;
import org.scratch.ws.PingWebServicePlainTextImpl;
import org.scratch.ws.config.security.ServerPasswordCallback;
import org.scratch.ws.generated.PingWebService;

public class ScratchWsCxfServlet extends CXFNonSpringServlet {

    /** generated serial version UID */
    private static final long serialVersionUID = 2824057877125098622L;

    public static final String SERVICE_NAME = "PingService";
    public static final String PLAIN_TEXT_SUFFIX = SERVICE_NAME;
    public static final String SSL_SUFFIX = "/ssl/" + SERVICE_NAME;

    public static final String PLAIN_TEXT_ADDRESS = "http://localhost:" + 8080 + "/ws/" + SERVICE_NAME;
    public static final String SSL_ADDRESS = "https://localhost:" + 8080 + "/ws/ssl/" + SERVICE_NAME;

    @Override
    public void loadBus( ServletConfig servletConfig ) {
        // TODO: retrieve ports for normal + SSL from servletConfig

        // bus setup
        BusFactory busFactory = new JBossWSNonSpringBusFactory();
        Bus bus = busFactory.createBus();
        setBus(bus);
      
        try {
            // more bus setup
            BusFactory.setDefaultBus(bus);
            BusFactory.setThreadDefaultBus(bus);
           
            // enable ws-policy, etc.
            bus.getExtension(PolicyEngine.class).setEnabled(true);

            // setup and initalize endpoints
            String wsdlPath = "/wsdl/PingService.wsdl";
            URL wsdl = ScratchWsCxfServlet.class.getResource(wsdlPath);
            if( wsdl == null ) {
                throw new IllegalStateException("No wsdl for PingService could be found at path [" + wsdlPath + "]");
            }
            System.out.println( ">> ADDING PINGSERVICE TO BUS! <<");
            setupPingServiceEndpoint(wsdl, PLAIN_TEXT_SUFFIX, new PingWebServicePlainTextImpl());
            setupPingServiceEndpoint(wsdl, SSL_SUFFIX, new PingWebServicePlainTextImpl());
        } finally { 
            BusFactory.setThreadDefaultBus(null);
        }
    }

    public static Endpoint setupPingServiceEndpoint( URL wsdlUrl, String address, PingWebService webServiceImpl ) {
        EndpointImpl ep = (EndpointImpl) Endpoint.create(webServiceImpl);
        ep.setAddress(address);
        ep.getProperties().put(SecurityConstants.CALLBACK_HANDLER, new ServerPasswordCallback());
        
        ep.publish();
        return ep;
    }

}
