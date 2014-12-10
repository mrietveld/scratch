package org.scratch.ws.utils;

import java.io.File;
import java.util.Properties;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeployUtil {

    protected static final Logger logger = LoggerFactory.getLogger(DeployUtil.class);
  
    /**
     * Project version (arquillian jars)
     */
    public final static String PROJECT_VERSION;
    static { 
        Properties testProps = new Properties();
        try {
            testProps.load(DeployUtil.class.getResourceAsStream("/test.properties"));
        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize projectVersion property: " + e.getMessage(), e);
        }
        PROJECT_VERSION = testProps.getProperty("project.version");
    }
    
    
    public static WebArchive getWebArchive(String groupId, String artifactId, String classifier, String version) { 
        StringBuilder gavId = new StringBuilder( groupId + ":" + artifactId + ":war:");
        if( classifier != null && ! classifier.trim().isEmpty() ) {
           gavId.append(classifier + ":");
        }
        gavId.append(version);
        
        File [] warFile = 
                Maven.resolver()
                .loadPomFromFile("pom.xml")
                .resolve( gavId.toString() )
                .withoutTransitivity()
                .asFile();
        ZipImporter zipWar = ShrinkWrap.create(ZipImporter.class, "test.war").importFrom(warFile[0]);
        
        WebArchive war = zipWar.as(WebArchive.class);
        return war;
    }
    
    public static void replaceJars(WebArchive war, String projectVersion, String [][] jarsToReplace) { 
         String [] jarsArg = new String[jarsToReplace.length];
         for( String [] jar : jarsToReplace ) { 
             logger.info( "Deleting '{}' from war", jar[1] );
             String version = projectVersion;
             if( jar.length > 2 ) { 
                 version = jar[2];
             }
             war.delete("WEB-INF/lib/" + jar[1] + "-" + version + ".jar");
         }
         for( int i = 0; i < jarsToReplace.length; ++i ) { 
             jarsArg[i] = jarsToReplace[i][0] + ":" + jarsToReplace[i][1];
         }
         
         File [] kieRemoteDeps = Maven.resolver()
                 .loadPomFromFile("pom.xml")
                 .resolve(jarsArg)
                 .withoutTransitivity()
                 .asFile();
         for( File dep : kieRemoteDeps ) { 
             logger.info("Replacing with '{}'", dep.getName() ); 
         }
         war.addAsLibraries(kieRemoteDeps);
     }
     
     public static void addNewJars(WebArchive war, String [][] jarsToAdd) { 
         if( jarsToAdd.length > 0 ) { 
             String [] jarsArg = new String[jarsToAdd.length];
             for( int i = 0; i < jarsToAdd.length; ++i ) { 
                 jarsArg[i] = jarsToAdd[i][0] + ":" + jarsToAdd[i][1];
                 logger.info("Resolving '{}'", jarsArg[i]);
             }

             File [] depsToAdd = Maven.resolver()
                     .loadPomFromFile("pom.xml")
                     .resolve(jarsArg)
                     .withoutTransitivity()
                     .asFile();
             for( File dep : depsToAdd ) { 
                 logger.info("Adding '{}'", dep.getName() ); 
             }
             war.addAsLibraries(depsToAdd);
         }
     }

     public static void deleteJars(WebArchive war, String [] jarsToDelete) { 
         if( jarsToDelete.length > 0 ) { 
             for( int i = 0; i < jarsToDelete.length; ++i ) { 
                 war.delete("WEB-INF/lib/" + jarsToDelete[i] );
             }
         }
     }
}