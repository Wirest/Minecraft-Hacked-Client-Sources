// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Arrays;
import java.util.Collections;
import java.security.AccessController;
import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Collection;
import java.net.URL;
import java.security.PrivilegedAction;
import java.util.List;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Set;
import java.util.HashSet;
import java.util.MissingResourceException;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;

public class ICUResourceBundle extends UResourceBundle
{
    protected static final String ICU_DATA_PATH = "com/ibm/icu/impl/";
    public static final String ICU_BUNDLE = "data/icudt51b";
    public static final String ICU_BASE_NAME = "com/ibm/icu/impl/data/icudt51b";
    public static final String ICU_COLLATION_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/coll";
    public static final String ICU_BRKITR_NAME = "/brkitr";
    public static final String ICU_BRKITR_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/brkitr";
    public static final String ICU_RBNF_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/rbnf";
    public static final String ICU_TRANSLIT_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/translit";
    public static final String ICU_LANG_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/lang";
    public static final String ICU_CURR_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/curr";
    public static final String ICU_REGION_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/region";
    public static final String ICU_ZONE_BASE_NAME = "com/ibm/icu/impl/data/icudt51b/zone";
    private static final String NO_INHERITANCE_MARKER = "\u2205\u2205\u2205";
    protected String resPath;
    public static final ClassLoader ICU_DATA_CLASS_LOADER;
    protected static final String INSTALLED_LOCALES = "InstalledLocales";
    public static final int FROM_FALLBACK = 1;
    public static final int FROM_ROOT = 2;
    public static final int FROM_DEFAULT = 3;
    public static final int FROM_LOCALE = 4;
    private int loadingStatus;
    private static final String ICU_RESOURCE_INDEX = "res_index";
    private static final String DEFAULT_TAG = "default";
    private static final String FULL_LOCALE_NAMES_LIST = "fullLocaleNames.lst";
    private static final boolean DEBUG;
    private static CacheBase<String, AvailEntry, ClassLoader> GET_AVAILABLE_CACHE;
    protected String localeID;
    protected String baseName;
    protected ULocale ulocale;
    protected ClassLoader loader;
    protected ICUResourceBundleReader reader;
    protected String key;
    protected int resource;
    public static final int RES_BOGUS = -1;
    public static final int ALIAS = 3;
    public static final int TABLE32 = 4;
    public static final int TABLE16 = 5;
    public static final int STRING_V2 = 6;
    public static final int ARRAY16 = 9;
    private static final int[] gPublicTypes;
    private static final char RES_PATH_SEP_CHAR = '/';
    private static final String RES_PATH_SEP_STR = "/";
    private static final String ICUDATA = "ICUDATA";
    private static final char HYPHEN = '-';
    private static final String LOCALE = "LOCALE";
    protected ICUCache<Object, UResourceBundle> lookup;
    private static final int MAX_INITIAL_LOOKUP_SIZE = 64;
    
    public void setLoadingStatus(final int newStatus) {
        this.loadingStatus = newStatus;
    }
    
    public int getLoadingStatus() {
        return this.loadingStatus;
    }
    
    public void setLoadingStatus(final String requestedLocale) {
        final String locale = this.getLocaleID();
        if (locale.equals("root")) {
            this.setLoadingStatus(2);
        }
        else if (locale.equals(requestedLocale)) {
            this.setLoadingStatus(4);
        }
        else {
            this.setLoadingStatus(1);
        }
    }
    
    public String getResPath() {
        return this.resPath;
    }
    
