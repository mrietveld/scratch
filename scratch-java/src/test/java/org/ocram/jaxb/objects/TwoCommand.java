package org.ocram.jaxb.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "two")
@XmlAccessorType(XmlAccessType.FIELD)
public class TwoCommand implements Command {

    @XmlElement
    @XmlSchemaType(name = "string")
    private String otherThing;

    @XmlElement
    @XmlSchemaType(name = "long")
    private Long existence;

    @XmlElement
    @XmlSchemaType(name = "string")
    private String version = "3";

    public String getOtherThing() {
        return otherThing;
    }

    public void setOtherThing( String otherThing ) {
        this.otherThing = otherThing;
    }

    public Long getExistence() {
        return existence;
    }

    public void setExistence( Long existence ) {
        this.existence = existence;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

}