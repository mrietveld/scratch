package org.ocram.string;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;
import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class StringTest extends ScratchBaseTest {

    @Test
    public void indexOfTest() {
        String origMe = "MAX_asdf_FFF";
        int _index = origMe.indexOf('_');

        String lcMe = origMe.substring(0, _index).toLowerCase() + origMe.substring(_index);
        assertEquals(lcMe, "max_asdf_FFF");
    }

    @Test
    public void java5StringMethod() {
        String javaVersion = System.getProperty("java.version");
        if( !javaVersion.matches("1.5.*") ) {
            return;
        }

        String test = "Does this work?";
        boolean og = test.contains("work");
        logger.debug(test + " : " + og);
    }

    @Test
    public void base64EncodingTest() throws Exception {

        String userPassword = "thisIsMyVeryCoolPassword";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            OutputStream base64OutputStream = MimeUtility.encode(baos, "base64");
            base64OutputStream.write(userPassword.getBytes());
            base64OutputStream.close();
        } catch( MessagingException me ) {
            throw new IOException("Unable to encode user password in base64: " + me.getMessage(), me);
        }

        String encodedAuthorization = new String(baos.toByteArray());
        logger.debug(encodedAuthorization);

    }

    @Test
    public void emptyStringMatchesTest() {
        assertTrue(" \n".matches("\\s+"));
        assertTrue(" ".matches("\\s+"));
        assertFalse(" S ".matches("\\s+"));
    }

    @Test
    public void sortStringArraysTest() {
        String[] ooga = { "cc", "ad", "ba" };

        for( String og : ooga ) {
            logger.debug(og);
        }

        logger.debug("---");
        Arrays.sort(ooga);

        for( String og : ooga ) {
            logger.debug(og);
        }
    }

    @Test
    public void subStringTest() {
        String og = "0abcde";
        logger.debug(og.substring(1));

        List<String> of = new ArrayList<String>();

    }

    @Test
    public void capitalCaseTest() {
        String statusStr = "aRmeD";
        String goodStatusStr = statusStr.toLowerCase();
        goodStatusStr = goodStatusStr.substring(0, 1).toUpperCase() + goodStatusStr.substring(1);
        logger.debug(goodStatusStr);
    }

    @Test
    public void letterLoopingTest() {
        for( int i = 0; i < 90; ++i ) {
            int id = i % 26;
            char first = (char) ('A' + id);

            System.out.print(new String(first + String.valueOf((i + 1) / 26)) + " ");
            if( i % 26 == 25 ) {
                System.out.println();
            }
        }
    }

    @Test
    public void testTest() {
       for( int i = 0; i < hexArray.length; ++i ) { 
          System.out.println( 
                  hexArray[i]
                  + " : "  + (int) hexArray[i]
                  );
          int j = 16*i;
          System.out.println( 
                  j  + " : " + (j >>> 4) );
       }
    }

    @Test
    public void byteArrayAsString() {

        String[] vals = { 
                "a long string containing spaces and other characters +ěš@#$%^*()_{}\\/.,", 
                "Ampersand in the string &.",
                "\"quoted string\"" };

        for( int i = 0; i < vals.length; ++i ) {
            
            System.out.println(i + " [" + vals[i] + "] " + vals[i].length());
            String out = stringToHex(vals[i]);
            System.out.println( out.length() + " [" + out + "]");
            String copy = hexToString(out);;
            assertEquals(copy, vals[i]);
        }
    }

    final protected static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String stringToHex( String in ) {
        byte[] bytes = stringToBytes(in);
        return DatatypeConverter.printBase64Binary(bytes);
    }
    
    public static String hexToString( String in ) {
        byte [] bytes = DatatypeConverter.parseBase64Binary(in);
        return bytesToString(bytes);
    }

    // The following methods bypass issues with string encoding
    
    public static byte[] stringToBytes( String str ) {
        char[] buffer = str.toCharArray();
        byte[] b = new byte[buffer.length << 1];
        for( int i = 0; i < buffer.length; i++ ) {
            int bpos = i << 1;
            b[bpos] = (byte) ((buffer[i] & 0xFF00) >> 8);
            b[bpos + 1] = (byte) (buffer[i] & 0x00FF);
        }
        return b;
    }

    public static String bytesToString( byte[] bytes ) {
        char[] buffer = new char[bytes.length >> 1];
        for( int i = 0; i < buffer.length; i++ ) {
            int bpos = i << 1;
            char c = (char) (((bytes[bpos] & 0x00FF) << 8) + (bytes[bpos + 1] & 0x00FF));
            buffer[i] = c;
        }
        return new String(buffer);
    }
    
    @Test
    public void replaceAll() { 
        String with = "ggg";
        String og = "asdf fff";
        og.replaceAll("fff", with);
        assertTrue( "Replace all failed: [" + og + "]", og.contains(with) );
    }

}