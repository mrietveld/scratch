package org.ocram.reflection;

import static java.lang.System.out;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.jbpm.process.audit.JPAProcessInstanceDbLog;
import org.jbpm.process.audit.ProcessInstanceDbLog;
import org.junit.Ignore;
import org.junit.Test;

public class ProcessInstanceDbLogInterfaceTest extends Assert {

    @Test
    @Ignore //OCRAM only if not run by maven
    public void compareMethodsJPAAndHibernate() throws Exception {

        List<Method[]> methodMethods = new ArrayList<Method[]>();
        methodMethods.add(ProcessInstanceDbLog.class.getMethods());
        methodMethods.add(JPAProcessInstanceDbLog.class.getDeclaredMethods());

        String[] type = { "hibernate", "jpa" };
        for (int i = 0; i < 2; ++i) {
            Method[] compareMethods = methodMethods.get(i);
            Method[] toMethods = methodMethods.get(1 - i);
            METHODS: for (Method thisMethod : compareMethods) {
                int modifier = thisMethod.getModifiers();
                if (Modifier.isStatic(modifier) && Modifier.isPublic(modifier)) {
                    boolean found = false;
                    for (Method toMethod : toMethods) {
                        if ("setEnvironment".equals(thisMethod.getName())
                            || "dispose".equals(thisMethod.getName())) {
                            continue METHODS;
                        }
                        if (thisMethod.getName().equals(toMethod.getName())) {
                            Class<?>[] params = thisMethod.getParameterTypes();
                            Class<?>[] toParams = toMethod.getParameterTypes();
                            if (params.length != toParams.length) {
                                continue;
                            }
                            found = true;
                            for (int p = 0; p < params.length; ++p) {
                                if (!params[p].equals(toParams[p])) {
                                    found = false;
                                }
                            }
                        }
                    }
                    if( ! found ) { 
                        out.print(thisMethod.getName() + ": ");
                        for( Class param : thisMethod.getParameterTypes() ) { 
                            out.print( param + " " );
                        }
                        out.println();
                    }
                    assertTrue(thisMethod.getName() + " could not be found in the " + type[1 - i] + " methods", found);
                }
            }
        }

    }
}
