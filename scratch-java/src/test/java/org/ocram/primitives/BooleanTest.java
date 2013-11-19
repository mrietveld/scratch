package org.ocram.primitives;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class BooleanTest extends ScratchBaseTest {

    @Test
    public void stringTest() { 
       logger.debug(Boolean.TRUE.toString()); 
       logger.debug(Boolean.FALSE.toString()); 
    }
}
