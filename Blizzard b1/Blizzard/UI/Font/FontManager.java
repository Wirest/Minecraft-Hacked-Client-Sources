/*
 * Decompiled with CFR 0_122.
 */
package Blizzard.UI.Font;

import java.awt.Font;

import Blizzard.UI.Font.MinecraftFontRenderer;

public class FontManager {
    public MinecraftFontRenderer hud = null;
    public MinecraftFontRenderer arraylist = null;
    public MinecraftFontRenderer mainMenu = null;
    public MinecraftFontRenderer chat = null;
    private static String fontName = "Comfortaa";

    public void loadFonts() {
        this.hud = new MinecraftFontRenderer(new Font(fontName, 0, 22), true, true);
        this.arraylist = new MinecraftFontRenderer(new Font(fontName, 0, 18), true, true);
        this.mainMenu = new MinecraftFontRenderer(new Font(fontName, 0, 50), true, true);
        this.chat = new MinecraftFontRenderer(new Font("Verdana", 0, 17), true, true);
    }

    public static String getFontName() {
        return fontName;
    }

    public static void setFontName(String fontName) {
        FontManager.fontName = fontName;
    }
}

