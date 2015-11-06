package org.ocram.reflection;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class StackTraceTest extends ScratchBaseTest {

    @Test
    public void og() {
        String thisMethodName = "og";

        assertEquals(this.getClass().getName(), new Throwable().getStackTrace()[0].getClassName());
        assertEquals(thisMethodName, new Throwable().getStackTrace()[0].getMethodName());

        try {
            unsupported(this.getClass());
        } catch( UnsupportedOperationException uoe ) {
            assertTrue( "Incorrect message in UOE", uoe.getMessage().contains(thisMethodName) );
        }
    }

    static void unsupported(Class<?> realClass) {
        String methodName = (new Throwable()).getStackTrace()[1].getMethodName();
        throw new UnsupportedOperationException(methodName + " is not supported on the " + realClass.getSimpleName() + " implementation.");
    }

}
