// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.gui;

import net.minecraft.client.gui.FontRenderer;
import java.awt.Color;
import de.Hero.clickgui.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiScreen;

public class Changelog extends GuiScreen
{
    private GuiScreen parent;
    private String version;
    private String added;
    private String improved;
    private String removed;
    private String update1;
    private String update2;
    private String update3;
    private String update4;
    private String update5;
    private String update6;
    private String update7;
    private String update8;
    private String update9;
    private String update10;
    private String update11;
    private String update12;
    private String update13;
    private String update14;
    private String update15;
    private String update16;
    private String update17;
    private String update18;
    private String update19;
    private String update20;
    
    public Changelog(final GuiScreen guiMainMenu) {
        this.parent = guiMainMenu;
    }
    
    @Override
    public void initGui() {
        this.version = "Latest Updates";
        this.added = "§f- §aAdded §f-";
        this.improved = "§f- §eImproved §f-";
        this.removed = "§f- §cRemoved §f& §cOther §f-";
        this.update1 = "KillAura mode intave -  With ticks";
        this.update2 = "Items - Custom size settings";
        this.update3 = "Custom Client-Color";
        this.update4 = "Custom stepheight";
        this.update5 = "New HUD settings";
        this.update6 = "Zodiac theme";
        this.update7 = "SlowDown";
        this.update8 = "Hotbar";
        this.update9 = "Bhop";
        this.update10 = "Client performance";
        this.update11 = "Hush theme";
        this.update12 = "FastPlace";
        this.update13 = "KillAura";
        this.update14 = "Velocity";
        this.update15 = "";
        this.update16 = "";
        this.update17 = "";
        this.update18 = "";
        this.update19 = "";
        this.update20 = "";
        final int width = 100;
        final int height = 20;
        final int offset = 7;
        this.buttonList.add(new Button(0, this.width / 2 - width / 2, this.height - height - offset, width, height, I18n.format("gui.back", new Object[0])));
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if (keyCode == 1) {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        if (button.id == 0) {
            this.mc.displayGuiScreen(this.parent);
        }
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        final ScaledResolution s1 = new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("Zodiac/MainBackGround.jpg"));
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, s1.getScaledWidth(), s1.getScaledHeight(), (float)s1.getScaledWidth(), (float)s1.getScaledHeight());
        Gui.drawRect(0.0, 0.0, this.width, this.height, 1073741824);
        this.drawCenteredString(this.fontRendererObj, "Changelog", this.width / 2, 10, ColorUtil.getClickGUIColor().getRGB());
        this.drawCenteredString(this.fontRendererObj, "Latest Updates", this.width / 2, 20, ColorUtil.getClickGUIColor().getRGB());
        this.drawCenteredString(this.fontRendererObj, this.added, this.width / 2, 50, Color.GREEN.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.improved, this.width / 2, 160, Color.YELLOW.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.removed, this.width / 2, 250, Color.RED.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update1, this.width / 2, 60, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update2, this.width / 2, 70, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update3, this.width / 2, 80, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update4, this.width / 2, 90, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update5, this.width / 2, 100, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update6, this.width / 2, 110, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update7, this.width / 2, 120, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update8, this.width / 2, 130, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update9, this.width / 2, 140, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update10, this.width / 2, 170, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update11, this.width / 2, 180, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update12, this.width / 2, 190, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update13, this.width / 2, 200, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update14, this.width / 2, 210, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update15, this.width / 2, 220, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update16, this.width / 2, 230, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update17, this.width / 2, 240, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update18, this.width / 2, 270, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update19, this.width / 2, 280, Color.LIGHT_GRAY.getRGB());
        this.drawCenteredString(this.fontRendererObj, this.update20, this.width / 2, 290, Color.LIGHT_GRAY.getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
