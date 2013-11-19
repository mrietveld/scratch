package org.ocram.reflection;

import org.drools.persistence.jpa.JpaTimerJobInstance;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ClassTest extends ScratchBaseTest { 

    @Test
    public void extendClassTest() { 
        JpaTimerJobInstance og = new JpaTimerJobInstance(null, null, null, null, null);
        
        Class<?> clazz = og.getClass();
        logger.debug( clazz.getSimpleName() );
        logger.debug( "d: " + clazz.getDeclaringClass() );
        logger.debug( "e: " + clazz.getEnclosingClass() );
        int i = 0;
        do { 
            logger.debug( i++ + ": " + clazz.getSimpleName() );
            clazz = clazz.getSuperclass();
        }
        while( clazz!= null );
    }
}
