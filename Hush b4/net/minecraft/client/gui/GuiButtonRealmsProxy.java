// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.realms.RealmsButton;

public class GuiButtonRealmsProxy extends GuiButton
{
    private RealmsButton realmsButton;
    
    public GuiButtonRealmsProxy(final RealmsButton realmsButtonIn, final int buttonId, final int x, final int y, final String text) {
        super(buttonId, x, y, text);
        this.realmsButton = realmsButtonIn;
    }
    
    public GuiButtonRealmsProxy(final RealmsButton realmsButtonIn, final int buttonId, final int x, final int y, final String text, final int widthIn, final int heightIn) {
        super(buttonId, x, y, widthIn, heightIn, text);
        this.realmsButton = realmsButtonIn;
    }
    
    public int getId() {
        return super.id;
    }
    
    public boolean getEnabled() {
        return super.enabled;
    }
    
    public void setEnabled(final boolean isEnabled) {
        super.enabled = isEnabled;
    }
    
    public void setText(final String text) {
        super.displayString = text;
    }
    
    @Override
    public int getButtonWidth() {
        return super.getButtonWidth();
    }
    
    public int getPositionY() {
        return super.yPosition;
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.realmsButton.clicked(mouseX, mouseY);
        }
        return super.mousePressed(mc, mouseX, mouseY);
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.realmsButton.released(mouseX, mouseY);
    }
    
    public void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        this.realmsButton.renderBg(mouseX, mouseY);
    }
    
    public RealmsButton getRealmsButton() {
        return this.realmsButton;
    }
    
    public int getHoverState(final boolean mouseOver) {
        return this.realmsButton.getYImage(mouseOver);
    }
    
    public int func_154312_c(final boolean p_154312_1_) {
        return super.getHoverState(p_154312_1_);
    }
    
    public int func_175232_g() {
        return this.height;
    }
}
