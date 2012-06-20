package org.ocram.hash;

import static junit.framework.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;

import org.junit.Ignore;
import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.objects.ClassToBeProxied;

public class HashGenerationTest extends ScratchBaseTest {

    private static MessageDigest hashAlgorithm;
    static {
        try {
            hashAlgorithm = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            // do nothing?
        }
    }

    @Test
    @Ignore //OCRAM only if not run by maven
    public void objectBytesHashIdGenerationTest() throws Exception {

        ClassToBeProxied object = new ClassToBeProxied();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();

        byte[] serializedData = baos.toByteArray();
        baos.close();

        hashAlgorithm.reset();
        hashAlgorithm.update(serializedData);
        byte[] messageDigest = hashAlgorithm.digest();

        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < messageDigest.length; i++) {
            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
        }
        out.println("MD5 Hash for object is: " + hexString);
    }

    @Test
    @Ignore  //OCRAM only if not run by maven
    public void identityHashCodeTest() {
        ClassToBeProxied object = new ClassToBeProxied();

        out.println("System identity hash code is: " + System.identityHashCode(object));
        out.println("System identity hash code is: " + System.identityHashCode(new ClassToBeProxied()));
        out.println("System identity hash code is: " + System.identityHashCode(new ClassToBeProxied()));
    }

    @Test
    @Ignore  //OCRAM only if not run by maven
    public void modifiationIdentityHashCodeTest() throws Exception {
        ClassToBeProxied object;

        for( long l = 0; l < 2247483645l; ++l ) {
            object = new ClassToBeProxied();
            int hashCode = System.identityHashCode(object);
            assertTrue( "Hash code <= 0", hashCode > 0 );
            object.stringField = "changed";
            assertTrue( "Hash code has changed", hashCode == System.identityHashCode(object));
            object.stringField = null;
            ClassToBeProxied.class.getField("stringField").setAccessible(false);
            assertTrue( "Hash code has changed", hashCode == System.identityHashCode(object));
        }
    }

    @Test
    @Ignore  //OCRAM only if not run by maven
    public void hashClassTest() {
        final Object obj = new Object();
        final int target = obj.hashCode();
        Object clash;
        long ct = 0;
        out.println( "Start.." );
        do {
            clash = new Object();
            ++ct;
        } while (clash.hashCode() != target && ct < 10L * 1000 * 1000 * 1000L);
        if (clash.hashCode() == target) {
            out.println(ct + ": " + obj + " - " + clash);
        } else {
            out.println("No clashes found");
        }
    }

}
