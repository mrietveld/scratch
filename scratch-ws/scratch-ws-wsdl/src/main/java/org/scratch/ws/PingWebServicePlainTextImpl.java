package org.scratch.ws;

import javax.jws.WebService;

import org.jboss.ws.api.annotation.EndpointConfig;

@WebService(targetNamespace = PingWebServicePlainTextImpl.NAMESPACE,
        serviceName = "PingService", 
        endpointInterface = "org.scratch.ws.generated.PingWebService",
        portName="PingServicePlainTextPort")
@EndpointConfig(configFile = "WEB-INF/jaxws-endpoint-config.xml", configName = "Custom WS-Security Endpoint")
public class PingWebServicePlainTextImpl extends AbstractPingWebServiceImpl {

}
