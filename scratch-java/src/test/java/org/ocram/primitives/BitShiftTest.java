package org.ocram.primitives;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class BitShiftTest extends ScratchBaseTest {

    @Test
    public void testBitWiseOr() { 
       short activate = 1;
       logger.debug( "{}", createPermission(false, false, false, false, false, true) );
       logger.debug( "{}", createPermission(false, false, false, false, true, false) );
       logger.debug( "{}", createPermission(false, false, false, true, false, false) );
       logger.debug( "{}", createPermission(false, false, true, false, false, false) );
    }
    
    static short createPermission(boolean initator, boolean stakeholders, boolean potential, boolean actual, boolean businessAdmin, boolean recipients ) { 
       short permission = 0;
       if(initator) { 
          ++permission;
       }
       permission <<= 1;
       
       if(stakeholders) { 
           ++permission;
       }
       permission <<= 1;
       
       if(potential) { 
           ++permission;
       }
       permission <<= 1;
       
       if(actual) { 
           ++permission;
       }
       permission <<= 1;
       
       if(businessAdmin) { 
           ++permission;
       }
       permission <<= 1;
       
       if(recipients) { 
           ++permission;
       }
       
       return permission;  
    }
}
