package org.ocram.primitives;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class NumberTest extends ScratchBaseTest {

    @Test
    public void maxNumsTest() { 
        logger.debug("Short: ");
        logger.debug(" " + Short.MAX_VALUE);
        logger.debug(" " + Short.MIN_VALUE);
        logger.debug("Integer: ");
        logger.debug(" " + Integer.MAX_VALUE);
        logger.debug(" " + Integer.MIN_VALUE);
        logger.debug("Long: ");
        logger.debug(" " + Long.MAX_VALUE);
        logger.debug(" " + Long.MIN_VALUE);
    }
    
}
