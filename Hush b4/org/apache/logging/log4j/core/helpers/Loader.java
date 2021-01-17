// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.status.StatusLogger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.io.InputStream;
import java.net.URL;
import org.apache.logging.log4j.Logger;

public final class Loader
{
    private static boolean ignoreTCL;
    private static final Logger LOGGER;
    private static final String TSTR = "Caught Exception while in Loader.getResource. This may be innocuous.";
    
    public static ClassLoader getClassLoader() {
        return getClassLoader(Loader.class, null);
    }
    
    public static ClassLoader getClassLoader(final Class<?> class1, final Class<?> class2) {
        ClassLoader loader1 = null;
        try {
            loader1 = getTCL();
        }
        catch (Exception ex) {
            Loader.LOGGER.warn("Caught exception locating thread ClassLoader {}", ex.getMessage());
        }
        final ClassLoader loader2 = (class1 == null) ? null : class1.getClassLoader();
        final ClassLoader loader3 = (class2 == null) ? null : class2.getClass().getClassLoader();
        if (isChild(loader1, loader2)) {
            return isChild(loader1, loader3) ? loader1 : loader3;
        }
        return isChild(loader2, loader3) ? loader2 : loader3;
    }
    
    public static URL getResource(final String resource, final ClassLoader defaultLoader) {
        try {
            ClassLoader classLoader = getTCL();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [" + resource + "] using context classloader " + classLoader + '.');
                final URL url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                final URL url = classLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
            if (defaultLoader != null) {
                Loader.LOGGER.trace("Trying to find [" + resource + "] using " + defaultLoader + " class loader.");
                final URL url = defaultLoader.getResource(resource);
                if (url != null) {
                    return url;
                }
            }
        }
        catch (Throwable t) {
            Loader.LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
        }
        Loader.LOGGER.trace("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResource(resource);
    }
    
    public static InputStream getResourceAsStream(final String resource, final ClassLoader defaultLoader) {
        try {
            ClassLoader classLoader = getTCL();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [" + resource + "] using context classloader " + classLoader + '.');
                final InputStream is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            classLoader = Loader.class.getClassLoader();
            if (classLoader != null) {
                Loader.LOGGER.trace("Trying to find [" + resource + "] using " + classLoader + " class loader.");
                final InputStream is = classLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
            if (defaultLoader != null) {
                Loader.LOGGER.trace("Trying to find [" + resource + "] using " + defaultLoader + " class loader.");
                final InputStream is = defaultLoader.getResourceAsStream(resource);
                if (is != null) {
                    return is;
                }
            }
        }
        catch (Throwable t) {
            Loader.LOGGER.warn("Caught Exception while in Loader.getResource. This may be innocuous.", t);
        }
        Loader.LOGGER.trace("Trying to find [" + resource + "] using ClassLoader.getSystemResource().");
        return ClassLoader.getSystemResourceAsStream(resource);
    }
    
    private static ClassLoader getTCL() {
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
        return cl;
    }
    
    private static boolean isChild(final ClassLoader loader1, final ClassLoader loader2) {
        if (loader1 != null && loader2 != null) {
            ClassLoader parent;
            for (parent = loader1.getParent(); parent != null && parent != loader2; parent = parent.getParent()) {}
            return parent != null;
        }
        return loader1 != null;
    }
    
    public static Class<?> loadClass(final String className) throws ClassNotFoundException {
        if (Loader.ignoreTCL) {
            return Class.forName(className);
        }
        try {
            return getTCL().loadClass(className);
        }
        catch (Throwable e) {
            return Class.forName(className);
        }
    }
    
    private Loader() {
    }
    
    static {
        Loader.ignoreTCL = false;
        LOGGER = StatusLogger.getLogger();
        final String ignoreTCLProp = PropertiesUtil.getProperties().getStringProperty("log4j.ignoreTCL", null);
        if (ignoreTCLProp != null) {
            Loader.ignoreTCL = OptionConverter.toBoolean(ignoreTCLProp, true);
        }
    }
}
