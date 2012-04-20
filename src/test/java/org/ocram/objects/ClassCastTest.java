package org.ocram.objects;

import static junit.framework.Assert.*;
import java.io.IOException;

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
}
