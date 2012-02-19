package org.ocram.properties;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class SystemPropertiesTest extends ScratchBaseTest {

    private static final String INT_PROP = "integer.property";
    @Test
    public void increaseIntegerPropertyTest() { 
      
        String propVal = System.getProperty(INT_PROP);
        assertNull(propVal);
        
        increaseProperty(INT_PROP);
        propVal = System.getProperty(INT_PROP);
        assertTrue(Integer.parseInt(propVal) == 0);
        
        increaseProperty(INT_PROP);
        propVal = System.getProperty(INT_PROP);
        assertTrue(Integer.parseInt(propVal) == 1);
        
        increaseProperty(INT_PROP);
        propVal = System.getProperty(INT_PROP);
        assertTrue(Integer.parseInt(propVal) == 2);
    }
    
    private void increaseProperty(String propertyName) { 
        String val = System.getProperty(propertyName);
        int valInt = 0;
        if( val != null ) { 
            valInt = Integer.parseInt(val) + 1;
        }
        System.setProperty(propertyName, String.valueOf(valInt));
    }
}
