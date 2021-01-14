package com.etb.client.utils.font;

import java.awt.Font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class Fonts {
    public static final MCFontRenderer verdana12 = new MCFontRenderer(new Font("Verdana", Font.PLAIN, 12), true, true);
    public static final MCFontRenderer verdana14 = new MCFontRenderer(new Font("Verdana", Font.PLAIN, 13), true, true);


    public static Font fontFromTTF(ResourceLocation fontLocation, float fontSize, int fontType) {
        Font output = null;
        try {
            output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(fontLocation).getInputStream());
            output = output.deriveFont(fontSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

}
