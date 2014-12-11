package org.scratch.ws;

import javax.jws.WebService;

import org.jboss.ws.api.annotation.EndpointConfig;

@WebService(targetNamespace = PingWebServiceSimpleSslImpl.NAMESPACE,
        serviceName = "PingService", 
        endpointInterface = "org.scratch.ws.generated.PingWebService",
        portName="PingServiceSslPort",
        wsdlLocation="wsdl/PingService.wsdl")
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
public class PingWebServiceSimpleSslImpl extends AbstractPingWebServiceImpl {

}