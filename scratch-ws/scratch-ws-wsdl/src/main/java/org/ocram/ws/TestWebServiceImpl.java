package org.ocram.ws;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

import javax.jws.WebService;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.cxf.interceptor.InInterceptors;
import org.kie.remote.services.ws.common.ExceptionType;
import org.kie.remote.services.ws.common.WebServiceFaultInfo;
import org.ocram.ws.generated.PingRequest;
import org.ocram.ws.generated.PingResponse;
import org.ocram.ws.generated.TestRequest;
import org.ocram.ws.generated.TestResponse;
import org.ocram.ws.generated.TestWebService;
import org.ocram.ws.generated.TestWebServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebService(
        serviceName = "TestService", 
        portName = "TestServiceClient", 
        name = "TestService", 
        targetNamespace = TestWebServiceImpl.NAMESPACE)
@InInterceptors()
public class TestWebServiceImpl implements TestWebService {

    private static final Logger logger = LoggerFactory.getLogger(TestWebServiceImpl.class);
    
    static final String NAMESPACE = "http://services.remote.kie.org/" + "0.1.0" + "/command";
   
    private final static AtomicInteger idGen = new AtomicInteger(0);
    
    public PingResponse ping( PingRequest pingReq ) throws TestWebServiceException {
        TestRequest req = pingReq.getRequest();
        if( req == null ) { 
           WebServiceFaultInfo faultInfo = new WebServiceFaultInfo();
           faultInfo.setCorrelationId(null);
           faultInfo.setType(ExceptionType.VALIDATION);
           throw new TestWebServiceException("null test request", faultInfo);
        }
       
        TestResponse resp = new TestResponse();
        resp.setId(idGen.incrementAndGet());

        resp.setRequestName(req.getRequestName());
        resp.setRequestDate(toXMLGregorianCalendar(new Date()));
        resp.setRequestId(req.getId());
       
        PingResponse pingResp = new PingResponse();
        pingResp.setReturn(resp);
        return pingResp;
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
