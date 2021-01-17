// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.PropertyResourceBundle;
import java.io.BufferedInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.io.InputStream;
import com.ibm.icu.util.ULocale;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.List;
import java.util.ResourceBundle;
import com.ibm.icu.util.UResourceBundle;

public class ResourceBundleWrapper extends UResourceBundle
{
    private ResourceBundle bundle;
    private String localeID;
    private String baseName;
    private List<String> keys;
    private static final boolean DEBUG;
    
    private ResourceBundleWrapper(final ResourceBundle bundle) {
        this.bundle = null;
        this.localeID = null;
        this.baseName = null;
        this.keys = null;
        this.bundle = bundle;
    }
    
    @Override
    protected void setLoadingStatus(final int newStatus) {
    }
    
    @Override
    protected Object handleGetObject(final String aKey) {
        ResourceBundleWrapper current = this;
        Object obj = null;
        while (current != null) {
            try {
                obj = current.bundle.getObject(aKey);
            }
            catch (MissingResourceException ex) {
                current = (ResourceBundleWrapper)current.getParent();
                continue;
            }
            break;
        }
        if (obj == null) {
            throw new MissingResourceException("Can't find resource for bundle " + this.baseName + ", key " + aKey, this.getClass().getName(), aKey);
        }
        return obj;
    }
    
    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(this.keys);
    }
    
    private void initKeysVector() {
        ResourceBundleWrapper current = this;
        this.keys = new ArrayList<String>();
        while (current != null) {
            final Enumeration<String> e = current.bundle.getKeys();
            while (e.hasMoreElements()) {
                final String elem = e.nextElement();
                if (!this.keys.contains(elem)) {
                    this.keys.add(elem);
                }
            }
            current = (ResourceBundleWrapper)current.getParent();
        }
    }
    
    @Override
    protected String getLocaleID() {
        return this.localeID;
    }
    
    @Override
    protected String getBaseName() {
        return this.bundle.getClass().getName().replace('.', '/');
    }
    
    @Override
    public ULocale getULocale() {
        return new ULocale(this.localeID);
    }
    
    public UResourceBundle getParent() {
        return (UResourceBundle)this.parent;
    }
    
    public static UResourceBundle getBundleInstance(final String baseName, final String localeID, final ClassLoader root, final boolean disableFallback) {
        final UResourceBundle b = instantiateBundle(baseName, localeID, root, disableFallback);
        if (b == null) {
            String separator = "_";
            if (baseName.indexOf(47) >= 0) {
                separator = "/";
            }
            throw new MissingResourceException("Could not find the bundle " + baseName + separator + localeID, "", "");
        }
        return b;
    }
    
    protected static synchronized UResourceBundle instantiateBundle(final String baseName, final String localeID, ClassLoader root, final boolean disableFallback) {
        if (root == null) {
            root = Utility.getFallbackClassLoader();
        }
        final ClassLoader cl = root;
        String name = baseName;
        final ULocale defaultLocale = ULocale.getDefault();
        if (localeID.length() != 0) {
            name = name + "_" + localeID;
        }
        ResourceBundleWrapper b = (ResourceBundleWrapper)UResourceBundle.loadFromCache(cl, name, defaultLocale);
        if (b == null) {
            ResourceBundleWrapper parent = null;
            final int i = localeID.lastIndexOf(95);
            boolean loadFromProperties = false;
            if (i != -1) {
                final String locName = localeID.substring(0, i);
                parent = (ResourceBundleWrapper)UResourceBundle.loadFromCache(cl, baseName + "_" + locName, defaultLocale);
                if (parent == null) {
                    parent = (ResourceBundleWrapper)instantiateBundle(baseName, locName, cl, disableFallback);
                }
            }
            else if (localeID.length() > 0) {
                parent = (ResourceBundleWrapper)UResourceBundle.loadFromCache(cl, baseName, defaultLocale);
                if (parent == null) {
                    parent = (ResourceBundleWrapper)instantiateBundle(baseName, "", cl, disableFallback);
                }
            }
            try {
                final Class<? extends ResourceBundle> cls = cl.loadClass(name).asSubclass(ResourceBundle.class);
                final ResourceBundle bx = (ResourceBundle)cls.newInstance();
                b = new ResourceBundleWrapper(bx);
                if (parent != null) {
                    b.setParent(parent);
                }
                b.baseName = baseName;
                b.localeID = localeID;
            }
            catch (ClassNotFoundException e2) {
                loadFromProperties = true;
            }
            catch (NoClassDefFoundError e3) {
                loadFromProperties = true;
            }
            catch (Exception e) {
                if (ResourceBundleWrapper.DEBUG) {
                    System.out.println("failure");
                }
                if (ResourceBundleWrapper.DEBUG) {
                    System.out.println(e);
                }
            }
            if (loadFromProperties) {
                try {
                    final String resName = name.replace('.', '/') + ".properties";
                    InputStream stream = AccessController.doPrivileged((PrivilegedAction<InputStream>)new PrivilegedAction<InputStream>() {
                        public InputStream run() {
                            if (cl != null) {
                                return cl.getResourceAsStream(resName);
                            }
                            return ClassLoader.getSystemResourceAsStream(resName);
                        }
                    });
                    if (stream != null) {
                        stream = new BufferedInputStream(stream);
                        try {
                            b = new ResourceBundleWrapper(new PropertyResourceBundle(stream));
                            if (parent != null) {
                                b.setParent(parent);
                            }
                            b.baseName = baseName;
                            b.localeID = localeID;
                        }
                        catch (Exception ex) {}
                        finally {
                            try {
                                stream.close();
                            }
                            catch (Exception ex2) {}
                        }
                    }
                    if (b == null) {
                        final String defaultName = defaultLocale.toString();
                        if (localeID.length() > 0 && localeID.indexOf(95) < 0 && defaultName.indexOf(localeID) == -1) {
                            b = (ResourceBundleWrapper)UResourceBundle.loadFromCache(cl, baseName + "_" + defaultName, defaultLocale);
                            if (b == null) {
                                b = (ResourceBundleWrapper)instantiateBundle(baseName, defaultName, cl, disableFallback);
                            }
                        }
                    }
                    if (b == null) {
                        b = parent;
                    }
                }
                catch (Exception e) {
                    if (ResourceBundleWrapper.DEBUG) {
                        System.out.println("failure");
                    }
                    if (ResourceBundleWrapper.DEBUG) {
                        System.out.println(e);
                    }
                }
            }
            b = (ResourceBundleWrapper)UResourceBundle.addToCache(cl, name, defaultLocale, b);
        }
        if (b != null) {
            b.initKeysVector();
        }
        else if (ResourceBundleWrapper.DEBUG) {
            System.out.println("Returning null for " + baseName + "_" + localeID);
        }
        return b;
    }
    
    static {
        DEBUG = ICUDebug.enabled("resourceBundleWrapper");
    }
}
