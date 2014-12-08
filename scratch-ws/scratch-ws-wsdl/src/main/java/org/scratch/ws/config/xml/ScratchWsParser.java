package org.scratch.ws.config.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ScratchWsParser extends DefaultHandler { 

    
    public Object read(final InputSource in) throws SAXException, IOException {
        DocumentBuilderFactory f;
        try {
            f =  DocumentBuilderFactory.newInstance();
        } catch ( Exception e ) {
            throw new RuntimeException( "Unable to create new DOM Document",
                                        e );
        }
        return null;
    }
}
