package nl.rietveld.proces;

import static java.lang.System.out;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class ClassyNode {

	// Production
	
	public static void main( String args[] ) {
		ClassyNode cn = new ClassyNode();
		cn.doStuff();
	}
	
	public ClassyNode() { 
	}
	
	public void doStuff() { 
		out.println( "ahahahaha.. ");
		out.println( doContract() );
		out.println( nogIets( 10 ) );
	}
	
	public String doContract() { 
		return "ContractRekeningen.. ";
	}
	
	public String nogIets( int i ) { 
		String s = "";
		for( int j = 0; j < i; ++j ) { 
			s += j;
		}
		return s;
	}

	public void testTimeZone() {
		GregorianCalendar date = new GregorianCalendar();
		
		TimeZone EST = TimeZone.getTimeZone("GMT+2:00");
		date.setTimeZone(EST);
		
		SimpleDateFormat calFormatter = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss z Z");
		out.println( calFormatter.format( date.getTime() ) );
	}
	
}
