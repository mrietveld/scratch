package org.ocram.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class TimeTest extends ScratchBaseTest { 
    
    @Test
    public void ScriptTimeCompare() { 

        long now = System.currentTimeMillis();
        Calendar cal = GregorianCalendar.getInstance();
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.set(Calendar.HOUR_OF_DAY, 18);
        long tooEarly = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 6);
        long tooLate = cal.getTimeInMillis();
        
        out.println( now );
        out.println( tooEarly );
        out.println( tooLate );
        out.println( now < tooLate );
        out.println( now > tooEarly );
    }

}
