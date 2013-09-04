package org.ocram.thread.specific;

import org.junit.Test;

public class VolatileVsSynchronizedTest {

    @Test
    public void synchronizedDoesntMeanVolatile() throws Exception { 

        ValueHolder singleHolder = new ValueHolder();
        singleHolder.value = new ValueHolder();
        
        ValueHolder chain = (ValueHolder) singleHolder.value;
        int length = 10;
        while( length-- > 0 ) { 
            chain.value = new ValueHolder();
            chain = (ValueHolder) chain.value;
        }
        
        int size = 750;
        Thread [] threads = new Thread[size];
        for( int i = 0; i < size; ++i ) { 
           Runnable target = new ChangeValueRunnable(singleHolder); 
           threads[i] = new Thread(target);
           threads[i].setName("" + i);
           threads[i].start();
        }
        System.out.println( "Threads started" );
     
        int dead = 0;
        for( int i = 0; i < size; ++i ) { 
            while( threads[i].isAlive() ) { 
                Thread.sleep(5000);
            }
            ++dead;
        }
        System.out.println( dead ); 
    }
    
    
}
