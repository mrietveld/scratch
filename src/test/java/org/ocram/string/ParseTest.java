package org.ocram.string;

import java.util.Scanner;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
public class ParseTest extends ScratchBaseTest {
	
    @Test
	public void testLong() throws Exception { 
		long asdf = 120301201L;
		Long testLong = new Long(asdf);
		assertTrue( testLong.longValue() == 120301201L );
	}
	
    @Test
	public void testReplaceAll() throws Exception {
		String test = "asdf<\r>asdfasdf<\n>asdfasdfsdf<\r\n>asdfasdfdas<\n\r>asdfd";
		
		String testRep = test.replaceAll("\\r", "");
		assertTrue( testRep.equals("asdf<>asdfasdf<\n>asdfasdfsdf<\n>asdfasdfdas<\n>asdfd") );
		
		testRep = testRep.replaceAll("\\n", "");
		assertTrue( testRep.equals("asdf<>asdfasdf<>asdfasdfsdf<>asdfasdfdas<>asdfd") );
		
		testRep = test.replaceAll("\r|\n", "");
		assertTrue( testRep.equals("asdf<>asdfasdf<>asdfasdfsdf<>asdfasdfdas<>asdfd") );
	}

	// ---------------
	
    @Test
	public void testRegex() { 
	
		String alleenEenEmailAdres = "marco@log.com; marco@log.com";
		String nietAlleenEenEmailAdres = "marco@log.com;marco@log.com";
		String regex = "^[\\w]([\\w\\.-]*[\\w])?@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$'";

		// assertTrue( alleenEenEmailAdres.matches(regex) );
		if( nietAlleenEenEmailAdres.matches( regex ) ) { 
			fail( "Hacker krijgt ook een email!" );
		}
	}
    
    @Test
    public void caseInsensitiveMatchTest() { 
        String [] queryStrs = new String [3];
        queryStrs[0] = " select blah blah";
        queryStrs[1] = " sElect blah blah";
        queryStrs[2] = "  SELECT blah blah";
       
        String regex = "(?i) *select .*";
        String badRegex = "(?i).*(delete|update).*";
        
        for( int i = 0; i < queryStrs.length; ++i ) { 
            assertTrue( queryStrs[i] + " should have matched [" + regex + "]", queryStrs[i].matches(regex) ); 
            assertFalse( queryStrs[i] + " should NOT have matched [" + badRegex + "]", queryStrs[i].matches(badRegex) );
        }
        
        queryStrs[0] = "selec adsf fds (DELete from)";
        queryStrs[1] = "update select adsf fds";
        queryStrs[2] = " select_ delete adsf fds";
        for( int i = 0; i < queryStrs.length; ++i ) { 
            assertFalse( queryStrs[i] + " should NOT have matched [" + regex + "]", queryStrs[i].matches(regex) ); 
            assertTrue( queryStrs[i] + " should have matched [" + badRegex + "]", queryStrs[i].matches(badRegex) );
        }
    }
    
    @Test
    public void scannerTest() { 
        String [] tags = { "tag1", "tag2", "adsf", "whoahPigsAreFlying!" };
       String input = "{ ";
       int i = 0;
       input += tags[i];
       for( i = 1; i < tags.length; ++i ) { 
          input += ", " + tags[i]; 
       }
       input += "} ";
                   
       Scanner s = new Scanner(input).useDelimiter("\\s*[{},]\\s*");
       i = 0;
       while(s.hasNext()) { 
          assertEquals(s.next(), tags[i++]);
       }
    }
}
