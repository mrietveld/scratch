package org.scratch.ws.config.xml;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.BusApplicationContext;
import org.apache.cxf.common.classloader.ClassLoaderUtils;
import org.apache.cxf.interceptor.Fault;

public class ScratchWsConfigContext {

    private String[] configLocations;
   
    private boolean includeDefaults;
    private String[] cfgFiles;
    private URL[] cfgFileURLs;
    
    private Bus bus;
    
    public ScratchWsConfigContext( String cf, boolean include ) {
        this(new String [] { cf } , include);
    }

    public ScratchWsConfigContext( String[] cf, boolean include) {
        // super
        setConfigLocations(configLocations);
        
        // the rest
        cfgFiles = cf;
        includeDefaults = include;

        try {
            AccessController.doPrivileged(new PrivilegedExceptionAction<Boolean>() {
                public Boolean run() throws Exception {
                    refresh();
                    return Boolean.TRUE;
                }

            });
        } catch( PrivilegedActionException e ) {
            if( e.getException() instanceof RuntimeException ) {
                throw (RuntimeException) e.getException();
            }
            throw new Fault(e);
        }
    }

    public void refresh() { 
        // do we need to refresh something? 
    }
    
    /**
     * Set the config locations for this application context.
     * <p>If not set, the implementation may use a default as appropriate.
     */
    public void setConfigLocations(String[] locations) {
        if (locations != null) {
            // TODO: check that  all locations are non-nul
            // Assert.noNullElements(locations, "Config locations must not be null");
            this.configLocations = new String[locations.length];
            for (int i = 0; i < locations.length; i++) {
                // TODO: test that config locations actually exist
                // this.configLocations[i] = resolvePath(locations[i]).trim();
            }
        }
        else {
            this.configLocations = null;
        }
    }
    
    public void close() {

    }

    public boolean containsBus() {
        return (bus != null);
    }
    
    public Bus getBus() {

        // TODO
        return bus;
    }

    public static URL resourceExists( final String resourceFilePath ) {
        try {
            return AccessController.doPrivileged(new PrivilegedAction<URL>() {
                public URL run() {
                    URL url = findOnClassPath(resourceFilePath);
                    if (url != null ) { 
                        return url;
                    }
                 
                    //see if it's a URL
                    url = findOnUrl(resourceFilePath);
                    if (url != null) { 
                        return url;
                    }
                    
                    //try loading it our way
                    url = getResourceUrlViaClassLoaders(resourceFilePath, BusApplicationContext.class);
                    if (url != null) {
                        url = findOnUrl(url);
                        if (url != null ) { 
                            return url;
                        }
                    }
                    
                    url = findViaFileSystem(resourceFilePath);
                    if (url != null ) { 
                        return url;
                    }
                    return null;
                }
            });
        } catch (AccessControlException ex) {
            //cannot read the user config file
            return null;
        }
    }
    
    private static URL findOnClassPath(String resourceFilePath) { 
        if (resourceFilePath.startsWith("/")) {
            resourceFilePath = resourceFilePath.substring(1);
        } 
        ClassLoader classLoader = getDefaultClassLoader();
        return classLoader.getResource(resourceFilePath);
    }
    
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ScratchWsConfigContext.class.getClassLoader();
        }
        return cl;
    }
    
    private static URL findOnUrl(String resourceUrlString) { 
       try { 
          URL url = new URL(resourceUrlString);
          return findOnUrl(url);
       } catch( MalformedURLException murle) { 
           return null;
       }
    }
  
    private static URL findOnUrl(URL url) { 
        try { 
           if( url.getProtocol().equals("file") ) { 
               URI uri = url.toURI();
               File file = new File(uri);
               if( file.exists() ) { 
                   return url;
               }
           }
           return null;
        } catch( IllegalArgumentException iae) { 
            // TODO: log!
            // invalid URI
            return null;
        } catch( URISyntaxException e ) {
            // TODO: log!
            // bad URI!
            return null;
        }
     }
    
    public static URL getResourceUrlViaClassLoaders(String resourceName, Class<?> callingClass) {
        URL url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (url == null && resourceName.startsWith("/")) {
            //certain classloaders need it without the leading /
            url = Thread.currentThread().getContextClassLoader()
                .getResource(resourceName.substring(1));
        }

        ClassLoader cluClassloader = ClassLoaderUtils.class.getClassLoader();
        if (cluClassloader == null) {
            cluClassloader = ClassLoader.getSystemClassLoader();
        }
        if (url == null) {
            url = cluClassloader.getResource(resourceName);
        }
        if (url == null && resourceName.startsWith("/")) {
            //certain classloaders need it without the leading /
            url = cluClassloader.getResource(resourceName.substring(1));
        }

        if (url == null) {
            ClassLoader cl = callingClass.getClassLoader();

            if (cl != null) {
                url = cl.getResource(resourceName);
            }
        }

        if (url == null) {
            url = callingClass.getResource(resourceName);
        }
        
        if ((url == null) && (resourceName != null) && (resourceName.charAt(0) != '/')) {
            return getResourceUrlViaClassLoaders('/' + resourceName, callingClass);
        }

        return url;
    }
    
    public static URL findViaFileSystem(String resourceFilePath) { 
        File file = new File(resourceFilePath); 
        if( file.exists() ) { 
            URI uri =  file.toURI();
            try {
                return uri.toURL();
            } catch( MalformedURLException e ) {
                // TODO: log!
                return null;
            }
        }
        return null;
    }
    
}
