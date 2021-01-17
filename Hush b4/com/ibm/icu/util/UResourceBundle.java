// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.TreeSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.nio.ByteBuffer;
import com.ibm.icu.impl.ICUResourceBundleReader;
import java.util.MissingResourceException;
import com.ibm.icu.impl.ResourceBundleWrapper;
import com.ibm.icu.impl.SimpleCache;
import java.util.Locale;
import com.ibm.icu.impl.ICUResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.ref.SoftReference;
import com.ibm.icu.impl.ICUCache;
import java.util.ResourceBundle;

public abstract class UResourceBundle extends ResourceBundle
{
    private static ICUCache<ResourceCacheKey, UResourceBundle> BUNDLE_CACHE;
    private static final ResourceCacheKey cacheKey;
    private static final int ROOT_MISSING = 0;
    private static final int ROOT_ICU = 1;
    private static final int ROOT_JAVA = 2;
    private static SoftReference<ConcurrentHashMap<String, Integer>> ROOT_CACHE;
    private Set<String> keys;
    public static final int NONE = -1;
    public static final int STRING = 0;
    public static final int BINARY = 1;
    public static final int TABLE = 2;
    public static final int INT = 7;
    public static final int ARRAY = 8;
    public static final int INT_VECTOR = 14;
    
    public static UResourceBundle getBundleInstance(final String baseName, final String localeName) {
        return getBundleInstance(baseName, localeName, ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }
    
    public static UResourceBundle getBundleInstance(final String baseName, final String localeName, final ClassLoader root) {
        return getBundleInstance(baseName, localeName, root, false);
    }
    
    protected static UResourceBundle getBundleInstance(final String baseName, final String localeName, final ClassLoader root, final boolean disableFallback) {
        return instantiateBundle(baseName, localeName, root, disableFallback);
    }
    
    public UResourceBundle() {
        this.keys = null;
    }
    
    public static UResourceBundle getBundleInstance(ULocale locale) {
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        return getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }
    
    public static UResourceBundle getBundleInstance(String baseName) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        final ULocale uloc = ULocale.getDefault();
        return getBundleInstance(baseName, uloc.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }
    
    public static UResourceBundle getBundleInstance(String baseName, final Locale locale) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        final ULocale uloc = (locale == null) ? ULocale.getDefault() : ULocale.forLocale(locale);
        return getBundleInstance(baseName, uloc.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }
    
    public static UResourceBundle getBundleInstance(String baseName, ULocale locale) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        return getBundleInstance(baseName, locale.toString(), ICUResourceBundle.ICU_DATA_CLASS_LOADER, false);
    }
    
