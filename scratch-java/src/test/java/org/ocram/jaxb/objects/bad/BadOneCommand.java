package org.ocram.jaxb.objects.bad;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

import org.ocram.jaxb.objects.Command;

@XmlRootElement(name = "one")
@XmlAccessorType(XmlAccessType.FIELD)
public class BadOneCommand implements Command {

    public BadOneCommand(String depId) {
        this.deploymentId = depId;
    }
    
    @XmlElement
    @XmlSchemaType(name = "long")
    private String deploymentId;

    @XmlElement
    @XmlSchemaType(name = "long")
    private Long processInstanceId;

    private String version = "3";

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId( Long processInstanceId ) {
        this.processInstanceId = processInstanceId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion( String version ) {
        this.version = version;
    }

    
}
