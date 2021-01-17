// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.util.UResourceBundle;
import java.util.Collections;
import java.util.Map;
import java.util.Iterator;
import java.util.Set;
import java.util.Locale;
import com.ibm.icu.util.ULocale;

public class ICULocaleService extends ICUService
{
    private ULocale fallbackLocale;
    private String fallbackLocaleName;
    
    public ICULocaleService() {
    }
    
    public ICULocaleService(final String name) {
        super(name);
    }
    
    public Object get(final ULocale locale) {
        return this.get(locale, -1, null);
    }
    
    public Object get(final ULocale locale, final int kind) {
        return this.get(locale, kind, null);
    }
    
    public Object get(final ULocale locale, final ULocale[] actualReturn) {
        return this.get(locale, -1, actualReturn);
    }
    
    public Object get(final ULocale locale, final int kind, final ULocale[] actualReturn) {
        final Key key = this.createKey(locale, kind);
        if (actualReturn == null) {
            return this.getKey(key);
        }
        final String[] temp = { null };
        final Object result = this.getKey(key, temp);
        if (result != null) {
            final int n = temp[0].indexOf("/");
            if (n >= 0) {
                temp[0] = temp[0].substring(n + 1);
            }
            actualReturn[0] = new ULocale(temp[0]);
        }
        return result;
    }
    
    public Factory registerObject(final Object obj, final ULocale locale) {
        return this.registerObject(obj, locale, -1, true);
    }
    
    public Factory registerObject(final Object obj, final ULocale locale, final boolean visible) {
        return this.registerObject(obj, locale, -1, visible);
    }
    
    public Factory registerObject(final Object obj, final ULocale locale, final int kind) {
        return this.registerObject(obj, locale, kind, true);
    }
    
    public Factory registerObject(final Object obj, final ULocale locale, final int kind, final boolean visible) {
        final Factory factory = new SimpleLocaleKeyFactory(obj, locale, kind, visible);
        return this.registerFactory(factory);
    }
    
    public Locale[] getAvailableLocales() {
        final Set<String> visIDs = this.getVisibleIDs();
        final Locale[] locales = new Locale[visIDs.size()];
        int n = 0;
        for (final String id : visIDs) {
            final Locale loc = LocaleUtility.getLocaleFromName(id);
            locales[n++] = loc;
        }
        return locales;
    }
    
    public ULocale[] getAvailableULocales() {
        final Set<String> visIDs = this.getVisibleIDs();
        final ULocale[] locales = new ULocale[visIDs.size()];
        int n = 0;
        for (final String id : visIDs) {
            locales[n++] = new ULocale(id);
        }
        return locales;
    }
    
    public String validateFallbackLocale() {
        final ULocale loc = ULocale.getDefault();
        if (loc != this.fallbackLocale) {
            synchronized (this) {
                if (loc != this.fallbackLocale) {
                    this.fallbackLocale = loc;
                    this.fallbackLocaleName = loc.getBaseName();
                    this.clearServiceCache();
                }
            }
        }
        return this.fallbackLocaleName;
    }
    
    @Override
    public Key createKey(final String id) {
        return LocaleKey.createWithCanonicalFallback(id, this.validateFallbackLocale());
    }
    
    public Key createKey(final String id, final int kind) {
        return LocaleKey.createWithCanonicalFallback(id, this.validateFallbackLocale(), kind);
    }
    
    public Key createKey(final ULocale l, final int kind) {
        return LocaleKey.createWithCanonical(l, this.validateFallbackLocale(), kind);
    }
    
    public static class LocaleKey extends Key
    {
        private int kind;
        private int varstart;
        private String primaryID;
        private String fallbackID;
        private String currentID;
        public static final int KIND_ANY = -1;
        
        public static LocaleKey createWithCanonicalFallback(final String primaryID, final String canonicalFallbackID) {
            return createWithCanonicalFallback(primaryID, canonicalFallbackID, -1);
        }
        
        public static LocaleKey createWithCanonicalFallback(final String primaryID, final String canonicalFallbackID, final int kind) {
            if (primaryID == null) {
                return null;
            }
            final String canonicalPrimaryID = ULocale.getName(primaryID);
            return new LocaleKey(primaryID, canonicalPrimaryID, canonicalFallbackID, kind);
        }
        
