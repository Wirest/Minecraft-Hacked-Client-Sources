package com.minimap.interfaces;

import java.util.*;
import net.minecraft.client.resources.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;

public class CursorBox
{
    private ArrayList<String> strings;
    private String language;
    private String fullCode;
    private int boxWidth;
    private static final int color = -2147483640;
    
    public CursorBox(final String code) {
        this.fullCode = "";
        this.boxWidth = 150;
        this.fullCode = code;
        final String text = I18n.format(code, new Object[0]);
        this.createLines(text);
    }
    
    public void createLines(final String text) {
        this.language = Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
        this.strings = new ArrayList<String>();
        final String[] words = text.split(" ");
        final int spaceWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(" ");
        String line = "";
        int width = 0;
        for (int i = 0; i < words.length; ++i) {
            final int wordWidth = Minecraft.getMinecraft().fontRendererObj.getStringWidth(words[i]);
            if (width == 0 && wordWidth > this.boxWidth - 15) {
                this.boxWidth = wordWidth + 15;
            }
            if (width + wordWidth <= this.boxWidth - 15) {
                width += spaceWidth + wordWidth;
                line = line + words[i] + " ";
            }
            else {
                this.strings.add(line);
                line = new String("");
                width = 0;
                --i;
            }
            if (i == words.length - 1) {
                this.strings.add(line);
            }
        }
    }
    
    public CursorBox(final int size) {
        this.fullCode = "";
        this.boxWidth = 150;
        this.strings = new ArrayList<String>();
        for (int i = 0; i < size; ++i) {
            this.strings.add("");
        }
    }
    
    public String getString(final int line) {
        return this.strings.get(line);
    }
    
    public void drawBox(final int x, final int y, final int width, final int height) {
        try {
            if (!this.language.equals(Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode())) {
                this.createLines(I18n.format(this.fullCode, new Object[0]));
            }
        }
        catch (Exception ex) {}
        final int fix = (x + this.boxWidth > width) ? (x + this.boxWidth - width) : 0;
        final int h = 5 + this.strings.size() * 10 + 5;
        final int fiy = (y + h > height) ? (y + h - height) : 0;
        Gui.drawRect(x - fix, y - fiy, x + this.boxWidth - fix, y + h - fiy, -2147483640);
        for (int i = 0; i < this.strings.size(); ++i) {
            final String s = this.getString(i);
            Minecraft.getMinecraft().fontRendererObj.drawString("§f" + s, x + 10 - fix, y + 5 + 10 * i - fiy, 16777215);
        }
    }
}
