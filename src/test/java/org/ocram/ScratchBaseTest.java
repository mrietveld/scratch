package org.ocram;

import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

public abstract class ScratchBaseTest extends Assert {

    @Rule
    public TestName testName = new TestName();
    
    protected PrintStream out = System.out;
    
    @Before
    public void before() { 
        
    }
    
}
