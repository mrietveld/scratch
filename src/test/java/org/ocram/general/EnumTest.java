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
    
    @Test
    public void testCreation() { 
        AssetType one = AssetType.valueOf("BPMN");
        
        assertTrue( one.value() == 3);
    }
    
    @Test
    public void testCreationError() { 
        AssetType one = AssetType.valueOf("BPMNX");
    }
    
    @Test
    public void testGet() { 
        AssetType one = AssetType.getType(3);
        
        assertTrue( one.value() == 3);
    }
    
    @Test
    public void testString() { 
        System.out.println(AssetType.BPMN);
    }
    
    
    
}
