package org.ocram.objects;

public enum AssetType {
    IMAGE(1), FORM(2), BPMN(3);
    
    private int value;
    
    AssetType(int value) { 
        this.value = value;
    }
    
    public int value() { 
        return value;
    }
    
    public static AssetType getType(int value) { 
        switch(value) {
            case 1: 
                return IMAGE;
            case 2: 
                return FORM;
            case 3: 
                return BPMN;
            default: 
                throw new IllegalStateException("Unknown type");
        }
    }
    
}
