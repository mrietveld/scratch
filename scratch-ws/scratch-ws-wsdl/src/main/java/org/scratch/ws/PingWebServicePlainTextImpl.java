package org.scratch.ws;

import javax.jws.WebService;

import org.jboss.ws.api.annotation.EndpointConfig;

@WebService(
        portName="PingServicePlainTextPort",
        serviceName = "PingPlainTextService", 
        wsdlLocation="wsdl/PingService.wsdl",
        targetNamespace = AbstractPingWebServiceImpl.NAMESPACE,
        endpointInterface = "org.scratch.ws.generated.PingWebService"
        )
@EndpointConfig(configFile = "WEB-INF/plain-text-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
public class PingWebServicePlainTextImpl extends AbstractPingWebServiceImpl {

}
