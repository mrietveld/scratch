package org.ocram.mx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class CacheHolderFactory { 
    private static class CacheHolder { 
        protected static List<String> listCache = new ArrayList<String>();
        protected final static long initializationTime = new Date().getTime();
    }
    
    public static long getCacheInitializationTime() { 
        return CacheHolder.initializationTime;
    }
}