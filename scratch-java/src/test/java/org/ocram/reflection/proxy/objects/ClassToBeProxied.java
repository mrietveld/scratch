package org.ocram.reflection.proxy.objects;

import static java.lang.System.out;

import java.io.Serializable;

public class ClassToBeProxied implements ClassInterface, Serializable {

    /* Generated serial version UID */
    private static final long serialVersionUID = -2307010830688962213L;
    
    int i = 2;
    public String stringField;

    public ClassToBeProxied( int i ) { 
        this.i = i;
    }

    public ClassToBeProxied() { 
        i = 2;
    }
    
    public int of() { 
        ++i;
        out.println( "of: " + i );
        return i;
    }

    public void og() { 
        ++i;
        out.println( "og" );
    }

    public void oh(int d) { 
        out.println( "oh: " + d);
    }

    public int ok(int d) { 
        out.println( "ok: " + d);
        return -1 * d;
    }

}