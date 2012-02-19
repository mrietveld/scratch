package org.ocram.reflection;

import org.drools.persistence.jpa.JpaTimerJobInstance;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ClassTest extends ScratchBaseTest { 

    @Test
    public void extendClassTest() { 
        JpaTimerJobInstance og = new JpaTimerJobInstance(null, null, null, null, null);
        
        Class<?> clazz = og.getClass();
        out.println( clazz.getSimpleName() );
        out.println( "d: " + clazz.getDeclaringClass() );
        out.println( "e: " + clazz.getEnclosingClass() );
        int i = 0;
        do { 
            out.println( i++ + ": " + clazz.getSimpleName() );
            clazz = clazz.getSuperclass();
        }
        while( clazz!= null );
    }
}
