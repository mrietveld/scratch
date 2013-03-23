package org.ocram.rest.resource;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@RequestScoped
public class BaseResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);
    
    @Inject
    private SubResource sub;

    @PreDestroy
    public void dispose() { 
        logger.info( "PREDESTROY [base]" );
    }
    
    @Path("{id : [a-zA-z0-9-]+}")
    public SubResource getSubResource(@PathParam("id") String base) {
        logger.info( "BASE: " + base );
        sub.setString(base);
        return sub;
    }

}
