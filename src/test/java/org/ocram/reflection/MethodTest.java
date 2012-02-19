package org.ocram.reflection;

import static junit.framework.Assert.*;
import java.lang.reflect.Method;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

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
}
