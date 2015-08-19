package org.ocram.syntax;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ControlFlowTest extends ScratchBaseTest { 

    @Test
    public void finallyTest() {
        boolean failure = false;
        try {
            Integer.parseInt("asdf");
            return;
        } catch (Exception e) {
            failure = true;
        } finally {
            logger.debug( "FINALLY!" );
            if (!failure) {
                fail("This was not supposed to happen.. or was it?");
            }
        }
    }
    
    @Test
    public void varArgsTest() { 
       String [] input = { "a", "b", "NOWHATTHISHUH?" };
       
       String result = varArgs(input);
       String arrResult = array(input);
       
       assertEquals("var args works differently than arrays?", result, arrResult);
    }
    
    private String varArgs(Object... params) { 
        StringBuilder result = new StringBuilder();
        for( int i = 0; i < params.length; ++i ) { 
            result.append(params[i]).append(":");
        }
        System.out.println( "1] " + result);
        System.out.println( "s] " + array(params));
        return result.toString();
    }
    
    private String array(Object [] params) { 
        StringBuilder result = new StringBuilder();
        for( int i = 0; i < params.length; ++i ) { 
            result.append(params[i]).append(":");
        }
        return result.toString();
    }
}
