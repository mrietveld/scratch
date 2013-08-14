package org.ocram.persistence;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.PessimisticLockException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.drools.persistence.info.WorkItemInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.persistence.objects.PersistenceUtil;
import org.ocram.persistence.objects.Stuff;

public class PessimisticLockingTest extends ScratchBaseTest {

    private EntityManagerFactory emf;
    private Map<String, Object> context;

    @Before
    public void before() {
        context = PersistenceUtil.setupWithPoolingDataSource("org.ocram.test.jta", "jdbc/testDs", "jdbc:h2:file:target/testDb;MVCC=true");
        emf = (EntityManagerFactory) context.get(PersistenceUtil.ENTITY_MANAGER_FACTORY);
    }

    @After
    public void after() {
        
    }

    @Test
    public void mergeAndLock() throws Exception {
        LockModeType lockMode = LockModeType.PESSIMISTIC_FORCE_INCREMENT;

        EntityManager em = emf.createEntityManager();

        UserTransaction ut = joinTx(em);
        Stuff stuff = new Stuff("gold", "virtual", 100100);
        em.persist(stuff);
        ut.commit();

        em.detach(stuff);
        
        ut = joinTx(em);
        stuff.type = "meta";
        stuff.name = "silver";
        
        Stuff dbStuff = em.find(Stuff.class, stuff.getId(), LockModeType.PESSIMISTIC_FORCE_INCREMENT);
        for( Field field : Stuff.class.getDeclaredFields() ) { 
            boolean access = field.isAccessible();
            field.setAccessible(true);
            try {
                field.set(dbStuff, field.get(stuff));
            } catch (Exception e) {
                fail("Unable to set field " + field.getName() + " of unmerged WorkItemInfo instance! (" + e.getMessage() + ")");
            } 
            field.setAccessible(access);
        }
        stuff = dbStuff;
        
        PartyCrasher pc = new PartyCrasher(emf, stuff, "Bob");

        em.lock(stuff, lockMode);
        em.lock(stuff, lockMode);
        em.merge(stuff);
        
        pc.getStart().countDown();
        pc.getDone().await();
        
        ut.commit();
    }

    private static UserTransaction joinTx(EntityManager em) {
        UserTransaction ut = PersistenceUtil.findUserTransaction();
        try {
            ut.begin();
        } catch (Exception e ){ 
            e.printStackTrace();
            fail( "Unable to begin tx: "+ e.getMessage());
        }
        em.joinTransaction();
        return ut;
    }

    private static class PartyCrasher implements Runnable {

        private final static LockModeType lockMode = LockModeType.PESSIMISTIC_FORCE_INCREMENT;

        private final EntityManagerFactory emf;
        private final Stuff stuff;

        private final CountDownLatch start = new CountDownLatch(1);
        private final CountDownLatch done = new CountDownLatch(1);

        public PartyCrasher(EntityManagerFactory emf, Stuff stuff, String name) {
            this.emf = emf;
            this.stuff = stuff;
            Thread thread = new Thread(this);
            thread.setName(name);
            thread.start();
        }

        public CountDownLatch getStart() {
            return start;
        }

        public CountDownLatch getDone() {
            return done;
        }

        @Override
        public void run() {
            try {
                start.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
                fail("Unable to even enter the house!");
            }
            EntityManager em = emf.createEntityManager();
            UserTransaction ut = joinTx(em);
            Stuff stuff = em.find(Stuff.class, this.stuff.getId());
            boolean faal = false;
            try { 
                em.lock(stuff, lockMode);
            } catch( PessimisticLockException ple ) { 
                faal = true;
                try {
                    ut.rollback();
                } catch (Exception e) {
                    fail( "Could not even leave the house!");
                }
                done.countDown();
            }
            assertTrue( "Did not fail!", faal );
            
        }

    }
}
