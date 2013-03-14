package org.jbpm.designer.assets.servlet;

import static org.jbpm.designer.assets.persistence.upload.JpaEnversPersistence.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jbpm.designer.assets.exception.AssetProcessingException;
import org.jbpm.designer.assets.persistence.AssetEntity;
import org.jbpm.designer.assets.persistence.AssetTagEntity;
import org.jbpm.designer.assets.persistence.AssetType;
import org.jbpm.designer.assets.persistence.upload.PersistenceStrategy;
import org.jbpm.designer.assets.persistence.upload.PersistenceStrategyFactory;
import org.jbpm.designer.assets.persistence.upload.PersistenceStrategyFactory.PersistenceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AssetResourceServlet extends HttpServlet {

    /** Generated serial version uid */
    private static final long serialVersionUID = -8869823563056984753L;

    private static final Logger _logger = LoggerFactory.getLogger(AssetResourceServlet.class);

    public static final int BUFFER_SIZE = 32 * 1024; // 32k
    
    public static final String DESIGNER_PERSISTENCE_PROPERTIES = "designer-persistence.properties";
    public static final String ERROR_MESSAGE = "Unable to process request: ";

    @PersistenceUnit(name = "jdbc/designer", unitName="org.jbpm.designer")
    private EntityManagerFactory emf;

    private PersistenceStrategyFactory strategyFactory;

    private static final String PERSISTENCE_STRATEGY_PROPERTY = "persistenceStrategy";
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        Properties designerPersistenceProps = new Properties();
        boolean useDefaultProperties = false;
        try {
            InputStream stream = getServletContext().getResourceAsStream(DESIGNER_PERSISTENCE_PROPERTIES);
            if( stream != null ) { 
                designerPersistenceProps.load(stream);
            } else { 
                useDefaultProperties = true;
            }
        } catch (IOException ioe) {
            _logger.debug("Using default properties: Unable to load properties for persistence from " + DESIGNER_PERSISTENCE_PROPERTIES);
            useDefaultProperties = true;
        }
        
        if( useDefaultProperties ) { 
            designerPersistenceProps.put(PERSISTENCE_STRATEGY_PROPERTY, "JPA");
        }
        
        PersistenceStrategyFactory.PersistenceType type = null;
        String typeString = designerPersistenceProps.get(PERSISTENCE_STRATEGY_PROPERTY).toString();
        try { 
            type = PersistenceStrategyFactory.PersistenceType.valueOf(typeString.toUpperCase());
        } catch( IllegalArgumentException iae ) { 
           _logger.warn("Unknown persistence type '" + typeString + "', defaulting to JPA.");
           type = PersistenceStrategyFactory.PersistenceType.JPA;
        }
        
        strategyFactory = new PersistenceStrategyFactory(type);
    }

    public AssetResourceServlet() {
        // default constructor
    }

    /**
     * Retrieve assets.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String assetId = request.getParameter("assetId");

        PersistenceStrategy persistence = strategyFactory.createStrategyInstance(emf.createEntityManager());
        AssetEntity asset = null;
        try {
            asset = persistence.find(Long.parseLong(assetId));
        } catch (AssetProcessingException ape) {
            handleAssetProcessingException(ape, response);
        }

        response.addHeader("assetId", Long.toString(asset.getId()));
        response.addHeader("processId", asset.getProcessId());
        response.addHeader("type", asset.getType());

        StringBuilder jsonTags = new StringBuilder("{");
        Set<AssetTagEntity> tags = asset.getTags();
        if (tags.size() > 0) {
            Iterator<AssetTagEntity> iter = tags.iterator();
            jsonTags.append(iter.next().getTag());
            while (iter.hasNext()) {
                jsonTags.append("," + iter.next().getTag());
            }
        }
        jsonTags.append("}");
        response.addHeader("tags", jsonTags.toString());

        response.setContentLength(asset.getContentLength());
        OutputStream responseStream = response.getOutputStream();
        InputStream contentStream = asset.getContentInputStream();
        if (contentStream != null) {
            // via stream
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            while ((bytesRead = contentStream.read(buffer)) != -1) {
                responseStream.write(buffer, 0, bytesRead);
            }
        } else {
            // via byte []
            responseStream.write(asset.getContent());
        }

        response.flushBuffer();
    }

    /**
     * When updating or creating an asset, the "content" field must ALWAYS be the last argument. 
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Check that we have a file upload request
        if (ServletFileUpload.isMultipartContent(request)) {

            // create progressListener and register?

            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload();
            HashMap<String, String> requestFields = new HashMap<String, String>();

            // Parse the request
            AssetEntity asset = new AssetEntity();
            try {
                boolean streamSet = false;
                FileItemIterator iter = upload.getItemIterator(request);
                while (!streamSet && iter.hasNext()) {
                    FileItemStream item = iter.next();
                    String name = item.getFieldName();

                    // All form fields should be strings
                    if (item.isFormField()) {
                        requestFields.put(name, convertInputStreamToString(item.openStream()));
                        item.getClass();
                    } else {
                        if (!streamSet && "content".equals(name)) {
                            asset.setContentInputStream(item.openStream());
                            streamSet = true;
                           
                            // Make sure that content is indeed the last part
                            if( ! requestFields.containsKey("processId") 
                                || ! requestFields.containsKey("type") 
                                || ! requestFields.containsKey("tags") ) { 
                                streamSet = false;
                               
                                // immediately convert stream to input
                                byte [] content = null;
                                try { 
                                    if( strategyFactory.getType() == PersistenceType.JDBC) { 
                                        throw new AssetProcessingException("When using JDBC strategy, content must be the last part of the POST!");
                                    }
                                    content = convertInputStreamToByteArr(asset.getContentInputStream());
                                } catch(AssetProcessingException ape) { 
                                    handleAssetProcessingException(ape, response);
                                }
                                asset.setContent(content);
                            }
                        } else {
                            _logger.warn("Unknown file stream in field '" + name + "'");
                        }
                    }
                }
            } catch (FileUploadException e) {
                _logger.warn(ERROR_MESSAGE + e.getClass().getSimpleName(), e);
                response.getWriter().append(ERROR_MESSAGE + e.getClass().getSimpleName());
                return;
            }

            long assetId = 0;
            try {
                asset = processRequestFields(requestFields, asset, response);
                assetId = strategyFactory.createStrategyInstance(emf.createEntityManager()).persist(asset);
            } catch (AssetProcessingException ape) {
                handleAssetProcessingException(ape, response);
            }

            // return assetId
            response.getWriter().append(Long.toString(assetId));
        } else {
            response.getWriter().append(ERROR_MESSAGE + "POST was not a multipart request.");
        }
        
        response.flushBuffer();
    }

    /**
     * Delete assets
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void handleAssetProcessingException(AssetProcessingException ape, HttpServletResponse response) {
        String msg = ape.getMessage();
        try {
            _logger.warn(msg);
            response.getWriter().append(ERROR_MESSAGE + msg);
        } catch (IOException ioe) {
            _logger.error("Unable to write to response: " + ioe.getClass().getSimpleName(), ioe);
        }
    }

    /**
     * Package scope in order to test it.
     * 
     * @param stream InputStream to read from 
     * @return a String to create from InputStream
     */
    String convertInputStreamToString(InputStream stream) {
        InputStreamReader reader = new InputStreamReader(stream);

        final char[] buffer = new char[1024];
        StringBuilder builder = new StringBuilder(1024);

        try {
            // offset is within buffer, not in reader/builder
            int read = reader.read(buffer, 0, buffer.length);
            while (read != -1) {
                builder.append(buffer, 0, read);
                read = reader.read(buffer, 0, buffer.length);
            }
        } catch (IOException ioe) {
            _logger.warn("Unable to read input stream.", ioe);
        }

        return builder.toString();
    }

    private AssetEntity processRequestFields(Map<String, String> requestFields, AssetEntity asset, HttpServletResponse response)
            throws AssetProcessingException {

        String fieldName = "processId";
        String fieldValue = requestFields.get(fieldName);
        if (fieldValue != null) {
            asset.setProcessId(fieldValue);
        } else {
            String msg = fieldName + " field missing from request.";
            throw new AssetProcessingException(msg);
        }

        fieldName = "type";
        fieldValue = requestFields.get(fieldName);
        if (fieldValue != null) {
            try {
                AssetType type = AssetType.valueOf(fieldValue.toUpperCase());
                asset.setType(type);
            } catch (IllegalArgumentException iae) {
                String msg = fieldName + " field value of '" + fieldValue + "' is unknown.";
                throw new AssetProcessingException(msg);
            }
        } else {
            String msg = fieldName + " field missing from request.";
            throw new AssetProcessingException(msg);
        }

        fieldName = "tags";
        fieldValue = requestFields.get(fieldName);
        if (fieldValue != null) {
            Scanner scan = new Scanner(fieldValue).useDelimiter("\\s*[{},]\\s*");
            while (scan.hasNext("\\S+")) { // avoids empty sets
                asset.getTagStrings().add(scan.next());
            }
        } else {
            String msg = fieldName + " field missing from request.";
            throw new AssetProcessingException(msg);
        }

        return asset;
    }

    private ProgressListener createProgressListener() {
        // Create a progress listener
        ProgressListener progressListener = new ProgressListener() {
            private long megaBytes = -1;

            public void update(long pBytesRead, long pContentLength, int pItems) {
                long mBytes = pBytesRead / 1000000;
                if (megaBytes == mBytes) {
                    return;
                }
                megaBytes = mBytes;
                System.out.println("We are currently reading item " + pItems);
                if (pContentLength == -1) {
                    System.out.println("So far, " + pBytesRead + " bytes have been read.");
                } else {
                    System.out.println("So far, " + pBytesRead + " of " + pContentLength + " bytes have been read.");
                }
            }
        };

        return progressListener;
    }

}
