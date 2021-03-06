package org.ocram.thread;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Ignore
public class SynchronizedMethodTest extends ScratchBaseTest {

    private static Logger logger = LoggerFactory.getLogger(SynchronizedMethodTest.class);

    private volatile String value = null;

    private synchronized void put(String newValue) {
        long now = System.currentTimeMillis();
        long next = 0;
        while (next < now + 10000) {
            next = System.currentTimeMillis();
            if (next % 200 == 0) {
                logger.debug(".");
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        this.value = newValue;
    }

    private synchronized String get() {
        return this.value;
    }

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    @Test
    public void synchronizedMethodTest() throws Exception {
        Function first = new Function() {

            @Override
            public void run() {
                logger.debug("First starts: " + sdf.format(new Date()));
                put("yes");
                logger.debug("First ends   : " + sdf.format(new Date()));
            }
        };

        Runnable r1 = new TestRunnable(first, "first");

        Function second = new Function() {

            @Override
            public void run() {
                logger.debug("Second starts: " + sdf.format(new Date()));
                String og = null;
                while (og == null) {
                    logger.debug("Second get: " + og);
                    og = get();
                }
                logger.debug("Second ends [" + og + "]: " + sdf.format(new Date()));
            }
        };

        Runnable r2 = new TestRunnable(second, "second");

        Thread t2 = new Thread(r2);
        t2.setName("t2");
        t2.start();

        Thread t1 = new Thread(r1);
        t1.setName("t1");
        t1.start();

        while (t1.isAlive() || t2.isAlive()) {
            Thread.sleep(1000);
        }
    }
    
    @Test
    public void reentrantSynchronizedTest() { 
        int l = recursive(0);
        assertTrue( l == 3 );
    }
    
    private synchronized int recursive(int l) { 
       if( l < 3 ) { 
           return recursive(l+1);
       }
       return l;
    }

    private static class TestRunnable implements Runnable {

        private Function function = null;
        final private String name;

        public TestRunnable(Function function, String name) {
            this.function = function;
            this.name = name;
        }

        public void run() {
            logger.debug(name + ": " + Thread.currentThread().getName());
            function.run();
        }
    }

    private interface Function {
        public void run();
    }

}
