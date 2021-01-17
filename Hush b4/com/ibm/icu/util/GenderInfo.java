// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.MissingResourceException;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.impl.ICUCache;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.Locale;

public class GenderInfo
{
    private final ListGenderStyle style;
    private static GenderInfo neutral;
    private static Cache genderInfoCache;
    
    public static GenderInfo getInstance(final ULocale uLocale) {
        return GenderInfo.genderInfoCache.get(uLocale);
    }
    
    public static GenderInfo getInstance(final Locale locale) {
        return getInstance(ULocale.forLocale(locale));
    }
    
    public Gender getListGender(final Gender... genders) {
        return this.getListGender(Arrays.asList(genders));
    }
    
    public Gender getListGender(final List<Gender> genders) {
        if (genders.size() == 0) {
            return Gender.OTHER;
        }
        if (genders.size() == 1) {
            return genders.get(0);
        }
        switch (this.style) {
            case NEUTRAL: {
                return Gender.OTHER;
            }
            case MIXED_NEUTRAL: {
                boolean hasFemale = false;
                boolean hasMale = false;
                for (final Gender gender : genders) {
                    switch (gender) {
                        case FEMALE: {
                            if (hasMale) {
                                return Gender.OTHER;
                            }
                            hasFemale = true;
                            continue;
                        }
                        case MALE: {
                            if (hasFemale) {
                                return Gender.OTHER;
                            }
                            hasMale = true;
                            continue;
                        }
                        case OTHER: {
                            return Gender.OTHER;
                        }
                    }
                }
                return hasMale ? Gender.MALE : Gender.FEMALE;
            }
            case MALE_TAINTS: {
                for (final Gender gender : genders) {
                    if (gender != Gender.FEMALE) {
                        return Gender.MALE;
                    }
                }
                return Gender.FEMALE;
            }
            default: {
                return Gender.OTHER;
            }
        }
    }
    
    public GenderInfo(final ListGenderStyle genderStyle) {
        this.style = genderStyle;
    }
    
    static {
        GenderInfo.neutral = new GenderInfo(ListGenderStyle.NEUTRAL);
        GenderInfo.genderInfoCache = new Cache();
    }
    
    public enum Gender
    {
        MALE, 
        FEMALE, 
        OTHER;
    }
    
    public enum ListGenderStyle
    {
        NEUTRAL, 
        MIXED_NEUTRAL, 
        MALE_TAINTS;
        
        private static Map<String, ListGenderStyle> fromNameMap;
        
        public static ListGenderStyle fromName(final String name) {
            final ListGenderStyle result = ListGenderStyle.fromNameMap.get(name);
            if (result == null) {
                throw new IllegalArgumentException("Unknown gender style name: " + name);
            }
            return result;
        }
        
        static {
            (ListGenderStyle.fromNameMap = new HashMap<String, ListGenderStyle>(3)).put("neutral", ListGenderStyle.NEUTRAL);
            ListGenderStyle.fromNameMap.put("maleTaints", ListGenderStyle.MALE_TAINTS);
            ListGenderStyle.fromNameMap.put("mixedNeutral", ListGenderStyle.MIXED_NEUTRAL);
        }
    }
    
    private static class Cache
    {
        private final ICUCache<ULocale, GenderInfo> cache;
        
        private Cache() {
            this.cache = new SimpleCache<ULocale, GenderInfo>();
        }
        
        public GenderInfo get(final ULocale locale) {
            GenderInfo result = this.cache.get(locale);
            if (result == null) {
                result = load(locale);
                if (result == null) {
                    final ULocale fallback = locale.getFallback();
                    result = ((fallback == null) ? GenderInfo.neutral : this.get(fallback));
                }
                this.cache.put(locale, result);
            }
            return result;
        }
        
        private static GenderInfo load(final ULocale ulocale) {
            final UResourceBundle rb = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "genderList", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
            final UResourceBundle genderList = rb.get("genderList");
            try {
                return new GenderInfo(ListGenderStyle.fromName(genderList.getString(ulocale.toString())));
            }
            catch (MissingResourceException mre) {
                return null;
            }
        }
    }
}
