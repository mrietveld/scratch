package org.ocram.reflection;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
}
