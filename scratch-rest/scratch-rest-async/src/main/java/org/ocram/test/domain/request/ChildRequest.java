package org.ocram.test.domain.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "child-request")
@XmlAccessorType(XmlAccessType.FIELD)
public class ChildRequest extends ParentRequest {

    @XmlElement
    @XmlSchemaType(name = "string")
    protected String child;

    public String getChild() {
        return child;
    }

    public void setChild( String child ) {
        this.child = child;
    }

}
