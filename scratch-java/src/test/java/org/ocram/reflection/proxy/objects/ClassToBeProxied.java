package org.ocram.reflection.proxy.objects;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassToBeProxied implements ClassInterface, Serializable {

    private static Logger logger = LoggerFactory.getLogger(ClassToBeProxied.class);
    
    /* Generated serial version UID */
    private static final long serialVersionUID = -2307010830688962213L;
    
    public int i = 2;
    public String stringField;

    public ClassToBeProxied( int i ) { 
        this.i = i;
    }

    public ClassToBeProxied() { 
        i = 2;
    }
    
    public int of() { 
        ++i;
        logger.debug( "of: " + i );
        return i;
    }

    public void og() { 
        ++i;
        logger.debug( "og" );
    }

    public void oh(int d) { 
        logger.debug( "oh: " + d);
    }

    public int ok(int d) { 
        logger.debug( "ok: " + d);
        return -1 * d;
    }

}
