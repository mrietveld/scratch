package org.scratch.ws;

import javax.jws.WebService;

import org.jboss.ws.api.annotation.EndpointConfig;

@WebService(
        portName="PingServiceSslPort",
        serviceName = "PingService", 
        wsdlLocation="wsdl/PingService.wsdl",
        targetNamespace = PingWebServiceSimpleSslImpl.NAMESPACE,
        endpointInterface = "org.scratch.ws.generated.PingWebService"
        )
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
public class PingWebServiceSimpleSslImpl extends AbstractPingWebServiceImpl {

}