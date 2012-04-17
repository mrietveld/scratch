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
       Runner one = new Runner(lock); 
       Runner two = new Runner(lock); 
       System.out.println( "Runner one is Thread " + one.getId() );
       System.out.println( "Runner two is Thread " + two.getId() );
       
       one.start();
       two.start();
       
       System.out.println( "Getting ready to join two/" + two.getId() );
       two.join();
       System.out.println("Two has joined.");
    }
  
    private static class Runner implements Runnable {

        private Thread thread;
        private Object lock;
        private final int id = idGenerator.getAndIncrement();
        
        public Runner(Object lock) { 
            this.lock = lock;
        }

        public void start() {
            thread = new Thread(this);
            System.out.println("[" + id + "] is NOT alive: " + thread.isAlive() );
            thread.start();
        }
        
        public void run() {
            System.out.println( "[" + id + "] running." );
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
