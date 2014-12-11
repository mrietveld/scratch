package org.scratch.ws.config.security;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ServerPasswordManagerImpl implements ServerPasswordManager {

    private Map<String, String> userPasswordMap = new ConcurrentHashMap<String, String>();
    
    public ServerPasswordManagerImpl() {
        // Default constructor
        userPasswordMap.put("mary", "mary123@");
    }

    @Override
    public String getPassword( String user ) {
        if( user == null ) {
            return null;
        } 
        return userPasswordMap.get(user);
    }
}
