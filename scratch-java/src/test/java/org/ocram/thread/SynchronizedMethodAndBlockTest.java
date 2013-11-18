package org.ocram.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SynchronizedMethodAndBlockTest extends ScratchBaseTest {

    private static Logger logger = LoggerFactory.getLogger(SynchronizedMethodAndBlockTest.class);

    @Test
    public void doTheyOverlapTest() {

        MethodAndBlockRunnable run = new MethodAndBlockRunnable();
        Thread syncThread = new Thread(run);
        syncThread.start();

        logger.info("before sync");
        synchronized (run) {
            logger.info("in sync");
            run.methodAfterBlockSignal.countDown();
            logger.info("after countdown");
        }
        logger.info("after sync");
    }

    private class MethodAndBlockRunnable implements Runnable {

        public CountDownLatch methodAfterBlockSignal = new CountDownLatch(1);

        @Override
        public void run() {
            logger.info("*before sync");
            try { 
                synchronized(this) { 
                    syncd();
                }
            } catch( InterruptedException ie ) { 
               fail( ie.getMessage() ); 
            }
            logger.info("* after sync");
        }

        private synchronized void syncd() throws InterruptedException {
            logger.info("* in sync");
             methodAfterBlockSignal.await();
            logger.info("* after countdown");
        }

    }

}
