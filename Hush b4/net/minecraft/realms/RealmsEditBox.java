// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;

public class RealmsEditBox
{
    private final GuiTextField editBox;
    
    public RealmsEditBox(final int p_i45743_1_, final int p_i45743_2_, final int p_i45743_3_, final int p_i45743_4_, final int p_i45743_5_) {
        this.editBox = new GuiTextField(p_i45743_1_, Minecraft.getMinecraft().fontRendererObj, p_i45743_2_, p_i45743_3_, p_i45743_4_, p_i45743_5_);
    }
    
    public String getValue() {
        return this.editBox.getText();
    }
    
    public void tick() {
        this.editBox.updateCursorCounter();
    }
    
    public void setFocus(final boolean p_setFocus_1_) {
        this.editBox.setFocused(p_setFocus_1_);
    }
    
    public void setValue(final String p_setValue_1_) {
        this.editBox.setText(p_setValue_1_);
    }
    
    public void keyPressed(final char p_keyPressed_1_, final int p_keyPressed_2_) {
        this.editBox.textboxKeyTyped(p_keyPressed_1_, p_keyPressed_2_);
    }
    
    public boolean isFocused() {
        return this.editBox.isFocused();
    }
    
    public void mouseClicked(final int p_mouseClicked_1_, final int p_mouseClicked_2_, final int p_mouseClicked_3_) {
        this.editBox.mouseClicked(p_mouseClicked_1_, p_mouseClicked_2_, p_mouseClicked_3_);
    }
    
    public void render() {
        this.editBox.drawTextBox();
    }
    
    public void setMaxLength(final int p_setMaxLength_1_) {
        this.editBox.setMaxStringLength(p_setMaxLength_1_);
    }
    
    public void setIsEditable(final boolean p_setIsEditable_1_) {
        this.editBox.setEnabled(p_setIsEditable_1_);
    }
}
