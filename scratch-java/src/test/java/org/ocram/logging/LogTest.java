package org.ocram.logging;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.ocram.ScratchBaseTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest extends ScratchBaseTest {

    @Test
    public void logThis() { 
        Exception blah = new UnsupportedEncodingException();
        blah.fillInStackTrace();
        AtomicInteger og = new AtomicInteger(1);
        logger.info("yes: {} no: {} ", og, blah);
    }
}
