package nl.rietveld.test;

import static java.lang.System.out;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class DateTest {

	DateTest() { 
	}
	
	public void testFormat() { 
	
		GregorianCalendar gregorianCalendar = new GregorianCalendar();
		
		SimpleDateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy hh:mm:ss");
		SimpleDateFormat dateFormatTwee = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss");
		String newPeildatum = dateFormat.format(gregorianCalendar.getTime());
		String peildatum = getOldPeildatum(gregorianCalendar);
		
		out.println( "old: " + peildatum );
		out.println( "new: " + newPeildatum );
		
		newPeildatum = dateFormatTwee.format(gregorianCalendar.getTime());
		peildatum = getOldPeildatum(gregorianCalendar);
		
		out.println( "old: " + peildatum );
		out.println( "new: " + newPeildatum );
		
	}
	
	private String getOldPeildatum(GregorianCalendar gregorianCalendar) { 
		String month = String.valueOf(gregorianCalendar.get(GregorianCalendar.MONTH) + 1);
		String day = String.valueOf(gregorianCalendar.get(GregorianCalendar.DAY_OF_MONTH));
		String year = String.valueOf(gregorianCalendar.get(GregorianCalendar.YEAR));
		String peildatum = day + "-" + month + "-" + year;
		return peildatum;
	}

	public void testDate() {
		GregorianCalendar date = new GregorianCalendar( 2008, 6, 11 );
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyyMMdd" );
		SimpleDateFormat logFormatter = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
	
		out.println( logFormatter.format( date.getTime() ) );
		GregorianCalendar gc = new GregorianCalendar();
		out.println( logFormatter.format( gc.getTime()) );
		out.println( dateFormatter.format( date.getTime() ) );
		
	}

	public void testTimeZone() {
		GregorianCalendar date = new GregorianCalendar();
		
		TimeZone EST = TimeZone.getTimeZone("GMT+2:00");
		date.setTimeZone(EST);
		
		SimpleDateFormat calFormatter = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss z Z");
		out.println( calFormatter.format( date.getTime() ) );
	}
	
}
