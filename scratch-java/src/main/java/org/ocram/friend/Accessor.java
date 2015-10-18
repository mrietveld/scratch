package org.ocram.friend;

import org.ocram.friend.hidden.Exposed;

public abstract class Accessor {

    private static Accessor instance;

    static Accessor getInstance() {
        Accessor a = instance;
        if (a != null) {
            return a;
        }

        return createInstance();
    }

    private static Accessor createInstance() {
        try {
            Class.forName(Exposed.class.getName(), true, Exposed.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }

        return instance;
    }

    public static void setInstance(Accessor accessor) {
        if (instance != null) {
            throw new IllegalStateException(
                "Accessor instance already set");
        }

        instance = accessor;
    }

    protected abstract Exposed newExposedInstance();
}
