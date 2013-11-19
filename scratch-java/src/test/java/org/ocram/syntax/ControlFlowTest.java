package org.ocram.syntax;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ControlFlowTest extends ScratchBaseTest { 

    @Test
    public void finallyTest() {
        boolean failure = false;
        try {
            Integer.parseInt("asdf");
            return;
        } catch (Exception e) {
            failure = true;
        } finally {
            logger.debug( "FINALLY!" );
            if (!failure) {
                fail("This was not supposed to happen.. or was it?");
            }
        }
    }
}
