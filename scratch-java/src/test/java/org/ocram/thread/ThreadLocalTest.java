package org.ocram.thread;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ThreadLocalTest extends ScratchBaseTest {

    private static final ThreadLocal<Long> threadLocalLong = new ThreadLocal<Long>();
    
    @Test
    public void isNull() { 
        assertNotNull(threadLocalLong);
        assertNull(threadLocalLong.get());
        threadLocalLong.set(new Long(2));
        assertNotNull(threadLocalLong.get());
        
        threadLocalLong.set(null);
        assertNull(threadLocalLong.get());
    }
}
