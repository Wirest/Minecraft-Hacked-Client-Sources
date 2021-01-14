package cn.kody.debug.ui;

import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import java.io.IOException;
import java.util.Iterator;

import cn.kody.debug.Client;
import cn.kody.debug.ui.altlogin.GuiAltLogin;
import cn.kody.debug.ui.guilogin.GuiLogin;
import cn.kody.debug.utils.color.Colors;
import cn.kody.debug.utils.handler.MouseInputHandler;
import cn.kody.debug.utils.render.RenderUtil;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.ScaledResolution;

public class GUIMainScreen extends GuiScreen
{
    public ScaledResolution sr;
    public Minecraft mc;
    public ArrayList<Slot> slots;
    public MouseInputHandler handler;
    
    public GUIMainScreen() {
        super();
        this.mc = Minecraft.getMinecraft();
        this.slots = new ArrayList<Slot>();
        this.handler = new MouseInputHandler(0);
    }
    
    @Override
    public void initGui() {
        this.sr = new ScaledResolution(this.mc);
        this.slots.clear();
        this.slots.add(new Slot("SinglePlayer", "h", new GuiSelectWorld(this)));
        this.slots.add(new Slot("MultiPlayer", "i", new GuiMultiplayer(this)));
        this.slots.add(new Slot("AltManager", "j", new GuiAltLogin(this)));
        this.slots.add(new Slot("Options", "k", new GuiOptions(this, this.mc.gameSettings)));
        this.slots.add(new Slot("QuitGame", "l", null));
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int p_draw_1_, final int p_draw_2_, final float n) {
        final float n2 = (float)this.sr.getScaledWidth();
        final float n3 = (float)this.sr.getScaledHeight();
        RenderUtil.drawImage(new ResourceLocation("client/mainmenu_back.png"), 0, 0, (int)n2, (int)n3);
        Client.instance.fontMgr.tahoma16.drawString("Version " + Client.CLIENT_VERSION + " jar leak by idk i found it on google, eclipse ready by havoc#11, thanks to snowflake", 8.0f, n3 - 25.0f, Notification.reAlpha(Colors.WHITE.c, 0.6f));
        Client.instance.fontMgr.tahoma70.drawCenteredString(Client.CLIENT_NAME, n2 / 2.0f, n3 / 2.0f - 60.0f, Notification.reAlpha(Colors.WHITE.c, 0.6f));
        float p_draw_3_ = n2 / 2.0f - this.slots.size() * 40;
        final float p_draw_4_ = n3 / 2.0f + 20.0f;
        int n4 = 0;
        for (final Slot Slot : this.slots) {
            Slot.draw(p_draw_1_, p_draw_2_, p_draw_3_, p_draw_4_);
            if (++n4 == 4) {
                Slot.drawRect(p_draw_3_ + 82.0f, p_draw_4_ + 10.0f, p_draw_3_ + 82.5f, p_draw_4_ + 30.0f, Notification.reAlpha(Colors.WHITE.c, 0.6f));
                p_draw_3_ += 90.0f;
            }else {
                p_draw_3_ += 80.0f;
            }
        }
    }
    
    @Override
    protected void mouseClicked(final int p_onCrink_1_, final int p_onCrink_2_, final int n) throws IOException {
        final float n2 = (float)this.sr.getScaledWidth();
        final float n3 = (float)this.sr.getScaledHeight();
        float p_onCrink_3_ = n2 / 2.0f - this.slots.size() * 40;
        final float p_onCrink_4_ = n3 / 2.0f + 20.0f;
        int n4 = 0;
        for (final Slot Slot : this.slots) {
            if (n == 0) {
                Slot.onCrink(p_onCrink_1_, p_onCrink_2_, p_onCrink_3_, p_onCrink_4_);
            }
            if (++n4 == 4) {
                p_onCrink_3_ += 90.0f;
            }
            else {
                p_onCrink_3_ += 80.0f;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        this.sr = new ScaledResolution(this.mc);
        super.updateScreen();
    }
    
    public void drawGradientRect(final float n, final float n2, final float n3, final float n4, final int n5, final int n6) {
        final float n7 = (n5 >> 24 & 0xFF) / 255.0f;
        final float n8 = (n5 >> 16 & 0xFF) / 255.0f;
        final float n9 = (n5 >> 8 & 0xFF) / 255.0f;
        final float n10 = (n5 & 0xFF) / 255.0f;
        final float n11 = (n6 >> 24 & 0xFF) / 255.0f;
        final float n12 = (n6 >> 16 & 0xFF) / 255.0f;
        final float n13 = (n6 >> 8 & 0xFF) / 255.0f;
        final float n14 = (n6 & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        final Tessellator instance = Tessellator.getInstance();
        final WorldRenderer worldRenderer = instance.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(n3, n2, this.zLevel).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n, n2, this.zLevel).color(n8, n9, n10, n7).endVertex();
        worldRenderer.pos(n, n4, this.zLevel).color(n12, n13, n14, n11).endVertex();
        worldRenderer.pos(n3, n4, this.zLevel).color(n12, n13, n14, n11).endVertex();
        instance.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
}
