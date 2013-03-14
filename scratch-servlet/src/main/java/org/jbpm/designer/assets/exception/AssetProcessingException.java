package org.jbpm.designer.assets.exception;

/**
 * Exception to use for internal processing purposes.
 */
public class AssetProcessingException extends Exception {

    private static final long serialVersionUID = -2863862505309298394L;

    /**
     * Default constructor which does NOT fill the stack trace.
     */
    public AssetProcessingException() { 
        // no need to fill in the stack trace
    }
    
    public AssetProcessingException(String msg) { 
        super(msg);
    }
    
    public AssetProcessingException(String msg, Throwable cause) { 
        super(msg, cause);
    }
    
    public AssetProcessingException(Throwable cause) { 
        super(cause);
    }
}
