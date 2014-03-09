package org.ocram.arquillian.as7.timer;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
@Lock(LockType.READ)
public class TimerSchedulerBean {

    private Logger logger = LoggerFactory.getLogger(TimerSchedulerBean.class);
    
    @Resource
    TimerService timerService;
   
    @Timeout
    public void timeout(Timer timer) { 
        logger.info( timer.getInfo().toString() + " fired." );
    }
    
    public void createTimerJob() { 
        final TimerConfig oneTimeConfig = new TimerConfig("one-time", true);
        Calendar cal = GregorianCalendar.getInstance();
        cal.roll(Calendar.SECOND, 5);
        timerService.createSingleActionTimer(cal.getTime(), oneTimeConfig);
    }
}
