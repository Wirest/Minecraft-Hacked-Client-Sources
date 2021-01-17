// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.util.UResourceBundleIterator;
import java.util.ArrayList;
import java.util.MissingResourceException;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.ULocale;

public class CalendarData
{
    private ICUResourceBundle fBundle;
    private String fMainType;
    private String fFallbackType;
    
    public CalendarData(final ULocale loc, final String type) {
        this((ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", loc), type);
    }
    
    public CalendarData(final ICUResourceBundle b, final String type) {
        this.fBundle = b;
        if (type == null || type.equals("") || type.equals("gregorian")) {
            this.fMainType = "gregorian";
            this.fFallbackType = null;
        }
        else {
            this.fMainType = type;
            this.fFallbackType = "gregorian";
        }
    }
    
    public ICUResourceBundle get(final String key) {
        try {
            return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + key);
        }
        catch (MissingResourceException m) {
            if (this.fFallbackType != null) {
                return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + key);
            }
            throw m;
        }
    }
    
    public ICUResourceBundle get(final String key, final String subKey) {
        try {
            return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + key + "/format/" + subKey);
        }
        catch (MissingResourceException m) {
            if (this.fFallbackType != null) {
                return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + key + "/format/" + subKey);
            }
            throw m;
        }
    }
    
    public ICUResourceBundle get(final String key, final String contextKey, final String subKey) {
        try {
            return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + key + "/" + contextKey + "/" + subKey);
        }
        catch (MissingResourceException m) {
            if (this.fFallbackType != null) {
                return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + key + "/" + contextKey + "/" + subKey);
            }
            throw m;
        }
    }
    
    public ICUResourceBundle get(final String key, final String set, final String contextKey, final String subKey) {
        try {
            return this.fBundle.getWithFallback("calendar/" + this.fMainType + "/" + key + "/" + set + "/" + contextKey + "/" + subKey);
        }
        catch (MissingResourceException m) {
            if (this.fFallbackType != null) {
                return this.fBundle.getWithFallback("calendar/" + this.fFallbackType + "/" + key + "/" + set + "/" + contextKey + "/" + subKey);
            }
            throw m;
        }
    }
    
    public String[] getStringArray(final String key) {
        return this.get(key).getStringArray();
    }
    
    public String[] getStringArray(final String key, final String subKey) {
        return this.get(key, subKey).getStringArray();
    }
    
    public String[] getStringArray(final String key, final String contextKey, final String subKey) {
        return this.get(key, contextKey, subKey).getStringArray();
    }
    
    public String[] getEras(final String subkey) {
        final ICUResourceBundle bundle = this.get("eras/" + subkey);
        return bundle.getStringArray();
    }
    
    public String[] getDateTimePatterns() {
        final ICUResourceBundle bundle = this.get("DateTimePatterns");
        final ArrayList<String> list = new ArrayList<String>();
        final UResourceBundleIterator iter = bundle.getIterator();
        while (iter.hasNext()) {
            final UResourceBundle patResource = iter.next();
            final int resourceType = patResource.getType();
            switch (resourceType) {
                case 0: {
                    list.add(patResource.getString());
                    continue;
                }
                case 8: {
                    final String[] items = patResource.getStringArray();
                    list.add(items[0]);
                    continue;
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }
    
    public String[] getOverrides() {
        final ICUResourceBundle bundle = this.get("DateTimePatterns");
        final ArrayList<String> list = new ArrayList<String>();
        final UResourceBundleIterator iter = bundle.getIterator();
        while (iter.hasNext()) {
            final UResourceBundle patResource = iter.next();
            final int resourceType = patResource.getType();
            switch (resourceType) {
                case 0: {
                    list.add(null);
                    continue;
                }
                case 8: {
                    final String[] items = patResource.getStringArray();
                    list.add(items[1]);
                    continue;
                }
            }
        }
        return list.toArray(new String[list.size()]);
    }
    
    public ULocale getULocale() {
        return this.fBundle.getULocale();
    }
}
