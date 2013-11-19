package org.ocram;

import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ScratchBaseTest extends Assert {

    protected final Logger logger;
    
    public ScratchBaseTest() { 
         logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Rule
    public TestName testName = new TestName();
    
    protected PrintStream out = System.out;
    
    @Before
    public void before() { 
        
    }
    
}
