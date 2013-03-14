package org.jbpm.designer.assets.persistence.upload;

import static org.jbpm.designer.assets.servlet.AssetResourceServlet.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.persistence.EntityManager;

import org.jbpm.designer.assets.exception.AssetProcessingException;
import org.jbpm.designer.assets.persistence.AssetEntity;

public class JpaEnversPersistence implements PersistenceStrategy {

    private EntityManager em;
   
    public JpaEnversPersistence(EntityManager em) { 
        this.em = em;
    }

    public long persist(AssetEntity asset) throws AssetProcessingException {
        em.getTransaction().begin();
        asset.addTags(em, asset.getTagStrings());
        
        if( asset.getContent() == null ) { 
            byte [] content = convertInputStreamToByteArr(asset.getContentInputStream());
            asset.setContent(content);
        }
        asset.setContentLength(asset.getContent().length);
        
        em.persist(asset);
        em.getTransaction().commit();
        
        return asset.getId();
    }

    public static byte [] convertInputStreamToByteArr(InputStream input) throws AssetProcessingException { 
        byte [] content;
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int n = 0;
            while(-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            content = output.toByteArray();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            throw new AssetProcessingException("Unable to read content input stream: " + ioe.getMessage(), ioe);
        }
        return content;
    }
    
    public AssetEntity find(long assetId) throws AssetProcessingException {
        debug();
        em.getTransaction().begin();
        AssetEntity asset =  em.find(AssetEntity.class, assetId);
        em.getTransaction().commit();
        if( asset == null ) { 
            throw new AssetProcessingException("Asset with id " + assetId + " could not be found.");
        }
        return asset;
    }

    public AssetEntity findByProcessId(String processId) throws AssetProcessingException {
        debug();
        return null;
    }

    public AssetEntity findByTag(String... tag) throws AssetProcessingException {
        debug();
        return null;
    }

    public void update(AssetEntity asset, InputStream contentInputStream) throws AssetProcessingException {
        debug();
    }
    
    private void debug() { 
        System.out.println(">> " + this.getClass().getSimpleName() + "." + (new Throwable()).getStackTrace()[1].getMethodName() + "()");
    }
}
