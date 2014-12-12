package org.scratch.ws.config.xml;

import java.net.URL;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.CXFBusFactory;
import org.apache.cxf.buslifecycle.BusLifeCycleListener;
import org.apache.cxf.buslifecycle.BusLifeCycleManager;
import org.apache.cxf.common.util.SystemPropertyAction;
import org.apache.cxf.configuration.Configurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScratchWsCXFBusFactory extends CXFBusFactory {

    protected static final Logger logger = LoggerFactory.getLogger(ScratchWsCXFBusFactory.class);
  
    private final ScratchWsConfigContext context; 
   
    public ScratchWsCXFBusFactory() { 
        this.context = null;
    }

    public ScratchWsCXFBusFactory(ScratchWsConfigContext context) {
        this.context = context;
    }
    
    public Bus createBus(String cfgFile) {
        return createBus(cfgFile, defaultBusNotExists()); 
    }
    
    public Bus createBus(String cfgFile, boolean includeDefaults) {
        if (cfgFile == null) {
            return createBus((String[])null, includeDefaults);
        }
        return createBus(new String[] {cfgFile}, includeDefaults);
    }   

    public Bus createBus(String cfgFiles[], boolean includeDefaults) {
        try {
            String userCfgFile = SystemPropertyAction.getPropertyOrNull(Configurer.USER_CFG_FILE_PROPERTY_NAME);
            String sysCfgFileUrl = SystemPropertyAction.getPropertyOrNull(Configurer.USER_CFG_FILE_PROPERTY_URL);

            URL defaultUserCfgFileUrl = ScratchWsConfigContext.resourceExists(Configurer.DEFAULT_USER_CFG_FILE);
            if (userCfgFile == null && cfgFiles == null && sysCfgFileUrl == null 
                    && defaultUserCfgFileUrl == null && includeDefaults) {
                return new org.apache.cxf.bus.CXFBusFactory().createBus();
            }
            ScratchWsConfigContext context = createScratchWsConfigContext(cfgFiles, includeDefaults);
            return finishCreatingBus(context);
        } catch (Exception ex) {
            logger.warn("Bus creation failed:" + ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }
   
    protected ScratchWsConfigContext createScratchWsConfigContext(String cfgFiles[], boolean includeDefaults) {
        try {      
            return new ScratchWsConfigContext(cfgFiles, includeDefaults);
        } catch (RuntimeException re) {
            logger.warn( "INITIAL_APP_CONTEXT_CREATION_FAILED_MSG", re, (Object[])null);
            ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
            if (contextLoader != ScratchWsConfigContext.class.getClassLoader()) {
                Thread.currentThread().setContextClassLoader(ScratchWsConfigContext.class.getClassLoader());
                try {
                    return new ScratchWsConfigContext(cfgFiles, includeDefaults);        
                } finally {
                    Thread.currentThread().setContextClassLoader(contextLoader);
                }
            } else {
                throw re;
            }
        }
    }
    
    protected Bus finishCreatingBus(ScratchWsConfigContext ctx) {
        final Bus bus = ctx.getBus();

        bus.setExtension(ctx, ScratchWsConfigContext.class);
        possiblySetDefaultBus(bus);
        
        initializeBus(bus);        
        
        registerApplicationContextLifeCycleListener(bus, ctx);
        
        return bus;
    }
    
    private boolean defaultBusNotExists() {
        if (null != context) {
            return !context.containsBus();
        }
        return true;
    }

    void registerApplicationContextLifeCycleListener(Bus bus, ScratchWsConfigContext ctx) {
        BusLifeCycleManager lm = bus.getExtension(BusLifeCycleManager.class);
        if (null != lm) {
            lm.registerLifeCycleListener(new BusApplicationContextLifeCycleListener(ctx));
        }
    } 

    static class BusApplicationContextLifeCycleListener implements BusLifeCycleListener {
        private ScratchWsConfigContext swcc;

        BusApplicationContextLifeCycleListener(ScratchWsConfigContext b) {
            swcc = b;
        }

        public void initComplete() {
        }

        public void preShutdown() {
        }

        public void postShutdown() {
            swcc.close();
        }
        
    }
}
