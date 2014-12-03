package org.ocram.jaxb.objects;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestRequest {

    @XmlAnyElement(lax=true)
    @XmlMixed
    List<Object> commands = new ArrayList<Object>();
   
    public TestRequest() { 
        
    }

    public List<Object> getCommands() {
        if( commands == null ) { 
            commands = new ArrayList<Object>();
        }
        return commands;
    }

    public void setCommands( List<Object> commands ) {
        this.commands = commands;
    }
    
}
