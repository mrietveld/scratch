package org.ocram.rest.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class IllegalStateExceptionMapper implements ExceptionMapper<IllegalStateException> 
{
   public Response toResponse(IllegalStateException exception)
   {
      return Response.status(Response.Status.NOT_FOUND).build();
   }
}
