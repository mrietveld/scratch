package org.ocram.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.persistence.objects.PersistenceUtil;
import org.ocram.persistence.objects.Stuff;

public class JtaTransactionsTest extends ScratchBaseTest {

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
    public void idDeterminationTest() throws Exception { 
        Stuff stuff = new Stuff();
        
        UserTransaction ut = PersistenceUtil.findUserTransaction();
        
        ut.begin();
        EntityManager em = emf.createEntityManager();
        em.persist(stuff);
        assertTrue( "" + stuff.getId(), stuff.getId() >= 0 );
        ut.commit();
        assertTrue( "" + stuff.getId(), stuff.getId() >= 0 );
        em.close();
        
        stuff = new Stuff();
        stuff.name = "illusionary-id";
        em = emf.createEntityManager();
        em.persist(stuff);
        assertNull("Persisting outside of a tx does not trigger id generation!", stuff.id);
        
    }
}