package org.ocram.test.domain.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "child-response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildResponse extends ParentResponse {

    @XmlElement
    @XmlSchemaType(name = "int")
    protected Integer child;

    public Integer getChild() {
        return child;
    }

    public void setChild( Integer child ) {
        this.child = child;
    }

    public ChildResponse() { 
       // default 
    }
    
    public ChildResponse(ChildRequest request) { 
        String parent = request.parent == null ? "" : request.parent;
        String child = request.child == null ? "" : request.child;
       this.parent = parent.length(); 
       this.child = child.length(); 
    }
}
