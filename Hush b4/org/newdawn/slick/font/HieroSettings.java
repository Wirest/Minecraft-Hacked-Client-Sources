// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.font;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.File;
import java.util.Iterator;
import org.newdawn.slick.font.effects.ConfigurableEffect;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.ResourceLoader;
import java.util.ArrayList;
import java.util.List;

public class HieroSettings
{
    private int fontSize;
    private boolean bold;
    private boolean italic;
    private int paddingTop;
    private int paddingLeft;
    private int paddingBottom;
    private int paddingRight;
    private int paddingAdvanceX;
    private int paddingAdvanceY;
    private int glyphPageWidth;
    private int glyphPageHeight;
    private final List effects;
    
    public HieroSettings() {
        this.fontSize = 12;
        this.bold = false;
        this.italic = false;
        this.glyphPageWidth = 512;
        this.glyphPageHeight = 512;
        this.effects = new ArrayList();
    }
    
    public HieroSettings(final String hieroFileRef) throws SlickException {
        this(ResourceLoader.getResourceAsStream(hieroFileRef));
    }
    
    public HieroSettings(final InputStream in) throws SlickException {
        this.fontSize = 12;
        this.bold = false;
        this.italic = false;
        this.glyphPageWidth = 512;
        this.glyphPageHeight = 512;
        this.effects = new ArrayList();
        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                line = line.trim();
                if (line.length() == 0) {
                    continue;
                }
                final String[] pieces = line.split("=", 2);
                String name = pieces[0].trim();
                final String value = pieces[1];
                if (name.equals("font.size")) {
                    this.fontSize = Integer.parseInt(value);
                }
                else if (name.equals("font.bold")) {
                    this.bold = Boolean.valueOf(value);
                }
                else if (name.equals("font.italic")) {
                    this.italic = Boolean.valueOf(value);
                }
                else if (name.equals("pad.top")) {
                    this.paddingTop = Integer.parseInt(value);
                }
                else if (name.equals("pad.right")) {
                    this.paddingRight = Integer.parseInt(value);
                }
                else if (name.equals("pad.bottom")) {
                    this.paddingBottom = Integer.parseInt(value);
                }
                else if (name.equals("pad.left")) {
                    this.paddingLeft = Integer.parseInt(value);
                }
                else if (name.equals("pad.advance.x")) {
                    this.paddingAdvanceX = Integer.parseInt(value);
                }
                else if (name.equals("pad.advance.y")) {
                    this.paddingAdvanceY = Integer.parseInt(value);
                }
                else if (name.equals("glyph.page.width")) {
                    this.glyphPageWidth = Integer.parseInt(value);
                }
                else if (name.equals("glyph.page.height")) {
                    this.glyphPageHeight = Integer.parseInt(value);
                }
                else {
                    if (name.equals("effect.class")) {
                        try {
                            this.effects.add(Class.forName(value).newInstance());
                            continue;
                        }
                        catch (Exception ex) {
                            throw new SlickException("Unable to create effect instance: " + value, ex);
                        }
                    }
                    if (!name.startsWith("effect.")) {
                        continue;
                    }
                    name = name.substring(7);
                    final ConfigurableEffect effect = this.effects.get(this.effects.size() - 1);
                    final List values = effect.getValues();
                    for (final ConfigurableEffect.Value effectValue : values) {
                        if (effectValue.getName().equals(name)) {
                            effectValue.setString(value);
                            break;
                        }
                    }
                    effect.setValues(values);
                }
            }
            reader.close();
        }
        catch (Exception ex2) {
            throw new SlickException("Unable to load Hiero font file", ex2);
        }
    }
    
    public int getPaddingTop() {
        return this.paddingTop;
    }
    
    public void setPaddingTop(final int paddingTop) {
        this.paddingTop = paddingTop;
    }
    
    public int getPaddingLeft() {
        return this.paddingLeft;
    }
    
    public void setPaddingLeft(final int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }
    
    public int getPaddingBottom() {
        return this.paddingBottom;
    }
    
    public void setPaddingBottom(final int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }
    
    public int getPaddingRight() {
        return this.paddingRight;
    }
    
    public void setPaddingRight(final int paddingRight) {
        this.paddingRight = paddingRight;
    }
    
    public int getPaddingAdvanceX() {
        return this.paddingAdvanceX;
    }
    
    public void setPaddingAdvanceX(final int paddingAdvanceX) {
        this.paddingAdvanceX = paddingAdvanceX;
    }
    
    public int getPaddingAdvanceY() {
        return this.paddingAdvanceY;
    }
    
    public void setPaddingAdvanceY(final int paddingAdvanceY) {
        this.paddingAdvanceY = paddingAdvanceY;
    }
    
    public int getGlyphPageWidth() {
        return this.glyphPageWidth;
    }
    
    public void setGlyphPageWidth(final int glyphPageWidth) {
        this.glyphPageWidth = glyphPageWidth;
    }
    
    public int getGlyphPageHeight() {
        return this.glyphPageHeight;
    }
    
    public void setGlyphPageHeight(final int glyphPageHeight) {
        this.glyphPageHeight = glyphPageHeight;
    }
    
    public int getFontSize() {
        return this.fontSize;
    }
    
    public void setFontSize(final int fontSize) {
        this.fontSize = fontSize;
    }
    
    public boolean isBold() {
        return this.bold;
    }
    
    public void setBold(final boolean bold) {
        this.bold = bold;
    }
    
    public boolean isItalic() {
        return this.italic;
    }
    
    public void setItalic(final boolean italic) {
        this.italic = italic;
    }
    
    public List getEffects() {
        return this.effects;
    }
    
    public void save(final File file) throws IOException {
        final PrintStream out = new PrintStream(new FileOutputStream(file));
        out.println("font.size=" + this.fontSize);
        out.println("font.bold=" + this.bold);
        out.println("font.italic=" + this.italic);
        out.println();
        out.println("pad.top=" + this.paddingTop);
        out.println("pad.right=" + this.paddingRight);
        out.println("pad.bottom=" + this.paddingBottom);
        out.println("pad.left=" + this.paddingLeft);
        out.println("pad.advance.x=" + this.paddingAdvanceX);
        out.println("pad.advance.y=" + this.paddingAdvanceY);
        out.println();
        out.println("glyph.page.width=" + this.glyphPageWidth);
        out.println("glyph.page.height=" + this.glyphPageHeight);
        out.println();
        for (final ConfigurableEffect effect : this.effects) {
            out.println("effect.class=" + effect.getClass().getName());
            for (final ConfigurableEffect.Value value : effect.getValues()) {
                out.println("effect." + value.getName() + "=" + value.getString());
            }
            out.println();
        }
        out.close();
    }
}
