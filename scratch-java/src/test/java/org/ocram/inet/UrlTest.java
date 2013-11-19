package org.ocram.inet;

import java.net.URL;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class UrlTest extends ScratchBaseTest {

    @Test
    public void formattingTest() throws Exception { 
        URL url = new URL("http://www.blam.org/kabooie");
        URL otherUrl = new URL("http://www.blam.org/kabooie/");
        
        
        logger.debug( url.toExternalForm() ); 
        logger.debug( url.toString() );
        logger.debug( otherUrl.toExternalForm() ); 
        logger.debug( otherUrl.toString() ); 
    }
}
