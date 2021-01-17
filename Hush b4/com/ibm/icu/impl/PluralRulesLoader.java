// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.text.ParseException;
import com.ibm.icu.util.UResourceBundle;
import java.util.MissingResourceException;
import java.util.Collections;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashMap;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.text.PluralRules;
import java.util.Map;

public class PluralRulesLoader
{
    private final Map<String, PluralRules> rulesIdToRules;
    private Map<String, String> localeIdToCardinalRulesId;
    private Map<String, String> localeIdToOrdinalRulesId;
    private Map<String, ULocale> rulesIdToEquivalentULocale;
    public static final PluralRulesLoader loader;
    
    private PluralRulesLoader() {
        this.rulesIdToRules = new HashMap<String, PluralRules>();
    }
    
    public ULocale[] getAvailableULocales() {
        final Set<String> keys = this.getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL).keySet();
        final ULocale[] locales = new ULocale[keys.size()];
        int n = 0;
        final Iterator<String> iter = keys.iterator();
        while (iter.hasNext()) {
            locales[n++] = ULocale.createCanonical(iter.next());
        }
        return locales;
    }
    
    public ULocale getFunctionalEquivalent(final ULocale locale, final boolean[] isAvailable) {
        if (isAvailable != null && isAvailable.length > 0) {
            final String localeId = ULocale.canonicalize(locale.getBaseName());
            final Map<String, String> idMap = this.getLocaleIdToRulesIdMap(PluralRules.PluralType.CARDINAL);
            isAvailable[0] = idMap.containsKey(localeId);
        }
        final String rulesId = this.getRulesIdForLocale(locale, PluralRules.PluralType.CARDINAL);
        if (rulesId == null || rulesId.trim().length() == 0) {
            return ULocale.ROOT;
        }
        final ULocale result = this.getRulesIdToEquivalentULocaleMap().get(rulesId);
        if (result == null) {
            return ULocale.ROOT;
        }
        return result;
    }
    
    private Map<String, String> getLocaleIdToRulesIdMap(final PluralRules.PluralType type) {
        this.checkBuildRulesIdMaps();
        return (type == PluralRules.PluralType.CARDINAL) ? this.localeIdToCardinalRulesId : this.localeIdToOrdinalRulesId;
    }
    
    private Map<String, ULocale> getRulesIdToEquivalentULocaleMap() {
        this.checkBuildRulesIdMaps();
        return this.rulesIdToEquivalentULocale;
    }
    
    private void checkBuildRulesIdMaps() {
        final boolean haveMap;
        synchronized (this) {
            haveMap = (this.localeIdToCardinalRulesId != null);
        }
        if (!haveMap) {
            Map<String, String> tempLocaleIdToCardinalRulesId;
            Map<String, ULocale> tempRulesIdToEquivalentULocale;
            Map<String, String> tempLocaleIdToOrdinalRulesId;
            try {
                final UResourceBundle pluralb = this.getPluralBundle();
                UResourceBundle localeb = pluralb.get("locales");
                tempLocaleIdToCardinalRulesId = new TreeMap<String, String>();
                tempRulesIdToEquivalentULocale = new HashMap<String, ULocale>();
                for (int i = 0; i < localeb.getSize(); ++i) {
                    final UResourceBundle b = localeb.get(i);
                    final String id = b.getKey();
                    final String value = b.getString().intern();
                    tempLocaleIdToCardinalRulesId.put(id, value);
                    if (!tempRulesIdToEquivalentULocale.containsKey(value)) {
                        tempRulesIdToEquivalentULocale.put(value, new ULocale(id));
                    }
                }
                localeb = pluralb.get("locales_ordinals");
                tempLocaleIdToOrdinalRulesId = new TreeMap<String, String>();
                for (int i = 0; i < localeb.getSize(); ++i) {
                    final UResourceBundle b = localeb.get(i);
                    final String id = b.getKey();
                    final String value = b.getString().intern();
                    tempLocaleIdToOrdinalRulesId.put(id, value);
                }
            }
            catch (MissingResourceException e) {
                tempLocaleIdToCardinalRulesId = Collections.emptyMap();
                tempLocaleIdToOrdinalRulesId = Collections.emptyMap();
                tempRulesIdToEquivalentULocale = Collections.emptyMap();
            }
            synchronized (this) {
                if (this.localeIdToCardinalRulesId == null) {
                    this.localeIdToCardinalRulesId = tempLocaleIdToCardinalRulesId;
                    this.localeIdToOrdinalRulesId = tempLocaleIdToOrdinalRulesId;
                    this.rulesIdToEquivalentULocale = tempRulesIdToEquivalentULocale;
                }
            }
        }
    }
    
    public String getRulesIdForLocale(final ULocale locale, final PluralRules.PluralType type) {
        final Map<String, String> idMap = this.getLocaleIdToRulesIdMap(type);
        String localeId = ULocale.canonicalize(locale.getBaseName());
        String rulesId = null;
        while (null == (rulesId = idMap.get(localeId))) {
            final int ix = localeId.lastIndexOf("_");
            if (ix == -1) {
                break;
            }
            localeId = localeId.substring(0, ix);
        }
        return rulesId;
    }
    
    public PluralRules getRulesForRulesId(final String rulesId) {
        PluralRules rules = null;
        final boolean hasRules;
        synchronized (this.rulesIdToRules) {
            hasRules = this.rulesIdToRules.containsKey(rulesId);
            if (hasRules) {
                rules = this.rulesIdToRules.get(rulesId);
            }
        }
        if (!hasRules) {
            try {
                final UResourceBundle pluralb = this.getPluralBundle();
                final UResourceBundle rulesb = pluralb.get("rules");
                final UResourceBundle setb = rulesb.get(rulesId);
                final StringBuilder sb = new StringBuilder();
                for (int i = 0; i < setb.getSize(); ++i) {
                    final UResourceBundle b = setb.get(i);
                    if (i > 0) {
                        sb.append("; ");
                    }
                    sb.append(b.getKey());
                    sb.append(": ");
                    sb.append(b.getString());
                }
                rules = PluralRules.parseDescription(sb.toString());
            }
            catch (ParseException e) {}
            catch (MissingResourceException ex) {}
            synchronized (this.rulesIdToRules) {
                if (this.rulesIdToRules.containsKey(rulesId)) {
                    rules = this.rulesIdToRules.get(rulesId);
                }
                else {
                    this.rulesIdToRules.put(rulesId, rules);
                }
            }
        }
        return rules;
    }
    
    public UResourceBundle getPluralBundle() throws MissingResourceException {
        return ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "plurals", ICUResourceBundle.ICU_DATA_CLASS_LOADER, true);
    }
    
    public PluralRules forLocale(final ULocale locale, final PluralRules.PluralType type) {
        final String rulesId = this.getRulesIdForLocale(locale, type);
        if (rulesId == null || rulesId.trim().length() == 0) {
            return PluralRules.DEFAULT;
        }
        PluralRules rules = this.getRulesForRulesId(rulesId);
        if (rules == null) {
            rules = PluralRules.DEFAULT;
        }
        return rules;
    }
    
    static {
        loader = new PluralRulesLoader();
    }
}
