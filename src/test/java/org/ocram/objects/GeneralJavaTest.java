package org.ocram.objects;

import java.util.Arrays;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class GeneralJavaTest extends ScratchBaseTest {

    @Test
    public void nullInstanceOfTest() { 
        Integer nullInt = null;
       
        try { 
            if( nullInt instanceof Integer ) { 
               fail("Null is not an instace of an Integer!");
            }
        }
        catch( Throwable t ) { 
            fail( t.getClass().getSimpleName() + " thrown: " + t.getMessage() );
        }
    }
    
    @Test
    public void compareCharPrimitiveArrays() { 
        String one = "One String to rule them all, One String to find them\\ One String to bring them all and in the heap bind them";
        char [] thisCharArr = one.toCharArray();
        char [] thatCharArr = new char [thisCharArr.length];
        for( int i = 0; i < thisCharArr.length; ++i ) { 
            thisCharArr[i] = thatCharArr[i];
        }
        assertTrue("The string length is not equal to the char [] length: " + one.length() + ", " + thisCharArr.length, 
                thisCharArr.length == one.length());
        
        assertTrue("Apparently, two primitive arrays should have an equal (identity) hash code?", thisCharArr != thatCharArr);
        assertTrue(Arrays.equals(thisCharArr, thatCharArr));
    }
}
