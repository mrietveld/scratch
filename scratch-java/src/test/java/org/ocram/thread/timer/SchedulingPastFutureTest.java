package org.ocram.thread.timer;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class SchedulingPastFutureTest {

    protected ScheduledThreadPoolExecutor scheduler;
    
    @Before
    public void before() { 
        scheduler =  new ScheduledThreadPoolExecutor( 1 );
    }
    
    @Test
    public void scheduleDueTimerAsFuture() { 
        Calendar cal = GregorianCalendar.getInstance();
        cal.add(Calendar.MINUTE, -1);
        Date date = cal.getTime();
        Callable<Void> item = (Callable<Void>) new Callable<Void>() {

            public Void call() throws Exception {
                // DBG Auto-generated method stub
                return null;
            }
        };

        long then = date.getTime();
        long now = System.currentTimeMillis();
        ScheduledFuture<Void> future = null;
        if ( then >= now ) {
            future = scheduler.schedule( item,
                                         then - now,
                                         TimeUnit.MILLISECONDS );
        } else {
            future = scheduler.schedule( item,
                                         0,
                                         TimeUnit.MILLISECONDS );
        }

    }
    
}
