package org.ocram.rest.basic;

public class GreetingService {

    public static final String GREETING_PREPENDED = "Hello, ";

    /**
     * Prepends the specified name with
     * {@link GreetingService#GREETING_PREPENDED}
     * 
     * @param name
     * @return
     */
    public String greet(final String name) {
        return GREETING_PREPENDED + name;
    }
}