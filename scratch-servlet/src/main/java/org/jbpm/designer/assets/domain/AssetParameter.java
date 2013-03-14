package org.jbpm.designer.assets.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

import org.jbpm.designer.assets.persistence.AssetType;

@XmlRootElement(name = "asset")
@XmlAccessorType(XmlAccessType.FIELD)
public class AssetParameter { 
    
    public AssetParameter() {} 
    
    @XmlElement
    private Long id;
    
    @XmlElement
    private String processId;
    
    @XmlElement
    private String type;
   
    @XmlMimeType("application/octet-stream")
    private byte [] content;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setType(AssetType type) {
        this.type = type.toString();
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
