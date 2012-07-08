package org.ocram.general;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.ocram.objects.AssetType;

public class EnumTest {

    @Test
    public void testValues() { 
        AssetType one = AssetType.BPMN;
        
        assertTrue( one.value() == 3);
    }
}
