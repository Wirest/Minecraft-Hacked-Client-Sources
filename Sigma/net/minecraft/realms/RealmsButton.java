package net.minecraft.realms;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonRealmsProxy;
import net.minecraft.util.ResourceLocation;

public class RealmsButton {
    protected static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");
    private GuiButtonRealmsProxy proxy;
    private static final String __OBFID = "CL_00001890";

    public RealmsButton(int p_i1177_1_, int p_i1177_2_, int p_i1177_3_, String p_i1177_4_) {
        proxy = new GuiButtonRealmsProxy(this, p_i1177_1_, p_i1177_2_, p_i1177_3_, p_i1177_4_);
    }

    public RealmsButton(int p_i1178_1_, int p_i1178_2_, int p_i1178_3_, int p_i1178_4_, int p_i1178_5_, String p_i1178_6_) {
        proxy = new GuiButtonRealmsProxy(this, p_i1178_1_, p_i1178_2_, p_i1178_3_, p_i1178_6_, p_i1178_4_, p_i1178_5_);
    }

    public GuiButton getProxy() {
        return proxy;
    }

    public int id() {
        return proxy.getId();
    }

    public boolean active() {
        return proxy.getEnabled();
    }

    public void active(boolean p_active_1_) {
        proxy.setEnabled(p_active_1_);
    }

    public void msg(String p_msg_1_) {
        proxy.setText(p_msg_1_);
    }

    public int getWidth() {
        return proxy.getButtonWidth();
    }

    public int getHeight() {
        return proxy.func_175232_g();
    }

    public int y() {
        return proxy.getPositionY();
    }

    public void render(int p_render_1_, int p_render_2_) {
        proxy.drawButton(Minecraft.getMinecraft(), p_render_1_, p_render_2_);
    }

    public void clicked(int p_clicked_1_, int p_clicked_2_) {
    }

    public void released(int p_released_1_, int p_released_2_) {
    }

    public void blit(int p_blit_1_, int p_blit_2_, int p_blit_3_, int p_blit_4_, int p_blit_5_, int p_blit_6_) {
        proxy.drawTexturedModalRect(p_blit_1_, p_blit_2_, p_blit_3_, p_blit_4_, p_blit_5_, p_blit_6_);
    }

    public void renderBg(int p_renderBg_1_, int p_renderBg_2_) {
    }

    public int getYImage(boolean p_getYImage_1_) {
        return proxy.func_154312_c(p_getYImage_1_);
    }
}
