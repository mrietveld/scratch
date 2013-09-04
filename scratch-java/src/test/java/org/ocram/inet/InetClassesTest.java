package org.ocram.inet;

import java.net.InetAddress;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class InetClassesTest extends ScratchBaseTest {

    @Test
    public void localhostString() throws Exception { 
        out.println( InetAddress.getLocalHost().toString() );
    }
}
