package org.ocram.test.rest.async;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;

import org.jboss.resteasy.logging.Logger;

/**
 * A lot of the ideas in this class have been taken from the 
 * <code>org.jboss.resteasy.coreAsynchronousDispatcher</code> class.
 * </p>
 * Unfortunately, the Resteasy asynchronous job mechanism has a number 
 * of bugs (most importantly, RESTEASY-682) which make it unusable for
 * our purposes.
 */
@ApplicationScoped
public class AsyncJobObserverExecutor {

    private final static Logger logger = Logger.getLogger(AsyncJobObserverExecutor.class);
    
    protected ExecutorService executor = null;
    private int threadPoolSize = 100;
    
    private Map<String, Future<Void>> jobs;
    private Cache<Void> cache;
    private int maxCacheSize = 100;
    
    private AtomicLong counter = new AtomicLong(0);

    public AsyncJobObserverExecutor() { 
        cache = new Cache<Void>(maxCacheSize);
        jobs = Collections.synchronizedMap(cache);
        if (executor == null) executor = Executors.newFixedThreadPool(threadPoolSize);
    }
    
    public void onEvent(final @Observes AsyncJobRequest jobRequest) { 
        Future<Void> future = executor.submit(new JobCallable(jobRequest.input));
        
        String id = "" + System.currentTimeMillis() + "-" + counter.incrementAndGet();
        jobs.put(id, future);
    }

    
    private static class Cache<T> extends LinkedHashMap<String, Future<T>> {
       private int maxSize = 100;

       public Cache(int maxSize) {
          this.maxSize = maxSize;
       }

       @Override
       protected boolean removeEldestEntry(Map.Entry<String, Future<T>> stringFutureEntry) {
          return size() > maxSize;
       }

       public void setMaxSize(int maxSize) {
          this.maxSize = maxSize;
       }
    }
    
    private static class JobCallable implements Callable<Void> {

        private String input;
        
        public JobCallable( String input ) { 
            this.input = input;
        }
        
        @Override
        public Void call() throws Exception {
            Thread.sleep(1000);
            logger.info("JOB: " + input);
            
            return null;
        } 
        
    }
}
