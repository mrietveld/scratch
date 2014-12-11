package org.scratch.ws.tomcat;

import static org.scratch.ws.utils.DeployUtil.*;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScratchWsWarTomcatDeploy {

    protected static final Logger logger = LoggerFactory.getLogger(ScratchWsWarTomcatDeploy.class);

    static WebArchive createTestWar(boolean replace) {
        // Import kie-wb war
        WebArchive war = getWebArchive("org.scratch.ws", "scratch-ws-wars", "tomcat7", PROJECT_VERSION);

        war.addAsWebInfResource("war/logging.properties", "classes/logging.properties");

        if( replace ) { 
          war.delete("WEB-INF/web.xml");
          war.addAsWebResource("WEB-INF/web.xml", "WEB-INF/web.xml");
        }
        
        return war;
    }

    protected void printTestName() { 
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        logger.info( "] Starting " + ste.getMethodName());
    }
    
}