        public static LocaleKey createWithCanonical(final ULocale locale, final String canonicalFallbackID, final int kind) {
            if (locale == null) {
                return null;
            }
            final String canonicalPrimaryID = locale.getName();
            return new LocaleKey(canonicalPrimaryID, canonicalPrimaryID, canonicalFallbackID, kind);
        }
        
        protected LocaleKey(final String primaryID, final String canonicalPrimaryID, final String canonicalFallbackID, final int kind) {
            super(primaryID);
            this.kind = kind;
            if (canonicalPrimaryID == null || canonicalPrimaryID.equalsIgnoreCase("root")) {
                this.primaryID = "";
                this.fallbackID = null;
            }
            else {
                final int idx = canonicalPrimaryID.indexOf(64);
                if (idx == 4 && canonicalPrimaryID.regionMatches(true, 0, "root", 0, 4)) {
                    this.primaryID = canonicalPrimaryID.substring(4);
                    this.varstart = 0;
                    this.fallbackID = null;
                }
                else {
                    this.primaryID = canonicalPrimaryID;
                    this.varstart = idx;
                    if (canonicalFallbackID == null || this.primaryID.equals(canonicalFallbackID)) {
                        this.fallbackID = "";
                    }
                    else {
                        this.fallbackID = canonicalFallbackID;
                    }
                }
            }
            this.currentID = ((this.varstart == -1) ? this.primaryID : this.primaryID.substring(0, this.varstart));
        }
        
        public String prefix() {
            return (this.kind == -1) ? null : Integer.toString(this.kind());
        }
        
        public int kind() {
            return this.kind;
        }
        
        @Override
        public String canonicalID() {
            return this.primaryID;
        }
        
        @Override
        public String currentID() {
            return this.currentID;
        }
        
        @Override
        public String currentDescriptor() {
            String result = this.currentID();
            if (result != null) {
                final StringBuilder buf = new StringBuilder();
                if (this.kind != -1) {
                    buf.append(this.prefix());
                }
                buf.append('/');
                buf.append(result);
                if (this.varstart != -1) {
                    buf.append(this.primaryID.substring(this.varstart, this.primaryID.length()));
                }
                result = buf.toString();
            }
            return result;
        }
        
        public ULocale canonicalLocale() {
            return new ULocale(this.primaryID);
        }
        
        public ULocale currentLocale() {
            if (this.varstart == -1) {
                return new ULocale(this.currentID);
            }
            return new ULocale(this.currentID + this.primaryID.substring(this.varstart));
        }
        
        @Override
        public boolean fallback() {
            int x = this.currentID.lastIndexOf(95);
            if (x != -1) {
                while (--x >= 0 && this.currentID.charAt(x) == '_') {}
                this.currentID = this.currentID.substring(0, x + 1);
                return true;
            }
            if (this.fallbackID != null) {
                this.currentID = this.fallbackID;
                if (this.fallbackID.length() == 0) {
                    this.fallbackID = null;
                }
                else {
                    this.fallbackID = "";
                }
                return true;
            }
            this.currentID = null;
            return false;
        }
        
        @Override
        public boolean isFallbackOf(final String id) {
            return LocaleUtility.isFallbackOf(this.canonicalID(), id);
        }
    }
    
    public abstract static class LocaleKeyFactory implements Factory
    {
        protected final String name;
        protected final boolean visible;
        public static final boolean VISIBLE = true;
        public static final boolean INVISIBLE = false;
        
        protected LocaleKeyFactory(final boolean visible) {
            this.visible = visible;
            this.name = null;
        }
        
        protected LocaleKeyFactory(final boolean visible, final String name) {
            this.visible = visible;
            this.name = name;
        }
        
        public Object create(final Key key, final ICUService service) {
            if (this.handlesKey(key)) {
                final LocaleKey lkey = (LocaleKey)key;
                final int kind = lkey.kind();
                final ULocale uloc = lkey.currentLocale();
                return this.handleCreate(uloc, kind, service);
            }
            return null;
        }
        
