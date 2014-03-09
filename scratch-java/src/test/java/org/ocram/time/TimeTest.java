package org.ocram.time;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.thread.ThreadGroupDemo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTest extends ScratchBaseTest { 

    private final static Logger logger = LoggerFactory.getLogger(TimeTest.class);
    
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
        
       logger.debug( "{}", now );
       logger.debug( "{}", tooEarly );
       logger.debug( "{}", tooLate );
       logger.debug( "{}", now < tooLate );
       logger.debug( "{}", now > tooEarly );
    }

    @Test
    public void secondsMillisTest() throws Exception { 

        long now = System.currentTimeMillis();
        Date future = new Date((new Date().getTime() + 5 * 1000));
        
       logger.debug( "now   : " + sdf.format(new Date()));
       logger.debug( "future: " + sdf.format(future));
        long diff = (future.getTime() - now)/1000;
       logger.debug( "diff  : " + diff );
       
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(new PrintMe(), diff, TimeUnit.SECONDS);
        sdf.format(new Date());
        
        Thread.sleep((diff + 1)*1000);
        
       logger.debug( "done  : " + sdf.format(new Date()) );
    }
    
    public static class PrintMe implements Runnable {

        @Override
        public void run() {
            logger.debug( sdf.format(new Date()) + " Hello!" );
        } 
    }
    
    @Test
    public void millisTest() { 
        long one = 1385504092114l;
        long two = 1385504096173l;
        Date bef = new Date(one);
        Date aft = new Date(two);
        
        logger.debug(sdf.format(bef));
        logger.debug(sdf.format(aft));
    }
    
}
