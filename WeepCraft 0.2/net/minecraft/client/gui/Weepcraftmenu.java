// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui;

import java.io.IOException;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.ISaveFormat;
import me.razerboy420.weepcraft.gui.deleteFiles.GuiDeleteFiles;
import me.razerboy420.weepcraft.gui.changelog.GuiChangelog;
import me.razerboy420.weepcraft.util.WebUtils;
import me.razerboy420.weepcraft.util.FileUtils;
import java.io.File;
import me.razerboy420.weepcraft.gui.clientsettings.GuiClientSettings;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import me.razerboy420.weepcraft.util.Wrapper;
import me.razerboy420.weepcraft.util.ColorUtil;
import me.razerboy420.weepcraft.Weepcraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.resources.I18n;

public class Weepcraftmenu extends GuiScreen
{ private final GuiScreen parentScreen;
	 public Weepcraftmenu(final GuiScreen parentScreen) {
		 this.parentScreen = parentScreen;
	    }
	@Override
    public void initGui() {
        final int j = this.height / 4 + 48;
        this.buttonList.add(new GuiButton(300, this.width / 2 + 120, j - 20, 90, 20, I18n.format("Client Settings", new Object[0])));
        this.buttonList.add(new GuiButton(301, this.width / 2 + 120, j + 0, 90, 20, I18n.format("Setting Folder", new Object[0])));
        this.buttonList.add(new GuiButton(302, this.width / 2 + 120, j + 20, 90, 20, I18n.format("Force Update", new Object[0])));
        this.buttonList.add(new GuiButton(303, this.width / 2 + 120, j + 40, 90, 20, I18n.format("Reset to default", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 + 120, j + 60, 90, 20, "Back"));
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.mc.getTextureManager().bindTexture(new ResourceLocation("weepcraft/ff1.jpg"));
        Gui.drawScaledCustomSizeModalRect(0, 0, 0.0f, 0.0f, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
        final String weepcraftString = String.valueOf(String.valueOf(ColorUtil.getColor(Weepcraft.primaryColor))) + "§lWeep" + ColorUtil.getColor(Weepcraft.secondaryColor) + "§lCraft";
        final int i = 274;
        final int j = this.width / 2 - 137;
        final int k = 30;
        this.drawGradientRect(0, 0, this.width, this.height, -2130706433, 16777215);
        this.drawGradientRect(0, 0, this.width, this.height, 0, Integer.MIN_VALUE);
        Gui.drawModalRectWithCustomSizedTexture(0, 0, 0.0f, 0.0f, this.width, this.height, this.width, this.height);
        this.drawGradientRect(0, 0, this.width, this.height, -1876942816, -1874837440);
        final int j2 = this.height / 4 + 48;
        Wrapper.drawBorderRect(this.width / 2 - 270, j2 - 1 -30, this.width / 2 + 230, j2 + 91 -1, 0x20ffffff, 0x20ffffff, 0.7f);
        GL11.glPushMatrix();
        GL11.glScaled(6.0, 6.0, 1.0);
        int x = this.width / 2;
        int y = 130;
        x /= 7.5;
        y /= 5.5;
        Gui.drawCenteredString(Wrapper.fr(), weepcraftString, x, y, -1);
        GL11.glPopMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.width / 2 + 90, 70.0f, 0.0f);
        GlStateManager.rotate(-20.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.popMatrix();
        String s = String.valueOf(String.valueOf(ColorUtil.getColor(Weepcraft.normalColor))) + "MC " + ColorUtil.getColor(Weepcraft.enabledColor) + "1.11.2";
        if (this.mc.isDemo()) {
            s = String.valueOf(String.valueOf(s)) + " Demo";
        }
        else {
            s = String.valueOf(String.valueOf(s)) + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : ("/" + this.mc.getVersionType()));
        }
        final String s2 = String.valueOf(String.valueOf(ColorUtil.getColor(Weepcraft.whiteColor))) + "Originally by " + ColorUtil.getColor(Weepcraft.whiteColor) + "FlyCoder";
        Gui.drawString(this.fontRendererObj, s2, 2.0f, this.height - 20, -1);
        final String s3 = String.valueOf(String.valueOf(ColorUtil.getColor(Weepcraft.whiteColor))) + "Remade by " + ColorUtil.getColor(Weepcraft.whiteColor) + "RazerBoy420";
        Gui.drawString(this.fontRendererObj, s3, 2.0f, this.height - 10, -1);
        super.drawScreen(mouseX, mouseY, partialTicks);
        final int scale = 5;
        GL11.glScalef((float)scale, (float)scale, (float)scale);
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (button.id == 0) {
        	  this.mc.displayGuiScreen(this.parentScreen);
        }
        if (button.id == 300) {
            this.mc.displayGuiScreen(new GuiClientSettings(this));
        }
        if (button.id == 5) {
            this.mc.displayGuiScreen(new GuiLanguage(this, this.mc.gameSettings, this.mc.getLanguageManager()));
        }
        if (button.id == 1) {
            this.mc.displayGuiScreen(new GuiWorldSelection(this));
        }
        if (button.id == 2) {
            this.mc.displayGuiScreen(new GuiMultiplayer(this));
        }
        if (button.id == 4) {
            this.mc.shutdown();
        }
        if (button.id == 11) {
        }
        if (button.id == 12) {
            final ISaveFormat isaveformat = this.mc.getSaveLoader();
            final WorldInfo worldinfo = isaveformat.getWorldInfo("Demo_World");
            if (worldinfo != null) {
                this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("selectWorld.deleteQuestion", new Object[0]), "'" + worldinfo.getWorldName() + "' " + I18n.format("selectWorld.deleteWarning", new Object[0]), I18n.format("selectWorld.deleteButton", new Object[0]), I18n.format("gui.cancel", new Object[0]), 12));
            }
        }
        if (button.id == 302) {
            try {
                try {
                    FileUtils.openFile(Wrapper.mc().mcDataDir + File.separator + "versions" + File.separator + "Weepcraft");
                }
                catch (Exception e) {
                    FileUtils.openFile(Wrapper.mc().mcDataDir + File.separator + "versions");
                }
                this.mc.shutdown();
            }
            catch (Exception ex) {}
        }
        if (button.id == 307) {
            Wrapper.mc().displayGuiScreen(new GuiChangelog());
        }
        if (button.id == 301) {
            FileUtils.openFile(Weepcraft.weepcraftDir);
        }
        if (button.id == 303) {
            Wrapper.mc().displayGuiScreen(new GuiDeleteFiles());
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
    }
}
