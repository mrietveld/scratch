package org.ocram.collections;

import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.ocram.collections.objects.Bom;
import org.ocram.collections.objects.BomComparator;

public class CollectionsTest {

	@Test
	public void arrayTest() { 
		
		String [] strarr = new String [5];
		
		if( 5 == strarr.length ) { 
			out.println( "5!" );
		}
		else if( 4 == strarr.length ) { 
			out.println( "4." );
		}
	}
	
	@Test
	public void comparatorTest() { 
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
