package org.ocram.other;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class MediaTypeTest extends ScratchBaseTest {

    @Test
    public void compatible() { 
       assertTrue( MediaType.APPLICATION_XML_TYPE.isCompatible(MediaType.APPLICATION_XML_TYPE) );
    }
}
