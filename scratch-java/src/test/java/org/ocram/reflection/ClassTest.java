package org.ocram.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Predicate;

import org.drools.persistence.jpa.JpaTimerJobInstance;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.reflections.util.ClasspathHelper;

public class ClassTest extends ScratchBaseTest {

    @Test
    public void extendClassTest() {
        JpaTimerJobInstance og = new JpaTimerJobInstance(null, null, null, null, null);

        Class<?> clazz = og.getClass();
        logger.debug( clazz.getSimpleName() );
        logger.debug( "d: " + clazz.getDeclaringClass() );
        logger.debug( "e: " + clazz.getEnclosingClass() );
        int i = 0;
        do {
            logger.debug( i++ + ": " + clazz.getSimpleName() );
            clazz = clazz.getSuperclass();
        }
        while( clazz!= null );
    }

    @Test
    public void nonExistentPkgsTest() {
        Set<URL> urls = ClasspathHelper.forPackage("does.not.exist", this.getClass().getClassLoader());
        assertTrue( "URLs found for non-existent package?!?", urls.isEmpty() );
    }

    @Test
    public void paramTypesTest() {
        List<String> list = new ArrayList<String>(0);
        Type paramType = list.getClass().getGenericInterfaces()[0];
        assertTrue( paramType instanceof ParameterizedType );
        assertTrue( ((ParameterizedType) paramType).getRawType().equals(List.class) );
        Type genType = ((ParameterizedType) paramType).getActualTypeArguments()[0];
        assertTrue( genType.equals(String.class));
    }

    @Test
    public void retrieveGenericType() {

        Predicate proxy = (Predicate) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { Predicate.class},
                new InvocationHandler() {

                    @Override
                    public Object invoke( Object proxy, Method method, Object[] args ) throws Throwable {
                        throw new IllegalStateException(Predicate.class.getSimpleName() + "." + method.getName() + " should not be called on the EMPTY PREDICATE instance!");
                    }
                });

        Map<String, Predicate> predicates = new HashMap<String, Predicate>();
        predicates.put("test", proxy);
        assertTrue( proxy == proxy );

    }

}
