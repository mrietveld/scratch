package org.jbpm.designer.assets.persistence;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;

import org.hibernate.envers.Audited;

@Entity
@SequenceGenerator(name = "assetIdSeq", sequenceName = "ASSET_ID_SEQ", allocationSize = 1)
@Audited
public class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "assetIdSeq")
    private long id;
    
    @Column(nullable=true)
    private String processId;
    
    @Column(nullable=false)
    private String type;

    @Lob
    @Column(length = 2147483647)
    private byte[] content;
    
    public long getId() {
        return id;
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

    public void setType(AssetType type) {
        this.type = type.toString();
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
