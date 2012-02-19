package org.ocram.reflection;

import static junit.framework.Assert.assertEquals;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ReflectionTest extends ScratchBaseTest {

    @Test
    public void methodNameTest() { 
       String correctName = "methodNameTest";
       String testName = Thread.currentThread().getStackTrace()[1].getMethodName();
       assertEquals("Method name should be " + correctName + " not " + testName, correctName, testName );
    }
   
}
