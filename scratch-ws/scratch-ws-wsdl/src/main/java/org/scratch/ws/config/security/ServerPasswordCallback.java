package org.scratch.ws.config.security;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.apache.deltaspike.core.api.provider.BeanManagerProvider;
import org.apache.ws.security.WSPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerPasswordCallback implements CallbackHandler {

    private final static Logger logger = LoggerFactory.getLogger(ServerPasswordCallback.class);
    
    public void handle( Callback[] callbacks ) throws IOException, UnsupportedCallbackException {
        WSPasswordCallback pc = (WSPasswordCallback) callbacks[0];

        String user = pc.getIdentifier();
        System.out.println("Password callback: " + user );
        String pass = null;
        try { 
            ServerPasswordManager pwdMgr = getServerPasswordManager();
            if( user != null ) { 
                pass = pwdMgr.getPassword(user);
            }
        } catch (Exception e ) { 
            logger.error("Unable to retrieve password for user {}", user, e ); 
        }
        if( user != null ) { 
            pc.setPassword(pass);
        }
    }
    
    private ServerPasswordManager getServerPasswordManager() throws Exception {
        BeanManager beanManager = getBeanManager();
        return createBean(ServerPasswordManager.class, beanManager);
    }
   
    private BeanManager getBeanManager() { 
        try {
            return BeanManagerProvider.getInstance().getBeanManager();
        } catch( IllegalStateException ise ) {
           logger.error( "Returning null bean manager: " + ise.getMessage() );
           return null;
        }
    } 
       
    @SuppressWarnings("unchecked")
    public static <T> T createBean(Class<T> beanType, BeanManager beanManager, Annotation... bindings) throws Exception {
        if( beanManager == null ) { 
           if( beanType.equals(ServerPasswordManager.class) ) { 
               return (T) new ServerPasswordManagerImpl();
           }
        }
        Set<Bean<?>> beans = beanManager.getBeans( beanType, bindings );
   
        if (beans != null && !beans.isEmpty()) {
            Bean<T> bean = (Bean<T>) beans.iterator().next();

            return (T) beanManager.getReference(bean, beanType, beanManager.createCreationalContext(bean));
        }
        
        return null;
    }
}
