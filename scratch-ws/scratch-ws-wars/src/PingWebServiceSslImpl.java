package org.scratch.ws;

import javax.jws.WebService;

import org.jboss.ws.api.annotation.EndpointConfig;

@WebService(
        portName="PingServiceSslPort",
        serviceName = "PingSslService", 
        wsdlLocation="wsdl/PingService.wsdl",
        targetNamespace = PingWebServiceSslImpl.NAMESPACE,
        endpointInterface = "org.scratch.ws.generated.PingWebService"
        )
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
public class PingWebServiceSslImpl extends AbstractPingWebServiceImpl {

}