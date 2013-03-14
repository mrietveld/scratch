package org.ocram.abdera;

import java.io.ByteArrayInputStream;

import javax.xml.namespace.QName;

import org.apache.abdera.Abdera;
import org.apache.abdera.model.*;
import org.junit.Test;
import org.ocram.ScratchBaseTest;

public class ParserTest extends ScratchBaseTest {

    private Abdera abdera = new Abdera();

    @Test
    public void testParser() {

        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><atom:entry xmlns:atom=\"http://www.w3.org/2005/Atom\" xml:base=\"http://127.0.0.1:8080/drools-guvnor/rest/packages/one-105730/assets/test-105730\"><atom:title>test-105730</atom:title><atom:id>http://127.0.0.1:8080/drools-guvnor/rest/packages/one-105730/assets/test-105730</atom:id><atom:published>2012-10-09T10:57:41.435+02:00</atom:published><atom:author><atom:name>admin</atom:name></atom:author><atom:content type=\"application/octet-stream\" src=\"http://127.0.0.1:8080/drools-guvnor/rest/packages/one-105730/assets/test-105730/binary\"/><atom:summary></atom:summary><metadata><uuid><value>fa67af21-2211-437c-bfac-f3672eba00f7</value></uuid><categories/><format><value>txt</value></format><state><value>Draft</value></state><versionNumber><value>1</value></versionNumber><checkinComment><value>update binary</value></checkinComment><archived><value>false</value></archived></metadata></atom:entry>";

        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        assertNotNull(bais);

        Document<Entry> doc = abdera.getParser().parse(bais);
        Element elem = findElem("uuid", doc.getRoot().getExtension(new QName("", "metadata")));

        String uuid = elem.getElements().get(0).getText();
        assertTrue("Empty uuid returned.", uuid != null && ! uuid.isEmpty());
        System.out.println("UUID: " + uuid);
    }

    private Element findElem(String name, Element elem) {
        if (! name.equals(elem.getQName().getLocalPart())) {
            for (Element child : elem.getElements()) {
                Element result = findElem(name, child);
                if (result != null) {
                    return result;
                }
            }
        } else {
            return elem;
        }
        return null;
    }

}
