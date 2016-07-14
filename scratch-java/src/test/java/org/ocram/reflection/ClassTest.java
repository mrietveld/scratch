package org.ocram.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.Predicate;

import org.drools.persistence.jpa.JpaTimerJobInstance;
import org.hibernate.transform.AliasToBeanConstructorResultTransformer;
import org.junit.Test;
import org.kie.internal.utils.KieService;
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

    private List<String> listField = new ArrayList<String>(0);

    @Test
    public void paramTypesTest() {
        List<String> list = new ArrayList<String>(0);
        Type paramType = list.getClass().getGenericInterfaces()[0];
        assertTrue( paramType instanceof ParameterizedType );
        assertTrue( ((ParameterizedType) paramType).getRawType().equals(List.class) );
        TypeVariable genType = (TypeVariable) ((ParameterizedType) paramType).getActualTypeArguments()[0];

        // Type info is not available for decl's in methods
        assertTrue( ((TypeVariable) genType).getTypeName(), genType.getTypeName().equals("E"));

        paramType = listField.getClass().getGenericInterfaces()[0];
        assertTrue( paramType instanceof ParameterizedType );
        assertTrue( ((ParameterizedType) paramType).getRawType().equals(List.class) );
        genType = (TypeVariable) ((ParameterizedType) paramType).getActualTypeArguments()[0];

        // Type info is not available for fields
        assertTrue( ((TypeVariable) genType).getTypeName(), genType.getTypeName().equals("E"));
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

    @Test
    public void classNameTest() {
        System.out.println( "N: [" + this.getClass().getName() + "]" );
        System.out.println( "C: [" + this.getClass().getCanonicalName() + "]" );
    }

    @Test
    public void codeSourceTest() throws URISyntaxException {

        Class [] classes = {
                StackTraceElement.class,
            Integer.class,
            this.getClass(),
            KieService.class,
        };

        for( Class clazz : classes ) {
            ProtectionDomain domain = clazz.getProtectionDomain();
            CodeSource source = domain.getCodeSource();

            String loc = ( source == null ) ? "null" : source.getLocation().toURI().toString();
            System.out.println( clazz.getName() + ": " + loc );
        }
    }

    public Object callOnlyStaticMethods(String name, Class [] parameterTypes, Object [] parameters) throws Exception {

        // use getDeclaredMethod and setAccessible if you want to call non-public methods
        Method method =  this.getClass().getMethod(name, parameterTypes);
        if( ! Modifier.isStatic(method.getModifiers()) ) {
            throw new UnsupportedOperationException("Only static methods may be called, " + method.getName() + " is not static!");
        }
        return method.invoke(null, parameters);
    }


}
