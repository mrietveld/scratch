package org.ocram.reflection.proxy.objects;

import java.lang.reflect.Method;

public class MethodRequest {

    private final Method method; 
    private final Object [] args;
    
    public MethodRequest(Method method, Object [] args) { 
        this.method = method;
        this.args = args;
    }
    
    public Method getMethod() { 
        return method;
    }
    
    public Object [] getArgs() { 
       return args; 
    }
    
}
