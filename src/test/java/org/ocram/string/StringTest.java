package org.ocram.string;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class StringTest extends ScratchBaseTest {

    @Test
    public void indexOfTest() { 
        String shortenMe = "org.jbpm.persistence.session.PersistentStatefulSessionTest.testMeNOW";
        String shortTestMethod = shortenMe.substring( 0, shortenMe.lastIndexOf('.'));
        out.println( shortTestMethod );
        
        shortTestMethod = shortenMe.substring(shortTestMethod.lastIndexOf('.')+1);
        assertEquals( shortTestMethod, "PersistentStatefulSessionTest.testMeNOW" );
        
        shortTestMethod = shortenMe.substring( shortenMe.substring(0, shortenMe.lastIndexOf('.')).lastIndexOf('.')+1 );
        assertEquals( shortTestMethod, "PersistentStatefulSessionTest.testMeNOW" );
    }
    
    @Test
    public void java5StringMethod() { 
      String test = "Does this work?";
      boolean og = test.contains("work");
      out.println( test + " : " + og );
    }
    
}
