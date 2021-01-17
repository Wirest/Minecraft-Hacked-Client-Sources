// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Locale;
import java.util.ResourceBundle;

public class OverlayBundle extends ResourceBundle
{
    private String[] baseNames;
    private Locale locale;
    private ResourceBundle[] bundles;
    
    @Deprecated
    public OverlayBundle(final String[] baseNames, final Locale locale) {
        this.baseNames = baseNames;
        this.locale = locale;
        this.bundles = new ResourceBundle[baseNames.length];
    }
    
    @Override
    @Deprecated
    protected Object handleGetObject(final String key) throws MissingResourceException {
        Object o = null;
        for (int i = 0; i < this.bundles.length; ++i) {
            this.load(i);
            try {
                o = this.bundles[i].getObject(key);
            }
            catch (MissingResourceException e) {
                if (i == this.bundles.length - 1) {
                    throw e;
                }
            }
            if (o != null) {
                break;
            }
        }
        return o;
    }
    
    @Override
    @Deprecated
    public Enumeration<String> getKeys() {
        final int i = this.bundles.length - 1;
        this.load(i);
        return this.bundles[i].getKeys();
    }
    
    private void load(final int i) throws MissingResourceException {
        if (this.bundles[i] == null) {
            boolean tryWildcard = false;
            try {
                this.bundles[i] = ResourceBundle.getBundle(this.baseNames[i], this.locale);
                if (this.bundles[i].getLocale().equals(this.locale)) {
                    return;
                }
                if (this.locale.getCountry().length() != 0 && i != this.bundles.length - 1) {
                    tryWildcard = true;
                }
            }
            catch (MissingResourceException e) {
                if (i == this.bundles.length - 1) {
                    throw e;
                }
                tryWildcard = true;
            }
            if (tryWildcard) {
                final Locale wildcard = new Locale("xx", this.locale.getCountry(), this.locale.getVariant());
                try {
                    this.bundles[i] = ResourceBundle.getBundle(this.baseNames[i], wildcard);
                }
                catch (MissingResourceException e2) {
                    if (this.bundles[i] == null) {
                        throw e2;
                    }
                }
            }
        }
    }
}
