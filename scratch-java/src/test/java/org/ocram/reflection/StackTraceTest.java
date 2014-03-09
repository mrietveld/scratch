package org.ocram.reflection;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class StackTraceTest extends ScratchBaseTest {

    @Test
    public void og() {
        assertEquals(this.getClass().getName(), new Throwable().getStackTrace()[0].getClassName());
        assertEquals("og", new Throwable().getStackTrace()[0].getMethodName());
        unsupported(this.getClass());
    }
    
    static void unsupported(Class<?> realClass) { 
        String methodName = (new Throwable()).getStackTrace()[1].getMethodName();
        throw new UnsupportedOperationException(methodName + " is not supported on the JAXB " + realClass.getSimpleName() + " implementation.");
    }

}
