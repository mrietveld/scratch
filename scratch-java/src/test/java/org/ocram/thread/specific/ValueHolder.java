package org.ocram.thread.specific;

import org.ocram.reflection.proxy.objects.ClassToBeProxied;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ValueHolder {

    private static Logger logger = LoggerFactory.getLogger(ClassToBeProxied.class);
    
    public transient Object value = null;

    public synchronized void changeValue() throws Exception {
        if (value == null) {
            logger.debug(Thread.currentThread().getName() + ": new");
            value = new Object();
            Thread.sleep(1000);
        }
        value.toString();
        if (value instanceof ValueHolder) {
            ((ValueHolder) value).changeValue();
        }  else { 
            logger.debug(Thread.currentThread().getName() + ": NULL");
            value = null;
        }
    }

}
