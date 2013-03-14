package org.ocram.time;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class TimeTest extends ScratchBaseTest { 

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
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

    @Test
    public void secondsMillisTest() throws Exception { 

        long now = System.currentTimeMillis();
        Date future = new Date((new Date().getTime() + 5 * 1000));
        
        out.println( "now   : " + sdf.format(new Date()));
        out.println( "future: " + sdf.format(future));
        long diff = (future.getTime() - now)/1000;
        out.println( "diff  : " + diff );
       
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(new PrintMe(), diff, TimeUnit.SECONDS);
        sdf.format(new Date());
        
        Thread.sleep((diff + 1)*1000);
        
        out.println( "done  : " + sdf.format(new Date()) );
    }
    
    public static class PrintMe implements Runnable {

        @Override
        public void run() {
            System.out.println( sdf.format(new Date()) + " Hello!" );
        } 
    }
    
}
