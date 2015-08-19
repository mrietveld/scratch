package org.ocram.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

import javax.naming.InitialContext;

import org.drools.core.command.CommandService;
import org.drools.core.command.Interceptor;
import org.junit.Test;
import org.kie.api.command.Command;
import org.kie.internal.command.Context;
import org.ocram.ScratchBaseTest;
import org.ocram.collections.objects.Bam;
import org.ocram.collections.objects.Bom;
import org.ocram.collections.objects.BomComparator;
import org.ocram.collections.objects.ComparableInterceptor;

public class CollectionsTest extends ScratchBaseTest {

    @Test
    public void og() throws Exception { 
        InitialContext ctx = new InitialContext();
        logger.warn( ctx.getClass().getName());
    }
    
    @Test
    public void arrayTest() {

        String[] strarr = new String[5];

        if (5 == strarr.length) {
            logger.debug("5!");
        } else if (4 == strarr.length) {
            logger.debug("4.");
        }
    }

    @Test
    public void comparatorTest() {
        List<Bom> bomList = new ArrayList<Bom>();

        Bom b = null;
        for (int i = 0; i < 15; ++i) {
            b = new Bom();
            bomList.add(b);
        }

        Collections.sort(bomList, new BomComparator());

        for (Bom ba : bomList) {
            logger.debug("{}", ba.getId());
        }
    }

    @Test
    public void deQueueStackTest() {
        int[] data = { 4, 3, 2, 1 };

        Stack<Integer> nested = new Stack<Integer>();
        for (int i : data) {
            nested.add(i);
        }

        while (!nested.isEmpty()) {
            logger.debug("{}", nested.pop());
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
            if (value != null) {
                value = super.put(key, value);
            } else {
                value = super.put(key, (V) NULL);
                if (value == NULL) {
                    return null;
                }
            }
            return value;
        }

        public V get(Object key) {
            Object value = super.get(key);
            if (value == NULL) {
                return null;
            }
            return (V) value;
        }
    }

    @Test
    public void arraysArrayList() {
        Class<?>[] cl = { this.getClass() };
        List<Class<?>> cls = new ArrayList<Class<?>>(Arrays.asList(cl));
        cls.add(Bam.class);
    }

    @Test
    public void mapRemoveValue() {
        Map<String, Object> map = Collections.synchronizedMap(new LinkedHashMap<String, Object>());
        Object val = new Object();
        map.put("one", val);
        map.values().remove(val);
        assertEquals(null, map.get("one"));
        
        map.put("one", val);
        map.put("one", new Object());
        map.values().remove(val);
        assertNotEquals(null, map.get("one"));
    }
    
    @Test
    public void emptyListIteration() {
        List<String> emptyList = Collections.emptyList(); 
        for( String og : emptyList ) { 
            System.out.println( "WTF?" );
        }
    }
    
    @Test(expected=NullPointerException.class)
    public void concurrentHashMapNullKey() {
        ConcurrentHashMap<String, Object> chm = new ConcurrentHashMap<String, Object>();
        chm.put("adsf", "asdf");
        chm.get(null);
    }
    
    @Test
    public void arrayListSizeGet() { 
        String [] content = { null, null };
        List<String> strList = Arrays.asList(content);
        assertNull(strList.get(0));
        assertNull(strList.get(1));
        strList.set(0, "one");
        strList.set(1, "two");
        assertEquals("one", strList.get(0));
        assertEquals("two", strList.get(1));
    }
    
    @Test
    public void addComparableToTreeSet() { 
       ComparableInterceptor one = new ComparableInterceptor(1, new Long(1)) ;
       ComparableInterceptor two = new ComparableInterceptor(2, new Long(2)) ;
       ComparableInterceptor thr = new ComparableInterceptor(3, new Long(3));
       
       Set<ComparableInterceptor> list = new TreeSet<ComparableInterceptor>();
       list.add(one);
       list.add(thr);
       list.add(two);
       
       for( ComparableInterceptor cin : list ) { 
          System.out.println( cin.getInterceptor() ); 
       }
       
    }
}
