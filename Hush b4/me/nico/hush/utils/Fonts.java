// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import java.io.InputStream;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.Font;

public class Fonts
{
    public static UnicodeFontRenderer smallHush2;
    public static UnicodeFontRenderer menuHush2;
    public static UnicodeFontRenderer watermarkHush2;
    public static UnicodeFontRenderer versionHush2;
    public static UnicodeFontRenderer arraylistHush2;
    public static UnicodeFontRenderer zodiacname;
    public static UnicodeFontRenderer zodiacother;
    public static UnicodeFontRenderer zodiacwatermark;
    public static UnicodeFontRenderer zodiacarraylist;
    
    public static void loadFonts() {
        final InputStream is2 = Fonts.class.getResourceAsStream("fonts/Raleway-Light.ttf");
        final InputStream is3 = Fonts.class.getResourceAsStream("fonts/jet.ttf");
        final InputStream is4 = Fonts.class.getResourceAsStream("fonts/arial.ttf");
        Font font2 = null;
        try {
            font2 = Font.createFont(0, is2);
        }
        catch (FontFormatException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        Font font3 = null;
        try {
            font3 = Font.createFont(0, is3);
        }
        catch (FontFormatException e3) {
            e3.printStackTrace();
        }
        catch (IOException e4) {
            e4.printStackTrace();
        }
        Font font4 = null;
        try {
            font4 = Font.createFont(0, is4);
        }
        catch (FontFormatException e5) {
            e5.printStackTrace();
        }
        catch (IOException e6) {
            e6.printStackTrace();
        }
        Fonts.arraylistHush2 = new UnicodeFontRenderer(font2.deriveFont(15.0f));
        Fonts.smallHush2 = new UnicodeFontRenderer(font2.deriveFont(23.0f));
        Fonts.versionHush2 = new UnicodeFontRenderer(font2.deriveFont(50.0f));
        Fonts.watermarkHush2 = new UnicodeFontRenderer(font2.deriveFont(55.0f));
        Fonts.menuHush2 = new UnicodeFontRenderer(font2.deriveFont(50.0f));
        Fonts.zodiacname = new UnicodeFontRenderer(font3.deriveFont(33.0f));
        Fonts.zodiacother = new UnicodeFontRenderer(font3.deriveFont(18.0f));
        Fonts.zodiacwatermark = new UnicodeFontRenderer(font4.deriveFont(50.0f));
        Fonts.zodiacarraylist = new UnicodeFontRenderer(font4.deriveFont(16.0f));
        if (Minecraft.getMinecraft().gameSettings.language != null) {
            Fonts.smallHush2.setUnicodeFlag(true);
            Fonts.menuHush2.setUnicodeFlag(true);
            Fonts.watermarkHush2.setUnicodeFlag(true);
            Fonts.versionHush2.setUnicodeFlag(true);
            Fonts.arraylistHush2.setUnicodeFlag(true);
            if (Minecraft.getMinecraft().gameSettings.language != null) {
                Fonts.zodiacname.setUnicodeFlag(true);
                Fonts.zodiacother.setUnicodeFlag(true);
                Fonts.zodiacwatermark.setUnicodeFlag(true);
                Fonts.zodiacarraylist.setUnicodeFlag(true);
            }
            Fonts.zodiacname.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.zodiacother.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.zodiacwatermark.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.zodiacarraylist.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.smallHush2.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.menuHush2.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.watermarkHush2.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.versionHush2.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
            Fonts.versionHush2.setBidiFlag(Minecraft.getMinecraft().mcLanguageManager.isCurrentLanguageBidirectional());
        }
    }
    
    public enum FontType
    {
        EMBOSS_BOTTOM("EMBOSS_BOTTOM", 0), 
        EMBOSS_TOP("EMBOSS_TOP", 1), 
        NORMAL("NORMAL", 2), 
        OUTLINE_THIN("OUTLINE_THIN", 3), 
        SHADOW_THICK("SHADOW_THICK", 4), 
        SHADOW_THIN("SHADOW_THIN", 5);
        
        private FontType(final String name, final int ordinal) {
        }
    }
}
