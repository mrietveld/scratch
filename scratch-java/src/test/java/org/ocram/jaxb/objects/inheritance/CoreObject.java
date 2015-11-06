package org.ocram.jaxb.objects.inheritance;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name=CoreObject.xmlName)
public class CoreObject {

    protected static final String xmlName = "cmd";

    @XmlElement
    protected String one;

    public String getOne() {
        return one;
    }

    public void setOne( String one ) {
        this.one = one;
    }

}
