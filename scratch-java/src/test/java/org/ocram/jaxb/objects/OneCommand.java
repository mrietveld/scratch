package org.ocram.jaxb.objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;

@XmlRootElement(name = "one")
@XmlAccessorType(XmlAccessType.FIELD)
public class OneCommand implements Command {

    @XmlElement
    @XmlSchemaType(name = "string")
    private String deploymentId;

    @XmlElement
    @XmlSchemaType(name = "long")
    private Long processInstanceId;

    @XmlElement
    @XmlSchemaType(name = "string")
    private String version = "3";

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId( String deploymentId ) {
        this.deploymentId = deploymentId;
    }

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
