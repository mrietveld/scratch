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

@Entity
public class Stuff {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
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
    
    public Stuff clone() { 
        Stuff clone = new Stuff();
        clone.name = this.name;
        clone.type = this.type;
        clone.size = this.size;
        clone.quality = this.quality;
        return clone;
    }
}
