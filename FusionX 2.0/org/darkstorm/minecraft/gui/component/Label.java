// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

public interface Label extends TextComponent
{
    TextAlignment getHorizontalAlignment();
    
    TextAlignment getVerticalAlignment();
    
    void setHorizontalAlignment(final TextAlignment p0);
    
    void setVerticalAlignment(final TextAlignment p0);
    
    public enum TextAlignment
    {
        CENTER("CENTER", 0), 
        LEFT("LEFT", 1), 
        RIGHT("RIGHT", 2), 
        TOP("TOP", 3), 
        BOTTOM("BOTTOM", 4);
        
        private TextAlignment(final String s, final int n) {
        }
    }
}
