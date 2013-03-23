package org.ocram.rest.basic;

import javax.inject.Inject;

import org.junit.Assert;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ArquillianBasicTest {

    @Deployment
    public static JavaArchive create() {
        final JavaArchive archive = ShrinkWrap.create(JavaArchive.class).addClass(GreetingService.class);
        archive.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
        return archive;
    }

    @Inject
    private GreetingService service;

    @Test
    public void shouldBeAbleToInject() throws Exception {
        Assert.assertNotNull(service);
        final String name = "ALR";
        Assert.assertEquals(GreetingService.GREETING_PREPENDED + name, service.greet(name));
    }
}