package com.mentalfrostbyte.jello.font;

import java.awt.Font;
import java.io.InputStream;

import com.mentalfrostbyte.jello.font.*;
import com.mentalfrostbyte.jello.util.FontUtil;


public class JelloFontRenderer {

    public static JelloFontRenderer createFontRenderer(Font font) {
        return new MinecraftFontRenderer(font, true, true);
    }

    public int drawString(String text, double d, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    
    public int drawPassword(String text, double d, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    
    public int drawNoBSString(String text, double d, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    
    public int drawSmoothString(String text, double d, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }

    public int drawStringWithShadow(String text, double d, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }

    public float drawNoBSCenteredString(String text, float x2, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    public float drawCenteredString(String text, float x2, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    public float drawCenteredStringWithShadow(String text, float x2, float y2, int color) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    public double getStringWidth(String text) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }
    
    public double getPasswordWidth(String text) {
        text = text.replaceAll("\u00c3\u201a", "");
        return 0;
    }

    public int getHeight() {
        return 0;
    }

    public static Font createFontFromFile(String name, int size) {
        Font f2;
        try {
            f2 = Font.createFont(0, new Object().getClass().getResourceAsStream("/" + name + ".ttf"));
        }
        catch (Exception e2) {
            return null;
        }
        f2 = f2.deriveFont(0, size);
        return f2;
    }


    public String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_) {
    	p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }
    
    public String trimStringToWidth(String p_78262_1_, int p_78262_2_) {
    	p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }
    
    public String trimStringToWidthPassword(String p_78262_1_, int p_78262_2_, boolean custom) {
    	p_78262_1_ = p_78262_1_.replaceAll("\u00c3\u201a", "");
        return "";
    }


}

