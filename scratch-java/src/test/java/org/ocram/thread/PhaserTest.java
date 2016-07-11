package org.ocram.thread;

import static org.junit.Assert.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

public class PhaserTest {

    private static final Random random = new Random();

    @Test
    public void mainWaitsOnThreads() {

        final Phaser phaser = new Phaser(1);
        final CountDownLatch startLatch = new CountDownLatch(2);
        final AtomicBoolean threadStartLatch = new AtomicBoolean(false);

        int numThreads = 4;
        final AtomicInteger count = new AtomicInteger(numThreads);
        for(int i = 0; i < numThreads; ++i ) {
            final int tid = i;
            new Thread() {
                public void run() {
                    phaser.register();
                    if( threadStartLatch.compareAndSet(false, true) ) {
                        startLatch.countDown();
                    }
                    startLatch.countDown();
                    long sleep = (long) random.nextInt(100);
                    System.out.println( tid + ": " + sleep );
                    try {
                        Thread.currentThread().sleep(sleep);
                    } catch( InterruptedException e ) {
                    }
                    count.decrementAndGet();
                    System.out.println( tid + ": done" );
                    try {
                        Thread.currentThread().sleep(10);
                    } catch( InterruptedException e ) {
                    }

                    phaser.arriveAndDeregister();
                }
            }.start();
          }

          // allow threads to start and deregister self
          startLatch.countDown();
          phaser.arriveAndAwaitAdvance();
          System.out.println( "Done!" );
          assertEquals( "Not all threads finished!", 0, count.get());
    }

}
