package org.ocram.test.rest.context;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.ocram.test.domain.JaxbTestInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/context")
@RequestScoped
public class ContextResource {

    @Context
    private HttpServletRequest request;
    
    @Context
    private Request restRequest;
    
    @Context 
    private UriInfo uriInfo;
    
    private static final Logger logger = LoggerFactory.getLogger(ContextResource.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @GET
    @Path("/")
    public void getInfo() {
        System.out.println("TEST!");
    }

    @GET
    @Path("/ping")
    public void testHelp() {
        System.out.println("PING!");
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Path("/test")
    public Response processInput(JaxbTestInput input) {
        logger.info("IN: " + input.getObjects().get(0).getClass().getName());
        
        return Response.ok(input).build();
    }

}
