package org.ocram.arquillian.as7;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocram.arquillian.as7.security.SecurityBean;
import org.ocram.arquillian.as7.security.UserPassCallbackHandler;

@RunWith(Arquillian.class)
public class JavaxSecurityTest {

    @EJB
    private SecurityBean securityBean;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "securityBean.jar")
                .addClasses(SecurityBean.class, UserPassCallbackHandler.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testSecurity() throws Exception {
        System.out.println( ">");
        securityBean.explore();
        System.out.println( "<");
    }
}

