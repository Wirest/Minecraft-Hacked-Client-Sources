// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.ui;

import net.minecraft.client.gui.Gui;
import me.CheerioFX.FusionX.events.EventRender2D;
import com.darkmagician6.eventapi.EventTarget;
import me.CheerioFX.FusionX.GUI.tabGui.TabGui;
import me.CheerioFX.FusionX.GUI.clickgui.UI;
import me.CheerioFX.FusionX.utils.Wrapper;
import java.util.Iterator;
import me.CheerioFX.FusionX.utils.RenderUtils;
import me.CheerioFX.FusionX.module.Category;
import me.CheerioFX.FusionX.module.Module;
import me.CheerioFX.FusionX.module.ModuleManager;
import me.CheerioFX.FusionX.FusionX;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.Color;
import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import me.CheerioFX.FusionX.utils.TimeHelper2;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiIngame;

public class GuiIngameHook extends GuiIngame
{
    public static boolean renderGUI;
    public String s;
    private ResourceLocation logo;
    boolean updateChecked;
    private int currentColor;
    private int fadeState;
    private boolean goingUp;
    private TimeHelper2 timer;
    
    static {
        GuiIngameHook.renderGUI = true;
    }
    
    public GuiIngameHook(final Minecraft mcIn) {
        super(mcIn);
        this.s = " ";
        this.logo = new ResourceLocation("FusionX/logo.png");
        this.updateChecked = false;
        this.timer = new TimeHelper2();
        EventManager.register(this);
    }
    
    private void updateFade() {
        if (this.fadeState >= 20 || this.fadeState <= 0) {
            this.goingUp = !this.goingUp;
        }
        if (this.goingUp) {
            ++this.fadeState;
        }
        else {
            --this.fadeState;
        }
        final double ratio = this.fadeState / 20.0;
        this.currentColor = this.getFadeHex(new Color(155, 0, 0).getRGB(), new Color(255, 100, 100).getRGB(), ratio);
    }
    
    private int getFadeHex(final int hex1, final int hex2, final double ratio) {
        int r = hex1 >> 16;
        int g = hex1 >> 8 & 0xFF;
        int b = hex1 & 0xFF;
        r += (int)(((hex2 >> 16) - r) * ratio);
        g += (int)(((hex2 >> 8 & 0xFF) - g) * ratio);
        b += (int)(((hex2 & 0xFF) - b) * ratio);
        return r << 16 | g << 8 | b;
    }
    
    private void renderHud() {
        final ScaledResolution sr = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int y = 0;
        if (this.timer.hasPassed(76.92307692307692)) {
            this.updateFade();
            this.timer.reset();
        }
        final ModuleManager moduleManager = FusionX.theClient.moduleManager;
        ModuleManager.getModules().sort((m1, m2) -> this.mc.fontRendererObj.getStringWidth(String.valueOf(m2.getName()) + m2.getExtraInfo()) - this.mc.fontRendererObj.getStringWidth(String.valueOf(m1.getName()) + m1.getExtraInfo()));
        final ModuleManager moduleManager2 = FusionX.theClient.moduleManager;
        for (final Module i : ModuleManager.getModules()) {
            if (i.getState() && !i.isCategory(Category.GUI) && !i.isCategory(Category.SETTINGS)) {
                RenderUtils.drawBorderRect2(sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(String.valueOf(i.getName()) + i.getExtraInfo()) - 4, y, sr.getScaledWidth(), y + 11, 1, new Color(40, 40, 40).getRGB(), this.currentColor);
                RenderUtils.drawRect(sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(String.valueOf(i.getName()) + i.getExtraInfo()) - 3, y, sr.getScaledWidth() - 1, y + 1, new Color(41, 40, 40).getRGB());
                this.mc.fontRendererObj.func_175063_a(String.valueOf(i.getName()) + "§7 " + i.getExtraInfo(), sr.getScaledWidth() - this.mc.fontRendererObj.getStringWidth(String.valueOf(i.getName()) + i.getExtraInfo()) - 2, y + 2, this.currentColor);
                y += 10;
            }
        }
    }
    
    @EventTarget
    @Override
    public void func_175180_a(final float p_175180_1_) {
        super.func_175180_a(p_175180_1_);
        if (FusionX.renderTheGui) {
            final int leftx = 6;
            final int rightx = (int)(Wrapper.mc.displayWidth / 2.5);
            final int lefty = 20;
            final int righty = 5;
            if (GuiIngameHook.renderGUI && !Wrapper.mc.gameSettings.showDebugInfo) {
                this.renderHud();
            }
            FusionX.theClient.getGuiManager().update();
            if (UI.isTabGUI() && GuiIngameHook.renderGUI && !Wrapper.mc.gameSettings.showDebugInfo) {
                TabGui.drawTabGui();
            }
        }
    }
    
    @EventTarget
    public void onRender(final EventRender2D e) {
        if (FusionX.renderTheGui) {
            this.mc.getTextureManager().bindTexture(this.logo);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, 75, 75, 75.0f, 75.0f);
        }
    }
}
