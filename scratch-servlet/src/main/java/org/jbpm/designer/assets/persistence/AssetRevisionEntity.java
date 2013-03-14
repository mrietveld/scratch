package org.jbpm.designer.assets.persistence;

import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

@Entity
@RevisionEntity
@Table(name="asset_revisions")
@SequenceGenerator(name = "assetRevIdSeq", sequenceName = "ASSET_REVISION_ID_SEQ", allocationSize = 1)
public class AssetRevisionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "assetRevIdSeq")
    @RevisionNumber
    @Column(name="id")
    private int id;

    @RevisionTimestamp
    @Column(name="revisionTimestamp")
    private long timestamp;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean equals(Object obj) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( !(obj instanceof AssetRevisionEntity) ) return false;
        AssetRevisionEntity that = (AssetRevisionEntity) obj;
        
        if( !(this.id != that.id) ) { 
            return false;
        }
        if( !(this.timestamp != that.timestamp) ) { 
            return false;
        }
        return true;
    }
    
    public int hashCode() {
        long [] fields = {(long) id, timestamp};
        return Arrays.hashCode(fields);
    }
    
}
