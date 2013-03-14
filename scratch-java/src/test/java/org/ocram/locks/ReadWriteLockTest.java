package org.ocram.locks;

import static junit.framework.Assert.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ReadWriteLockTest extends ScratchBaseTest {

    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");

    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    @Test
    public void readLockTest() {
        boolean noException = true;
        try { 
            readWriteLock.readLock().lock();
            if( readWriteLock.isWriteLockedByCurrentThread() ) { 
                readWriteLock.writeLock().unlock();
            }
            else { 
                readWriteLock.readLock().unlock();
            }
        }
        catch( IllegalMonitorStateException imse ) { 
            imse.printStackTrace();
            noException = false;
        }
        assertTrue(noException);
    }
}
