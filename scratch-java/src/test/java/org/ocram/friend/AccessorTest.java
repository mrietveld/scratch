package org.ocram.friend;

import org.junit.Test;
import org.ocram.friend.hidden.Exposed;

public class AccessorTest {

    @Test
    public void friendTest() {
        Accessor accessor = Accessor.getInstance();
        Exposed exposed = accessor.newExposedInstance();
    }
}
