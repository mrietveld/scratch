package org.ocram.jaxb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPathFactory;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.ocram.jaxb.objects.Command;
import org.ocram.jaxb.objects.OneCommand;
import org.ocram.jaxb.objects.TestRequest;
import org.ocram.jaxb.objects.TwoCommand;
import org.ocram.jaxb.objects.bad.BadOneCommand;
import org.ocram.jaxb.objects.bad.BadStringHolder;
import org.ocram.jaxb.objects.bad.BadTestRequest;
import org.ocram.jaxb.objects.bad.BadTwoCommand;

import com.sun.xml.bind.v2.runtime.IllegalAnnotationException;
import com.sun.xml.bind.v2.runtime.IllegalAnnotationsException;
import com.sun.xml.bind.v2.runtime.Location;

public class SmartJaxbTest extends ScratchBaseTest {

    @Test
    public void jaxbAnyElementMixedTest() throws Exception {
        Class[] classes = { TestRequest.class, OneCommand.class, TwoCommand.class, Command.class, BadOneCommand.class,
                BadStringHolder.class, BadTestRequest.class, BadTwoCommand.class };

        List<Class> classList = new ArrayList<Class>(Arrays.asList(classes));

        System.out.println( JAXBContext.newInstance(new Class[0]).getClass().getName());
        System.out.println( DocumentBuilderFactory.newInstance().getClass().getName() );
        System.out.println( XPathFactory.newInstance().getClass().getName() );
        System.out.println( TransformerFactory.newInstance().getClass().getName() );
        System.out.println( SAXParserFactory.newInstance().getClass().getName() );
        
        JAXBContext ctx = null;
        boolean retryJaxbContextCreation = true;
        while( retryJaxbContextCreation )  {
            try {
                ctx = JAXBContext.newInstance(classList.toArray(new Class[classList.size()]));
                retryJaxbContextCreation = false;
            } catch( IllegalAnnotationsException iae ) {
                if( ! processIllegalAnnotationsException(classList, iae) ) { 
                    throw iae;
                }
            } 
        }
        assertNotNull( ctx );
    }

    private boolean processIllegalAnnotationsException( List<Class> classList, IllegalAnnotationsException iae ) {
        Set<Class> removedClasses = new HashSet<Class>();
        for( IllegalAnnotationException error : iae.getErrors() ) {
            List<Location> classLocs = error.getSourcePos().get(0);
            String errMsg = error.getMessage();
            
            if( classLocs != null ) { 
               String className = classLocs.listIterator(classLocs.size()).previous().toString();
               Class removeClass = null;
               try {
                   removeClass = Class.forName(className);
                   if( ! removedClasses.add(removeClass) ) { 
                       continue;
                   }
               } catch( ClassNotFoundException cnfe ) {
                   throw new RuntimeException("Class [" + className + "] does not exist: "  + cnfe.getMessage(), cnfe);
               }
               if( classList.remove(removeClass) ) { 
                   continue;
               }
            }
            return false;
        }
        return true;
    }
    
}