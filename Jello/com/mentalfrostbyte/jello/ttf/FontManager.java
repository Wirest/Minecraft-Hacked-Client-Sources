package com.mentalfrostbyte.jello.ttf;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontManager
{
    private static Map<Integer, MinecraftFontRenderer> fontRendererMap = new HashMap<Integer, MinecraftFontRenderer>();
    
    public static MinecraftFontRenderer fr = FontManager.getFont(10, "Jello/jelloregular.ttf");
    
    public static MinecraftFontRenderer getFont(final int size, String s) {
        if (!FontManager.fontRendererMap.containsKey(size)) {
            final MinecraftFontRenderer fr = fontFromTTF(new ResourceLocation(s), 0, size);
            FontManager.fontRendererMap.put(size, fr);
            return fr;
        }
        return FontManager.fontRendererMap.get(size);
    }
    
   /* public static MinecraftFontRenderer findFont(final float width, final float height, final String s, final FontManager.FontType fontType) {
        MinecraftFontRenderer renderer = getFont(2, fontType);
        int fontSize = 3;
        for (int i = 0; i < 100; ++i) {
            final MinecraftFontRenderer testRenderer = getFont(fontSize, fontType);
            if (testRenderer.getStringWidth(s) > width) {
                break;
            }
            if (testRenderer.getStringHeight(s) > height) {
                break;
            }
            renderer = testRenderer;
            ++fontSize;
        }
        return renderer;
    }*/
    
    public static MinecraftFontRenderer fontFromTTF(final ResourceLocation fontLocation, final int fontType, final float fontSize) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final MinecraftFontRenderer fr = new MinecraftFontRenderer();
        fr.setFont(output, true);
        return fr;
    }
    


public enum FontType
{
    HELVETICA_NEUE_REGULAR("jelloregular"),
    HELVETICA_NEUE_MEDIUM("jellomedium");
    
    public String name;
    
    private FontType(String name) {
        this.name = name;
    }
    
}
}
