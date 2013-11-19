package org.ocram.objects;

import static java.lang.System.out;

import java.io.Serializable;

import org.ocram.reflection.proxy.objects.ClassToBeProxied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestObject implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(TestObject.class);
    
    /* Generated serial version UID */
    private static final long serialVersionUID = -2307010830688962213L;
    
    int i = 2;
    public String stringField;

    public TestObject( int i ) { 
        this.i = i;
    }

    public TestObject() { 
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

}
