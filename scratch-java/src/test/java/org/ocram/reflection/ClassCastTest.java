package org.ocram.reflection;

import static junit.framework.Assert.*;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ClassCastTest extends ScratchBaseTest {

    @Test
    public void exceptionCastMethod() {
        IOException ioe = new IOException("This is a test exception");
        ClassCastException cause = new ClassCastException();
        ioe.initCause(cause);

        assertEquals(ioe.getCause(), cause);
    }

    @Test(expected=Throwable.class)
    public void throwableCastTest() {
        RuntimeException runner = new RuntimeException("weave");
        Throwable rug = (Throwable) runner;
        throw runner;
    }

    @Test
    public void primitiveCastBoxedTest() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        int one = 23;
        params.put("one", one);
        int eno = (Integer) params.get("one");
        assertTrue( eno == one );
    }

    @Test
    public void nullInstanceOfTest() {
        String og = null;

        assertFalse( "null is no instance of String", og instanceof String );
        og = "og";
        assertTrue( "null is an instance of String", og instanceof String );

    }

    @Test
    public void arrayClassNametest() throws Exception {
        String className = (new String[0]).getClass().getName();
        Class<?> clazz = Class.forName(className);
    }

    @Test
    public void arrayClassNameSerializationTest() throws Exception {
        String [] cereal = { "cheerios", "raisin flakes" };
        String className = cereal.getClass().getName();
        assertNotNull(className);
        logger.debug(className);
        logger.debug(cereal.getClass().getCanonicalName());
        assertNotNull( Class.forName(className) );
        Constructor<?> [] cons = Class.forName(className).getConstructors();
        assertEquals( "There are no constructors for String arrays", 0, cons.length );
    }
}
