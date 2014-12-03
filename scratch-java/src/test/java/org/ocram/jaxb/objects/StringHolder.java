package org.ocram.jaxb.objects;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "string-holder")
@XmlAccessorType(XmlAccessType.FIELD)
public class StringHolder {

    @XmlElement
    private Map<String, Object> string = new HashMap<String, Object>();

    public StringHolder() { 
        // default
    }
    
    public StringHolder(String str) { 
       this.string.put("test", str);
    }
    
    public Map<String, Object> getString() {
        return string;
    }

    public void setString( Map<String, Object> string ) {
        this.string = string;
    }

}
