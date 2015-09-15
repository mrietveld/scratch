package org.ocram.test.rest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/test")
public class TestResource {

    private static final Logger logger = LoggerFactory.getLogger(TestResource.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    @Context
    private HttpServletRequest request;

    @Context
    private Request restRequest;

    @Context
    private UriInfo uriInfo;

    @GET
    @Path("/ping")
    @RolesAllowed({"test"})
    public Response testHelp() {
        System.out.println("PING!");
        return Response.ok().build();
    }

    @POST
    @Path("/test/{ref: [a-zA-Z0-9-:\\._]+}/")
    @RolesAllowed({"test"})
    public Response async( @PathParam("ref") String ref ) {
        Map<String, List<String>> params = getRequestParams(request);
        String input = "DEFAULT";
        if( params.size() > 0 ) {
            input = params.entrySet().iterator().next().getKey();
        }

        String result = input + ":" + ref;
        return Response.ok(result).build();
    }

    protected static Map<String, List<String>> getRequestParams( HttpServletRequest request ) {
        Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        Enumeration<String> names = request.getParameterNames();
        while( names.hasMoreElements() ) {
            String name = names.nextElement();
            parameters.put(name, Arrays.asList(request.getParameterValues(name)));
        }

        return parameters;
    }

}
