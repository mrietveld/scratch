package org.ocram.test.domain.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "parent-request")
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings("rawtypes")
public class ParentRequest {

    @XmlElement
    @XmlSchemaType(name = "string")
    protected String parent;

    public String getParent() {
        return parent;
    }

    public void setParent( String parent ) {
        this.parent = parent;
    }


}
