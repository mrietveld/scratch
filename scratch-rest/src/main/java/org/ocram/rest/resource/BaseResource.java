package org.ocram.rest.resource;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/")
@RequestScoped
public class BaseResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);

    @Path("{id : [a-zA-z0-9-]+}")
    public SubResource getSubResource(@PathParam("id") String base) {
        logger.info( "BASE: " + base );
        return new SubResource(base);
    }

}
