package org.ocram.objects;

import static java.lang.System.out;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestInvocationHandler implements InvocationHandler {

    private Object target;
    
    public TestInvocationHandler(Object target) { 
        this.target = target;
    }
    
    public static Object createProxy( Object obj ) { 
        Class objClass = obj.getClass();
        return Proxy.newProxyInstance(objClass.getClassLoader(), 
                                      objClass.getInterfaces(), 
                                      new TestInvocationHandler(obj) );
    }
    
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if( "getClass".equals(method.getName()) ) { 
           return ClassToBeProxied.class; 
        }
        out.println( "'proxy' parameter is class [" + proxy.getClass() + "] :" + (proxy instanceof ClassToBeProxied) );
        out.println( ((Proxy) proxy).isProxyClass(ClassToBeProxied.class) );
        Object result = null;
        if( "of".equals(method.getName()) ) { 
            out.print("Proxied: " );
        }
        else { 
            out.print( ": " );
        }
        
        try { 
           result = method.invoke(target, args );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    } 
    
}