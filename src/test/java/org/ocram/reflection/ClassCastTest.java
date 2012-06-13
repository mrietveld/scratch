package org.ocram.reflection;

import static junit.framework.Assert.*;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ClassCastTest extends ScratchBaseTest {

    @Test
    public void exceptionCastMethod() { 
        IOException ioe = new IOException("This is a test exception");
        ClassCastException cause = new ClassCastException();
        ioe.initCause(cause);
        
        assertEquals(ioe.getCause(), cause);
    }
    
    @Test
    public void primitiveCastBoxedTest() { 
        HashMap<String, Object> params = new HashMap<String, Object>();
        
        int one = 23;
        params.put("one", one);
        int eno = (Integer) params.get("one");
        assertTrue( eno == one );
    }
    
    @Test
    public void nullInstanceOfTest() { 
        String og = null;
        
        assertFalse( "null is no instance of String", og instanceof String );
        og = "og";
        assertTrue( "null is an instance of String", og instanceof String );
        
    }
}
