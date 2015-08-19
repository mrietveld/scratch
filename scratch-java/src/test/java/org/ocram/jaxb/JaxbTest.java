package org.ocram.jaxb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.jaxb.objects.OneCommand;
import org.ocram.jaxb.objects.StringHolder;
import org.ocram.jaxb.objects.TestRequest;
import org.ocram.jaxb.objects.TwoCommand;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

public class JaxbTest extends ScratchBaseTest {

    @Test
    public void jaxbAnyElementMixedTest() throws Exception {
        Class[] classes = { TestRequest.class, OneCommand.class, TwoCommand.class };
        JAXBContext ctx = JAXBContext.newInstance(classes);

        TestRequest testReq = new TestRequest();
        OneCommand one = new OneCommand();
        one.setDeploymentId("asdf");
        one.setProcessInstanceId(23l);
        one.setVersion("4");
        testReq.getCommands().add(one);
        TwoCommand two = new TwoCommand();
        two.setExistence(55l);
        two.setOtherThing("that");
        two.setVersion("323");
        testReq.getCommands().add(two);

        String out = serialize(ctx, true, testReq);
        logger.info( out);
        TestRequest rebornReq = (TestRequest) deserialize(ctx, out, TestRequest.class);
        assertEquals( 2, rebornReq.getCommands().size() );
    }

    @Test
    public void deserializingStringsTest() throws Exception { 
        Class[] classes = { StringHolder.class };
        JAXBContext ctx = JAXBContext.newInstance(classes);
       
        String expectedMyObject = "\"quoted string\"";
        StringHolder test = new StringHolder(expectedMyObject);
        
        String xmlStr = serialize(ctx, true, test);
        System.out.println( xmlStr );
        StringHolder copy = deserialize(ctx, xmlStr, StringHolder.class);
        assertEquals( "String not equal", test.getString(), copy.getString());
    }
    
    public static String serialize( JAXBContext jaxbContext, boolean prettyPrint, Object object ) throws Exception {
        Marshaller marshaller = null;
        marshaller = jaxbContext.createMarshaller();
        if( prettyPrint ) {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }

        marshaller.setProperty(CharacterEscapeHandler.class.getName(), new CharacterEscapeHandler() {
            public void escape( char[] ac, int i, int j, boolean flag, Writer writer ) throws IOException {
                writer.write(ac, i, j);
            }
        });

        StringWriter stringWriter = new StringWriter();

        marshaller.marshal(object, stringWriter);
        String output = stringWriter.toString();

        return output;
    }

    public static <T> T deserialize( JAXBContext jaxbContext, String xmlStr, Class<T> objClass  ) throws Exception {
        Unmarshaller unmarshaller = null;
        unmarshaller = jaxbContext.createUnmarshaller();
        ByteArrayInputStream xmlStrInputStream = new ByteArrayInputStream(xmlStr.getBytes(Charset.forName("UTF-8")));

        Object jaxbObj = null;
        jaxbObj = unmarshaller.unmarshal(xmlStrInputStream);

        return (T) jaxbObj;
    }
}
