package org.jbpm.designer.assets.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name="AssetTag")
@Table(name="asset_tag")
@SequenceGenerator(name = "assetTagIdSeq", sequenceName = "ASSET_TAG_ID_SEQ", allocationSize = 1)
public class AssetTagEntity implements Comparable<AssetTagEntity> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "assetTagIdSeq")
    @Column(name="id")
    private long id;
   
    @Column(name="tag", nullable=false, unique=true)
    private String tag;

    public AssetTagEntity() { 
        // default constructor for hibernate and other ORM's
    }
   
    public AssetTagEntity(String tag) { 
        this.tag = tag;
    }
    
    public long getId() {
        return id;
    }
    
    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if( obj instanceof String && obj.equals(tag) ) { 
            return true;
        }
        if ( !(obj instanceof AssetTagEntity) ) return false;
        
        // Compare based on tag string, not on id
        AssetTagEntity that = (AssetTagEntity) obj; 
        return this.tag == that.tag || (this.tag != null && this.tag.equals(that.tag));
    }
    
    public int hashCode() {
        if( tag == null ) { 
            return 0;
        }
        return tag.intern().hashCode();
    }
    
    public String toString() { 
        return "[" + this.tag + "]";
    }

    public int compareTo(AssetTagEntity that) {
       if( that == null ) { 
           return 1;
       }
       if( that.tag == this.tag ) { 
           return 0;
       } 
       if( that.tag == null ) { 
          return 1; 
       }
       if( this.tag == null ) { 
           return -1;
       }
       return this.tag.compareTo(that.tag);
    }

}
