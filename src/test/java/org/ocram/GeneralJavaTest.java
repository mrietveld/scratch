package org.ocram;

import static java.lang.System.out;
import static junit.framework.Assert.assertTrue;

import java.io.Serializable;
import java.util.Arrays;

import org.junit.Test;
import org.ocram.collections.objects.Bam;

public class GeneralJavaTest extends ScratchBaseTest {

    @Test
    public void modTest() { 
        int testNum = 10;
        assertTrue( testNum % 2 == 0 );
        
        testNum = 9;
        assertTrue( testNum % 2 == 1 );
    }

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
    
    @Test
    public void byteArrayIsSerializable() { 
        byte [] testByteArray = new byte[10];
        Arrays.fill(testByteArray, "0".getBytes()[0]);
        Serializable serTestArray = null;
        try { 
            serTestArray = (Serializable) testByteArray;
        } catch(Exception e ){
            e.printStackTrace();
            fail( "Could not cast B[ to Serializable.");
        }
        
        assertNotNull(serTestArray);
    }
    

    @Test
    public void testNullPointerLoop() { 
        String [] arguments = new String [10];
    
        String een,twe,dri,vie,fij;
    
        een = "Er was een ";
        twe = StringIndexOutOfBoundsException.class.getSimpleName();
        dri = "+";
        vie = null;
        fij = dri + dri + dri;
        
        Integer one,two,thr;
        
        one = 123123123;
        two = 45238934;
        thr = null;
    
        Bam ich = new Bam();
        Bam ni = null;
        
        for( int i = 0; i < 9; ) { 
            try {
                switch(i) { 
                case 0:
                    arguments[i++] = ich.toString();
                case 1:
                    arguments[i++] = een + twe + dri + vie + fij;
                case 2:
                    arguments[i++] = vie.concat(een);
                case 3:
                    arguments[i++] = Integer.toString(thr);
                case 4:
                    arguments[i++] = Integer.toString(ich.geta_1());
                case 5:
                    arguments[i++] = Integer.toString(ni.geta_4());
                case 6:
                    arguments[i++] = dri + one + two;
                case 7: 
                    arguments[i++] = thr.toString();
                case 8:     
                    arguments[i++] = "asdfasdffasdf" + fij;
                }
            }
            catch( NullPointerException npe ) { 
                arguments[i-1] = "";
            }
        }
        
        for( int i = 0; i < 10; ++i ) { 
            out.println( "" + i + ": " + arguments[i] );
        }
    }
}
