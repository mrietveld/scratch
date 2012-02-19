package nl.rietveld.test;




public class Test {
	
	static CalendarTest calTest = new CalendarTest();
	static DateTest dateTest = new DateTest();
	static ParseTest parseTest = new ParseTest();
	static CollectionsTest colTest = new CollectionsTest();
	static ObjectTest objTest = new ObjectTest();
	static HashTest hashTest = new HashTest();

	// Tests
	Test() { 
	}
	
	public static void main( String args[] ) throws Exception {
		hashTest.testSHA2();
		
	}
	
}
