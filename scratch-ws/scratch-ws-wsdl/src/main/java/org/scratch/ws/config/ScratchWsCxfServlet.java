package org.scratch.ws.config;

import java.net.URL;

import javax.servlet.ServletConfig;
import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.apache.cxf.ws.policy.PolicyEngine;
import org.apache.cxf.ws.security.SecurityConstants;
import org.scratch.ws.PingWebServiceImpl;
import org.scratch.ws.config.security.ServerPasswordCallback;

public class ScratchWsCxfServlet extends CXFNonSpringServlet {


    /** generated serial version UID */
    private static final long serialVersionUID = 2824057877125098622L;

    public static final String SERVICE_SUFFIX = "/ws/PingService";
    public static final String TEST_ADDRESS = "http://localhost:" + 8080 + SERVICE_SUFFIX;
    public static final String TEST_HTTPS_ADDRESS = "https://localhost:" + 8081 + SERVICE_SUFFIX;
            
    @Override
    public void loadBus(ServletConfig servletConfig) {
       // TODO: retrieve ports for normal + SSL from servletConfig 
        
        // bus setup
      super.loadBus(servletConfig);
      Bus bus = getBus();
      BusFactory.setDefaultBus(bus);
      bus.getExtension(PolicyEngine.class).setEnabled(true);
   
      URL wsdl = ScratchWsCxfServlet.class.getResource("TestService.wsdl");
     
      // setup the PingService endpoint
      EndpointImpl ep = (EndpointImpl) Endpoint.create(new PingWebServiceImpl());
      ep.setEndpointName(new QName(PingWebServiceImpl.NAMESPACE, "PingService"));
      ep.setWsdlLocation(wsdl.getPath());
      ep.setAddress(TEST_HTTPS_ADDRESS);
      ep.publish();
      ep.getServer().getEndpoint().getEndpointInfo().setProperty(
              SecurityConstants.CALLBACK_HANDLER, 
              new ServerPasswordCallback());
     
      // publish the endpoint
      Endpoint.publish(TEST_ADDRESS, new PingWebServiceImpl());
    }
}
