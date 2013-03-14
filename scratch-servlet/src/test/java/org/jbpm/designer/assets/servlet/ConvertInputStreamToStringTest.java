package org.jbpm.designer.assets.servlet;

import static junit.framework.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

public class ConvertInputStreamToStringTest {

    @Test
    public void testConvertInputStream() throws Exception { 
       AssetResourceServlet servlet = new AssetResourceServlet(); 
       
       StringBuilder input = new StringBuilder("a really long string, ");
       for( int i = 0; i < 10; ++i ) { // (x 2) ^10
           input.append(input.toString()); 
       }
       
       InputStream stream = new ByteArrayInputStream(input.toString().getBytes("UTF-8"));
       
       String result = servlet.convertInputStreamToString(stream);
       
       assertEquals("Input and result are not equal.", input.toString(), result);
    }
}
