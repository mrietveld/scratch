package org.ocram.iter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Stack;

import org.junit.Ignore;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class IterationTest extends ScratchBaseTest {

    @Test
    public void reverseIterationTest() {
        List<Integer> lis = new ArrayList<Integer>();
        for( int i = 0; i < 10; ++i ) {
            lis.add(i);
        }

        ListIterator<Integer> iter = lis.listIterator(lis.size());
        int i = 0;
        int t = 10;
        while( iter.hasPrevious() ) {
            ++i;
            int e = iter.previous();
            if( e == 9 ) {
                t = i;
            }
            out.print( e + ", ");
        }
        assertTrue( "While loop did not run [" + i + "].", i == 10);
        assertTrue( "While loop did not run correctly: " + t, t == 1);
    }

    @Test
    // FIFO
    public void queueIterationTest() {
        LinkedList<Integer> lis = new LinkedList<Integer>();
        for( int i = 0; i < 10; ++i ) {
            lis.add(i);
        }

        int i = 0;
        int t = -1;
        while( ! lis.isEmpty() ) {
            int e = lis.poll();
            out.print( e + ", " );
            if( e == 0 ) {
                t = i;
            }
            ++i;
        }
        assertTrue( "While loop did not run as expected: " + t, t == 0);
    }

    @Test
    // LIFO
    @Ignore //OCRAM only if not run by maven
    public void stackIterationTest() {
        Stack<Integer> lis = new Stack<Integer>();
        for( int i = 0; i < 10; ++i ) {
            lis.add(i);
        }

        int t = -1;
        Object [] arr = lis.toArray();
        for( int i = 0; i < arr.length; ++i ) {
            out.print( arr[i] + ", " );
            if( 9 == (Integer) arr[i]  ) {
                t = i;
            }
        }
        assertTrue( "While loop did not run as expected: " + t, t == 0);
    }
}
