package org.ocram.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.ocram.rest.domain.InfoXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class InfoResource {

    private Logger logger = LoggerFactory.getLogger(BaseResource.class);
    
    private String last;
    
    public InfoResource() { 
        this.last = "last";
        logger.info( "[1] INFO: [" + last + "]");
    }
    
    public InfoResource(String last) { 
        this.last = last;
        logger.info( "[0] INFO: " + last );
    }
    
    public static final long testId = 23l;
    public static final String testProcessId = "illuminate";
    public static final String testType = "test";

    @GET
    @Path("test")
    @Produces("application/xml")
    public InfoXml test() {
        logger.info( "TEST: " + last );

        InfoXml xml = new InfoXml();
        xml.setId(23l);
        xml.setWhat(this.last);
        return xml;
    }
}
