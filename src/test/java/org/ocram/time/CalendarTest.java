package org.ocram.time;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.jbpm.calendar.BusinessCalendar;
import org.jbpm.calendar.Duration;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

import com.sun.org.apache.xerces.internal.jaxp.datatype.DatatypeFactoryImpl;

public class CalendarTest extends ScratchBaseTest {

	
    @Test
	public void testAdd() throws Exception { 
		
		DatatypeFactory df = DatatypeFactoryImpl.newInstance();
		String rep = "2009-08-27T14:42:52.368+02:00";
		XMLGregorianCalendar xmlGC = df.newXMLGregorianCalendar(rep);
		
		Date beginDatum = xmlGC.toGregorianCalendar().getTime();
    	Date eindDatum = beginDatum;
    
		// Add first reminder period
    	long rappelPeriode = 12000 * 1000L;
    	Duration rappelDuration = new Duration(rappelPeriode);
    
		int maxReminders = 3;
		BusinessCalendar calendar = new BusinessCalendar();
		eindDatum = calendar.add(eindDatum, rappelDuration);
		maxReminders--;
		
		// Add subsequent reminder periods
    	rappelDuration = new Duration(rappelPeriode);
    	
		for( int i = 0; i < maxReminders; ++i ) { 
			eindDatum = calendar.add(eindDatum, rappelDuration);
		}

		SimpleDateFormat format = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss");
		
		out.println( "Begin: " + format.format(beginDatum));
		out.println( "Eind : " + format.format(eindDatum));
		out.println( "Offset : " + xmlGC.getTimezone());

		Date now = new Date();
		// Is state too old? 
		if( eindDatum.before(now)) { 
			// Why is this state being polled!?
			out.println("state from " 
				+ format.format(beginDatum)
				+ " expired on "
				+ format.format(eindDatum)
				+ " but has status: " + "DOC_BEW"
				+ "; the state will be polled.");
		}
		else { 
			out.println("State from " 
				+ format.format(beginDatum)
				+ " will expire on "
				+ format.format(eindDatum)
				+ "; it is now "
				+ format.format(now));
		}
	}
	
    @Test
	public void timeZoneTest() { 
		GregorianCalendar gc = new GregorianCalendar();
		TimeZone tz = gc.getTimeZone();
		out.println("Offset in hours: " + tz.getRawOffset()/(60*60*1000));
	}
}
