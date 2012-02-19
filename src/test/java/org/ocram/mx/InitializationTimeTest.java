package org.ocram.mx;

import static junit.framework.Assert.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class InitializationTimeTest extends ScratchBaseTest {

    private final static long initializationTime = new Date().getTime();
    private static long jvmStartTime;
    static {
        try {
            RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
            jvmStartTime = mx.getStartTime();
        }
        catch(Exception e ) { 
            // nothing to do.. 
        }
    }
   
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
    
    @Test
    public void upTimeTest() {
        long now = new Date().getTime();
        assertTrue(now > initializationTime);
        out.println( sdf.format(new Date(now)));
        
        assertTrue(now > initializationTime);
        out.println( sdf.format(new Date(initializationTime)));
        
        assertTrue(initializationTime > jvmStartTime);
        out.println( sdf.format(new Date(jvmStartTime)));
    }
    
    @Test
    public void lazyClassInitializationTest() throws Exception { 
        long now = new Date().getTime();
        assertTrue("Initialization happened after now?", now > initializationTime);
        out.println( "This init time: " + sdf.format(new Date(initializationTime)));
       
        Thread.sleep(500);
        
        assertTrue("Initialization happened after cache holder initialization?", 
                getCacheInitTime() > initializationTime);
        out.println( "cache init time: " + sdf.format(new Date(getCacheInitTime())));
    }
    
    private long getCacheInitTime() { 
        return CacheHolderFactory.getCacheInitializationTime();
    }
}

