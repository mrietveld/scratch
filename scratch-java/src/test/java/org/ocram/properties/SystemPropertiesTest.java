package org.ocram.properties;

import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class SystemPropertiesTest extends ScratchBaseTest {

    private static final String INT_PROP = "integer.property";

    @Test
    public void increaseIntegerPropertyTest() {

        String propVal = System.getProperty(INT_PROP);
        assertNull(propVal);

        increaseProperty(INT_PROP);
        propVal = System.getProperty(INT_PROP);
        assertTrue(Integer.parseInt(propVal) == 0);

        increaseProperty(INT_PROP);
        propVal = System.getProperty(INT_PROP);
        assertTrue(Integer.parseInt(propVal) == 1);

        increaseProperty(INT_PROP);
        propVal = System.getProperty(INT_PROP);
        assertTrue(Integer.parseInt(propVal) == 2);
    }

    private void increaseProperty(String propertyName) {
        String val = System.getProperty(propertyName);
        int valInt = 0;
        if (val != null) {
            valInt = Integer.parseInt(val) + 1;
        }
        System.setProperty(propertyName, String.valueOf(valInt));
    }

    @Test
    @Ignore //OCRAM only if not run by maven
    public void testRootClassPathForFindingPropertiesFiles() {
        Class clazz = getClass();
        out.println("c: " + clazz.getSimpleName());
        ProtectionDomain pd = clazz.getProtectionDomain();
        out.println("p: " + pd.toString());
        CodeSource cs = pd.getCodeSource();
        out.println("c: " + cs.toString());
        String path = cs.getLocation().toString();
        out.println("l: " + path);
        path = path.substring(5);
        out.println("p: " + path);
    }

    @Test
    public void testFindingProperties() throws Exception {
        Properties props = new Properties();

        InputStream is = null;
        is = getClass().getResourceAsStream("root.properties");
        assertNull(is);
        is = getClass().getResourceAsStream("/root.properties");
        assertNotNull(is);
        props.load(is);
        props.list(out);
        
        is = null;
        is = getClass().getResourceAsStream("pkg.properties");
        assertNotNull(is);
        props.load(is);
        props.list(out);
        is = getClass().getResourceAsStream("/pkg.properties");
        assertNull(is);

        // Try the classpath
        props = new Properties();
        URL url = ClassLoader.getSystemResource("root.properties");
        assertNotNull(url);
        props.load(url.openStream());
        props.list(out);

        props = new Properties();
        url = ClassLoader.getSystemResource("org/ocram/properties/pkg.properties");
        assertNotNull(url);
        props.load(url.openStream());
        props.list(out);
    }
}
