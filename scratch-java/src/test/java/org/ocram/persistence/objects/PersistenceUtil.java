/*
 * Copyright 2011 Red Hat Inc.
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
package org.ocram.persistence.objects;


import java.util.HashMap;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.UserTransaction;

import org.drools.core.base.MapGlobalResolver;
import org.drools.core.impl.EnvironmentFactory;
import org.drools.persistence.TransactionManager;
import org.junit.Assert;
import org.kie.api.runtime.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bitronix.tm.BitronixTransactionManager;
import bitronix.tm.TransactionManagerServices;
import bitronix.tm.resource.jdbc.PoolingDataSource;

public class PersistenceUtil {

    private static Logger logger = LoggerFactory.getLogger( PersistenceUtil.class );

    protected static final String DATASOURCE_PROPERTIES = "/datasource.properties";
    private static Properties defaultProperties = null;
   
    // Setup and marshalling setup constants
    public static String DATASOURCE = "org.ocram.persistence.datasource";
    public static String ENTITY_MANAGER_FACTORY = "org.ocram.persistence.emf";

    /**
     * This method does all of the setup for the test and returns a HashMap
     * containing the persistence objects that the test might need.
     * 
     * @param persistenceUnitName
     *            The name of the persistence unit used by the test.
     * @return HashMap<String Object> with persistence objects, such as the
     *         EntityManagerFactory and DataSource
     */
    public static HashMap<String, Object> setupWithPoolingDataSource(final String persistenceUnitName, String dataSourceName, String jdbcUrl) {
        HashMap<String, Object> context = new HashMap<String, Object>();

        // set jdbc url
        Properties dsProps = getDefaultProperties();
        dsProps.setProperty("url", jdbcUrl);

        // Setup the datasource
        PoolingDataSource dataSource = setupPoolingDataSource(dsProps, dataSourceName);
        dataSource.init();
        context.put(DATASOURCE, dataSource);

        // Setup persistence
        EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        context.put(ENTITY_MANAGER_FACTORY, emf);
        return context;
    }

    /**
     * This sets up a Bitronix PoolingDataSource.
     * 
     * @return PoolingDataSource that has been set up but _not_ initialized.
     */
    private static PoolingDataSource setupPoolingDataSource(Properties dsProps, String datasourceName) {
        PoolingDataSource pds = new PoolingDataSource();

        // The name must match what's in the persistence.xml!
        pds.setUniqueName(datasourceName);

        pds.setClassName(dsProps.getProperty("className"));

        pds.setMaxPoolSize(Integer.parseInt(dsProps.getProperty("maxPoolSize")));
        pds.setAllowLocalTransactions(Boolean.parseBoolean(dsProps.getProperty("allowLocalTransactions")));
        for (String propertyName : new String[] { "user", "password" }) {
            pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
        }

        String driverClass = dsProps.getProperty("driverClassName");
        if (driverClass.startsWith("org.h2")) {
            for (String propertyName : new String[] { "url", "driverClassName" }) {
                pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
            }
        } else {
            pds.setClassName(dsProps.getProperty("className"));

            if (driverClass.startsWith("oracle")) {
                pds.getDriverProperties().put("driverType", "thin");
                pds.getDriverProperties().put("URL", dsProps.getProperty("url"));
            } else if (driverClass.startsWith("com.ibm.db2")) {
                // http://docs.codehaus.org/display/BTM/JdbcXaSupportEvaluation#JdbcXaSupportEvaluation-IBMDB2
                pds.getDriverProperties().put("databaseName", dsProps.getProperty("databaseName"));
                pds.getDriverProperties().put("driverType", "4");
                pds.getDriverProperties().put("serverName", dsProps.getProperty("serverName"));
                pds.getDriverProperties().put("portNumber", dsProps.getProperty("portNumber"));
            } else if (driverClass.startsWith("com.microsoft")) {
                for (String propertyName : new String[] { "serverName", "portNumber", "databaseName" }) {
                    pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
                }
                pds.getDriverProperties().put("URL", dsProps.getProperty("url"));
                pds.getDriverProperties().put("selectMethod", "cursor");
                pds.getDriverProperties().put("InstanceName", "MSSQL01");
            } else if (driverClass.startsWith("com.mysql")) {
                for (String propertyName : new String[] { "databaseName", "serverName", "portNumber", "url" }) {
                    pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
                }
            } else if (driverClass.startsWith("com.sybase")) {
                for (String propertyName : new String[] { "databaseName", "portNumber", "serverName" }) {
                    pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
                }
                pds.getDriverProperties().put("REQUEST_HA_SESSION", "false");
                pds.getDriverProperties().put("networkProtocol", "Tds");
            } else if (driverClass.startsWith("org.postgresql")) {
                for (String propertyName : new String[] { "databaseName", "portNumber", "serverName" }) {
                    pds.getDriverProperties().put(propertyName, dsProps.getProperty(propertyName));
                }
            } else {
                throw new RuntimeException("Unknown driver class: " + driverClass);
            }
        }

        return pds;
    }

    /**
     * Return the default database/datasource properties - These properties use
     * an in-memory H2 database
     * 
     * This is used when the developer is somehow running the tests but
     * bypassing the maven filtering that's been turned on in the pom.
     * 
     * @return Properties containing the default properties
     */
    private static Properties getDefaultProperties() {
        if (defaultProperties == null) {
            String[] keyArr = { 
                    "serverName", 
                    "portNumber", 
                    "databaseName", 
                    "user", 
                    "password", 
                    "driverClassName",
                    "className", 
                    "maxPoolSize", 
                    "allowLocalTransactions" };
            String[] defaultPropArr = { 
                    "", 
                    "", 
                    "", 
                    "sa", 
                    "", 
                    "org.h2.Driver",
                    "bitronix.tm.resource.jdbc.lrc.LrcXADataSource", 
                    "16", 
                    "true" };
            Assert.assertTrue("Unequal number of keys for default properties", keyArr.length == defaultPropArr.length);
            defaultProperties = new Properties();
            for (int i = 0; i < keyArr.length; ++i) {
                defaultProperties.put(keyArr[i], defaultPropArr[i]);
            }
        }

        return defaultProperties;
    }

    public static final String           DEFAULT_USER_TRANSACTION_NAME                     = "java:comp/UserTransaction";

    public static final String[]         FALLBACK_TRANSACTION_MANAGER_NAMES                = new String[]{"java:comp/TransactionManager", "java:appserver/TransactionManager", "java:pm/TransactionManager", "java:/TransactionManager", System.getProperty("jbpm.tm.jndi.lookup")};

    
    public static UserTransaction findUserTransaction() {
        try {
            InitialContext context = new InitialContext();
            return (UserTransaction) context.lookup( DEFAULT_USER_TRANSACTION_NAME );
        } catch ( NamingException ex ) {
            logger.debug( "No UserTransaction found at JNDI location [{}]", DEFAULT_USER_TRANSACTION_NAME, ex );
            // no user transaction found in default location try alternative ones
            // on some app servers access to default UT is not allowed from non manged thread, e.g. timers
            try {
                return InitialContext.doLookup(System.getProperty("jbpm.ut.jndi.lookup", "java:jboss/UserTransaction"));
            } catch (Exception e1) {
                throw new IllegalStateException("Unable to find transaction: " + ex.getMessage(), ex);
            }

        }
    }
    
    protected javax.transaction.TransactionManager findTransactionManager(UserTransaction ut) {
        if ( ut instanceof TransactionManager ) {
            logger.debug( "JTA UserTransaction object [{}] implements TransactionManager",
                          ut );
            return (javax.transaction.TransactionManager) ut;
        }

        InitialContext context = null;

        try {
            context = new InitialContext();
        } catch ( NamingException ex ) {
            logger.debug( "Could not initialise JNDI InitialContext",
                          ex );
            return null;
        }

        // Check fallback JNDI locations.
        for ( String jndiName : FALLBACK_TRANSACTION_MANAGER_NAMES ) {
            if (jndiName == null) {
                continue;
            }
            try {
                javax.transaction.TransactionManager tm = (javax.transaction.TransactionManager) context.lookup( jndiName );
                logger.debug( "JTA TransactionManager found at fallback JNDI location [{}]",
                              jndiName );
                return tm;
            } catch ( NamingException ex ) {
                logger.debug( "No JTA TransactionManager found at fallback JNDI location [{}]",
                              jndiName,
                              ex );
            }
        }

        // OK, so no JTA TransactionManager is available...
        return null;
    }
    /**
     * This method should be called in the @After method of a test to clean up
     * the persistence unit and datasource.
     * 
     * @param context
     *            A HashMap generated by
     *            {@link org.kie.api.persistence.util.PersistenceUtil setupWithPoolingDataSource(String)}
     * 
     */
    public static void cleanUp(HashMap<String, Object> context) {
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

    /**
     * This method returns whether or not transactions should be used when
     * dealing with the SessionInfo object (or any other persisted entity that
     * contains @Lob's )
     * 
     * @return boolean Whether or not to use transactions
    public static boolean useTransactions() {
        boolean useTransactions = false;
        String databaseDriverClassName = getDatasourceProperties().getProperty("driverClassName");

        // Postgresql has a "Large Object" api which REQUIRES the use of transactions
        //  since @Lob/byte array is actually stored in multiple tables.
        if (databaseDriverClassName.startsWith("org.postgresql")) {
            useTransactions = true;
        }
        return useTransactions;
    }
     */

}
