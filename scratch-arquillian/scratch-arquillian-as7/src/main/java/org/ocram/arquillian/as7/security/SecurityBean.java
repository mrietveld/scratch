package org.ocram.arquillian.as7.security;

import java.security.Principal;
import java.security.Provider;
import java.security.acl.Group;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.login.Configuration;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import javax.security.jacc.PolicyContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class SecurityBean {

    @Resource
    private SessionContext ctx;

    private Logger logger = LoggerFactory.getLogger(SecurityBean.class);

    public void explore() throws Exception {
        logger.info("Caller principal: " + ctx.getCallerPrincipal().getName());

        Subject realSubject = (Subject) PolicyContext.getContext("javax.security.auth.Subject.container");
        // exploreSubject(realSubject);

        Subject subject = tryLogin();
        exploreSubject(subject);

    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Configuration getConfiguration() {
        Configuration config = (Configuration) java.security.AccessController.doPrivileged(new java.security.PrivilegedAction() {
            public Object run() {
                return Configuration.getConfiguration();
            }
        });
        return config;
    }

    private Subject tryLogin() throws LoginException {
        String[] creds = { "mary", "mary123@" };
        CallbackHandler handler = new UserPassCallbackHandler(creds);
        LoginContext lc = new LoginContext("ApplicationRealm", handler);
        lc.login();
        return lc.getSubject();
    }

    private void exploreSubject(Subject subject) {
        if (subject == null) {
            throw new RuntimeException("Subject is null!");
        }

        logger.info("Read-only? : " + subject.isReadOnly());
        logger.info("Principals: ");
        for (Principal prin : subject.getPrincipals()) {
            logger.info("> " + prin.getName());
            if ("Roles".equals(prin.getName())) {
                getGroups(prin);
            }
        }
    }

    private void getGroups(Principal principal) {
        if (principal instanceof Group && "Roles".equalsIgnoreCase(principal.getName())) {
            Enumeration<? extends Principal> groups = ((Group) principal).members();

            while (groups.hasMoreElements()) {
                Principal groupPrincipal = (Principal) groups.nextElement();
                logger.info(">> " + groupPrincipal.getName());
            }
        }
    }

    private void exploreConfigAndProvider() {
        Configuration config = getConfiguration();
        Provider provider = config.getProvider();
        if (provider != null) {
            logger.info("Name   : " + provider.getName());
            logger.info("Version: " + provider.getVersion());
            logger.info("Info   : " + provider.getInfo());
            logger.info("Class  : " + provider.getClass().getName());
        } else {
            logger.warn("No provider could be found!");
        }
        logger.info("Config type: " + config.getType());
        if (config.getParameters() != null) {
            logger.info(config.getParameters().getClass().getName());
        }
    }
}
