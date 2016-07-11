package org.ocram.experiment;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ocram.experiment.ForeignKeyParent;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
public class ForeignKeyHackTest {

    @Test
    public void testParentPersistenceWithoutConstraint() throws Exception {

        EntityManagerFactory emf = (EntityManagerFactory) context.get(ENTITY_MANAGER_FACTORY);
        EntityManager em = emf.createEntityManager();

        ForeignKeyParent [] fkps = new ForeignKeyParent[5];
        for( int i = 0; i < fkps.length; ++i ) {
            fkps[i] = addParent(em);

            if( i == 1 ) {
               System.out.println( "Add a debug point here, and remove the foreign constraint on \"childTypes\"!" );
            }
        }


        txm.begin();
        em.joinTransaction();
        ForeignKeyParent fkp = em.find(ForeignKeyParent.class, fkps[0].parentId);
        em.remove(fkp);
        txm.commit();

        // 0 was created with the constraint
        // are the corresponding elements removed from the ChildTypes table??

        txm.begin();
        em.joinTransaction();
        fkp = em.find(ForeignKeyParent.class, fkps[1].parentId);
        em.remove(fkp);
        txm.commit();

        // 0 was created without the constraint
        // are the corresponding elements removed from the ChildTypes table??
    }

    private static int childIndex = 0;

    private ForeignKeyParent addParent(EntityManager em) throws Exception {
        ForeignKeyParent par = new ForeignKeyParent();
        for( int i = 0; i < 3; ++i ) {
            par.childTypes.add(childIndex + ":" + UUID.randomUUID().toString());
        }
        ++childIndex;

        txm.begin();

        em.joinTransaction();
        em.persist(par);

        txm.commit();

        return par;
    }
    private HashMap<String, Object> context;
    private BitronixTransactionManager txm;


    @Before
    public void databaseSetup() {
        context = setupWithPoolingDataSource("org.ocram.experiment");
        txm = TransactionManagerServices.getTransactionManager();
    }

    @After
    public void tearDown() throws Exception {
        cleanUp(context);
    }

    private static TestH2Server h2Server                = new TestH2Server();
    public static final String DATASOURCE               = "org.ocram.datasource";
    public static final String ENTITY_MANAGER_FACTORY   = "org.ocram.EntityManagerFactory";
    protected static final String DATASOURCE_PROPERTIES = "/datasource.properties";


    public static HashMap<String, Object> setupWithPoolingDataSource(final String persistenceUnitName) {
        HashMap<String, Object> context = new HashMap<String, Object>();

        // set the right jdbc url
        Properties dsProps = getDatasourceProperties();
        String jdbcUrl = dsProps.getProperty("url");
        String driverClass = dsProps.getProperty("driverClassName");

        boolean startH2TcpServer = false;
        if( jdbcUrl.matches("jdbc:h2:tcp:.*") ) {
            startH2TcpServer = true;
        }

        // Setup the datasource
        PoolingDataSource pds = new PoolingDataSource();

        // The name must match what's in the persistence.xml!
        pds.setUniqueName("jdbc/testDS1");

        pds.setClassName(dsProps.getProperty("className"));

        pds.setMaxPoolSize(Integer.parseInt(dsProps.getProperty("maxPoolSize")));
        pds.setAllowLocalTransactions(Boolean.parseBoolean(dsProps.getProperty("allowLocalTransactions")));
        for (String propertyName : new String[] { "user", "password" }) {
            pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
        }

        if (driverClass.startsWith("org.h2") || driverClass.startsWith("org.hsqldb")) {
            if (startH2TcpServer) {
                h2Server.start();
            }
        }
        setDatabaseSpecificDataSourceProperties(pds, dsProps);

        if( driverClass.startsWith("org.h2") ) {
            pds.getDriverProperties().setProperty("url", jdbcUrl);
        }
        pds.init();
        context.put(DATASOURCE, pds);

        // Setup persistence
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        context.put(ENTITY_MANAGER_FACTORY, emf);

        return context;
    }

    /**
     * This reads in the (maven filtered) datasource properties from the test
     * resource directory.
     *
     * @return Properties containing the datasource properties.
     */
    public static Properties getDatasourceProperties() {
        String propertiesNotFoundMessage = "Unable to load datasource properties [" + DATASOURCE_PROPERTIES + "]";

        // Central place to set additional H2 properties
        System.setProperty("h2.lobInDatabase", "true");

        InputStream propsInputStream = ForeignKeyHackTest.class.getResourceAsStream(DATASOURCE_PROPERTIES);
        assertNotNull(propertiesNotFoundMessage, propsInputStream);
        Properties props = new Properties();
        try {
            props.load(propsInputStream);
        } catch (IOException ioe) {
            throw new RuntimeException("Loading properties failed!", ioe);
        }

        return props;
    }

    public static void setDatabaseSpecificDataSourceProperties(PoolingDataSource pds, Properties dsProps) {
        String driverClass = dsProps.getProperty("driverClassName");
        if (driverClass.startsWith("org.h2") || driverClass.startsWith("org.hsqldb")) {
            for (String propertyName : new String[] { "url", "driverClassName"}) {
                pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
            }
        } else {
            pds.setClassName(dsProps.getProperty("className"));
            pds.getDriverProperties().setProperty("driverClassName", driverClass);
            if (driverClass.startsWith("org.mariadb")) {
                for (String propertyName : new String[]{"url"}) {
                    pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
                }
            } else {
                throw new RuntimeException("Unknown driver class: " + driverClass);
            }
        }
    }

    public static void cleanUp(Map<String, Object> context) {
        if (context != null) {

            BitronixTransactionManager txm = TransactionManagerServices.getTransactionManager();
            if( txm != null ) {
                txm.shutdown();
            }

            Object emfObject = context.remove(ENTITY_MANAGER_FACTORY);
            if (emfObject != null) {
                try {
                    EntityManagerFactory emf = (EntityManagerFactory) emfObject;
                    emf.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

            Object ds1Object = context.remove(DATASOURCE);
            if (ds1Object != null) {
                try {
                    PoolingDataSource ds1 = (PoolingDataSource) ds1Object;
                    ds1.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }

        }

    }


    private static class TestH2Server {
        private Server realH2Server;

        public void start() {
            if (realH2Server == null || !realH2Server.isRunning(false)) {
                try {
                    DeleteDbFiles.execute("", "JPADroolsFlow", true);
                    realH2Server = Server.createTcpServer(new String[0]);
                    realH2Server.start();
                } catch (SQLException e) {
                    throw new RuntimeException("can't start h2 server db", e);
                }
            }
        }

        @Override
        protected void finalize() throws Throwable {
            if (realH2Server != null) {
                realH2Server.stop();
            }
            DeleteDbFiles.execute("", "target/jbpm-test", true);
            super.finalize();
        }

    }
}
