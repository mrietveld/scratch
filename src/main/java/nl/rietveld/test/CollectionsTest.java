package nl.rietveld.test;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.rietveld.test.objects.Bom;
import nl.rietveld.test.objects.BomComparator;
public class CollectionsTest {

	public CollectionsTest() { }
	
	public static void arrayTest() { 
		
		String [] strarr = new String [5];
		
		if( 5 == strarr.length ) { 
			out.println( "5!" );
		}
		else if( 4 == strarr.length ) { 
			out.println( "4." );
		}
	}
	
	public static void comparatorTest() { 
		List<Bom> bomList = new ArrayList<Bom>();
	
		Bom b = null;
		for( int i = 0; i < 15; ++i ) { 
			b = new Bom(); 
			bomList.add(b);
		}
		
		Collections.sort(bomList, new BomComparator());
		
		for( Bom ba : bomList ) { 
			out.println(ba.getId());
		}
	}
}