    public static final ULocale getFunctionalEquivalent(final String baseName, final ClassLoader loader, final String resName, final String keyword, final ULocale locID, final boolean[] isAvailable, final boolean omitDefault) {
        String kwVal = locID.getKeywordValue(keyword);
        final String baseLoc = locID.getBaseName();
        String defStr = null;
        ULocale parent = new ULocale(baseLoc);
        ULocale defLoc = null;
        boolean lookForDefault = false;
        ULocale fullBase = null;
        int defDepth = 0;
        int resDepth = 0;
        if (kwVal == null || kwVal.length() == 0 || kwVal.equals("default")) {
            kwVal = "";
            lookForDefault = true;
        }
        ICUResourceBundle r = null;
        r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
        if (isAvailable != null) {
            isAvailable[0] = false;
            final ULocale[] availableULocales = getAvailEntry(baseName, loader).getULocaleList();
            for (int i = 0; i < availableULocales.length; ++i) {
                if (parent.equals(availableULocales[i])) {
                    isAvailable[0] = true;
                    break;
                }
            }
        }
        do {
            try {
                final ICUResourceBundle irb = (ICUResourceBundle)r.get(resName);
                defStr = irb.getString("default");
                if (lookForDefault) {
                    kwVal = defStr;
                    lookForDefault = false;
                }
                defLoc = r.getULocale();
            }
            catch (MissingResourceException ex) {}
            if (defLoc == null) {
                r = (ICUResourceBundle)r.getParent();
                ++defDepth;
            }
        } while (r != null && defLoc == null);
        parent = new ULocale(baseLoc);
        r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
        do {
            try {
                final ICUResourceBundle irb = (ICUResourceBundle)r.get(resName);
                irb.get(kwVal);
                fullBase = irb.getULocale();
                if (fullBase != null && resDepth > defDepth) {
                    defStr = irb.getString("default");
                    defLoc = r.getULocale();
                    defDepth = resDepth;
                }
            }
            catch (MissingResourceException ex2) {}
            if (fullBase == null) {
                r = (ICUResourceBundle)r.getParent();
                ++resDepth;
            }
        } while (r != null && fullBase == null);
        if (fullBase == null && defStr != null && !defStr.equals(kwVal)) {
            kwVal = defStr;
            parent = new ULocale(baseLoc);
            r = (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, parent);
            resDepth = 0;
            do {
                try {
                    final ICUResourceBundle irb = (ICUResourceBundle)r.get(resName);
                    final UResourceBundle urb = irb.get(kwVal);
                    fullBase = r.getULocale();
                    if (!fullBase.toString().equals(urb.getLocale().toString())) {
                        fullBase = null;
                    }
                    if (fullBase != null && resDepth > defDepth) {
                        defStr = irb.getString("default");
                        defLoc = r.getULocale();
                        defDepth = resDepth;
                    }
                }
                catch (MissingResourceException ex3) {}
                if (fullBase == null) {
                    r = (ICUResourceBundle)r.getParent();
                    ++resDepth;
                }
            } while (r != null && fullBase == null);
        }
        if (fullBase == null) {
            throw new MissingResourceException("Could not find locale containing requested or default keyword.", baseName, keyword + "=" + kwVal);
        }
        if (omitDefault && defStr.equals(kwVal) && resDepth <= defDepth) {
            return fullBase;
        }
        return new ULocale(fullBase.toString() + "@" + keyword + "=" + kwVal);
    }
    
