package org.ocram.thread.specific;


public class ValueHolder {

    public transient Object value = null;

    public synchronized void changeValue() throws Exception {
        if (value == null) {
            System.out.println(Thread.currentThread().getName() + ": new");
            value = new Object();
            Thread.sleep(1000);
        }
        value.toString();
        if (value instanceof ValueHolder) {
            ((ValueHolder) value).changeValue();
        }  else { 
            System.out.println(Thread.currentThread().getName() + ": NULL");
            value = null;
        }
    }

}
