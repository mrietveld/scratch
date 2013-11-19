package org.ocram.test.rest;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

    @Override
    public JAXBContext getContext(Class<?> type) {
        JAXBContext context = null;
        try {
            context = JAXBContext.newInstance(type);
        } catch (JAXBException e) {
            System.out.println( "Unable to create new " + JAXBContext.class.getSimpleName() + " instance.");
            e.printStackTrace();
        }
        return context;
    }

}
