package org.ocram.string;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class StringTest extends ScratchBaseTest {

    @Test
    public void indexOfTest() {
        String shortenMe = "org.jbpm.persistence.session.PersistentStatefulSessionTest.testMeNOW";
        String shortTestMethod = shortenMe.substring(0, shortenMe.lastIndexOf('.'));
        out.println(shortTestMethod);

        shortTestMethod = shortenMe.substring(shortTestMethod.lastIndexOf('.') + 1);
        assertEquals(shortTestMethod, "PersistentStatefulSessionTest.testMeNOW");

        shortTestMethod = shortenMe.substring(shortenMe.substring(0, shortenMe.lastIndexOf('.')).lastIndexOf('.') + 1);
        assertEquals(shortTestMethod, "PersistentStatefulSessionTest.testMeNOW");
    }

    @Test
    public void java5StringMethod() {
        String javaVersion = System.getProperty("java.version");
        if( ! javaVersion.matches("1.5.*") ) { 
            return;
        }
        
        String test = "Does this work?";
        boolean og = test.contains("work");
        out.println(test + " : " + og);
    }

    @Test
    public void base64EncodingTest() throws Exception {

        String userPassword = "thisIsMyVeryCoolPassword";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream base64OutputStream = MimeUtility.encode(baos, "base64");
            base64OutputStream.write(userPassword.getBytes());
            base64OutputStream.close();
        } catch (MessagingException me) {
            throw new IOException("Unable to encode user password in base64: " + me.getMessage(), me);
        }

        String encodedAuthorization = new String(baos.toByteArray());
        System.out.println(encodedAuthorization);

    }
    
    @Test
    public void emptyStringMatchesTest() { 
       assertTrue( " \n".matches("\\s+"));
       assertTrue( " ".matches("\\s+"));
       assertFalse( " S ".matches("\\s+"));
    }
    
    @Test 
    public void sortStringArraysTest() { 
        String [] ooga = { "cc", "ad", "ba" };

        for( String og : ooga ) { 
            System.out.println( og );
        }
        
        System.out.println( "---" );
        Arrays.sort(ooga);
        
        for( String og : ooga ) { 
            System.out.println( og );
        }
    }
    
    @Test
    public void subStringTest() { 
        String og = "aabcde";
       out.println( og.substring(2, og.length()) );
    }

    
}