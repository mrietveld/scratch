package org.ocram.jaxb.objects.bad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

import org.ocram.jaxb.objects.Command;

@XmlRootElement(name = "two")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BadTwoCommand implements Command {

    @XmlElement
    @XmlSchemaType(name = "string")
    private String otherThing;

    @XmlElement
    @XmlSchemaType(name = "long")
    private Long existence;

    @XmlElement
    @XmlSchemaType(name = "string")
    private String version = "3";

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