package org.ocram.test.rest;

import javax.annotation.ManagedBean;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.ocram.test.domain.JaxbTestInput;

@ManagedBean
@Path("/test")
public class TestResource {
    
    @GET
    @Path("/ping")
    public void testHelp() {
        System.out.println("PING!");
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Path("/context")
    public Response processInput(JaxbTestInput input) {
        System.out.print("IN: " + input.getObjects().get(0).getClass().getName());
        
        return Response.ok(input).build();
    }

}
