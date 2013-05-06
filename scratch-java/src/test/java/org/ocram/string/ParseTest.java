package org.ocram.string;

import java.util.Scanner;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ParseTest extends ScratchBaseTest {

    @Test
    public void testLong() throws Exception {
        long asdf = 120301201L;
        Long testLong = new Long(asdf);
        assertTrue(testLong.longValue() == 120301201L);
    }

    @Test
    public void testReplaceAll() throws Exception {
        String test = "asdf<\r>asdfasdf<\n>asdfasdfsdf<\r\n>asdfasdfdas<\n\r>asdfd";

        String testRep = test.replaceAll("\\r", "");
        assertTrue(testRep.equals("asdf<>asdfasdf<\n>asdfasdfsdf<\n>asdfasdfdas<\n>asdfd"));

        testRep = testRep.replaceAll("\\n", "");
        assertTrue(testRep.equals("asdf<>asdfasdf<>asdfasdfsdf<>asdfasdfdas<>asdfd"));

        testRep = test.replaceAll("\r|\n", "");
        assertTrue(testRep.equals("asdf<>asdfasdf<>asdfasdfsdf<>asdfasdfdas<>asdfd"));
    }

    @Test
    public void testSplit() throws Exception {
        String url = "/og/ooga/sessoin/23/startProcess?param=mala&raram=para";
        
        String [] parts = url.split("[/?&]");
        
        for( String og : parts ) { 
            System.out.println(og);
        }
    }
}
