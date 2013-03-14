package org.jbpm.designer.assets.persistence.upload;

import java.io.InputStream;

import org.jbpm.designer.assets.exception.AssetProcessingException;
import org.jbpm.designer.assets.persistence.AssetEntity;


public interface PersistenceStrategy {

    /**
     * Persist a new asset. 
     * @param parameter The different fields
     */
    public long persist(AssetEntity asset) throws AssetProcessingException;
    
    public AssetEntity find(long assetId) throws AssetProcessingException;
    
    public AssetEntity findByProcessId(String processId) throws AssetProcessingException;
    
    public AssetEntity findByTag(String... tag) throws AssetProcessingException;
    
    public void update(AssetEntity asset, InputStream contentInputStream) throws AssetProcessingException;
    
}
