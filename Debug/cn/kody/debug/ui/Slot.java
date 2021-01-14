package cn.kody.debug.ui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import cn.kody.debug.Client;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class Slot
{
    public GuiScreen screen;
    public String display;
    public String icon;
    public boolean ishovered;
    public float alpha;
    public boolean crink;
    
    public Slot(final String p_i1463_1_, final String p_i1463_2_, final GuiScreen p_i1463_3_) {
        super();
        this.ishovered = false;
        this.alpha = 0.0f;
        this.crink = true;
        this.display = p_i1463_1_;
        this.icon = p_i1463_2_;
        this.screen = p_i1463_3_;
    }
    
    public void draw(final int p_draw_1_, final int p_draw_2_, final float p_draw_3_, final float p_draw_4_) {
        Client.instance.fontMgr.icon80.drawString("g", p_draw_3_, p_draw_4_, Notification.reAlpha(Colors.WHITE.c, 0.42f));
        if (this.alpha > 0.0f) {
            Client.instance.fontMgr.icon80.drawString("g", p_draw_3_, p_draw_4_, Notification.reAlpha(14400839, this.alpha));
        }
        if (this.ishovered) {
            this.alpha = (float) RenderUtil.getAnimationState(this.alpha, 0.72f, 12.0f);
        }
        else {
            this.alpha = (float) RenderUtil.getAnimationState(this.alpha, 0.0f, 12.0f);
        }
        RenderUtil.drawRoundedRect(p_draw_3_ + 23.0f, p_draw_4_ + 34.0f, p_draw_3_ + 45.0f, p_draw_4_ + 36.0f, Notification.reAlpha(-1118482, 0.2f), Notification.reAlpha(-1118482, 0.6f));
        Client.instance.fontMgr.icon45.drawString(this.icon, p_draw_3_ + 23.0f, p_draw_4_ + 8.0f, Notification.reAlpha(Colors.WHITE.c, 0.85f));
        if (RenderUtil.isHovering(p_draw_1_, p_draw_2_, p_draw_3_, p_draw_4_, p_draw_3_ + 72.0f, p_draw_4_ + 40.0f)) {
            this.ishovered = true;
        }else {
            this.ishovered = false;
        }
    }
    
    public void onCrink(final int p_onCrink_1_, final int p_onCrink_2_, final float p_onCrink_3_, final float p_onCrink_4_) {
        if (this.ishovered) {
            if (this.screen != null) {
                Minecraft.getMinecraft().displayGuiScreen(this.screen);
            }
            else {
                Minecraft.getMinecraft().shutdownMinecraftApplet();
            }
        }
    }
    
    public void drawRect(float p_drawRect_1_, float p_drawRect_2_, float p_drawRect_3_, float p_drawRect_4_, final int p_drawRect_5_) {
        if (p_drawRect_1_ < p_drawRect_3_) {
            final float n = p_drawRect_1_;
            p_drawRect_1_ = p_drawRect_3_;
            p_drawRect_3_ = n;
        }
        if (p_drawRect_2_ < p_drawRect_4_) {
            final float n2 = p_drawRect_2_;
            p_drawRect_2_ = p_drawRect_4_;
            p_drawRect_4_ = n2;
        }
        final float p_color_3_ = (p_drawRect_5_ >> 24 & 0xFF) / 255.0f;
        final float p_color_0_ = (p_drawRect_5_ >> 16 & 0xFF) / 255.0f;
        final float p_color_1_ = (p_drawRect_5_ >> 8 & 0xFF) / 255.0f;
        final float p_color_2_ = (p_drawRect_5_ & 0xFF) / 255.0f;
        final WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(p_color_0_, p_color_1_, p_color_2_, p_color_3_);
        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
        worldRenderer.pos(p_drawRect_1_, p_drawRect_4_, 0.0).endVertex();
        worldRenderer.pos(p_drawRect_3_, p_drawRect_4_, 0.0).endVertex();
        worldRenderer.pos(p_drawRect_3_ - 2.5f, p_drawRect_2_, 0.0).endVertex();
        worldRenderer.pos(p_drawRect_1_ - 2.5f, p_drawRect_2_, 0.0).endVertex();
   GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        Tessellator.getInstance().draw();
        
    }
}
