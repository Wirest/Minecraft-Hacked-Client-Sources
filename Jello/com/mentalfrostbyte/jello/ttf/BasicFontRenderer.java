package com.mentalfrostbyte.jello.ttf;

import com.mentalfrostbyte.jello.main.Jello;

import net.minecraft.client.renderer.GlStateManager;

public class BasicFontRenderer implements FontRenderer
	{
	    protected final FontData fontData;
	    protected int kerning;
	    
	    public BasicFontRenderer() {
	        this.fontData = new FontData();
	        this.kerning = 0;
	    }
	    
	    public int drawString(final FontData fontData, final String text, int x, final int y, final int color) {
	        if (!fontData.hasFont()) {
	            return 0;
	        }
	        GlStateManager.enableBlend();
	        fontData.bind();
	        GLUtils.glColor(color);
	        for (int size = text.length(), i = 0; i < size; ++i) {
	            final char character = text.charAt(i);
	            if (fontData.hasBounds(character)) {
	                final FontData.CharacterData area = fontData.getCharacterBounds(character);
	                FontUtils.drawTextureRect((float)x, (float)y, (float)area.width, (float)area.height, area.x / fontData.getTextureWidth(), area.y / fontData.getTextureHeight(), (area.x + area.width) / fontData.getTextureWidth(), (area.y + area.height) / fontData.getTextureHeight());
	                x += area.width + this.kerning;
	                
	            }
	        }
	        return x;
	    }
	    
	    public int getHeight() {
	        return this.fontData.getFontHeight();
	    }
	    
	    public int drawString(final String text, final int x, final int y, final int color) {
	        return this.drawString(this.fontData, text, x, y, color);
	    }
	    
	    public int getKerning() {
	        return this.kerning;
	    }
	    
	    public void setKerning(final int kerning) {
	        this.kerning = kerning;
	    }
	    
	    public FontData getFontData() {
	        return this.fontData;
	    }
}
