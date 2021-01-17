// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public class StatCollector
{
    private static StringTranslate localizedName;
    private static StringTranslate fallbackTranslator;
    
    static {
        StatCollector.localizedName = StringTranslate.getInstance();
        StatCollector.fallbackTranslator = new StringTranslate();
    }
    
    public static String translateToLocal(final String key) {
        return StatCollector.localizedName.translateKey(key);
    }
    
    public static String translateToLocalFormatted(final String key, final Object... format) {
        return StatCollector.localizedName.translateKeyFormat(key, format);
    }
    
    public static String translateToFallback(final String key) {
        return StatCollector.fallbackTranslator.translateKey(key);
    }
    
    public static boolean canTranslate(final String key) {
        return StatCollector.localizedName.isKeyTranslated(key);
    }
    
    public static long getLastTranslationUpdateTimeInMilliseconds() {
        return StatCollector.localizedName.getLastUpdateTimeInMilliseconds();
    }
}
