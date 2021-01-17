// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.ULocale;

public class ICUResourceTableAccess
{
    public static String getTableString(final String path, final ULocale locale, final String tableName, final String itemName) {
        final ICUResourceBundle bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(path, locale.getBaseName());
        return getTableString(bundle, tableName, null, itemName);
    }
    
    public static String getTableString(ICUResourceBundle bundle, final String tableName, final String subtableName, final String item) {
        String result = null;
        try {
            while (!"currency".equals(subtableName)) {
                final ICUResourceBundle table = lookup(bundle, tableName);
                if (table == null) {
                    return item;
                }
                ICUResourceBundle stable = table;
                if (subtableName != null) {
                    stable = lookup(table, subtableName);
                }
                if (stable != null) {
                    final ICUResourceBundle sbundle = lookup(stable, item);
                    if (sbundle != null) {
                        result = sbundle.getString();
                        return (result != null && result.length() > 0) ? result : item;
                    }
                }
                if (subtableName == null) {
                    String currentName = null;
                    if (tableName.equals("Countries")) {
                        currentName = LocaleIDs.getCurrentCountryID(item);
                    }
                    else if (tableName.equals("Languages")) {
                        currentName = LocaleIDs.getCurrentLanguageID(item);
                    }
                    final ICUResourceBundle sbundle2 = lookup(table, currentName);
                    if (sbundle2 != null) {
                        result = sbundle2.getString();
                        return (result != null && result.length() > 0) ? result : item;
                    }
                }
                final ICUResourceBundle fbundle = lookup(table, "Fallback");
                if (fbundle == null) {
                    return item;
                }
                String fallbackLocale = fbundle.getString();
                if (fallbackLocale.length() == 0) {
                    fallbackLocale = "root";
                }
                if (fallbackLocale.equals(table.getULocale().getName())) {
                    return item;
                }
                bundle = (ICUResourceBundle)UResourceBundle.getBundleInstance(bundle.getBaseName(), fallbackLocale);
            }
            ICUResourceBundle table = bundle.getWithFallback("Currencies");
            table = table.getWithFallback(item);
            return table.getString(1);
        }
        catch (Exception ex) {}
        return (result != null && result.length() > 0) ? result : item;
    }
    
    private static ICUResourceBundle lookup(final ICUResourceBundle bundle, final String resName) {
        return ICUResourceBundle.findResourceWithFallback(resName, bundle, null);
    }
}
