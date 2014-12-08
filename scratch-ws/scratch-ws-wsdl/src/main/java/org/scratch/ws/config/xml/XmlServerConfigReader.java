package org.scratch.ws.config.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

import javax.xml.parsers.SAXParser;

import org.kie.api.definition.process.Process;
import org.drools.core.xml.ExtensibleXmlParser;
import org.drools.core.xml.SemanticModules;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlServerConfigReader {
    private ExtensibleXmlParser parser;

    private ScratchWsConfigData configData;

    public XmlServerConfigReader(final SemanticModules modules, ClassLoader classLoader) {
        this( modules, classLoader, null );
    }

    public XmlServerConfigReader(final SemanticModules modules, ClassLoader classLoader, final SAXParser parser) {
        if ( parser == null ) {
            this.parser = new ExtensibleXmlParser();
        } else {
            this.parser = new ExtensibleXmlParser( parser );
        }      
        this.parser.setSemanticModules( modules );
        this.parser.setData( new ScratchWsConfigData() );
        this.parser.setClassLoader( classLoader );
    }

    /**
     * Read a <code>Process</code> from a <code>Reader</code>.
     *
     * @param reader
     *            The reader containing the rule-set.
     *
     * @return The rule-set.
     */
    public ScratchWsConfigData read(final Reader reader) throws SAXException,
                                                 IOException {
        this.configData = (ScratchWsConfigData) this.parser.read( reader );
        return this.configData;
    }

    /**
     * Read a <code>Process</code> from an <code>InputStream</code>.
     *
     * @param inputStream
     *            The input-stream containing the rule-set.
     *
     * @return The rule-set.
     */
    public ScratchWsConfigData read(final InputStream inputStream) throws SAXException,
                                                           IOException {
        this.configData = ((ScratchWsConfigData) this.parser.read( inputStream ));
        return this.configData;
    }

    /**
     * Read a <code>Process</code> from an <code>InputSource</code>.
     *
     * @param in
     *            The rule-set input-source.
     *
     * @return The rule-set.
     */
    public ScratchWsConfigData read(final InputSource in) throws SAXException,
                                                  IOException {
        this.configData = (ScratchWsConfigData)this.parser.read( in );
        return this.configData;
    }

    void setConfigData(final ScratchWsConfigData configData) {
        this.configData = configData;
    }

    public ScratchWsConfigData getConfigData() {
        return this.configData;
    }
}
