// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

import java.util.Properties;
import java.util.Enumeration;
import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.logging.log4j.status.StatusLogger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Iterator;
import org.apache.logging.log4j.spi.Provider;
import java.util.List;
import org.apache.logging.log4j.Logger;

public final class ProviderUtil
{
    private static final String PROVIDER_RESOURCE = "META-INF/log4j-provider.properties";
    private static final String API_VERSION = "Log4jAPIVersion";
    private static final String[] COMPATIBLE_API_VERSIONS;
    private static final Logger LOGGER;
    private static final List<Provider> PROVIDERS;
    
    private ProviderUtil() {
    }
    
    public static Iterator<Provider> getProviders() {
        return ProviderUtil.PROVIDERS.iterator();
    }
    
    public static boolean hasProviders() {
        return ProviderUtil.PROVIDERS.size() > 0;
    }
    
    public static ClassLoader findClassLoader() {
        ClassLoader cl;
        if (System.getSecurityManager() == null) {
            cl = Thread.currentThread().getContextClassLoader();
        }
        else {
            cl = AccessController.doPrivileged((PrivilegedAction<ClassLoader>)new PrivilegedAction<ClassLoader>() {
                @Override
                public ClassLoader run() {
                    return Thread.currentThread().getContextClassLoader();
                }
            });
        }
        if (cl == null) {
            cl = ProviderUtil.class.getClassLoader();
        }
        return cl;
    }
    
    private static boolean validVersion(final String version) {
        for (final String v : ProviderUtil.COMPATIBLE_API_VERSIONS) {
            if (version.startsWith(v)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        COMPATIBLE_API_VERSIONS = new String[] { "2.0.0" };
        LOGGER = StatusLogger.getLogger();
        PROVIDERS = new ArrayList<Provider>();
        final ClassLoader cl = findClassLoader();
        Enumeration<URL> enumResources = null;
        try {
            enumResources = cl.getResources("META-INF/log4j-provider.properties");
        }
        catch (IOException e) {
            ProviderUtil.LOGGER.fatal("Unable to locate META-INF/log4j-provider.properties", e);
        }
        if (enumResources != null) {
            while (enumResources.hasMoreElements()) {
                final URL url = enumResources.nextElement();
                try {
                    final Properties props = PropertiesUtil.loadClose(url.openStream(), url);
                    if (!validVersion(props.getProperty("Log4jAPIVersion"))) {
                        continue;
                    }
                    ProviderUtil.PROVIDERS.add(new Provider(props, url));
                }
                catch (IOException ioe) {
                    ProviderUtil.LOGGER.error("Unable to open " + url.toString(), ioe);
                }
            }
        }
    }
}
