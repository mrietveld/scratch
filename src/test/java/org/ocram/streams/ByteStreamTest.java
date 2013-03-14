package org.ocram.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class ByteStreamTest {

    @Test
    public void testBaosReopenStream() throws IOException { 
        String input = "asdfasdfasdfasfsadfsadf";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.write(input.getBytes());
        
        baos.toByteArray();
        
        
        
    }
}
