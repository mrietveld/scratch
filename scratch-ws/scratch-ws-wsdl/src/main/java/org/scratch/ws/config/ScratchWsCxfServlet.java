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
import org.scratch.ws.AbstractPingWebServiceImpl;
import org.scratch.ws.PingWebServicePlainTextImpl;
import org.scratch.ws.PingWebServiceSimpleSslImpl;
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
    public void loadBus(ServletConfig servletConfig) {
       // TODO: retrieve ports for normal + SSL from servletConfig 
        
        // bus setup
      super.loadBus(servletConfig);
      Bus bus = getBus();
      BusFactory.setDefaultBus(bus);
      bus.getExtension(PolicyEngine.class).setEnabled(true);
 
      String wsdlPath = "/wsdl/PingService.wsdl";
      URL wsdl = ScratchWsCxfServlet.class.getResource(wsdlPath);
      if( wsdl == null ) { 
          throw new IllegalStateException("No wsdl for PingService could be found at path [" + wsdlPath + "]");
      }
    
      setupPingServiceEndpoint(wsdl, PLAIN_TEXT_SUFFIX, "PingServicePlainTextPort", new PingWebServicePlainTextImpl());
      setupPingServiceEndpoint(wsdl, SSL_SUFFIX, "PingServiceSslPort", new PingWebServiceSimpleSslImpl());
    }
    
    public static Endpoint setupPingServiceEndpoint( URL wsdlUrl, String address, String portName, PingWebService webServiceImpl ) { 
      // setup the plain text PingService endpoint
      EndpointImpl ep = (EndpointImpl) Endpoint.create(webServiceImpl);
      ep.setEndpointName(new QName(AbstractPingWebServiceImpl.NAMESPACE, "PingService"));
      ep.setWsdlLocation(wsdlUrl.getPath());
      ep.setAddress(address);
      ep.publish();
      ep.getServer().getEndpoint().getEndpointInfo().setProperty(
              SecurityConstants.CALLBACK_HANDLER, 
              new ServerPasswordCallback());
      return ep;
    }
}
