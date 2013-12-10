package org.ocram.reflection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;

public class ByteCodeTest extends ScratchBaseTest {

    private static Reflections reflections = new Reflections(ClasspathHelper.forPackage("org.ocram"), new ResourcesScanner());

    @Test
    public void getByteCodeTest() throws Exception {
        Class<?> clazz = this.getClass();
        URL classFileUrl = this.getClass().getResource(clazz.getSimpleName() + ".class");
        if (!"file".equalsIgnoreCase(classFileUrl.getProtocol()))
            throw new IllegalStateException("Main class is not stored in a file.");
        File classFile = new File(classFileUrl.getPath());
        assertTrue(classFile != null);
        logger.info(classFile.getAbsolutePath());
        
        byte [] fileBytes = readFile(classFile);
        assertTrue( "no bytes retrieved", fileBytes.length > 0 );
    }

    private byte[] readFile(File file) throws Exception {
        FileChannel ch = null;
        FileInputStream fin = null;
        byte[] result = new byte[0];
        try {
            fin = new FileInputStream(file);
            ch = fin.getChannel();
            int size = (int) ch.size();
            MappedByteBuffer buf = ch.map(MapMode.READ_ONLY, 0, size);
            result = new byte[size];
            buf.get(result);
        } finally {
            if (fin != null) {
                fin.close();
            }
            if (ch != null) {
                ch.close();
            }
        }
        return result;
    }
}
