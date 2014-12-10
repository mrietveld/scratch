package org.scratch.ws.eap;

import static org.scratch.ws.utils.DeployUtil.*;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScratchWsWarJbossEapDeploy {

    protected static final Logger logger = LoggerFactory.getLogger(ScratchWsWarJbossEapDeploy.class);

    static WebArchive createTestWar() {
        // Import kie-wb war
        WebArchive war = getWebArchive("org.scratch.ws", "scratch-ws-wars", "tomcat7", PROJECT_VERSION);

        war.addAsWebInfResource("war/logging.properties", "classes/logging.properties");

        boolean replace = false;
        if( replace ) { 
            String [][] jarsToReplace = {
                    { "org.scratch.ws", "scratch-ws" },
            };
            replaceJars(war, PROJECT_VERSION, jarsToReplace);
        }
    
        /**
        String [] jarsToDelete = { "cxf-bundle-jaxrs-2.7.11.jar" };
        deleteJars(war, jarsToDelete);
        */
        
        boolean replaceWebXml = false;
        if( replaceWebXml ) { 
          war.delete("WEB-INF/web.xml");
          war.addAsWebResource("war/web.xml");
        }
        
        return war;
    }

    protected void printTestName() { 
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        logger.info( "] Starting " + ste.getMethodName());
    }
    
}
