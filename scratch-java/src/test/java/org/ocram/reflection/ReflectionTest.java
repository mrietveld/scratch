package org.ocram.reflection;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

@XmlAccessorType(XmlAccessType.FIELD)
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
        logger.debug( pkgFolder );
        assertTrue( pkgFolder, pkgFolder.endsWith("reflection"));
    }


    @Test
    public void annotation() {
        String pkgFolder = this.getClass().getPackage().toString();
    }

    @Test
    public void packageNameTest() {
        Package pkg = this.getClass().getPackage();

        System.out.println( "Name: " + pkg.getName() );
        System.out.println( "Spec title: " + pkg.getSpecificationTitle() );
        System.out.println( "Impl title: " + pkg.getImplementationTitle() );

        pkg = String.class.getPackage();

        System.out.println( "Name: " + pkg.getName() );
        System.out.println( "Spec title: " + pkg.getSpecificationTitle() );
        System.out.println( "Impl title: " + pkg.getImplementationTitle() );

        pkg = int.class.getPackage();
        System.out.println( "Name: " + pkg.getName() );
        System.out.println( "Spec title: " + pkg.getSpecificationTitle() );
        System.out.println( "Impl title: " + pkg.getImplementationTitle() );

    }
}
