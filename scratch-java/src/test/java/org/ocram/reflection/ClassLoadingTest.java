package org.ocram.reflection;

import org.junit.Test;
import org.kie.api.runtime.process.ProcessInstance;
import org.ocram.ScratchBaseTest;

public class ClassLoadingTest extends ScratchBaseTest {

    @Test
    public void tcclTest() {
       ClassLoader tccl = Thread.currentThread().getContextClassLoader();
       assertNotNull( "TCCL is null!", tccl);
    }

    @Test
    public void classLocationTest() throws Exception {
        String classLoc = ProcessInstance.class.getProtectionDomain().getCodeSource().getLocation().toExternalForm();
        System.out.println( classLoc );
    }

}
