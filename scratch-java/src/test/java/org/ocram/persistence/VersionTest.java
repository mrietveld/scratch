package org.ocram.persistence;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.persistence.objects.PersistenceUtil;
import org.ocram.persistence.objects.Stuff;

public class VersionTest extends ScratchBaseTest {

    private EntityManagerFactory emf;
    private HashMap<String, Object> context;

    @Before
    public void before() {
        context = PersistenceUtil.setupWithPoolingDataSource("org.ocram.test.jta", "jdbc/testDs",
                "jdbc:h2:file:target/testDb;MVCC=true");
        emf = (EntityManagerFactory) context.get(PersistenceUtil.ENTITY_MANAGER_FACTORY);
    }

    @After
    public void after() throws Exception {
        if (context != null) {
            PersistenceUtil.cleanUp(context);
        }
    }

    @Test
    public void versionTest() throws Exception { 
        Stuff stuff = new Stuff();
        
        UserTransaction ut = PersistenceUtil.findUserTransaction();
        
        ut.begin();
        EntityManager em = emf.createEntityManager();
        em.persist(stuff);
        ut.commit();

        Date origDate = stuff.versionDate;
        
        ut.begin();
        stuff.name = "other";
        em.joinTransaction();
        em.persist(stuff);
        ut.commit();
        
        Date secDate = stuff.versionDate;
        assertNotEquals(origDate, secDate);
        logger.debug( "1: " + origDate + ", 2: " + stuff.versionDate );
       
        ut.begin();
        em.joinTransaction();
        Stuff newStuff = em.find(Stuff.class, stuff.getId());
        ut.commit();
       
        assertEquals(secDate, stuff.versionDate);
        
        em.close();
    }
}
