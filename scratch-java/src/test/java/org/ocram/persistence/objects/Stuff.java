package org.ocram.persistence.objects;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.persistence.Version;

@Entity
public class Stuff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Version
    public Integer ver;
    
    @Basic
    public String name;

    @Basic
    public String type;
    
    @Basic
    public Integer size;
    
    @Lob
    public byte[] quality;
    
    public Integer getId() { 
        return id;
    }
    
    public Stuff() { 
       // default 
    }
    public Stuff(String name, String type, int size) { 
        this.name = name;
        this.type = type;
        this.size = size;
        this.quality = quality;
    }
   
    public Stuff clone() { 
        Stuff clone = new Stuff();
        clone.name = this.name;
        clone.type = this.type;
        clone.size = this.size;
        clone.quality = this.quality;
        return clone;
    }
    
    
}
