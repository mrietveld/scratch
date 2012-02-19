package org.ocram.primitives;

import static junit.framework.Assert.*;

import java.util.HashSet;
import java.util.Properties;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ConstantPoolTest extends ScratchBaseTest {

    @Test
    public void stringConstantPoolTest() {
        String lita = "literal test";
        String litb = "literal test";
        compareStrings(lita, litb);

        int og = 40230;
        int of = 40230;

        String one = String.valueOf(og).intern();
        String two = String.valueOf(of).intern();
        compareStrings(one, two);
    }

    private void compareStrings(String one, String two) {
        assertTrue("String [" + one + "] and [" + two + "] are not equal.", one == two);
    }

    @Test
    public void integerConstantPoolTest() throws Exception {
        Properties localProperties = System.getProperties();
        String integerCacheHighPropValue = (String) localProperties.get("java.lang.Integer.IntegerCache.high");

        int i = 127;
        if (integerCacheHighPropValue != null) {
            int j = Long.decode(integerCacheHighPropValue).intValue();

            j = Math.max(j, 127);
            i = Math.min(j, 2147483519);
        }

        out.println("integer high cache: " + i);

        HashSet<Integer> integerPool = new HashSet<Integer>();
        int poolTestInt = 0;
        if (i > 127) {
            poolTestInt = 130;
        } else {
            poolTestInt = 10;
        }

        integerPool.add(poolTestInt);
        assertFalse(poolTestInt + " too large for constant pool", integerPool.add(poolTestInt));
        out.println("Added " + poolTestInt);

    }

}
