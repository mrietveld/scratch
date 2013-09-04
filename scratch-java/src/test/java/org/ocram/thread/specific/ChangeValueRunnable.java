package org.ocram.thread.specific;

public class ChangeValueRunnable implements Runnable {

    private ValueHolder value;

    public ChangeValueRunnable(ValueHolder value) {
        this.value = value;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started");
        try {
            while (true) {
                value.changeValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
