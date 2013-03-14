package org.jbpm.designer.assets.persistence;

import static junit.framework.Assert.*;
import org.junit.Test;

public class AssetTagEntityTest {

    /**
     * Compares this object with the specified object for order. 
     * 
     * Returns a negative integer, zero, or a positive integer 
     * as this object is less than, equal to, or greater than the specified object.
     * 
     * The implementor must ensure 
     *   sgn(x.compareTo(y)) == -sgn(y.compareTo(x)) for all x and y. 
     * (This implies that 
     *   x.compareTo(y) 
     * must throw an exception iff 
     *   y.compareTo(x) 
     * throws an exception.)
     *
     * The implementor must also ensure that the relation is transitive: 
     *   (x.compareTo(y) > 0 && y.compareTo(z) > 0) 
     * implies 
     *   x.compareTo(z) > 0
     * .
     * 
     * Finally, the implementor must ensure that 
     *   x.compareTo(y)==0 
     * implies that 
     *   sgn(x.compareTo(z)) == sgn(y.compareTo(z))
     * , for all z.
     * 
     * It is strongly recommended, but not strictly required that 
     *   (x.compareTo(y)==0) == (x.equals(y))
     * . 
     * 
     * Generally speaking, any class that implements the Comparable interface 
     * and violates this condition should clearly indicate this fact. The recommended language is 
     *   "Note: this class has a natural ordering that is inconsistent with equals."
     * 
     * In the foregoing description, the notation 
     *   sgn(expression) 
     * designates the mathematical signum function, which is defined to return one of -1, 0, or 1 
     * according to whether the value of expression is negative, zero or positive.
     */
    @Test 
    public void testCompareTo() { 
        AssetTagEntity [] tag = { 
                null,
                new AssetTagEntity(),
                new AssetTagEntity("a"),
                new AssetTagEntity("b"),
                new AssetTagEntity("c")
        };
        
        // basic
        for( int i = 1; i < tag.length-1; ++i ) { 
            assertTrue( tag[i].compareTo(tag[i-1]) == 1 );
            assertTrue( tag[i].compareTo(tag[i+1]) == -1 );
        }
        // transitive
        for( int i = 1; i < tag.length-1; ++i ) { 
            if( i-2 > 0 ) { 
                assertTrue( tag[i] + " not more than " + tag[i-2], tag[i].compareTo(tag[i-2]) >= 1 );
            }
            if( i+2 < tag.length ) { 
                assertTrue( tag[i] + " not less than " + tag[i+2], tag[i].compareTo(tag[i+2]) <= -1 );
            }
        }
        AssetTagEntity [] duplicateTag = { 
                null,
                new AssetTagEntity(),
                new AssetTagEntity("a"),
                new AssetTagEntity("b"),
                new AssetTagEntity("c")
        };
        // identity
        for( int i = 1; i < tag.length; ++i ) { 
            assertTrue( tag[i].compareTo(tag[i]) == 0 );
            assertTrue( tag[i].compareTo(duplicateTag[i]) == 0 );
            assertTrue( tag[i].equals(duplicateTag[i]) );
        }
        // reverse
        for( int i = 2; i < tag.length; ++i ) { 
            assertTrue( tag[i].compareTo(tag[i-1]) == -1 * (tag[i-1].compareTo(tag[i])) );
            if( i > 2 ) { 
                assertTrue( tag[i].compareTo(tag[i-2]) == -1 * (tag[i-2].compareTo(tag[i])) );
            }
        }
    }
}
