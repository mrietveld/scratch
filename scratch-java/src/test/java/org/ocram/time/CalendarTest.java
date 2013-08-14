package org.ocram.time;


import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class CalendarTest extends ScratchBaseTest {

	
    @Test
	public void timeZoneTest() { 
		GregorianCalendar gc = new GregorianCalendar();
		TimeZone tz = gc.getTimeZone();
		out.println("Offset in hours: " + tz.getRawOffset()/(60*60*1000));
	}
}
