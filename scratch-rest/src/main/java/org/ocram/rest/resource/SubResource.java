package org.ocram.rest.resource;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class SubResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);
    
    @Inject
    private InfoResource info;
    
    private String string;
    
    public void setString(String str) { 
        this.string = str;
    }
    
    @PreDestroy
    public void dispose() { 
        logger.info( "PREDESTROY [sub]" );
    }
    
    @Path("next/id/{id: [a-zA-Z0-9-]*}")
    public InfoResource getInfoResource(@PathParam("id") String id) { 
        logger.info( "SUB: " + id);
        info.setString(string + ":" + id);
        return info;
    }
    
    @Path("next")
    public InfoResource getInfoResource() { 
        logger.info( "SUB: empty");
        return info;
    }
    
    
}
