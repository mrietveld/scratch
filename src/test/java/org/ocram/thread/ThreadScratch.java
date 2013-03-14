package org.ocram.thread;

import junit.framework.Assert;

import org.junit.Test;

public class ThreadScratch {

    @Test
    public void badThreadCode() throws Exception { 
        final boolean [] success = new boolean[1];
        success[0] = false;
        
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                success[0] = true;
            }
        }).start();
        
        Thread.sleep(1000);
        
        Assert.assertTrue( success[0] );
    }
}
