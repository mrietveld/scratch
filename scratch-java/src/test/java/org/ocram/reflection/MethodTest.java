package org.ocram.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.WorkItemManager;
import org.kie.api.task.TaskService;
import org.ocram.ScratchBaseTest;
import org.ocram.collections.objects.Bam;

public class MethodTest extends ScratchBaseTest {

    @Test
    public void useMethods() throws Exception {
        Method method = MethodDepot.class.getMethod("returnTwo", (Class[]) null);
        MethodDepot object = new MethodDepot();

        Object result = method.invoke(object, (Object[]) null);

        int resultVal = ((Integer) result).intValue();
        assertTrue("Result is not 2, but " + resultVal, resultVal == 2);
    }

    private static class MethodDepot {
        @SuppressWarnings("unused")
        public int returnTwo() {
            return 2;
        }
    }

    @Test
    public void testMethodReflection() {
        Bam bam = new Bam();
        Method method = null;
        String methodName = "geta_";

        Integer arr[] = new Integer[40];
        methods: for (int i = 1; i < 10; ++i) {
            try {
                method = Bam.class.getDeclaredMethod(methodName + i, (Class[]) null);
            } catch (NoSuchMethodException nsme) {
                assertTrue(nsme.getMessage().contains(methodName + 7));
                continue methods;
            }
            if (method != null) {
                try {
                    arr[i] = (Integer) method.invoke(bam);
                    int val = 0;
                    switch (i) {
                    case 1:
                        val = 1;
                        break;
                    case 2:
                        val = 22;
                        break;
                    case 3:
                        val = 33;
                        break;
                    case 4:
                    case 5:
                    case 6:
                        val = i;
                        break;
                    case 8:
                        val = 822;
                        break;
                    case 9:
                        val = 99;
                        break;
                    }
                    assertTrue("" + i + "(" + val + ":" + arr[i] + ")", val == arr[i]);
                } catch (IllegalArgumentException iae) {
                    out.println(iae.getClass().getSimpleName() + ": " + iae.getMessage());
                } catch (IllegalAccessException iae) {
                    out.println(iae.getClass().getSimpleName() + ": " + iae.getMessage());
                } catch (InvocationTargetException ite) {
                    out.println(ite.getClass().getSimpleName() + ": " + ite.getMessage());
                }
            }
        }

    }

    @Test
    public void testMethodParameters() throws Exception {
        Method meth = getMethodObject(null, null);
        for (Type type : meth.getGenericParameterTypes()) {
            if (type instanceof ParameterizedType) {
                Type[] atypes = ((ParameterizedType) type).getActualTypeArguments();
                assertEquals(((Class<?>) atypes[0]).getName(), "java.lang.String");
                assertEquals(((Class<?>) atypes[1]).getName(), "java.lang.Object");
            }
        }
    }

    private Method getMethodObject(HashMap<String, Object> bo, String og) throws SecurityException, NoSuchMethodException {
        String myMethod = new Throwable().getStackTrace()[0].getMethodName();
        Class[] types = { HashMap.class, String.class };
        return this.getClass().getDeclaredMethod(myMethod, types);
    }

    @Test
    public void testMethodParameterTypes() throws Exception {
        String[] skips = { "fireAllRules", "addEventListener", "removeEventListener" };
        HashMap<String, Method> methodNames = new HashMap<String, Method>();
        Queue<Class> interfaces = new java.util.LinkedList<Class>();
        Set<Class> inters = new java.util.HashSet<Class>();
        interfaces.add(KieSession.class);
        interfaces.add(TaskService.class);
        interfaces.add(WorkItemManager.class);
        Class service = interfaces.poll();
        do {
            inters.add(service);
            interfaces.addAll(Arrays.asList(service.getInterfaces()));
            service = interfaces.poll();

        } while (service != null);

        for (Class ser : inters) {
            for (Method dupMethod : ser.getMethods()) {
                Method method = methodNames.put(dupMethod.getName(), dupMethod);
                if (method != null) {
                    if (method.equals(dupMethod)) {
                        continue;
                    }
                    boolean check = true;
                    for (String skip : skips) {
                        if (skip.equals(method.getName())) {
                            check = false;
                        }
                    }
                    if (check) {
                        assertTrue(ser.getSimpleName() + "." + method.getName(),
                                method.getParameterTypes().length != dupMethod.getParameterTypes().length);
                    }
                }
            }
        }
    }

    @Test
    public void delMe() throws Exception {
        out.println( Thread.currentThread().getStackTrace()[1].getMethodName());
    }
}
