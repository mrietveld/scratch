package org.ocram.objects;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class GeneralJavaTest extends ScratchBaseTest {

    @Test
    public void nullInstanceOfTest() { 
        Integer nullInt = null;
       
        try { 
            if( nullInt instanceof Integer ) { 
               out.println("Yup!"); 
            }
            else { 
                out.println("Null is not an instance of Integer.");
            }
        }
        catch( Throwable t ) { 
            fail( t.getClass().getSimpleName() + " thrown: " + t.getMessage() );
        }
    }
}
