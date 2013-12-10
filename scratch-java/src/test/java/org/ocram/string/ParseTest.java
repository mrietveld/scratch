package org.ocram.string;

import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ParseTest extends ScratchBaseTest {

    @Test
    public void testLong() throws Exception {
        long asdf = 120301201L;
        Long testLong = new Long(asdf);
        assertTrue(testLong.longValue() == 120301201L);
    }

    @Test
    public void testReplaceAll() throws Exception {
        String test = "asdf<\r>asdfasdf<\n>asdfasdfsdf<\r\n>asdfasdfdas<\n\r>asdfd";

        String testRep = test.replaceAll("\\r", "");
        assertTrue(testRep.equals("asdf<>asdfasdf<\n>asdfasdfsdf<\n>asdfasdfdas<\n>asdfd"));

        testRep = testRep.replaceAll("\\n", "");
        assertTrue(testRep.equals("asdf<>asdfasdf<>asdfasdfsdf<>asdfasdfdas<>asdfd"));

        testRep = test.replaceAll("\r|\n", "");
        assertTrue(testRep.equals("asdf<>asdfasdf<>asdfasdfsdf<>asdfasdfdas<>asdfd"));
    }

    @Test
    public void testSplit() throws Exception {
        String url = "/og/ooga/sessoin/23/startProcess?param=mala&raram=para";

        String[] parts = url.split("[/?&]");

        for (String og : parts) {
            logger.debug(og);
        }
    }

    @Test
    public void testBooleanParse() throws Exception {
        assertFalse(Boolean.parseBoolean(null));
    }

    @Test
    public void splitAndJoin() {
        String in = "1";
        String[] out;
        String again;

        out = split(in);
        assertEquals(1, out.length);
        assertEquals("1", out[0]);
        again = join(out);
        assertEquals(in, again);

        out = split(" 1 ");
        assertEquals(1, out.length);
        assertEquals("1", out[0]);
        again = join(out);
        assertEquals("1", again);

        out = split("1,2");
        assertEquals(2, out.length);
        assertEquals("1", out[0]);
        assertEquals("2", out[1]);
        again = join(out);
        assertEquals(again, "1,2");

        out = split(" 1,2 ");
        assertEquals(2, out.length);
        assertEquals("1", out[0]);
        assertEquals("2", out[1]);
        out[0] = "1 ";
        out[1] = " 2";
        again = join(out);
        assertEquals("1,2", again);

        out = split(" 1, ");
        assertEquals(out.length, 1);
        assertEquals(out[0], "1");
        out[0] = "1 ";
        again = join(out);
        assertEquals("1", again);

        out = split(" , 2   ");
        assertEquals(out.length, 1);
        assertEquals(out[0], "2");
        out[0] = "2 ";
        again = join(out);
        assertEquals("2", again);

        out = split(" ,    ");
        assertEquals(out.length, 0);
        again = join(out);
        assertEquals("", again);

    }

    String[] split(String in) {
        String[] splitIn = in.split(",");
        List<String> outList = new ArrayList<String>();
        for (int i = 0; i < splitIn.length; ++i) {
            splitIn[i] = splitIn[i].trim();
            if (!splitIn[i].isEmpty()) {
                outList.add(splitIn[i]);
            }
        }
        return outList.toArray(new String[outList.size()]);
    }

    String join(String[] inArr) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < inArr.length; ++i) {
            String temp = inArr[i].trim();
            if (!temp.isEmpty()) {
                if (out.length() > 0) {
                    out.append(",");
                }
                out.append(temp);
            }
        }
        return out.toString();
    }

    @Test
    public void quickie() throws Exception {
        String fileName = "org/kie/tests/wb/base/test/MyType.class";
        String className = fileName.replaceAll(File.separator, ".");
        className = className.substring(0, fileName.length() - ".class".length());
        System.out.println( "FILE : " + fileName );
        System.out.println( "CLASS: " + className );
    }
    
}
