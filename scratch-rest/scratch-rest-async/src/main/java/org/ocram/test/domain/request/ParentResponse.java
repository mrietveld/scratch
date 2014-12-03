package org.ocram.test.domain.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "parent-response")
@XmlAccessorType(XmlAccessType.FIELD)
public class ParentResponse {

    @XmlElement
    @XmlSchemaType(name = "int")
    protected Integer parent;

    public Integer getParent() {
        return parent;
    }

    public void setParent( Integer parent ) {
        this.parent = parent;
    }

}
