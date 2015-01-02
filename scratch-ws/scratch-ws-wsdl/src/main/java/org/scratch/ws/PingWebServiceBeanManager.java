package org.scratch.ws;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;

@ApplicationScoped
public class PingWebServiceBeanManager {

    @RequestScoped
    AbstractPingWebServiceImpl pingServiceImpl;
    
}
