package org.kie.tests.wb.eap;

import static org.kie.remote.tests.base.DeployUtil.getWebArchive;
import static org.kie.remote.tests.base.DeployUtil.replaceJars;
import static org.kie.tests.wb.base.util.TestConstants.PROJECT_VERSION;

import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KieWbWarJbossEapDeploy {

    protected static final Logger logger = LoggerFactory.getLogger(KieWbWarJbossEapDeploy.class);
    
    static WebArchive createTestWar(boolean replace) {
        return createTestWar(null, replace);
    }
    
    static WebArchive createTestWar() {
        return createTestWar(null, true);
    }
    
    static WebArchive createTestWar(String database, boolean replace) {
        WebArchive war = getWebArchive("org.kie", "kie-wb-distribution-wars", "eap6_3", PROJECT_VERSION);
        
        // Replace persistence.xml with postgres version
        if( database != null ) { 
            war.delete("WEB-INF/classes/META-INF/persistence.xml");
            if( "oracle".equals(database) ) { 
                war.addAsResource("META-INF/persistence-oracle.xml", "META-INF/persistence.xml");
            } else if( "postgres".equals(database) ) { 
                war.addAsResource("META-INF/persistence-postgres.xml", "META-INF/persistence.xml");
            } else { 
                throw new IllegalArgumentException("Unknown database type: " + database );
            }
        } 

        if( replace ) { 
            String [][] jarsToReplace = { 
                    // kie-remote
                    { "org.kie.remote", "kie-remote-services" }
            };
            replaceJars(war, PROJECT_VERSION, jarsToReplace);
            
            // ADD
            /**
            String [][] jarsToAdd = { 
                    // web services
                    { "javax.xml.ws", "jaxws-api" }
            };
            addNewJars(war, jarsToAdd);
            **/
            replaceWebXmlForWebServices(war);
        }
       
        return war;
    }

    private static void replaceWebXmlForWebServices(WebArchive war) {
        war.delete("WEB-INF/web.xml");
        war.addAsWebInfResource("WEB-INF/web.xml", "web.xml");
    }

    protected void printTestName() { 
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        logger.info( "] Starting " + ste.getMethodName());
    }
    
}
