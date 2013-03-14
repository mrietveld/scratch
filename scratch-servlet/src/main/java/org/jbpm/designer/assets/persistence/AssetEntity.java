package org.jbpm.designer.assets.persistence;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RevisionEntity;

/**
 * 
 * 
 *
 */
@Entity(name="Asset")
@Table(name="asset")
@SequenceGenerator(name = "assetIdSeq", sequenceName = "ASSET_ID_SEQ", allocationSize = 1)
@Audited
@AuditTable(value="asset_ver")
public class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "assetIdSeq")
    @Column(name="id")
    private long id;
    
    @Column(name="processId", nullable=true)
    private String processId;
    
    @Column(name="type", nullable=false)
    private String type;

    @Lob
    @Column(name="content", length = 2147483647, nullable=true)
    private byte[] content;
    
    @Column(name="contentLength", nullable=true)
    private int contentLength = 0;
    
    @Transient
    private InputStream contentInputStream;
    
    @ManyToMany
    @JoinTable(name="asset_tag_join",
            joinColumns={@JoinColumn(name="assetId", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="tagId", referencedColumnName="id")})
    @NotAudited
    private Set<AssetTagEntity> tags;
    
    @Transient
    private Set<String> tagStrings = new HashSet<String>();
    
    
    public AssetEntity() { 
       // default constructor (for hibernate and other ORM's, among other things) 
    }
    
    public AssetEntity(String processId, String type, byte [] content, String... tags) {
        this.processId = processId;
        this.type = type;
        this.content = content;
    }
    
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
    
    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public InputStream getContentInputStream() {
        return contentInputStream;
    }
    
    public void setContentInputStream(InputStream inputStream) { 
        this.contentInputStream = inputStream;
    }
    
    public Set<AssetTagEntity> getTags() { 
        return Collections.unmodifiableSet(getTagsInternal());
    }
    
    private Set<AssetTagEntity> getTagsInternal() { 
        if( tags == null ) { 
            tags = new HashSet<AssetTagEntity>();
        }
        return tags;
    } 
    
    public Set<String> getTagStrings() {
        return tagStrings;
    }

    public void setTagStrings(Set<String> tagStrings) {
        this.tagStrings = tagStrings;
    }


    private static String FIND_TAG_BY_TAGS_QUERY_PART = "SELECT t from AssetTag t WHERE t.tag in ( ";
    
    // This method must happen within a tx
    public void addTags(EntityManager em, String... tag) { 
        if( tag.length > 0 ) { 
            List<AssetTagEntity> newTagList = new ArrayList<AssetTagEntity>();
            for( int i = 0; i < tag.length; ++i ) { 
                newTagList.add(new AssetTagEntity(tag[i])); 
            }
            addTagsInternal(em, newTagList);
        }
    }
    
    // This method must happen within a tx
    public void addTags(EntityManager em, Collection<String> newTagStrings) { 
        if( newTagStrings.size() > 0 ) { 
            List<AssetTagEntity> newTagList = new ArrayList<AssetTagEntity>();
            for( String tagString : newTagStrings ) { 
                newTagList.add(new AssetTagEntity(tagString));
            }
            addTagsInternal(em, newTagList);
        }
    }
    
    private void addTagsInternal(EntityManager em, List<AssetTagEntity> newTagList) { 
        Set<AssetTagEntity> tags = getTagsInternal();

        // Filter out tags that are already present
        Iterator<AssetTagEntity> newTagIter = newTagList.iterator();
        while(newTagIter.hasNext()) { 
           AssetTagEntity newTag = newTagIter.next();
           if( tags.contains(newTag) ) { 
               // skip, already present
               newTagIter.remove();
           }
        }
        
        if( newTagList.size() == 0 ) { 
            // tags to add already present in this.tags
            return;
        } else if( newTagList.size() > 1 ) { 
            Collections.sort(newTagList); // this helps out.. :) 
        }
        
        // build query to retrieve tags
        StringBuilder query = new StringBuilder(FIND_TAG_BY_TAGS_QUERY_PART);
        query.append("'" + newTagList.get(0).getTag() + "'");
        for( int i = 1; i < newTagList.size(); ++i ) { 
            query.append(", '" + newTagList.get(i).getTag() + "'");
        }
        query.append(" )");
        
        //  We only retrieve tags that don't yet exist in this.tags (see above).
        @SuppressWarnings("unchecked")
        List<AssetTagEntity> results = em.createQuery(query.toString()).getResultList();
        if( results.size() > 1 ) { 
            Collections.sort(results);
        }
        
        /**
         * Separate into 2 categories: 
         * 1. exists and is not present in tags -> (immediately add to tags)
         * 2. does not exist yet                -> (stays in tagList)
         */
        
        // 1. exists in DB, add to this.tags
        if( results.size() > 0 ) { 
            Iterator<AssetTagEntity> existing = results.iterator();
            while(existing.hasNext()) { 
                AssetTagEntity retrievedTag = existing.next();
                if( !tags.add(retrievedTag) ) { 
                    //DBG: developer exception, necessary to catch weird situations that shouldn't happen
                    throw new RuntimeException("Existing tag added to asset tags was already present: " + retrievedTag.getTag());
                }
            }
        }
        
        // 2. does not exist in DB, add new entity to this.tags
        for( int i = 0; i < newTagList.size(); ++i ) { 
            AssetTagEntity newTag = newTagList.get(i);
            if( results.contains(newTag) ) { 
                continue;
            }
            em.persist(newTag);
            if( !tags.add(newTag) ) { 
                //DBG: developer exception, necessary to catch weird situations that shouldn't happen
                throw new RuntimeException("New tag added to asset tags was already present!");
            }
        }
    }

    // This method must happen within a tx
    public void removeTags(String... tag) { 
        HashSet<String> removeMeTags = new HashSet<String>(Arrays.asList(tag));
        Iterator<AssetTagEntity> presentIter = this.tags.iterator();
        while(presentIter.hasNext()) { 
            if( removeMeTags.contains(presentIter.next()) ) { 
                presentIter.remove();
            }
        }
    }
    
    
    
}
