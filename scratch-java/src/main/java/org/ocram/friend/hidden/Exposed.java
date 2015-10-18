package org.ocram.friend.hidden;

import org.ocram.friend.Accessor;

public class Exposed {

    static {
        // Declare classes in the implementation package as 'friends'
        Accessor.setInstance(new AccessorImpl());
    }

    // Only accessible by 'friend' classes.
    static class Hidden extends Exposed {

    }

    static final class AccessorImpl extends Accessor {
        protected Exposed newExposedInstance() {
            return new Hidden();
        }
    }
}