    public static UResourceBundle getBundleInstance(String baseName, final Locale locale, final ClassLoader loader) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        final ULocale uloc = (locale == null) ? ULocale.getDefault() : ULocale.forLocale(locale);
        return getBundleInstance(baseName, uloc.toString(), loader, false);
    }
    
    public static UResourceBundle getBundleInstance(String baseName, ULocale locale, final ClassLoader loader) {
        if (baseName == null) {
            baseName = "com/ibm/icu/impl/data/icudt51b";
        }
        if (locale == null) {
            locale = ULocale.getDefault();
        }
        return getBundleInstance(baseName, locale.toString(), loader, false);
    }
    
    public abstract ULocale getULocale();
    
    protected abstract String getLocaleID();
    
    protected abstract String getBaseName();
    
    protected abstract UResourceBundle getParent();
    
    @Override
    public Locale getLocale() {
        return this.getULocale().toLocale();
    }
    
    @Deprecated
    public static void resetBundleCache() {
        UResourceBundle.BUNDLE_CACHE = new SimpleCache<ResourceCacheKey, UResourceBundle>();
    }
    
    @Deprecated
    protected static UResourceBundle addToCache(final ClassLoader cl, final String fullName, final ULocale defaultLocale, final UResourceBundle b) {
        synchronized (UResourceBundle.cacheKey) {
            UResourceBundle.cacheKey.setKeyValues(cl, fullName, defaultLocale);
            final UResourceBundle cachedBundle = UResourceBundle.BUNDLE_CACHE.get(UResourceBundle.cacheKey);
            if (cachedBundle != null) {
                return cachedBundle;
            }
            UResourceBundle.BUNDLE_CACHE.put((ResourceCacheKey)UResourceBundle.cacheKey.clone(), b);
            return b;
        }
    }
    
    @Deprecated
    protected static UResourceBundle loadFromCache(final ClassLoader cl, final String fullName, final ULocale defaultLocale) {
        synchronized (UResourceBundle.cacheKey) {
            UResourceBundle.cacheKey.setKeyValues(cl, fullName, defaultLocale);
            return UResourceBundle.BUNDLE_CACHE.get(UResourceBundle.cacheKey);
        }
    }
    
    private static int getRootType(final String baseName, final ClassLoader root) {
        ConcurrentHashMap<String, Integer> m = null;
        m = UResourceBundle.ROOT_CACHE.get();
        if (m == null) {
            synchronized (UResourceBundle.class) {
                m = UResourceBundle.ROOT_CACHE.get();
                if (m == null) {
                    m = new ConcurrentHashMap<String, Integer>();
                    UResourceBundle.ROOT_CACHE = new SoftReference<ConcurrentHashMap<String, Integer>>(m);
                }
            }
        }
        Integer rootType = m.get(baseName);
        if (rootType == null) {
            final String rootLocale = (baseName.indexOf(46) == -1) ? "root" : "";
            int rt = 0;
            try {
                ICUResourceBundle.getBundleInstance(baseName, rootLocale, root, true);
                rt = 1;
            }
            catch (MissingResourceException ex) {
                try {
                    ResourceBundleWrapper.getBundleInstance(baseName, rootLocale, root, true);
                    rt = 2;
                }
                catch (MissingResourceException ex2) {}
            }
            rootType = rt;
            m.putIfAbsent(baseName, rootType);
        }
        return rootType;
    }
    
    private static void setRootType(final String baseName, final int rootType) {
        final Integer rt = rootType;
        ConcurrentHashMap<String, Integer> m = null;
        m = UResourceBundle.ROOT_CACHE.get();
        if (m == null) {
            synchronized (UResourceBundle.class) {
                m = UResourceBundle.ROOT_CACHE.get();
                if (m == null) {
                    m = new ConcurrentHashMap<String, Integer>();
                    UResourceBundle.ROOT_CACHE = new SoftReference<ConcurrentHashMap<String, Integer>>(m);
                }
            }
        }
        m.put(baseName, rt);
    }
    
    protected static UResourceBundle instantiateBundle(final String baseName, final String localeName, final ClassLoader root, final boolean disableFallback) {
        UResourceBundle b = null;
        final int rootType = getRootType(baseName, root);
        final ULocale defaultLocale = ULocale.getDefault();
        switch (rootType) {
            case 1: {
                if (disableFallback) {
                    final String fullName = ICUResourceBundleReader.getFullName(baseName, localeName);
                    b = loadFromCache(root, fullName, defaultLocale);
                    if (b == null) {
                        b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
                    }
                }
                else {
                    b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
                }
                return b;
            }
            case 2: {
                return ResourceBundleWrapper.getBundleInstance(baseName, localeName, root, disableFallback);
            }
            default: {
                try {
                    b = ICUResourceBundle.getBundleInstance(baseName, localeName, root, disableFallback);
                    setRootType(baseName, 1);
                }
                catch (MissingResourceException ex) {
                    b = ResourceBundleWrapper.getBundleInstance(baseName, localeName, root, disableFallback);
                    setRootType(baseName, 2);
                }
                return b;
            }
        }
    }
    
    public ByteBuffer getBinary() {
        throw new UResourceTypeMismatchException("");
    }
    
    public String getString() {
        throw new UResourceTypeMismatchException("");
    }
    
    public String[] getStringArray() {
        throw new UResourceTypeMismatchException("");
    }
    
    public byte[] getBinary(final byte[] ba) {
        throw new UResourceTypeMismatchException("");
    }
    
    public int[] getIntVector() {
        throw new UResourceTypeMismatchException("");
    }
    
    public int getInt() {
        throw new UResourceTypeMismatchException("");
    }
    
    public int getUInt() {
        throw new UResourceTypeMismatchException("");
    }
    
    public UResourceBundle get(final String aKey) {
        final UResourceBundle obj = this.findTopLevel(aKey);
        if (obj == null) {
            final String fullName = ICUResourceBundleReader.getFullName(this.getBaseName(), this.getLocaleID());
            throw new MissingResourceException("Can't find resource for bundle " + fullName + ", key " + aKey, this.getClass().getName(), aKey);
        }
        return obj;
    }
    
    @Deprecated
    protected UResourceBundle findTopLevel(final String aKey) {
        for (UResourceBundle res = this; res != null; res = res.getParent()) {
            final UResourceBundle obj = res.handleGet(aKey, null, this);
            if (obj != null) {
                ((ICUResourceBundle)obj).setLoadingStatus(this.getLocaleID());
                return obj;
            }
        }
        return null;
    }
    
    public String getString(final int index) {
        final ICUResourceBundle temp = (ICUResourceBundle)this.get(index);
        if (temp.getType() == 0) {
            return temp.getString();
        }
        throw new UResourceTypeMismatchException("");
    }
    
    public UResourceBundle get(final int index) {
        UResourceBundle obj = this.handleGet(index, null, this);
        if (obj == null) {
            obj = this.getParent();
            if (obj != null) {
                obj = obj.get(index);
            }
            if (obj == null) {
                throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getKey(), this.getClass().getName(), this.getKey());
            }
        }
        ((ICUResourceBundle)obj).setLoadingStatus(this.getLocaleID());
        return obj;
    }
    
    @Deprecated
    protected UResourceBundle findTopLevel(final int index) {
        for (UResourceBundle res = this; res != null; res = res.getParent()) {
            final UResourceBundle obj = res.handleGet(index, null, this);
            if (obj != null) {
                ((ICUResourceBundle)obj).setLoadingStatus(this.getLocaleID());
                return obj;
            }
        }
        return null;
    }
    
    @Override
    public Enumeration<String> getKeys() {
        return Collections.enumeration(this.keySet());
    }
    
    @Override
    @Deprecated
    public Set<String> keySet() {
        if (this.keys == null) {
            if (!this.isTopLevelResource()) {
                return this.handleKeySet();
            }
            TreeSet<String> newKeySet;
            if (this.parent == null) {
                newKeySet = new TreeSet<String>();
            }
            else if (this.parent instanceof UResourceBundle) {
                newKeySet = new TreeSet<String>(((UResourceBundle)this.parent).keySet());
            }
            else {
                newKeySet = new TreeSet<String>();
                final Enumeration<String> parentKeys = this.parent.getKeys();
                while (parentKeys.hasMoreElements()) {
                    newKeySet.add(parentKeys.nextElement());
                }
            }
            newKeySet.addAll(this.handleKeySet());
            this.keys = Collections.unmodifiableSet((Set<? extends String>)newKeySet);
        }
        return this.keys;
    }
    
    @Override
    @Deprecated
    protected Set<String> handleKeySet() {
        return Collections.emptySet();
    }
    
    public int getSize() {
        return 1;
    }
    
    public int getType() {
        return -1;
    }
    
    public VersionInfo getVersion() {
        return null;
    }
    
    public UResourceBundleIterator getIterator() {
        return new UResourceBundleIterator(this);
    }
    
    public String getKey() {
        return null;
    }
    
    protected UResourceBundle handleGet(final String aKey, final HashMap<String, String> table, final UResourceBundle requested) {
        return null;
    }
    
    protected UResourceBundle handleGet(final int index, final HashMap<String, String> table, final UResourceBundle requested) {
        return null;
    }
    
    protected String[] handleGetStringArray() {
        return null;
    }
    
    protected Enumeration<String> handleGetKeys() {
        return null;
    }
    
    @Override
    protected Object handleGetObject(final String aKey) {
        return this.handleGetObjectImpl(aKey, this);
    }
    
    private Object handleGetObjectImpl(final String aKey, final UResourceBundle requested) {
        Object obj = this.resolveObject(aKey, requested);
        if (obj == null) {
            final UResourceBundle parentBundle = this.getParent();
            if (parentBundle != null) {
                obj = parentBundle.handleGetObjectImpl(aKey, requested);
            }
            if (obj == null) {
                throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + aKey, this.getClass().getName(), aKey);
            }
        }
        return obj;
    }
    
    private Object resolveObject(final String aKey, final UResourceBundle requested) {
        if (this.getType() == 0) {
            return this.getString();
        }
        final UResourceBundle obj = this.handleGet(aKey, null, requested);
        if (obj != null) {
            if (obj.getType() == 0) {
                return obj.getString();
            }
            try {
                if (obj.getType() == 8) {
                    return obj.handleGetStringArray();
                }
            }
            catch (UResourceTypeMismatchException ex) {
                return obj;
            }
        }
        return obj;
    }
    
    @Deprecated
    protected abstract void setLoadingStatus(final int p0);
    
    @Deprecated
    protected boolean isTopLevelResource() {
        return true;
    }
    
    static {
        UResourceBundle.BUNDLE_CACHE = new SimpleCache<ResourceCacheKey, UResourceBundle>();
        cacheKey = new ResourceCacheKey();
        UResourceBundle.ROOT_CACHE = new SoftReference<ConcurrentHashMap<String, Integer>>(new ConcurrentHashMap<String, Integer>());
    }
    
    private static final class ResourceCacheKey implements Cloneable
    {
        private SoftReference<ClassLoader> loaderRef;
        private String searchName;
        private ULocale defaultLocale;
        private int hashCodeCache;
        
        @Override
        public boolean equals(final Object other) {
            if (other == null) {
                return false;
            }
            if (this == other) {
                return true;
            }
            try {
                final ResourceCacheKey otherEntry = (ResourceCacheKey)other;
                if (this.hashCodeCache != otherEntry.hashCodeCache) {
                    return false;
                }
                if (!this.searchName.equals(otherEntry.searchName)) {
                    return false;
                }
                if (this.defaultLocale == null) {
                    if (otherEntry.defaultLocale != null) {
                        return false;
                    }
                }
                else if (!this.defaultLocale.equals(otherEntry.defaultLocale)) {
                    return false;
                }
                if (this.loaderRef == null) {
                    return otherEntry.loaderRef == null;
                }
                return otherEntry.loaderRef != null && this.loaderRef.get() == otherEntry.loaderRef.get();
            }
            catch (NullPointerException e) {
                return false;
            }
            catch (ClassCastException e2) {
                return false;
            }
        }
        
        @Override
        public int hashCode() {
            return this.hashCodeCache;
        }
        
        public Object clone() {
            try {
                return super.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new IllegalStateException();
            }
        }
        
        private synchronized void setKeyValues(final ClassLoader root, final String searchName, final ULocale defaultLocale) {
            this.searchName = searchName;
            this.hashCodeCache = searchName.hashCode();
            this.defaultLocale = defaultLocale;
            if (defaultLocale != null) {
                this.hashCodeCache ^= defaultLocale.hashCode();
            }
            if (root == null) {
                this.loaderRef = null;
            }
            else {
                this.loaderRef = new SoftReference<ClassLoader>(root);
                this.hashCodeCache ^= root.hashCode();
            }
        }
    }
}
