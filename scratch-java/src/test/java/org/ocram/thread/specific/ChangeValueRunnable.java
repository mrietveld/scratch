package org.ocram.thread.specific;

import org.ocram.reflection.proxy.objects.ClassToBeProxied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChangeValueRunnable implements Runnable {

    private static Logger logger = LoggerFactory.getLogger(ChangeValueRunnable.class);
    
    private ValueHolder value;

    public ChangeValueRunnable(ValueHolder value) {
        this.value = value;
    }

    @Override
    public void run() {
        logger.debug(Thread.currentThread().getName() + " started");
        try {
            while (true) {
                value.changeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
