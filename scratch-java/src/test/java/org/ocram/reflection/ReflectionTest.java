package org.ocram.reflection;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

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
        List<String> arrayString = new ArrayList<String>();
        arrayString.add("asdf");

        logger.debug("- " + arrayString.getClass().getName());
        Type type = arrayString.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) type;
            logger.debug("o : " + paramType.getOwnerType());
            logger.debug("r : " + paramType.getRawType());
            for (Type ptype : paramType.getActualTypeArguments()) {
                if (ptype instanceof TypeVariable) {
                    TypeVariable typeVar = (TypeVariable) ptype;
                    logger.debug("tv: " + typeVar.getName());
                    for( Type bound : typeVar.getBounds() ) { 
                       logger.debug( bound.getClass().getName() );
                    }
                }

            }
        }
    }
    
    @Test
    public void packageFolderTest() { 
        String pkgFolder = this.getClass().getPackage().toString();
        pkgFolder = pkgFolder.replace("package ", "");
        pkgFolder = pkgFolder.replaceAll("\\.", File.separator);
        logger.info( pkgFolder );
    }

}
