package org.jbpm.designer.assets.domain;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "asset")
@XmlAccessorType(XmlAccessType.FIELD)
public class AssetXml { 
    
    public AssetXml() {} 
    
    @XmlElement
    private Long id;
    
    @XmlElement
    private String processId;
    
    @XmlElement
    private String type;
   
    @XmlMimeType(MediaType.APPLICATION_OCTET_STREAM)
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
