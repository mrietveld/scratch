package org.ocram.junit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ChildTest extends ParentTest {

    @BeforeClass
    public static void voorKlass() { 
        printMethodName();
    }
    
    @Before
    public void ervoor() { 
        printMethodName();
    }
    
    @After
    public void erna() { 
        printMethodName();
    }
    
    @AfterClass
    public static void naKlass() { 
        printMethodName();
    }
    
    @Test
    public void test1() { 
        printMethodName();
    }

    @Test
    public void test2() { 
        printMethodName();
    }
}
