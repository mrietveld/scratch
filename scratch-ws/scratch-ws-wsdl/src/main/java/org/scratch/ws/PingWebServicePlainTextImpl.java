package org.scratch.ws;

import javax.enterprise.context.ApplicationScoped;
import javax.jws.WebService;

import org.jboss.ws.api.annotation.EndpointConfig;

@WebService(
        portName="PingServicePlainTextPort",
        serviceName = "PingPlainTextService", 
        wsdlLocation="wsdl/PingService.wsdl",
        targetNamespace = PingWebServicePlainTextImpl.NAMESPACE,
        endpointInterface = "org.scratch.ws.generated.PingWebService"
        )
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
public class PingWebServicePlainTextImpl extends AbstractPingWebServiceImpl {

}
