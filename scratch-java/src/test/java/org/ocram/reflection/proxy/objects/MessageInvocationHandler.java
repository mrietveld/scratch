package org.ocram.reflection.proxy.objects;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Set;

public class MessageInvocationHandler implements InvocationHandler {

    private boolean methodSet = false;
    private MethodRequest message;

    private static Set<String> unsupportedMethods = new HashSet<String>();
    static {
        Method[] objectMethods = Object.class.getMethods();
        for (Method objMethod : objectMethods) {
            unsupportedMethods.add(objMethod.getName());
            System.out.println("OBJ: " + objMethod.getName());
        }
    }

    public MessageInvocationHandler() {

    }

    public static ClassInterface createRequest() {
        Class<?>[] interfaces = { ClassInterface.class, MethodRequestFactory.class };
        return (ClassInterface) Proxy.newProxyInstance(MethodRequest.class.getClassLoader(), interfaces, new MessageInvocationHandler());
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if( "getRequest".equals(method.getName()) && args == null ) { 
            if( ! methodSet ) { 
                throw new IllegalStateException("No request method has been set yet!");
            }
            return this.message;
        }
        if (unsupportedMethods.contains(method.getName())) {
            throw new UnsupportedOperationException(method.getName() + " is unsupported as a request method!");
        }

        if (methodSet) {
            throw new IllegalStateException("The request method has already been set!");
        }
        methodSet = true;

        this.message = new MethodRequest(method, args);

        return null;
    }

}