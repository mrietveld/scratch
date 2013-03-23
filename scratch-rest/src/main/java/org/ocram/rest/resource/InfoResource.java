package org.ocram.rest.resource;

import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.ocram.rest.domain.InfoXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
@RequestScoped
public class InfoResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);

    private String string;

    public InfoResource() {
        this.string = "last";
        logger.info("[1] INFO: [" + string + "]");
    }

    public void setString(String str) { 
        this.string = str;
    }

    @PreDestroy
    public void dispose() {
        logger.info("PREDESTROY [info]");
    }

    @GET
    @Path("test")
    @Produces("application/xml")
    public InfoXml test() {
        logger.info("TEST: " + string);

        InfoXml xml = new InfoXml();
        xml.setId(23l);
        xml.setWhat(this.string);
        return xml;
    }
}
