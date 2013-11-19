package org.ocram.thread;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.junit.ParentTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadGroupDemo extends ScratchBaseTest {

    private final static Logger logger = LoggerFactory.getLogger(ThreadGroupDemo.class);
            
    @Test
    public void runThreadGroup() {
        ThreadGroup groupA = new ThreadGroup("Group A");
        ThreadGroup groupB = new ThreadGroup("Group B");
        TestThread ob1 = new TestThread("One", groupA);
        TestThread ob2 = new TestThread("Two", groupA);
        TestThread ob3 = new TestThread("Three", groupB);
        TestThread ob4 = new TestThread("Four", groupB);
        logger.debug("\\nHere is output from list():");
        groupA.list();
        groupB.list();
        logger.debug("Suspending Group A");
        Thread tga[] = new Thread[groupA.activeCount()];
        groupA.enumerate(tga); // get threads in group
        for (int i = 0; i < tga.length; i++) {
            ((TestThread) tga[i]).mysuspend(); // suspend each thread
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            logger.debug("Main thread interrupted.");
        }
        logger.debug("Resuming Group A");
        for (int i = 0; i < tga.length; i++) {
            ((TestThread) tga[i]).myresume(); // resume threads in group}
            // wait for threads to finish
            try {
                logger.debug("Waiting for threads to finish.");
                ob1.join();
                ob2.join();
                ob3.join();
                ob4.join();
            } catch (Exception e) {
                logger.debug("Exception in Main thread");
            }
            logger.debug("Main thread exiting.");
        }
    }
    
    private static final class TestThread extends Thread { 
        boolean suspendFlag;

        TestThread(String threadname, ThreadGroup tgOb) {
            super(tgOb, threadname);
            logger.debug("New thread: " + this);
            suspendFlag = false;
            start(); // Start the thread
        }

        // This is the entry point for thread.
        public void run() {
            try {
                for (int i = 5; i > 0; i--) {
                    logger.debug(getName() + ": " + i);
                    Thread.sleep(1000);
                    synchronized (this) {
                        while (suspendFlag) {
                            wait();
                        }
                    }
                }
            } catch (Exception e) {
                logger.debug("Exception in " + getName());
            }
            logger.debug(getName() + " exiting.");
        }

        void mysuspend() {
            suspendFlag = true;
        }

        synchronized void myresume() {
            suspendFlag = false;
            notify();
        }
    }
}
