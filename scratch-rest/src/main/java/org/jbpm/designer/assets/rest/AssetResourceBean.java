package org.jbpm.designer.assets.rest;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.jbpm.designer.assets.domain.AssetXml;
import org.jbpm.designer.assets.persistence.AssetEntity;

@Stateless
public class AssetResourceBean implements AssetResource {

    @PersistenceContext(name="org.jbpm.designer")
    private EntityManager em;

    public Response createAsset(AssetXml asset, UriInfo uriInfo) {
        AssetEntity entity = domain2entity(asset);
        em.persist(entity);
        em.flush();

        System.out.println("Created customer " + entity.getId());
        
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Long.toString(entity.getId()));
        
        return Response.created(builder.build()).build();
    }

    public AssetXml getAsset(int id) {
        AssetEntity entity = em.getReference(AssetEntity.class, id);
        
        return entity2domain(entity);
    }

    public static AssetEntity domain2entity(AssetXml xml) {
        AssetEntity entity = new AssetEntity();
        entity.setProcessId(xml.getProcessId());
        entity.setType(xml.getType());
        entity.setContent(xml.getContent());
        
        return entity;
    }

    public static AssetXml entity2domain(AssetEntity entity) {
        AssetXml xml = new AssetXml();
        xml.setId(entity.getId());
        xml.setProcessId(entity.getProcessId());
        xml.setType(entity.getType());
        xml.setContent(xml.getContent());
        
        return xml;
    }
    
    public static final long testId = 23l;
    public static final String testProcessId = "illuminate";
    public static final String testType = "test";
    public static final byte [] testContent = "i".getBytes();
    
    public AssetXml test() { 
       AssetXml xml = new AssetXml();
       xml.setId(testId);
       xml.setProcessId(testProcessId);
       xml.setType(testType);
       xml.setContent(testContent);
       return xml;
    }

}
