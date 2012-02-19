package nl.rietveld.test;

import static java.lang.System.out;
public class ParseTest {
	public ParseTest() {}
	

	
	public void testLong() throws Exception { 
		long asdf = 120301201L;
		Long testLong = new Long(asdf);
		out.println(testLong.toString());
	}
	
	public void testReplaceAll() throws Exception {
		String test = "asdf<\r>asdfasdf<\n>asdfasdfsdf<\r\n>asdfasdfdas<\n\r>asdfd";
		
		out.println( "---" );
		out.println( test );
		
		String testRep = test.replaceAll("\\r", "");
		
		out.println( "---" );
		out.println( testRep );
		
		testRep = testRep.replaceAll("\\n", "");
		
		out.println( "---" );
		out.println( testRep );
		
		testRep = test.replaceAll("\r|\n", "");
		
		out.println( "---" );
		out.println( testRep );
		
	}

	// ---------------
	
	public void testRegex() { 
	
		String alleenEenEmailAdres = "marco@log.com; marco@log.com";
		String nietAlleenEenEmailAdres = "marco@log.com;marco@log.com";
		String regex = "^[\\w]([\\w\\.-]*[\\w])?@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$'";
		out.println(regex);
		
		if( nietAlleenEenEmailAdres.matches( regex ) ) { 
			out.println( "Hacker krijgt ook een email!" );
		}
		else { 
			out.println( "Denied! Regex heeft gewerkt." );
			out.println( nietAlleenEenEmailAdres + ": [" + regex + "]" );
		}
	}
}
