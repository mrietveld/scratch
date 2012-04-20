package org.ocram.thread;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ThreadMethodsTest extends ScratchBaseTest {

    private volatile Object lock = new Object();
    private static AtomicInteger idGenerator = new AtomicInteger(1);
    private final int lockTime = 5 * 1000;
    
    @Test
    public void joinTest() throws Exception { 
       Runner [] runners = new Runner [600];
       
       for( int i =0; i < runners.length; ++i ) { 
           runners[i] = new Runner(lock);
           runners[i].start();
       }
       
       Runner joiner = runners[runners.length-5];
       assertTrue( joiner.ran == false );
       joiner.join();
       assertTrue( joiner.ran == false );
       System.out.println("Two has joined.");
    }
  
    private static class Runner implements Runnable {

        private Thread thread;
        private Object lock;
        private final int id = idGenerator.getAndIncrement();
        private volatile boolean ran = false;
        
        public Runner(Object lock) { 
            this.lock = lock;
        }

        public void start() {
            thread = new Thread(this);
            thread.start();
        }
        
        public void run() {
            System.out.println( "[" + id + "] running." );
            ran = true;
            synchronized(lock) { 
                System.out.println( "[" + id + "] has lock and will sleep." );
                try { 
                    Thread.sleep(5*1000);
                }
                catch(Exception e) { 
                    System.out.println( "Unable to sleep: " + e.getMessage() );
                }
                System.out.println( "[" + id + "] is releasing lock." );
            }
        }
        
        public synchronized void join() throws InterruptedException {
            System.out.println( "[" + id + "] is about to join." );
            thread.join();
        }
        
        public int getId() { 
            return id;
        }
        
    }
    
}
