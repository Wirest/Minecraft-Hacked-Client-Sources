// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.Label;
import org.darkstorm.minecraft.gui.component.AbstractComponent;

public class BasicLabel extends AbstractComponent implements Label
{
    protected String text;
    protected TextAlignment horizontalAlignment;
    protected TextAlignment verticalAlignment;
    
    public BasicLabel() {
        this.horizontalAlignment = TextAlignment.LEFT;
        this.verticalAlignment = TextAlignment.CENTER;
    }
    
    public BasicLabel(final String text) {
        this.horizontalAlignment = TextAlignment.LEFT;
        this.verticalAlignment = TextAlignment.CENTER;
        this.text = text;
    }
    
    @Override
    public String getText() {
        return this.text;
    }
    
    @Override
    public void setText(final String text) {
        this.text = text;
    }
    
    @Override
    public TextAlignment getHorizontalAlignment() {
        return this.horizontalAlignment;
    }
    
    @Override
    public TextAlignment getVerticalAlignment() {
        return this.verticalAlignment;
    }
    
    @Override
    public void setHorizontalAlignment(final TextAlignment alignment) {
        this.horizontalAlignment = alignment;
    }
    
    @Override
    public void setVerticalAlignment(final TextAlignment alignment) {
        this.verticalAlignment = alignment;
    }
}
