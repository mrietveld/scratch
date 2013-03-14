package org.jbpm.designer.assets.persistence.upload;

import javax.persistence.EntityManager;


public class PersistenceStrategyFactory {

    public enum PersistenceType { 
        JPA, JDBC, GUVNOR, MODESHAPE;
    }
    
    private PersistenceType type;
    
    public PersistenceStrategyFactory(PersistenceType type) { 
        this.type = type;
    }
    
    public PersistenceStrategyFactory.PersistenceType getType() { 
        return this.type;
    }
    
    public PersistenceStrategy createStrategyInstance(EntityManager em) { 
        PersistenceStrategy strategy = null;
        switch(type) { 
        case JPA:
            strategy = new JpaEnversPersistence(em);
            break;
        case JDBC:
            strategy = new JdbcStatementPersistence(em);
            break;
        case GUVNOR:
            strategy = new GuvnorViaServletPersistence(em);
            break;
        case MODESHAPE:
            strategy = new ModeshapePersistenceStrategy(em);
            break;
        default:
            throw new RuntimeException("Unknown persistence strategy type: " + this.type.toString());
       }
        return strategy;
    }
    
    
}