    public static final String[] getKeywordValues(final String baseName, final String keyword) {
        final Set<String> keywords = new HashSet<String>();
        final ULocale[] locales = createULocaleList(baseName, ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        for (int i = 0; i < locales.length; ++i) {
            try {
                final UResourceBundle b = UResourceBundle.getBundleInstance(baseName, locales[i]);
                final ICUResourceBundle irb = (ICUResourceBundle)b.getObject(keyword);
                final Enumeration<String> e = irb.getKeys();
                while (e.hasMoreElements()) {
                    final String s = e.nextElement();
                    if (!"default".equals(s)) {
                        keywords.add(s);
                    }
                }
            }
            catch (Throwable t) {}
        }
        return keywords.toArray(new String[0]);
    }
    
    public ICUResourceBundle getWithFallback(final String path) throws MissingResourceException {
        ICUResourceBundle result = null;
        final ICUResourceBundle actualBundle = this;
        result = findResourceWithFallback(path, actualBundle, null);
        if (result == null) {
            throw new MissingResourceException("Can't find resource for bundle " + this.getClass().getName() + ", key " + this.getType(), path, this.getKey());
        }
        if (result.getType() == 0 && result.getString().equals("\u2205\u2205\u2205")) {
            throw new MissingResourceException("Encountered NO_INHERITANCE_MARKER", path, this.getKey());
        }
        return result;
    }
    
    public ICUResourceBundle at(final int index) {
        return (ICUResourceBundle)this.handleGet(index, null, this);
    }
    
    public ICUResourceBundle at(final String key) {
        if (this instanceof ICUResourceBundleImpl.ResourceTable) {
            return (ICUResourceBundle)this.handleGet(key, null, this);
        }
        return null;
    }
    
    public ICUResourceBundle findTopLevel(final int index) {
        return (ICUResourceBundle)super.findTopLevel(index);
    }
    
    public ICUResourceBundle findTopLevel(final String aKey) {
        return (ICUResourceBundle)super.findTopLevel(aKey);
    }
    
    public ICUResourceBundle findWithFallback(final String path) {
        return findResourceWithFallback(path, this, null);
    }
    
    public String getStringWithFallback(final String path) throws MissingResourceException {
        return this.getWithFallback(path).getString();
    }
    
    public static Set<String> getAvailableLocaleNameSet(final String bundlePrefix, final ClassLoader loader) {
        return getAvailEntry(bundlePrefix, loader).getLocaleNameSet();
    }
    
    public static Set<String> getFullLocaleNameSet() {
        return getFullLocaleNameSet("com/ibm/icu/impl/data/icudt51b", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    }
    
    public static Set<String> getFullLocaleNameSet(final String bundlePrefix, final ClassLoader loader) {
        return getAvailEntry(bundlePrefix, loader).getFullLocaleNameSet();
    }
    
    public static Set<String> getAvailableLocaleNameSet() {
        return getAvailableLocaleNameSet("com/ibm/icu/impl/data/icudt51b", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    }
    
    public static final ULocale[] getAvailableULocales(final String baseName, final ClassLoader loader) {
        return getAvailEntry(baseName, loader).getULocaleList();
    }
    
    public static final ULocale[] getAvailableULocales() {
        return getAvailableULocales("com/ibm/icu/impl/data/icudt51b", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    }
    
    public static final Locale[] getAvailableLocales(final String baseName, final ClassLoader loader) {
        return getAvailEntry(baseName, loader).getLocaleList();
    }
    
    public static final Locale[] getAvailableLocales() {
        return getAvailEntry("com/ibm/icu/impl/data/icudt51b", ICUResourceBundle.ICU_DATA_CLASS_LOADER).getLocaleList();
    }
    
    public static final Locale[] getLocaleList(final ULocale[] ulocales) {
        final ArrayList<Locale> list = new ArrayList<Locale>(ulocales.length);
        final HashSet<Locale> uniqueSet = new HashSet<Locale>();
        for (int i = 0; i < ulocales.length; ++i) {
            final Locale loc = ulocales[i].toLocale();
            if (!uniqueSet.contains(loc)) {
                list.add(loc);
                uniqueSet.add(loc);
            }
        }
        return list.toArray(new Locale[list.size()]);
    }
    
    @Override
    public Locale getLocale() {
        return this.getULocale().toLocale();
    }
    
    private static final ULocale[] createULocaleList(final String baseName, final ClassLoader root) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.instantiateBundle(baseName, "res_index", root, true);
        bundle = (ICUResourceBundle)bundle.get("InstalledLocales");
        final int length = bundle.getSize();
        int i = 0;
        final ULocale[] locales = new ULocale[length];
        final UResourceBundleIterator iter = bundle.getIterator();
        iter.reset();
        while (iter.hasNext()) {
            final String locstr = iter.next().getKey();
            if (locstr.equals("root")) {
                locales[i++] = ULocale.ROOT;
            }
            else {
                locales[i++] = new ULocale(locstr);
            }
        }
        bundle = null;
        return locales;
    }
    
    private static final Locale[] createLocaleList(final String baseName, final ClassLoader loader) {
        final ULocale[] ulocales = getAvailEntry(baseName, loader).getULocaleList();
        return getLocaleList(ulocales);
    }
    
    private static final String[] createLocaleNameArray(final String baseName, final ClassLoader root) {
        ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.instantiateBundle(baseName, "res_index", root, true);
        bundle = (ICUResourceBundle)bundle.get("InstalledLocales");
        final int length = bundle.getSize();
        int i = 0;
        final String[] locales = new String[length];
        final UResourceBundleIterator iter = bundle.getIterator();
        iter.reset();
        while (iter.hasNext()) {
            final String locstr = iter.next().getKey();
            if (locstr.equals("root")) {
                locales[i++] = ULocale.ROOT.toString();
            }
            else {
                locales[i++] = locstr;
            }
        }
        bundle = null;
        return locales;
    }
    
    private static final List<String> createFullLocaleNameArray(final String baseName, final ClassLoader root) {
        final List<String> list = AccessController.doPrivileged((PrivilegedAction<List<String>>)new PrivilegedAction<List<String>>() {
            public List<String> run() {
                final String bn = baseName.endsWith("/") ? baseName : (baseName + "/");
                List<String> resList = null;
                final String skipScan = ICUConfig.get("com.ibm.icu.impl.ICUResourceBundle.skipRuntimeLocaleResourceScan", "false");
                if (!skipScan.equalsIgnoreCase("true")) {
                    try {
                        final Enumeration<URL> urls = root.getResources(bn);
                        while (urls.hasMoreElements()) {
                            final URL url = urls.nextElement();
                            final URLHandler handler = URLHandler.get(url);
                            if (handler != null) {
                                final List<String> lst = new ArrayList<String>();
                                final URLHandler.URLVisitor v = new URLHandler.URLVisitor() {
                                    public void visit(final String s) {
                                        if (s.endsWith(".res")) {
                                            final String locstr = s.substring(0, s.length() - 4);
                                            if (locstr.contains("_") && !locstr.equals("res_index")) {
                                                lst.add(locstr);
                                            }
                                            else if (locstr.length() == 2 || locstr.length() == 3) {
                                                lst.add(locstr);
                                            }
                                            else if (locstr.equalsIgnoreCase("root")) {
                                                lst.add(ULocale.ROOT.toString());
                                            }
                                        }
                                    }
                                };
                                handler.guide(v, false);
                                if (resList == null) {
                                    resList = new ArrayList<String>(lst);
                                }
                                else {
                                    resList.addAll(lst);
                                }
                            }
                            else {
                                if (!ICUResourceBundle.DEBUG) {
                                    continue;
                                }
                                System.out.println("handler for " + url + " is null");
                            }
                        }
                    }
                    catch (IOException e) {
                        if (ICUResourceBundle.DEBUG) {
                            System.out.println("ouch: " + e.getMessage());
                        }
                        resList = null;
                    }
                }
                if (resList == null) {
                    try {
                        final InputStream s = root.getResourceAsStream(bn + "fullLocaleNames.lst");
                        if (s != null) {
                            resList = new ArrayList<String>();
                            final BufferedReader br = new BufferedReader(new InputStreamReader(s, "ASCII"));
                            String line;
                            while ((line = br.readLine()) != null) {
                                if (line.length() != 0 && !line.startsWith("#")) {
                                    if (line.equalsIgnoreCase("root")) {
                                        resList.add(ULocale.ROOT.toString());
                                    }
                                    else {
                                        resList.add(line);
                                    }
                                }
                            }
                            br.close();
                        }
                    }
                    catch (IOException ex) {}
                }
                return resList;
            }
        });
        return list;
    }
    
    private static Set<String> createFullLocaleNameSet(final String baseName, final ClassLoader loader) {
        final List<String> list = createFullLocaleNameArray(baseName, loader);
        if (list == null) {
            if (ICUResourceBundle.DEBUG) {
                System.out.println("createFullLocaleNameArray returned null");
            }
            Set<String> locNameSet = createLocaleNameSet(baseName, loader);
            final String rootLocaleID = ULocale.ROOT.toString();
            if (!locNameSet.contains(rootLocaleID)) {
                final Set<String> tmp = new HashSet<String>(locNameSet);
                tmp.add(rootLocaleID);
                locNameSet = Collections.unmodifiableSet((Set<? extends String>)tmp);
            }
            return locNameSet;
        }
        final Set<String> fullLocNameSet = new HashSet<String>();
        fullLocNameSet.addAll(list);
        return Collections.unmodifiableSet((Set<? extends String>)fullLocNameSet);
    }
    
    private static Set<String> createLocaleNameSet(final String baseName, final ClassLoader loader) {
        try {
            final String[] locales = createLocaleNameArray(baseName, loader);
            final HashSet<String> set = new HashSet<String>();
            set.addAll((Collection<?>)Arrays.asList(locales));
            return Collections.unmodifiableSet((Set<? extends String>)set);
        }
        catch (MissingResourceException e) {
            if (ICUResourceBundle.DEBUG) {
                System.out.println("couldn't find index for bundleName: " + baseName);
                Thread.dumpStack();
            }
            return Collections.emptySet();
        }
    }
    
    private static AvailEntry getAvailEntry(final String key, final ClassLoader loader) {
        return ICUResourceBundle.GET_AVAILABLE_CACHE.getInstance(key, loader);
    }
    
    protected static final ICUResourceBundle findResourceWithFallback(String path, final UResourceBundle actualBundle, UResourceBundle requested) {
        ICUResourceBundle sub = null;
        if (requested == null) {
            requested = actualBundle;
        }
        ICUResourceBundle base = (ICUResourceBundle)actualBundle;
        for (String basePath = (((ICUResourceBundle)actualBundle).resPath.length() > 0) ? ((ICUResourceBundle)actualBundle).resPath : ""; base != null; base = (ICUResourceBundle)base.getParent(), path = ((basePath.length() > 0) ? (basePath + "/" + path) : path), basePath = "") {
            if (path.indexOf(47) == -1) {
                sub = (ICUResourceBundle)base.handleGet(path, null, requested);
                if (sub != null) {
                    break;
                }
            }
            else {
                ICUResourceBundle currentBase = base;
                final StringTokenizer st = new StringTokenizer(path, "/");
                while (st.hasMoreTokens()) {
                    final String subKey = st.nextToken();
                    sub = findResourceWithFallback(subKey, currentBase, requested);
                    if (sub == null) {
                        break;
                    }
                    currentBase = sub;
                }
                if (sub != null) {
                    break;
                }
            }
        }
        if (sub != null) {
            sub.setLoadingStatus(((ICUResourceBundle)requested).getLocaleID());
        }
        return sub;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof ICUResourceBundle) {
            final ICUResourceBundle o = (ICUResourceBundle)other;
            if (this.getBaseName().equals(o.getBaseName()) && this.getLocaleID().equals(o.getLocaleID())) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }
    
    public static UResourceBundle getBundleInstance(final String baseName, final String localeID, final ClassLoader root, final boolean disableFallback) {
        final UResourceBundle b = instantiateBundle(baseName, localeID, root, disableFallback);
        if (b == null) {
            throw new MissingResourceException("Could not find the bundle " + baseName + "/" + localeID + ".res", "", "");
        }
        return b;
    }
    
    protected static synchronized UResourceBundle instantiateBundle(final String baseName, final String localeID, final ClassLoader root, final boolean disableFallback) {
        final ULocale defaultLocale = ULocale.getDefault();
        String localeName = localeID;
        if (localeName.indexOf(64) >= 0) {
            localeName = ULocale.getBaseName(localeID);
        }
        final String fullName = ICUResourceBundleReader.getFullName(baseName, localeName);
        ICUResourceBundle b = (ICUResourceBundle)UResourceBundle.loadFromCache(root, fullName, defaultLocale);
        final String rootLocale = (baseName.indexOf(46) == -1) ? "root" : "";
        final String defaultID = defaultLocale.getBaseName();
        if (localeName.equals("")) {
            localeName = rootLocale;
        }
        if (ICUResourceBundle.DEBUG) {
            System.out.println("Creating " + fullName + " currently b is " + b);
        }
        if (b == null) {
            b = createBundle(baseName, localeName, root);
            if (ICUResourceBundle.DEBUG) {
                System.out.println("The bundle created is: " + b + " and disableFallback=" + disableFallback + " and bundle.getNoFallback=" + (b != null && b.getNoFallback()));
            }
            if (disableFallback || (b != null && b.getNoFallback())) {
                return UResourceBundle.addToCache(root, fullName, defaultLocale, b);
            }
            if (b == null) {
                final int i = localeName.lastIndexOf(95);
                if (i != -1) {
                    final String temp = localeName.substring(0, i);
                    b = (ICUResourceBundle)instantiateBundle(baseName, temp, root, disableFallback);
                    if (b != null && b.getULocale().getName().equals(temp)) {
                        b.setLoadingStatus(1);
                    }
                }
                else if (defaultID.indexOf(localeName) == -1) {
                    b = (ICUResourceBundle)instantiateBundle(baseName, defaultID, root, disableFallback);
                    if (b != null) {
                        b.setLoadingStatus(3);
                    }
                }
                else if (rootLocale.length() != 0) {
                    b = createBundle(baseName, rootLocale, root);
                    if (b != null) {
                        b.setLoadingStatus(2);
                    }
                }
            }
            else {
                UResourceBundle parent = null;
                localeName = b.getLocaleID();
                final int j = localeName.lastIndexOf(95);
                b = (ICUResourceBundle)UResourceBundle.addToCache(root, fullName, defaultLocale, b);
                if (b.getTableResource("%%Parent") != -1) {
                    final String parentLocaleName = b.getString("%%Parent");
                    parent = instantiateBundle(baseName, parentLocaleName, root, disableFallback);
                }
                else if (j != -1) {
                    parent = instantiateBundle(baseName, localeName.substring(0, j), root, disableFallback);
                }
                else if (!localeName.equals(rootLocale)) {
                    parent = instantiateBundle(baseName, rootLocale, root, true);
                }
                if (!b.equals(parent)) {
                    b.setParent(parent);
                }
            }
        }
        return b;
    }
    
    UResourceBundle get(final String aKey, final HashMap<String, String> table, final UResourceBundle requested) {
        ICUResourceBundle obj = (ICUResourceBundle)this.handleGet(aKey, table, requested);
        if (obj == null) {
            obj = (ICUResourceBundle)this.getParent();
            if (obj != null) {
                obj = (ICUResourceBundle)obj.get(aKey, table, requested);
            }
            if (obj == null) {
                final String fullName = ICUResourceBundleReader.getFullName(this.getBaseName(), this.getLocaleID());
                throw new MissingResourceException("Can't find resource for bundle " + fullName + ", key " + aKey, this.getClass().getName(), aKey);
            }
        }
        obj.setLoadingStatus(((ICUResourceBundle)requested).getLocaleID());
        return obj;
    }
    
    public static ICUResourceBundle createBundle(final String baseName, final String localeID, final ClassLoader root) {
        final ICUResourceBundleReader reader = ICUResourceBundleReader.getReader(baseName, localeID, root);
        if (reader == null) {
            return null;
        }
        return getBundle(reader, baseName, localeID, root);
    }
    
    @Override
    protected String getLocaleID() {
        return this.localeID;
    }
    
    @Override
    protected String getBaseName() {
        return this.baseName;
    }
    
    @Override
    public ULocale getULocale() {
        return this.ulocale;
    }
    
    public UResourceBundle getParent() {
        return (UResourceBundle)this.parent;
    }
    
    @Override
    protected void setParent(final ResourceBundle parent) {
        this.parent = parent;
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
    
    @Override
    public int getType() {
        return ICUResourceBundle.gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(this.resource)];
    }
    
    private boolean getNoFallback() {
        return this.reader.getNoFallback();
    }
    
    private static ICUResourceBundle getBundle(final ICUResourceBundleReader reader, final String baseName, final String localeID, final ClassLoader loader) {
        final int rootRes = reader.getRootResource();
        if (ICUResourceBundle.gPublicTypes[ICUResourceBundleReader.RES_GET_TYPE(rootRes)] != 2) {
            throw new IllegalStateException("Invalid format error");
        }
        final ICUResourceBundleImpl bundle = new ICUResourceBundleImpl.ResourceTable(reader, null, "", rootRes, null);
        bundle.baseName = baseName;
        bundle.localeID = localeID;
        bundle.ulocale = new ULocale(localeID);
        bundle.loader = loader;
        final UResourceBundle alias = bundle.handleGetImpl("%%ALIAS", null, bundle, null, null);
        if (alias != null) {
            return (ICUResourceBundle)UResourceBundle.getBundleInstance(baseName, alias.getString());
        }
        return bundle;
    }
    
    protected ICUResourceBundle(final ICUResourceBundleReader reader, final String key, final String resPath, final int resource, final ICUResourceBundle container) {
        this.loadingStatus = -1;
        this.reader = reader;
        this.key = key;
        this.resPath = resPath;
        this.resource = resource;
        if (container != null) {
            this.baseName = container.baseName;
            this.localeID = container.localeID;
            this.ulocale = container.ulocale;
            this.loader = container.loader;
            this.parent = container.parent;
        }
    }
    
    private String getAliasValue(final int res) {
        final String result = this.reader.getAlias(res);
        return (result != null) ? result : "";
    }
    
    protected ICUResourceBundle findResource(final String key, final String resPath, final int _resource, HashMap<String, String> table, final UResourceBundle requested) {
        ClassLoader loaderToUse = this.loader;
        String locale = null;
        String keyPath = null;
        final String rpath = this.getAliasValue(_resource);
        if (table == null) {
            table = new HashMap<String, String>();
        }
        if (table.get(rpath) != null) {
            throw new IllegalArgumentException("Circular references in the resource bundles");
        }
        table.put(rpath, "");
        String bundleName;
        if (rpath.indexOf(47) == 0) {
            final int i = rpath.indexOf(47, 1);
            final int j = rpath.indexOf(47, i + 1);
            bundleName = rpath.substring(1, i);
            if (j < 0) {
                locale = rpath.substring(i + 1);
                keyPath = resPath;
            }
            else {
                locale = rpath.substring(i + 1, j);
                keyPath = rpath.substring(j + 1, rpath.length());
            }
            if (bundleName.equals("ICUDATA")) {
                bundleName = "com/ibm/icu/impl/data/icudt51b";
                loaderToUse = ICUResourceBundle.ICU_DATA_CLASS_LOADER;
            }
            else if (bundleName.indexOf("ICUDATA") > -1) {
                final int idx = bundleName.indexOf(45);
                if (idx > -1) {
                    bundleName = "com/ibm/icu/impl/data/icudt51b/" + bundleName.substring(idx + 1, bundleName.length());
                    loaderToUse = ICUResourceBundle.ICU_DATA_CLASS_LOADER;
                }
            }
        }
        else {
            final int i = rpath.indexOf(47);
            if (i != -1) {
                locale = rpath.substring(0, i);
                keyPath = rpath.substring(i + 1);
            }
            else {
                locale = rpath;
                keyPath = resPath;
            }
            bundleName = this.baseName;
        }
        ICUResourceBundle bundle = null;
        ICUResourceBundle sub = null;
        if (bundleName.equals("LOCALE")) {
            bundleName = this.baseName;
            keyPath = rpath.substring("LOCALE".length() + 2, rpath.length());
            locale = ((ICUResourceBundle)requested).getLocaleID();
            bundle = (ICUResourceBundle)getBundleInstance(bundleName, locale, loaderToUse, false);
            if (bundle != null) {
                sub = findResourceWithFallback(keyPath, bundle, null);
            }
        }
        else {
            if (locale == null) {
                bundle = (ICUResourceBundle)getBundleInstance(bundleName, "", loaderToUse, false);
            }
            else {
                bundle = (ICUResourceBundle)getBundleInstance(bundleName, locale, loaderToUse, false);
            }
            final StringTokenizer st = new StringTokenizer(keyPath, "/");
            ICUResourceBundle current = bundle;
            while (st.hasMoreTokens()) {
                final String subKey = st.nextToken();
                sub = (ICUResourceBundle)current.get(subKey, table, requested);
                if (sub == null) {
                    break;
                }
                current = sub;
            }
        }
        if (sub == null) {
            throw new MissingResourceException(this.localeID, this.baseName, key);
        }
        return sub;
    }
    
    protected void createLookupCache() {
        this.lookup = new SimpleCache<Object, UResourceBundle>(1, Math.max(this.getSize() * 2, 64));
    }
    
    @Override
    protected UResourceBundle handleGet(final String resKey, final HashMap<String, String> table, final UResourceBundle requested) {
        UResourceBundle res = null;
        if (this.lookup != null) {
            res = this.lookup.get(resKey);
        }
        if (res == null) {
            final int[] index = { 0 };
            final boolean[] alias = { false };
            res = this.handleGetImpl(resKey, table, requested, index, alias);
            if (res != null && this.lookup != null && !alias[0]) {
                this.lookup.put(resKey, res);
                this.lookup.put(index[0], res);
            }
        }
        return res;
    }
    
    @Override
    protected UResourceBundle handleGet(final int index, final HashMap<String, String> table, final UResourceBundle requested) {
        UResourceBundle res = null;
        Integer indexKey = null;
        if (this.lookup != null) {
            indexKey = index;
            res = this.lookup.get(indexKey);
        }
        if (res == null) {
            final boolean[] alias = { false };
            res = this.handleGetImpl(index, table, requested, alias);
            if (res != null && this.lookup != null && !alias[0]) {
                this.lookup.put(res.getKey(), res);
                this.lookup.put(indexKey, res);
            }
        }
        return res;
    }
    
    protected UResourceBundle handleGetImpl(final String resKey, final HashMap<String, String> table, final UResourceBundle requested, final int[] index, final boolean[] isAlias) {
        return null;
    }
    
    protected UResourceBundle handleGetImpl(final int index, final HashMap<String, String> table, final UResourceBundle requested, final boolean[] isAlias) {
        return null;
    }
    
    @Deprecated
    protected int getTableResource(final String resKey) {
        return -1;
    }
    
    protected int getTableResource(final int index) {
        return -1;
    }
    
    @Deprecated
    public boolean isAlias(final int index) {
        return ICUResourceBundleReader.RES_GET_TYPE(this.getTableResource(index)) == 3;
    }
    
    @Deprecated
    public boolean isAlias() {
        return ICUResourceBundleReader.RES_GET_TYPE(this.resource) == 3;
    }
    
    @Deprecated
    public boolean isAlias(final String k) {
        return ICUResourceBundleReader.RES_GET_TYPE(this.getTableResource(k)) == 3;
    }
    
    @Deprecated
    public String getAliasPath(final int index) {
        return this.getAliasValue(this.getTableResource(index));
    }
    
    @Deprecated
    public String getAliasPath() {
        return this.getAliasValue(this.resource);
    }
    
    @Deprecated
    public String getAliasPath(final String k) {
        return this.getAliasValue(this.getTableResource(k));
    }
    
    protected String getKey(final int index) {
        return null;
    }
    
    @Deprecated
    public Enumeration<String> getKeysSafe() {
        if (!ICUResourceBundleReader.URES_IS_TABLE(this.resource)) {
            return this.getKeys();
        }
        final List<String> v = new ArrayList<String>();
        for (int size = this.getSize(), index = 0; index < size; ++index) {
            final String curKey = this.getKey(index);
            v.add(curKey);
        }
        return Collections.enumeration(v);
    }
    
    @Override
    protected Enumeration<String> handleGetKeys() {
        return Collections.enumeration(this.handleKeySet());
    }
    
    @Override
    protected boolean isTopLevelResource() {
        return this.resPath.length() == 0;
    }
    
    static {
        ClassLoader loader = ICUData.class.getClassLoader();
        if (loader == null) {
            loader = Utility.getFallbackClassLoader();
        }
        ICU_DATA_CLASS_LOADER = loader;
        DEBUG = ICUDebug.enabled("localedata");
        ICUResourceBundle.GET_AVAILABLE_CACHE = new SoftCache<String, AvailEntry, ClassLoader>() {
            @Override
            protected AvailEntry createInstance(final String key, final ClassLoader loader) {
                return new AvailEntry(key, loader);
            }
        };
        gPublicTypes = new int[] { 0, 1, 2, 3, 2, 2, 0, 7, 8, 8, -1, -1, -1, -1, 14, -1 };
    }
    
    private static final class AvailEntry
    {
        private String prefix;
        private ClassLoader loader;
        private volatile ULocale[] ulocales;
        private volatile Locale[] locales;
        private volatile Set<String> nameSet;
        private volatile Set<String> fullNameSet;
        
        AvailEntry(final String prefix, final ClassLoader loader) {
            this.prefix = prefix;
            this.loader = loader;
        }
        
        ULocale[] getULocaleList() {
            if (this.ulocales == null) {
                synchronized (this) {
                    if (this.ulocales == null) {
                        this.ulocales = createULocaleList(this.prefix, this.loader);
                    }
                }
            }
            return this.ulocales;
        }
        
        Locale[] getLocaleList() {
            if (this.locales == null) {
                synchronized (this) {
                    if (this.locales == null) {
                        this.locales = createLocaleList(this.prefix, this.loader);
                    }
                }
            }
            return this.locales;
        }
        
        Set<String> getLocaleNameSet() {
            if (this.nameSet == null) {
                synchronized (this) {
                    if (this.nameSet == null) {
                        this.nameSet = createLocaleNameSet(this.prefix, this.loader);
                    }
                }
            }
            return this.nameSet;
        }
        
        Set<String> getFullLocaleNameSet() {
            if (this.fullNameSet == null) {
                synchronized (this) {
                    if (this.fullNameSet == null) {
                        this.fullNameSet = createFullLocaleNameSet(this.prefix, this.loader);
                    }
                }
            }
            return this.fullNameSet;
        }
    }
}
