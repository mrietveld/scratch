package org.ocram.arquillian.as7;

import javax.ejb.EJB;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ocram.arquillian.as7.timer.TimerSchedulerBean;

@RunWith(Arquillian.class)
public class TimerServiceTest {

    @EJB
    private TimerSchedulerBean ts;

    @Deployment
    public static JavaArchive createTestArchive() {
        return ShrinkWrap.create(JavaArchive.class, "timerScheduler.jar")
                .addClasses(TimerSchedulerBean.class)
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    public void testTimerScheduler() throws Exception {
        ts.createTimerJob();
        Thread.sleep(10000);
    }
}
