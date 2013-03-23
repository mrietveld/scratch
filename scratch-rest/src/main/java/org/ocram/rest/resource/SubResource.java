package org.ocram.rest.resource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class SubResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);
    
    private String base;
    
    public SubResource(String base) { 
        this.base = base;
    }
    
    @Path("next/id/{id: [a-zA-Z0-9-]*}")
    public InfoResource getAssetResource(@PathParam("id") String id) { 
        logger.info( "SUB: " + id);
        return new InfoResource( this.base + ":" + id );
    }
    
    @Path("next")
    public InfoResource getAssetResource() { 
        logger.info( "SUB: empty");
        return new InfoResource();
    }
    
    
}
