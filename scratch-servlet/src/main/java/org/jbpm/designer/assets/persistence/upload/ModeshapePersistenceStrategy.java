package org.jbpm.designer.assets.persistence.upload;

import java.io.InputStream;

import javax.persistence.EntityManager;

import org.jbpm.designer.assets.exception.AssetProcessingException;
import org.jbpm.designer.assets.persistence.AssetEntity;

public class ModeshapePersistenceStrategy implements PersistenceStrategy {

    private EntityManager em;
   
    public ModeshapePersistenceStrategy(EntityManager em) { 
        this.em = em;
    }

    public long persist(AssetEntity asset) throws AssetProcessingException {
        
        return 0;
    }

    public AssetEntity find(long assetId) throws AssetProcessingException {
        // DBG Auto-generated method stub
        return null;
    }

    public AssetEntity findByProcessId(String processId) throws AssetProcessingException {
        // DBG Auto-generated method stub
        return null;
    }

    public AssetEntity findByTag(String... tag) throws AssetProcessingException {
        // DBG Auto-generated method stub
        return null;
    }

    public void update(AssetEntity asset, InputStream contentInputStream) throws AssetProcessingException {
        // DBG Auto-generated method stub
        
    }
}
