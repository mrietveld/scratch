package org.ocram.primitives;

import org.junit.Test;

public class BitShiftTest {

    @Test
    public void testBitWiseOr() { 
       short activate = 1;
       System.out.println( createPermission(false, false, false, false, false, true) );
       System.out.println( createPermission(false, false, false, false, true, false) );
       System.out.println( createPermission(false, false, false, true, false, false) );
       System.out.println( createPermission(false, false, true, false, false, false) );
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
