package org.ocram.reflection;

import java.lang.reflect.Array;

import org.apache.commons.lang3.ClassUtils;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ArrayTest extends ScratchBaseTest {

    @Test
    public void arrayReflectionTest() throws Exception {
       Object [] intArr = { 24, 96 };
       int length = Array.getLength(intArr);

       Class componentType = int.class;
       Object copyArr = Array.newInstance(componentType, length );
       for( int i = 0; i < length; ++i ) {
          Array.set(copyArr, i, intArr[i]);
       }

       int [] copyIntArr = (int[]) copyArr;

       for( int i = 0; i < length; ++i ) {
          assertEquals( "Unequal values for " + i, intArr[i], copyIntArr[i]);
       }

       Class intClass = ClassUtils.getClass("int");
       assertEquals( int.class, intClass);
    }
}
