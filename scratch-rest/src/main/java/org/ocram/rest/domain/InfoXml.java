package org.ocram.rest.domain;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlMimeType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "info")
@XmlAccessorType(XmlAccessType.FIELD)
public class InfoXml { 
    
    public InfoXml() {} 
    
    @XmlElement
    private Long id;
    
    @XmlElement
    private String what;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String info) {
        this.what = info;
    }

}
