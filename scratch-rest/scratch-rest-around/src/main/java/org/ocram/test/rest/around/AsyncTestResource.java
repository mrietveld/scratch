package org.ocram.test.rest.around;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/async")
public class AsyncTestResource {

    private static final Logger logger = LoggerFactory.getLogger(AsyncTestResource.class);
    private final static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
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
    @Path("/test")
    public Response async() {
        logger.info("BEF: " + sdf.format(new Date(System.currentTimeMillis())));
        try { 
            Map<String, List<String>> params = getRequestParams(request);
            String input = "DEFAULT";
            if( params.size() > 0 ) { 
                input = params.entrySet().iterator().next().getKey();
            }

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
