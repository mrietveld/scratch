package org.ocram.test.domain;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "asset")
@XmlAccessorType(XmlAccessType.FIELD)
public class JaxbTestInput { 
    
    public JaxbTestInput() {} 
    
    @XmlElement(name="id")
    private Long id;
    
    @XmlElement(name="name")
    private String name;
    
    @XmlElement(name="object")
    private List<Object> objects = new ArrayList<Object>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getObjects() {
        return objects;
    }

    public void setObjects(List<Object> objects) {
        this.objects = objects;
    }

}
