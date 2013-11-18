package org.ocram.inet;

import java.net.URL;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class UrlTest extends ScratchBaseTest {

    @Test
    public void formattingTest() throws Exception { 
        URL url = new URL("http://www.blam.org/kabooie");
        URL otherUrl = new URL("http://www.blam.org/kabooie/");
        
        
        out.println( url.toExternalForm() ); 
        out.println( url.toString() );
        out.println( otherUrl.toExternalForm() ); 
        out.println( otherUrl.toString() ); 
    }
}
