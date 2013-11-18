package org.ocram.locks;

import java.text.SimpleDateFormat;
import java.util.concurrent.locks.ReentrantLock;
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
    
    @Test
    public void reentrantLockTest() { 
        ReentrantLock lock = new ReentrantLock();
        
        int l = 0;
        while( lock.isLocked() ) { 
            ++l;
            lock.unlock();
        }
        assertEquals(l, 0);
        
        lock.lock();
        
        lock.lock();
       
        while( lock.isLocked() ) { 
            ++l;
            lock.unlock();
        }
        assertEquals(l, 2);
    }
}
