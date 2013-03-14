package org.ocram.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.persistence.objects.Stuff;

public class LocalTransactionsTest extends ScratchBaseTest {

    private EntityManagerFactory emf;
    
    @Before
    public void before() { 
        emf = Persistence.createEntityManagerFactory("org.ocram.test");
    }

    @After
    public void after() { 
        emf.close();
    }
    
    @Test
    public void localTxStatuses() {
        EntityManager em = emf.createEntityManager();
        
        EntityTransaction et = em.getTransaction();
        assertNotNull(et);
        // No transaction == et != null;
        printStatus(et, "no tx");

        et.begin();
        printStatus(et, "begin");
        
        Stuff stuff = new Stuff();
        stuff.name = "gold";
        stuff.type = "virtual";
        stuff.size = 100100;
        
        em.persist(stuff);
        printStatus(et, "after persist");
        
        et.setRollbackOnly();
        printStatus(et, "set rollback only");
        
        et.setRollbackOnly();
        printStatus(et, "set 2nd rollback only");
        
        et.rollback();
        printStatus(et, "after rollback");
        
        et.begin();
        printStatus(et, "begin");
        
        et = em.getTransaction();
        printStatus(et, "begin");
        
        em.persist(stuff.clone());
        printStatus(et, "after persist");
        
        et.commit();
        printStatus(et, "after commit");
        
        et.begin();
        printStatus(et, "after begin");
        
        Stuff found = em.find(Stuff.class, 1);
        printStatus(et, "after found");
        
        et.rollback();
        printStatus(et, "after rollback");
        
        em.close();
    }
    
    private void printStatus(EntityTransaction et, String state) { 
        Boolean rollback = null;
        boolean exceptionOnGRO = false;
        try { 
            rollback = et.getRollbackOnly();
        } catch( Exception e ) { 
            exceptionOnGRO = true;
           // do nothing..  
        }
        boolean active = et.isActive();
        
        out.println( "|- A[" + 
                        (active?1:0) + "] R[" + 
                        (rollback==null?2:(rollback?1:0)) + "] RE[" + 
                        (exceptionOnGRO?1:0) + "] (" + 
                        state + ")" );
    }
}