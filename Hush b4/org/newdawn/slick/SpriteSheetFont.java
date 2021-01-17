// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import java.io.UnsupportedEncodingException;
import org.newdawn.slick.util.Log;

public class SpriteSheetFont implements Font
{
    private SpriteSheet font;
    private char startingCharacter;
    private int charWidth;
    private int charHeight;
    private int horizontalCount;
    private int numChars;
    
    public SpriteSheetFont(final SpriteSheet font, final char startingCharacter) {
        this.font = font;
        this.startingCharacter = startingCharacter;
        this.horizontalCount = font.getHorizontalCount();
        final int verticalCount = font.getVerticalCount();
        this.charWidth = font.getWidth() / this.horizontalCount;
        this.charHeight = font.getHeight() / verticalCount;
        this.numChars = this.horizontalCount * verticalCount;
    }
    
    @Override
    public void drawString(final float x, final float y, final String text) {
        this.drawString(x, y, text, Color.white);
    }
    
    @Override
    public void drawString(final float x, final float y, final String text, final Color col) {
        this.drawString(x, y, text, col, 0, text.length() - 1);
    }
    
    @Override
    public void drawString(final float x, final float y, final String text, final Color col, final int startIndex, final int endIndex) {
        try {
            final byte[] data = text.getBytes("US-ASCII");
            for (int i = 0; i < data.length; ++i) {
                final int index = data[i] - this.startingCharacter;
                if (index < this.numChars) {
                    final int xPos = index % this.horizontalCount;
                    final int yPos = index / this.horizontalCount;
                    if (i >= startIndex || i <= endIndex) {
                        this.font.getSprite(xPos, yPos).draw(x + i * this.charWidth, y, col);
                    }
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            Log.error(e);
        }
    }
    
    @Override
    public int getHeight(final String text) {
        return this.charHeight;
    }
    
    @Override
    public int getWidth(final String text) {
        return this.charWidth * text.length();
    }
    
    @Override
    public int getLineHeight() {
        return this.charHeight;
    }
}
