package org.ocram.reflection.proxy;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.ejb.EntityManagerImpl;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.reflection.proxy.objects.ClassInterface;
import org.ocram.reflection.proxy.objects.ClassToBeProxied;
import org.ocram.reflection.proxy.objects.TestInvocationHandler;

@SuppressWarnings("rawtypes")
public class ProxyTest extends ScratchBaseTest {

    @Test
    public void testGetInterfaces() { 
        List<Class> interfaces = new ArrayList<Class>();
       
        addThese(EntityManagerImpl.class, interfaces);
        Class superClass = EntityManagerImpl.class.getSuperclass();
        while( superClass != null ) { 
            addThese(superClass, interfaces);
            superClass = superClass.getSuperclass();
        }
    }
   
    private void addThese(Class bob, List<Class> interfaces ) { 
        out.println( bob.getSimpleName() + ":" );
        if( bob != null ) { 
            Class [] addThese = bob.getInterfaces();
            for( int i = 0; i < addThese.length; ++i ) { 
                interfaces.add(addThese[i]);
            }
            for( Class clazz: interfaces ) { 
                out.println(clazz.getSimpleName());
            }
        }
        else { 
            out.println( "bob is NULL!");
        }
    }
    
    @Test
    public void testProxy() { 
        ClassInterface obj = new ClassToBeProxied(6);
        runTest(obj);
        Object proxy = TestInvocationHandler.createProxy(obj);
        obj = (ClassInterface) proxy;
        out.println( "--------------------" ) ;
        runTest(obj);
    }
    
    private void runTest(ClassInterface obj) { 
        int j = obj.of();
        obj.og();
        obj.of();
        obj.og();
        obj.oh(j);
    }
}