        protected boolean handlesKey(final Key key) {
            if (key != null) {
                final String id = key.currentID();
                final Set<String> supported = this.getSupportedIDs();
                return supported.contains(id);
            }
            return false;
        }
        
        public void updateVisibleIDs(final Map<String, Factory> result) {
            final Set<String> cache = this.getSupportedIDs();
            for (final String id : cache) {
                if (this.visible) {
                    result.put(id, this);
                }
                else {
                    result.remove(id);
                }
            }
        }
        
        public String getDisplayName(final String id, final ULocale locale) {
            if (locale == null) {
                return id;
            }
            final ULocale loc = new ULocale(id);
            return loc.getDisplayName(locale);
        }
        
        protected Object handleCreate(final ULocale loc, final int kind, final ICUService service) {
            return null;
        }
        
        protected boolean isSupportedID(final String id) {
            return this.getSupportedIDs().contains(id);
        }
        
        protected Set<String> getSupportedIDs() {
            return Collections.emptySet();
        }
        
        @Override
        public String toString() {
            final StringBuilder buf = new StringBuilder(super.toString());
            if (this.name != null) {
                buf.append(", name: ");
                buf.append(this.name);
            }
            buf.append(", visible: ");
            buf.append(this.visible);
            return buf.toString();
        }
    }
    
    public static class SimpleLocaleKeyFactory extends LocaleKeyFactory
    {
        private final Object obj;
        private final String id;
        private final int kind;
        
        public SimpleLocaleKeyFactory(final Object obj, final ULocale locale, final int kind, final boolean visible) {
            this(obj, locale, kind, visible, null);
        }
        
        public SimpleLocaleKeyFactory(final Object obj, final ULocale locale, final int kind, final boolean visible, final String name) {
            super(visible, name);
            this.obj = obj;
            this.id = locale.getBaseName();
            this.kind = kind;
        }
        
        @Override
        public Object create(final Key key, final ICUService service) {
            if (!(key instanceof LocaleKey)) {
                return null;
            }
            final LocaleKey lkey = (LocaleKey)key;
            if (this.kind != -1 && this.kind != lkey.kind()) {
                return null;
            }
            if (!this.id.equals(lkey.currentID())) {
                return null;
            }
            return this.obj;
        }
        
        @Override
        protected boolean isSupportedID(final String idToCheck) {
            return this.id.equals(idToCheck);
        }
        
        @Override
        public void updateVisibleIDs(final Map<String, Factory> result) {
            if (this.visible) {
                result.put(this.id, this);
            }
            else {
                result.remove(this.id);
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder buf = new StringBuilder(super.toString());
            buf.append(", id: ");
            buf.append(this.id);
            buf.append(", kind: ");
            buf.append(this.kind);
            return buf.toString();
        }
    }
    
    public static class ICUResourceBundleFactory extends LocaleKeyFactory
    {
        protected final String bundleName;
        
        public ICUResourceBundleFactory() {
            this("com/ibm/icu/impl/data/icudt51b");
        }
        
        public ICUResourceBundleFactory(final String bundleName) {
            super(true);
            this.bundleName = bundleName;
        }
        
        @Override
        protected Set<String> getSupportedIDs() {
            return ICUResourceBundle.getFullLocaleNameSet(this.bundleName, this.loader());
        }
        
        @Override
        public void updateVisibleIDs(final Map<String, Factory> result) {
            final Set<String> visibleIDs = ICUResourceBundle.getAvailableLocaleNameSet(this.bundleName, this.loader());
            for (final String id : visibleIDs) {
                result.put(id, this);
            }
        }
        
        @Override
        protected Object handleCreate(final ULocale loc, final int kind, final ICUService service) {
            return UResourceBundle.getBundleInstance(this.bundleName, loc, this.loader());
        }
        
        protected ClassLoader loader() {
            ClassLoader cl = this.getClass().getClassLoader();
            if (cl == null) {
                cl = Utility.getFallbackClassLoader();
            }
            return cl;
        }
        
        @Override
        public String toString() {
            return super.toString() + ", bundle: " + this.bundleName;
        }
    }
}
