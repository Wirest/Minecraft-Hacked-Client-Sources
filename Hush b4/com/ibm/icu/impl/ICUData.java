// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.MissingResourceException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.net.URL;

public final class ICUData
{
    public static boolean exists(final String resourceName) {
        URL i = null;
        if (System.getSecurityManager() != null) {
            i = AccessController.doPrivileged((PrivilegedAction<URL>)new PrivilegedAction<URL>() {
                public URL run() {
                    return ICUData.class.getResource(resourceName);
                }
            });
        }
        else {
            i = ICUData.class.getResource(resourceName);
        }
        return i != null;
    }
    
    private static InputStream getStream(final Class<?> root, final String resourceName, final boolean required) {
        InputStream i = null;
        if (System.getSecurityManager() != null) {
            i = AccessController.doPrivileged((PrivilegedAction<InputStream>)new PrivilegedAction<InputStream>() {
                public InputStream run() {
                    return root.getResourceAsStream(resourceName);
                }
            });
        }
        else {
            i = root.getResourceAsStream(resourceName);
        }
        if (i == null && required) {
            throw new MissingResourceException("could not locate data " + resourceName, root.getPackage().getName(), resourceName);
        }
        return i;
    }
    
    private static InputStream getStream(final ClassLoader loader, final String resourceName, final boolean required) {
        InputStream i = null;
        if (System.getSecurityManager() != null) {
            i = AccessController.doPrivileged((PrivilegedAction<InputStream>)new PrivilegedAction<InputStream>() {
                public InputStream run() {
                    return loader.getResourceAsStream(resourceName);
                }
            });
        }
        else {
            i = loader.getResourceAsStream(resourceName);
        }
        if (i == null && required) {
            throw new MissingResourceException("could not locate data", loader.toString(), resourceName);
        }
        return i;
    }
    
    public static InputStream getStream(final ClassLoader loader, final String resourceName) {
        return getStream(loader, resourceName, false);
    }
    
    public static InputStream getRequiredStream(final ClassLoader loader, final String resourceName) {
        return getStream(loader, resourceName, true);
    }
    
    public static InputStream getStream(final String resourceName) {
        return getStream(ICUData.class, resourceName, false);
    }
    
    public static InputStream getRequiredStream(final String resourceName) {
        return getStream(ICUData.class, resourceName, true);
    }
    
    public static InputStream getStream(final Class<?> root, final String resourceName) {
        return getStream(root, resourceName, false);
    }
    
    public static InputStream getRequiredStream(final Class<?> root, final String resourceName) {
        return getStream(root, resourceName, true);
    }
}
