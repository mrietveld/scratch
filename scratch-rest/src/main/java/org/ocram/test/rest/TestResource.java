package org.ocram.test.rest;

import javax.annotation.ManagedBean;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.ocram.test.domain.JaxbTestInput;

@ManagedBean
@Path("/test")
public class TestResource {

    @Context
    private HttpServletRequest request;
    
    @Context
    private Request restRequest;
    
    @Context 
    private UriInfo uriInfo;
    
    @GET
    @Path("/ping")
    public void testHelp() {
        System.out.println("PING!");
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Path("/context")
    public Response processInput(JaxbTestInput input) {
        System.out.println("IN: " + input.getObjects().get(0).getClass().getName());
        
        return Response.ok(input).build();
    }

}
