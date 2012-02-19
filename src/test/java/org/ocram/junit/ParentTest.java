package org.ocram.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ParentTest extends ScratchBaseTest {

    @BeforeClass
    public static void beforeClass() { 
        printMethodName();
    }
    
    @Before
    public void before() { 
        printMethodName();
    }
    
    @After
    public void after() { 
        printMethodName();
    }
    
    @AfterClass
    public static void afterClass() { 
        printMethodName();
    }
    
    protected static void printMethodName() { 
        String className = Thread.currentThread().getStackTrace()[2].getClassName();
        String out 
                = className.substring(className.lastIndexOf(".") + 1)
                + ": " 
                + Thread.currentThread().getStackTrace()[2].getMethodName();
        System.out.println(out);
    }
    
    @Test
    public void testa() { 
        printMethodName();
    }
    
    @Test
    public void testb() { 
        printMethodName();
    }
}
