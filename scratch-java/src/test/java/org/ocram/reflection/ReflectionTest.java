package org.ocram.reflection;

import static junit.framework.Assert.assertEquals;

import java.lang.reflect.*;
import java.util.ArrayList;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ReflectionTest extends ScratchBaseTest {

    @Test
    public void methodNameTest() {
        String correctName = "methodNameTest";
        String testName = Thread.currentThread().getStackTrace()[1].getMethodName();
        assertEquals("Method name should be " + correctName + " not " + testName, correctName, testName);
    }

    @Test
    public void genericTypesTest() {
        ArrayList<String> arrayString = new ArrayList<String>();
        arrayString.add("asdf");

        out.println("- " + arrayString.getClass().getName());
        for (Type type : arrayString.getClass().getGenericInterfaces()) {
            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;
                System.out.println("o : " + paramType.getOwnerType());
                System.out.println("r : " + paramType.getRawType());
                for (Type ptype : paramType.getActualTypeArguments()) {
                    if (ptype instanceof TypeVariable) {
                        TypeVariable typeVar = (TypeVariable) ptype;
                        System.out.println("tv: " + typeVar.getName());
                    }

                }
            }
        }
    }

}
