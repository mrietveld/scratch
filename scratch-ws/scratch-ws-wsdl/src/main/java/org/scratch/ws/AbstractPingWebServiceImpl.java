package org.scratch.ws;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jboss.ws.api.annotation.EndpointConfig;
import org.kie.remote.services.ws.common.ExceptionType;
import org.kie.remote.services.ws.common.WebServiceFaultInfo;
import org.scratch.ws.generated.PingRequest;
import org.scratch.ws.generated.PingResponse;
import org.scratch.ws.generated.PingWebService;
import org.scratch.ws.generated.PingWebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractPingWebServiceImpl implements PingWebService {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPingWebServiceImpl.class);
    
    public static final String NAMESPACE = "http://services.ws.scratch.org/" + "0.1.0" + "/test";
   
    private final static AtomicInteger idGen = new AtomicInteger(0);
    
    public PingResponse ping( PingRequest req ) throws PingWebServiceException {
        // DBG
        new Throwable().printStackTrace(System.out);
        
        if( req == null ) { 
           WebServiceFaultInfo faultInfo = new WebServiceFaultInfo();
           faultInfo.setCorrelationId(null);
           faultInfo.setType(ExceptionType.VALIDATION);
           throw new PingWebServiceException("null ping request", faultInfo);
        }
        if( req.getId() < 0 ) { 
           WebServiceFaultInfo faultInfo = new WebServiceFaultInfo();
           faultInfo.setCorrelationId(null);
           faultInfo.setType(ExceptionType.VALIDATION);
           throw new PingWebServiceException("null ping request", faultInfo);
        }
       
        PingResponse resp = new PingResponse();
        resp.setId(idGen.incrementAndGet());

        resp.setRequestName(req.getRequestName());
        resp.setRequestDate(toXMLGregorianCalendar(new Date()));
        resp.setRequestId(req.getId());
       
        return resp;
    }

    /*
     * Converts java.util.Date to javax.xml.datatype.XMLGregorianCalendar
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date){
        GregorianCalendar gCalendar = new GregorianCalendar();
        gCalendar.setTime(date);
        XMLGregorianCalendar xmlCalendar = null;
        try {
            xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
        } catch (DatatypeConfigurationException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return xmlCalendar;
    }


}
