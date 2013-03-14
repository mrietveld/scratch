package org.jbpm.designer.assets.persistence;

import static org.jbpm.designer.assets.test.TestUtil.*;
import static junit.framework.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.engine.SessionFactoryImplementor;
import org.jbpm.designer.assets.domain.AssetParameter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class PreparedStatementPersistenceTest {

    private static final Logger _logger = Logger.getLogger(PreparedStatementPersistenceTest.class);

    private EntityManagerFactory emf;
    private EntityManager em;
    
    private static final String INSERT_SQL = "INSERT INTO Asset (PROCESSID, TYPE, CONTENT) values (?,?,?)";

    @Rule
    public TestName testName = new TestName();

    @Before
    public void before() {
        emf = Persistence.createEntityManagerFactory("org.jbpm.designer");
        em = emf.createEntityManager();
    }

    @After
    public void after() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    public void testPreparedStatementBinaryStreams() throws Exception {
        Long entityId = null;
        byte[] originalContent = null;
        {
            // Create input
            AssetParameter asset = new AssetParameter();
            asset.setProcessId("23");
            asset.setType(AssetType.FORM);
            URL url = this.getClass().getResource("/all_seeing_eye.png");
            assertNotNull("URL is null", url);
            File file = new File(url.getPath());
            assertTrue("File too large: " + file.length(), file.length() < Integer.MAX_VALUE);
            InputStream contentInputStream = new FileInputStream(file);

            originalContent = convertFileToByteArray(file);

            Session session = ((HibernateEntityManager) em).getSession();
            SessionFactoryImplementor sfi = (SessionFactoryImplementor) session.getSessionFactory();

            // insert
            entityId = insertViaPreparedStatement(sfi, asset, file, contentInputStream);
            assertTrue(entityId != null);

            // retrieve via JDBC
            String QUERY_SQL = "SELECT * FROM Asset WHERE id = " + entityId;
            Connection connection = null;
            Statement statement = null;
            ResultSet result = null;
            try {
                connection = sfi.getConnectionProvider().getConnection();
                statement = connection.createStatement();
                result = statement.executeQuery(QUERY_SQL);
                result.next(); // always necessary before retrieval
                byte [] jdbcContent = (byte []) result.getObject("content");
                assertTrue("JDBC retrieved content is not equal.", Arrays.equals(originalContent, jdbcContent));
            } finally {
                closeObjects(result, statement, connection);
            }
        }

        // retrieve via JPA
        Object assetObject = em.find(AssetEntity.class, entityId);
        assertNotNull("Could not retrieve asset entity.", assetObject);
        AssetEntity asset = (AssetEntity) assetObject;
        assertTrue("Asset content is not equal.", Arrays.equals(originalContent, asset.getContent()));
    }
    
    private Long insertViaPreparedStatement(SessionFactoryImplementor sfi, AssetParameter asset, File file, InputStream contentInputStream) throws Exception { 
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        Long entityId = null;
        
        try {
            // Retrieve DB connection
            connection = sfi.getConnectionProvider().getConnection();
            statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, asset.getProcessId());
            statement.setString(2, asset.getType());
            statement.setBinaryStream(3, contentInputStream, (int) file.length());

            statement.execute();

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                entityId = generatedKeys.getLong(1);
            }
        } finally {
            closeObjects(generatedKeys, statement, connection);
        }
        
        return entityId;
    }
    
    private void closeObjects(ResultSet resultSet, Statement statement, Connection connection) throws Exception { 
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqle) {
                _logger.warn("Unable to release generated keys.", sqle);
                throw sqle;
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqle) {
                _logger.warn("Unable to release statement.", sqle);
                throw sqle;
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqle) {
                _logger.warn("Unable to release connection.", sqle);
                throw sqle;
            }
        }
    }

}
