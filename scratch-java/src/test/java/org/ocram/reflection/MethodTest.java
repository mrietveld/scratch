package org.ocram.reflection;

import static java.lang.System.out;
import static junit.framework.Assert.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.collections.objects.Bam;

public class MethodTest extends ScratchBaseTest {

    @Test
    public void useMethods() throws Exception { 
       Method method = MethodDepot.class.getMethod("returnTwo", (Class []) null); 
       MethodDepot object = new MethodDepot();
       
       Object result = method.invoke(object, (Object [])null);
      
       int resultVal = ((Integer) result).intValue();
       assertTrue("Result is not 2, but " + resultVal, resultVal == 2);
    }
    
    private static class MethodDepot {
        public int returnTwo() { 
           return 2; 
        }
    }
    
    @Test
    public void testMethodReflection( ) { 
        Bam bam = new Bam();
        Method method = null;
        String methodName = "geta_";
    
        Integer arr[] = new Integer [40];
        methods:
            for( int i = 1; i < 10; ++i ) {
                try {
                    method = Bam.class.getDeclaredMethod(methodName + i, (Class[]) null);
                }
                catch( NoSuchMethodException nsme ) { 
                    out.println( methodName + i + " does not exist." );
                    continue methods;
                }
                if( method != null ) { 
                    try {
                        arr[i] = (Integer) method.invoke(bam);
                        out.println( methodName + i + ": " + arr[i]);
                    } catch (IllegalArgumentException iae) {
                        out.println( iae.getClass().getSimpleName() + ": " + iae.getMessage() );
                    } catch (IllegalAccessException iae) {
                        out.println( iae.getClass().getSimpleName() + ": " + iae.getMessage() );
                    } catch (InvocationTargetException ite) {
                        out.println( ite.getClass().getSimpleName() + ": " + ite.getMessage() );
                    }
                }
            }
    
    }
}
