package org.ocram.primitives;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class NumberTest extends ScratchBaseTest {

    @Test
    public void maxNumsTest() { 
        out.println("Short: ");
        out.println(" " + Short.MAX_VALUE);
        out.println(" " + Short.MIN_VALUE);
        out.println("Integer: ");
        out.println(" " + Integer.MAX_VALUE);
        out.println(" " + Integer.MIN_VALUE);
        out.println("Long: ");
        out.println(" " + Long.MAX_VALUE);
        out.println(" " + Long.MIN_VALUE);
    }
    
}
