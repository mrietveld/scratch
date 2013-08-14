package org.ocram.reflection;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.reflection.proxy.objects.ClassInterface;

public class StackTraceTest extends ScratchBaseTest {

    @Test
    public void og() {
        out.println(new Throwable().getStackTrace()[0].getClassName());
        out.println(new Throwable().getStackTrace()[0].getMethodName());
    }

}
