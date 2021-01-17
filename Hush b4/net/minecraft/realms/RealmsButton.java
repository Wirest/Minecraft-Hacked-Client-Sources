// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonRealmsProxy;
import net.minecraft.util.ResourceLocation;

public class RealmsButton
{
    protected static final ResourceLocation WIDGETS_LOCATION;
    private GuiButtonRealmsProxy proxy;
    
    static {
        WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    }
    
    public RealmsButton(final int p_i1177_1_, final int p_i1177_2_, final int p_i1177_3_, final String p_i1177_4_) {
        this.proxy = new GuiButtonRealmsProxy(this, p_i1177_1_, p_i1177_2_, p_i1177_3_, p_i1177_4_);
    }
    
    public RealmsButton(final int p_i1178_1_, final int p_i1178_2_, final int p_i1178_3_, final int p_i1178_4_, final int p_i1178_5_, final String p_i1178_6_) {
        this.proxy = new GuiButtonRealmsProxy(this, p_i1178_1_, p_i1178_2_, p_i1178_3_, p_i1178_6_, p_i1178_4_, p_i1178_5_);
    }
    
    public GuiButton getProxy() {
        return this.proxy;
    }
    
    public int id() {
        return this.proxy.getId();
    }
    
    public boolean active() {
        return this.proxy.getEnabled();
    }
    
    public void active(final boolean p_active_1_) {
        this.proxy.setEnabled(p_active_1_);
    }
    
    public void msg(final String p_msg_1_) {
        this.proxy.setText(p_msg_1_);
    }
    
    public int getWidth() {
        return this.proxy.getButtonWidth();
    }
    
    public int getHeight() {
        return this.proxy.func_175232_g();
    }
    
    public int y() {
        return this.proxy.getPositionY();
    }
    
    public void render(final int p_render_1_, final int p_render_2_) {
        this.proxy.drawButton(Minecraft.getMinecraft(), p_render_1_, p_render_2_);
    }
    
    public void clicked(final int p_clicked_1_, final int p_clicked_2_) {
    }
    
    public void released(final int p_released_1_, final int p_released_2_) {
    }
    
    public void blit(final int p_blit_1_, final int p_blit_2_, final int p_blit_3_, final int p_blit_4_, final int p_blit_5_, final int p_blit_6_) {
        this.proxy.drawTexturedModalRect(p_blit_1_, p_blit_2_, p_blit_3_, p_blit_4_, p_blit_5_, p_blit_6_);
    }
    
    public void renderBg(final int p_renderBg_1_, final int p_renderBg_2_) {
    }
    
    public int getYImage(final boolean p_getYImage_1_) {
        return this.proxy.func_154312_c(p_getYImage_1_);
    }
}
