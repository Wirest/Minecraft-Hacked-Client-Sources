// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.InputStream;
import java.io.IOException;
import java.util.MissingResourceException;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

public class ICUConfig
{
    public static final String CONFIG_PROPS_FILE = "/com/ibm/icu/ICUConfig.properties";
    private static final Properties CONFIG_PROPS;
    
    public static String get(final String name) {
        return get(name, null);
    }
    
    public static String get(final String name, final String def) {
        String val = null;
        final String fname = name;
        if (System.getSecurityManager() != null) {
            try {
                val = AccessController.doPrivileged((PrivilegedAction<String>)new PrivilegedAction<String>() {
                    public String run() {
                        return System.getProperty(fname);
                    }
                });
            }
            catch (AccessControlException e) {}
        }
        else {
            val = System.getProperty(name);
        }
        if (val == null) {
            val = ICUConfig.CONFIG_PROPS.getProperty(name, def);
        }
        return val;
    }
    
    static {
        CONFIG_PROPS = new Properties();
        try {
            final InputStream is = ICUData.getStream("/com/ibm/icu/ICUConfig.properties");
            if (is != null) {
                ICUConfig.CONFIG_PROPS.load(is);
            }
        }
        catch (MissingResourceException mre) {}
        catch (IOException ex) {}
    }
}
