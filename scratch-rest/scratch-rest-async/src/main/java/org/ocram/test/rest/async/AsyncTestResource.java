package org.ocram.test.rest.async;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;
import org.ocram.test.domain.request.ChildRequest;
import org.ocram.test.domain.request.ChildResponse;
import org.ocram.test.domain.request.ParentRequest;
import org.ocram.test.domain.request.ParentResponse;
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
    
    @Inject
    private JobRequestProcessor jobRequestProcessor;

    @POST
    @Path("/ping")
    public Response ping() {
        System.out.println("PING!");
      
        Map<String, String[]> params = request.getParameterMap();
        for( String key : params.keySet() ) { 
            System.out.println( "param: " + key + "[" + params.get(key)[0] + "]" );
        }
        int i = 0;
        Enumeration<String> headers = request.getHeaderNames();
        while( headers.hasMoreElements() && i < 20 ) { 
            System.out.println( "header: " + headers.nextElement() );
            ++i;
        }
        
        return Response.ok().build();
    }


    @POST
    @Path("/inherit")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response inheritance(ParentRequest request) {
        return Response.ok(new ChildResponse((ChildRequest) request)).build();
    }
    
    @POST
    @Path("/test")
    public void async(final @Suspend(100) AsynchronousResponse response) throws Exception {
        logger.info("BEFO: " + sdf.format(new Date(System.currentTimeMillis())));
        AsyncContext ctx = request.startAsync();
        
        sendResponse(response);
        try { 
            Map<String, List<String>> params = getRequestParams(request);
            String input = "DEFAULT";
            if( params.size() > 0 ) { 
                input = params.entrySet().iterator().next().getKey();
            }
            jobRequestProcessor.processJob(input);
        } finally { 
            logger.info("AFTR: " + sdf.format(new Date(System.currentTimeMillis())));
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

    public void sendResponse(final AsynchronousResponse response) { 
        Thread t = new Thread() {
           @Override
           public void run() {
              try {
                 Response jaxrs = Response.status(202).build();
                 response.setResponse(jaxrs);
                 logger.info("SENT: " + sdf.format(new Date(System.currentTimeMillis())));
              } catch (Exception e) {
                 e.printStackTrace();
              }
           }
        };
        t.start();
        Thread.currentThread().yield();
    }
}
