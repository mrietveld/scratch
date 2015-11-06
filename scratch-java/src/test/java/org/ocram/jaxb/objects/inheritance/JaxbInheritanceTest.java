package org.ocram.jaxb.objects.inheritance;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

public class JaxbInheritanceTest {


    @Test
    public void inheritanceTest() throws Exception {
        Class[] clientClasses = { CoreObject.class };
        JAXBContext clientCtx = JAXBContext.newInstance(clientClasses);

        CoreObject clientSide = new CoreObject();
        clientSide.setOne("please");

        String xmlStr = serialize(clientCtx, true, clientSide);
        System.out.println( xmlStr );

        // The key is to list the (child) class you want back *LAST*
        Class[] serverClasses = { CoreObject.class, ImplObject.class };
        JAXBContext serverCtx = JAXBContext.newInstance(serverClasses);

        ImplObject serverSide = deserialize(serverCtx, xmlStr, ImplObject.class);

        assertEquals( clientSide.getOne(), serverSide.getOne() );
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
