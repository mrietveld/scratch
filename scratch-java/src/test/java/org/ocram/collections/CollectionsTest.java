package org.ocram.collections;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.collections.objects.Bam;
import org.ocram.collections.objects.Bom;
import org.ocram.collections.objects.BomComparator;

public class CollectionsTest extends ScratchBaseTest {

	@Test
	public void arrayTest() { 
		
		String [] strarr = new String [5];
		
		if( 5 == strarr.length ) { 
			logger.debug( "5!" );
		}
		else if( 4 == strarr.length ) { 
			logger.debug( "4." );
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
			logger.debug("{}", ba.getId());
		}
	}
	
	@Test
	public void deQueueStackTest() { 
	  int [] data = { 4, 3, 2, 1 };
	  
	  Stack<Integer> nested = new Stack<Integer>();
	  for( int i : data ) { 
	     nested.add(i);
	  }
	  
	  while( ! nested.isEmpty() ) { 
	      logger.debug("{}", nested.pop() );
	  }
	    
	}
	
	@Test
	public void concurrentHashMapNPE() { 
	    Map<String, String> testMap = new NullValueConcurrentHashMap<String, String>();
	    String value = null;
	    Object orig = testMap.put("test", value);
	    Object newVal = testMap.get("test");
	}
	
	private static class NullValueConcurrentHashMap<K, V> extends ConcurrentHashMap<K, V> {
	  
	    private static Object NULL = new Object();
	    
	    public V put(K key, V value) {
	       if( value != null ) { 
	          value = super.put(key, value);
	       } else { 
	          value = super.put(key, (V) NULL);
	          if( value == NULL ) { 
	              return null;
	          }
	       }
	       return value;
	    }
	    
	    public V get(Object key) {
	      Object value = super.get(key);  
	      if( value == NULL ) { 
	          return null;
	      }
	      return (V) value;
	    }
	}
	
	@Test
	public void ArraysArrayList() { 
	   Class<?> [] cl = { this.getClass() };
	   List<Class<?>> cls = new ArrayList<Class<?>>(Arrays.asList(cl));
	   cls.add(Bam.class);
	}
}
