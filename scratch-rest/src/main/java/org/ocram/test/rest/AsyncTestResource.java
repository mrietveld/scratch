package org.ocram.test.rest;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/async")
@RequestScoped
public class AsyncTestResource {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTestResource.class);
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @GET
    @Path("/ping")
    public void testHelp() {
        System.out.println("PING!");
    }

    @POST
    @Path("/test")
    public Response async(@Context HttpServletRequest request) {
        logger.info("BEF: " + sdf.format(new Date(System.currentTimeMillis())));
        try { 
        Map<String, List<String>> params = getRequestParams(request);
        String input = "DEFAULT";
        if( params.size() > 0 ) { 
            input = params.entrySet().iterator().next().getKey();
        }
        
        logger.info("DEP: " + input );
        
        return Response.status(Status.ACCEPTED).build();
        } finally { 
            logger.info("AFT: " + sdf.format(new Date(System.currentTimeMillis())));
        }
    }
    
    protected static Map<String, List<String>> getRequestParams(HttpServletRequest request) {
        Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            parameters.put(name, Arrays.asList(request.getParameterValues(name)));
        }

        return parameters;
    }
}
