package org.ocram.string;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class RegexTest extends ScratchBaseTest {

    @Test
    public void testFloatRegex() {
        String regex = "^\\d[\\d\\.]{1,9}(E-?\\d{1,2})?f?$";
        String floot = String.valueOf(Float.MAX_VALUE);
        assertTrue( "[" + floot + "] | [" + regex + "]", floot.matches(regex));
        floot = String.valueOf(Float.MIN_NORMAL);
        assertTrue( "[" + floot + "] | [" + regex + "]", floot.matches(regex));
        floot = String.valueOf(Float.MIN_VALUE);
        assertTrue( "[" + floot + "] | [" + regex + "]", floot.matches(regex));
        floot = String.valueOf(new Float(103030303.0202030504502101f));
        assertTrue( "[" + floot + "] | [" + regex + "]", floot.matches(regex));
        System.out.println( floot.length()  + ": " + floot);
        
        floot = "1.00f";
        Float.parseFloat(floot);
        assertTrue( "[" + floot + "] | [" + regex + "]", floot.matches(regex));
        floot = "1.00E32f";
        Float.parseFloat(floot);
        assertTrue( "[" + floot + "] | [" + regex + "]", floot.matches(regex));
    
        floot = ".00f";
        assertFalse( "[" + floot + "] | [" + regex + "]", floot.matches(regex));
    }

    @Test
    public void testDotStarRegexes() {
        String groupId = "Crusaders";
        String regex = ".rusade.*";
       
        assertTrue( "'" + regex + "' does not match '" + groupId + "'", groupId.matches(regex) );
    }
    
    @Test
    public void caseInsensitiveExactMatchTest() {
        String[] methodStrs = new String[3];
        methodStrs[0] = "selectblahblah";
        methodStrs[1] = "selectBlahblah";
        methodStrs[2] = "SelectBlahBLAH";

        for (int i = 0; i < methodStrs.length; ++i) {
            assertTrue(methodStrs[0] + " should be equal to [" + methodStrs[i] + "]", methodStrs[0].matches("(?i)" + methodStrs[i]));
        }
    }

    
    @Test
    public void caseInsensitiveMatchTest() {
        String[] queryStrs = new String[3];
        queryStrs[0] = " select blah blah";
        queryStrs[1] = " sElect blah blah";
        queryStrs[2] = "  SELECT blah blah";

        String regex = "(?i) *select .*";
        String badRegex = "(?i).*(delete|update).*";

        for (int i = 0; i < queryStrs.length; ++i) {
            assertTrue(queryStrs[i] + " should have matched [" + regex + "]", queryStrs[i].matches(regex));
            assertFalse(queryStrs[i] + " should NOT have matched [" + badRegex + "]", queryStrs[i].matches(badRegex));
        }

        queryStrs[0] = "selec adsf fds (DELete from)";
        queryStrs[1] = "update select adsf fds";
        queryStrs[2] = " select_ delete adsf fds";
        for (int i = 0; i < queryStrs.length; ++i) {
            assertFalse(queryStrs[i] + " should NOT have matched [" + regex + "]", queryStrs[i].matches(regex));
            assertTrue(queryStrs[i] + " should have matched [" + badRegex + "]", queryStrs[i].matches(badRegex));
        }
    }

    @Test
    public void scannerTest() {
        String[] tags = { "tag1", "tag2", "adsf", "whoahPigsAreFlying!" };
        String input = "{ ";
        int i = 0;
        input += tags[i];
        for (i = 1; i < tags.length; ++i) {
            input += ", " + tags[i];
        }
        input += "} ";

        String delimRegex = "\\s*[{},]\\s*";
        Scanner s = new Scanner(input).useDelimiter(delimRegex);
        
        i = 0;
        while (s.hasNext()) {
            assertEquals(s.next(), tags[i++]);
        }
        
        input = "{}";
        s = new Scanner(input).useDelimiter(delimRegex);
        int count = 0;
        while( s.hasNext("\\S+") && count < 10) { 
            count++;
        }
        assertEquals(0,count);
        
        input = "random wi 235%a, asdf! !23";
        s = new Scanner(input).useDelimiter(delimRegex);
        count = 0;
        while( s.hasNext("\\S+") && count < 10) { 
            count++;
        }
        assertEquals(0,count);
        
    }
    
    @Test
    public void numParamRegexTest() { 
       String regex = "^\\d+[li]?$";
       
       assertTrue( "23l".matches(regex));
       assertFalse( "2b3l".matches(regex));
       assertTrue( "3".matches(regex));
       assertTrue( "34949".matches(regex));
       assertTrue( "34949i".matches(regex));
       assertFalse( "l23l".matches(regex));
       assertTrue( "l23l", "l23l".matches(".*l$"));
       assertTrue( "23.3", "23.3".matches("[0-9\\.]*$"));
       assertFalse( "23.3a", "23.3a".matches("[0-9\\.]*$"));
    }
    
    @Test
    public void replaceTest() { 
        String og = "maro ooga q=1 q=.2 q=.3"; 
        logger.debug( og );
        if( og.matches(".*q=\\..*") ) { 
            og = og.replaceAll("q=\\.", "q=0.");
        } else { 
            fail( "nononos..");
        }
        logger.debug( og );
    }
    
    @Test
    public void endMatchesTest() { 
        String og = "statuses";
        assertTrue( "nononos..", og.matches(".*[aieou]s$") );
        assertFalse( "huh?", og.matches("[xzwfg]s$") );
    }
    
    @Test
    public void scratch() { 
        String pat = "^(TRUE|FALSE)$";
        assertTrue("TRUE".matches(pat));
        assertTrue("FALSE".matches(pat));
        assertTrue(Boolean.parseBoolean("TRUE"));
        assertTrue(!Boolean.parseBoolean("FALSE"));
    }
}
