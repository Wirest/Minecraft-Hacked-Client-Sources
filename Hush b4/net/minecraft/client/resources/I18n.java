// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import java.util.Map;

public class I18n
{
    private static Locale i18nLocale;
    private static final String __OBFID = "CL_00001094";
    
    static void setLocale(final Locale i18nLocaleIn) {
        I18n.i18nLocale = i18nLocaleIn;
    }
    
    public static String format(final String translateKey, final Object... parameters) {
        return I18n.i18nLocale.formatMessage(translateKey, parameters);
    }
    
    public static Map getLocaleProperties() {
        return I18n.i18nLocale.properties;
    }
}
