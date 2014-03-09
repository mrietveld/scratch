package org.ocram.thread;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedInstanceMethodTest extends ScratchBaseTest {

    private static Logger logger = LoggerFactory.getLogger(SharedInstanceMethodTest.class);
    
    static volatile int s = 0;
    int u = 0; 
    
    private void sharedMethod() throws InterruptedException { 
        int a = 1;
        ++s;
        ++u;
        int b = ++a;
        ++s;
        ++u;
        logger.debug( "[" + Thread.currentThread().getName() + "] "
                + " a: " + a
                + " b: " + b );
        if( "t1".equals(Thread.currentThread().getName()) ) { 
            logger.debug( "[" + Thread.currentThread().getName() + "] sleep" );
            Thread.sleep(1000);
        }
        // t1 at int b, but t2 at int x?
        int x = ++a;
        ++s;
        ++u;
        // t1 at int b, but t2 at int y?
        int y = ++x; 
        ++s;
        ++u;
        logger.debug( "[" + Thread.currentThread().getName() + "] "
                + " x: " + x
                + " y: " + y );
        logger.debug( "[" + Thread.currentThread().getName() + "] "
                + " s: " + s
                + " u: " + u );
    }

    @Test
    public void sharedInstanceMethodTest() throws Exception {
        Runnable r1 = new FirstRunnable(this);
        Runnable r2 = new SecondRunnable(this);

        Thread t1 = new Thread(r1);
        t1.setName("t1");
        t1.start();
        
        Thread t2 = new Thread(r2);
        t2.setName("t2");
        t2.start();
        
        while( t1.isAlive() || t2.isAlive() ) { 
//            logger.debug("t1: " + t1.isAlive() + " t2: " + t2.isAlive() );
            Thread.sleep(1000);
        }
    }

    private static class FirstRunnable implements Runnable {

        private SharedInstanceMethodTest sharedInstance = null;

        public FirstRunnable(SharedInstanceMethodTest sharedInstance) {
            this.sharedInstance = sharedInstance;
        }

        public void run() {
            logger.debug("First: " + Thread.currentThread().getName());

            try {
                sharedInstance.sharedMethod();
            } catch (InterruptedException e) {
                fail(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    private static class SecondRunnable implements Runnable {

        private SharedInstanceMethodTest sharedInstance = null;

        public SecondRunnable(SharedInstanceMethodTest sharedInstance) {
            this.sharedInstance = sharedInstance;
        }

        public void run() {
            logger.debug("Second: " + Thread.currentThread().getName());
            
            try {
                sharedInstance.sharedMethod();
            } catch (InterruptedException e) {
                fail(e.getMessage());
                e.printStackTrace();
            }
        }
    }
    
    
}
