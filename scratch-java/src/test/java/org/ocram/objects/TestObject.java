package org.ocram.objects;

import static java.lang.System.out;

import java.io.Serializable;

public class TestObject implements Serializable {

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

}