package org.ocram.test.rest.context;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

@Provider
public class JaxbContextResolver implements ContextResolver<JAXBContext> {

    @Context 
    private UriInfo uriInfo;

    @Inject
    private DeploymentClassGatherer classGatherer;
    
    @Override
    public JAXBContext getContext(Class<?> type) {
        System.out.println( "--- RESOLVER (" + getId(uriInfo) + ") STACK TRACE [" + type.getName() + "]");
        JAXBContext context = null;
        try {
            Set<Class<?>>  classList = classGatherer.getList();
            classList.add(type);
            Class<?> [] types = classList.toArray(new Class[classList.size()]);
            context = JAXBContext.newInstance(types);
        } catch (JAXBException e) {
            System.out.println( "Unable to create new " + JAXBContext.class.getSimpleName() + " instance.");
            e.printStackTrace();
        }
        return context;
    }

    private String getRelativePath(HttpServletRequest request) { 
        String path = request.getRequestURL().toString();
        path = request.getContextPath();
//        path = path.replaceAll( ".*" + request.getServletContext().getContextPath(), "");
        return path;
    }

    private String getId(UriInfo uriInfo) { 
        List<String> idParams = uriInfo.getQueryParameters().get("id");
        String id = null;
        if( idParams != null && ! idParams.isEmpty() ) { 
            id = idParams.get(0);
        }
        return id;
    }
}
