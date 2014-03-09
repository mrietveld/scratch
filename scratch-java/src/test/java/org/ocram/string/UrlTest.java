package org.ocram.string;

import static org.junit.Assert.*;
import java.net.URL;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class UrlTest extends ScratchBaseTest {
    
    @Test
    public void UrlPartsTest() throws Exception { 
       
        String protocol = "http";
        String server = "server";
        int port = 8080;
        String base = "base";
        String call = "rest/do/that/thing?param=whyNot";
        URL url = new URL( protocol + "://" + server + ":" + port + "/" + base + "/" + call );
        
        assertEquals(protocol, url.getProtocol());
        assertEquals(server, url.getHost());
        assertEquals(port, url.getPort());
        String urlBase = url.getPath().substring(1).replaceAll("/.*", "");
        System.out.println( "[" + urlBase + "]");
        assertEquals( base, urlBase);
    }
}
