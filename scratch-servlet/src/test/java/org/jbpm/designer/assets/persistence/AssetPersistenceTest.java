package org.jbpm.designer.assets.persistence;

import static junit.framework.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.id.UUIDHexGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class AssetPersistenceTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @Rule
    public TestName testName = new TestName();

    // In order to do stuff in tx's
    private abstract class DoInTransaction {
        public abstract void operation();
        public void execute() {
            em.getTransaction().begin();
            operation();
            em.getTransaction().commit();
        }
    }
    
    @Before
    public void before() {
        if (!"testPersistenceUnit".equals(testName.getMethodName())) {
            emf = Persistence.createEntityManagerFactory("org.jbpm.designer");
            em = emf.createEntityManager();
        }
    }

    @After
    public void after() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    public void testPersistenceUnit() {
        emf = Persistence.createEntityManagerFactory("org.jbpm.designer");
        EntityManager em = emf.createEntityManager();
        em.close();
        emf.close();
    }

    @Test
    public void assetEntityTest() {
        AssetEntity assetEntity = new AssetEntity();
        assetEntity.setType(AssetType.BPMN);
        em.getTransaction().begin();
        em.persist(assetEntity);
        em.getTransaction().commit();

        assertTrue(assetEntity.getId() > 0);
    }

    @Test
    public void testSimpleVersioning() {

        final String origContent = "original content";
        final String modContent = "modified content";
        final AssetEntity asset = new AssetEntity();

        new DoInTransaction() {
            public void operation() {
                asset.setType(AssetType.BPMN);
                asset.setProcessId("23");
                asset.setContent(origContent.getBytes());

                em.persist(asset);
            }
        }.execute();

        final long assetId = asset.getId();
        assertTrue(assetId > 0);

        new DoInTransaction() {
            public void operation() {
                AssetEntity asset = new AssetEntity();
                asset.setType(AssetType.FORM);
                asset.setProcessId("999");
                asset.setContent("random stuff to test with".getBytes());

                em.persist(asset);
            }
        }.execute();
        
        new DoInTransaction() {
            public void operation() {
                AssetEntity asset = em.find(AssetEntity.class, new Long(assetId));
                asset.setContent(modContent.getBytes());
                asset.setType(AssetType.IMAGE);
            }
        }.execute();
        
        compareLastTwoVersions(assetId, origContent, modContent);
    }
    
    private void compareLastTwoVersions(long assetId, final String origContent, final String modContent) { 
        AuditReader reader = AuditReaderFactory.get(em);
        Number revision = (Number) reader.createQuery()
            .forRevisionsOfEntity(AssetEntity.class, true, false)
            .addProjection(AuditEntity.revisionNumber().max())
            .add(AuditEntity.id().eq(assetId))
            .getSingleResult();
        
        int maxRev = (Integer) revision;
        
        AssetEntity modAsset = reader.find(AssetEntity.class, assetId, maxRev);
        AssetEntity origAsset = reader.find(AssetEntity.class, assetId, maxRev-1);

        assertEquals("Original content not equal.", origContent, new String(origAsset.getContent()));
        assertEquals("Modified content not equal.", modContent, new String(modAsset.getContent()));
        assertEquals("Modified type not equal.", AssetType.IMAGE.toString(), modAsset.getType());
        assertEquals("Original type not equal.", AssetType.BPMN.toString(), origAsset.getType());
    }
    
    private static final String [] inputTags = new String[3];
    
    @Test
    public void testVersioningWithTags() {
        for( int i = 0; i < inputTags.length; ++i ) { 
            inputTags[i] = UUID.randomUUID().toString();
        }
       
        final String origContent = UUID.randomUUID().toString();
        final String modContent  = UUID.randomUUID().toString();
        
        // Run with new tags 
        runTestVersioningWithTags(inputTags, origContent, modContent);
        
        // Run with tags that already exist (persisted during last run)..
        long assetId = runTestVersioningWithTags(inputTags, origContent, modContent);
        
        compareLastTwoVersions(assetId, origContent, modContent);
    }
    
    private long runTestVersioningWithTags(final String [] inputTags, final String origContent, final String modContent) { 

        final AssetEntity asset = new AssetEntity();

        new DoInTransaction() {
            public void operation() {
                asset.setType(AssetType.BPMN);
                asset.setProcessId("23");
                asset.setContent(origContent.getBytes());
                asset.addTags(em, inputTags[0], inputTags[1]);

                em.persist(asset);
            }
        }.execute();

        final long assetId = asset.getId();
        assertTrue(assetId > 0);

        new DoInTransaction() {
            public void operation() {
                AssetEntity asset = em.find(AssetEntity.class, new Long(assetId));
                asset.setContent(modContent.getBytes());
                asset.setType(AssetType.IMAGE);
                asset.addTags(em, inputTags[1], inputTags[2]);
            }
        }.execute();
        
        final AssetEntity [] retrievedAsset = new AssetEntity[1];
        new DoInTransaction() {
            public void operation() {
                retrievedAsset[0] = em.find(AssetEntity.class, new Long(assetId));
            }
        }.execute();
        
        Set<AssetTagEntity> retrievedTags = retrievedAsset[0].getTags();
        assertTrue("Found " + retrievedTags.size() + " tags instead of 3", retrievedTags.size() == 3);
        
        List<AssetTagEntity> checkListTags = new ArrayList<AssetTagEntity>(retrievedTags);
        for( Iterator<AssetTagEntity> iter = checkListTags.iterator(); iter.hasNext(); ) { 
            if( retrievedTags.contains(iter.next()) ) { 
                iter.remove();
            }
        }
        assertTrue("Other tags retrieved than expected: " + checkListTags.size() + " over.", checkListTags.size() == 0 );
        
        return assetId;
    }

}
