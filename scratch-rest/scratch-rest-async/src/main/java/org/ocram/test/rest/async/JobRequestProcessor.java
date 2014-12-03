package org.ocram.test.rest.async;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import javax.enterprise.context.ApplicationScoped;

import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.logging.Logger;
import org.jboss.resteasy.annotations.GZIP;
import org.jboss.resteasy.logging.Logger;

@ApplicationScoped
@GZIP
public class JobRequestProcessor {

    private final static Logger logger = Logger.getLogger(JobRequestProcessor.class);
    
    private Map<String, Future<Void>> jobs;
    private Cache<Void> cache;
    private int maxCacheSize = 100;
    
    public JobRequestProcessor() { 
        Cache<Void> cache = new Cache<Void>(maxCacheSize);
        jobs = Collections.synchronizedMap(cache);
    }
    
    public void processJob(String input) throws Exception { 
        Thread.sleep(5*1000);
        logger.info("JOB: " + input);
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
    
}
