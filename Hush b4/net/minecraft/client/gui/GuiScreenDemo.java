// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.gui;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.renderer.GlStateManager;
import java.io.IOException;
import java.net.URI;
import net.minecraft.client.resources.I18n;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.Logger;

public class GuiScreenDemo extends GuiScreen
{
    private static final Logger logger;
    private static final ResourceLocation field_146348_f;
    
    static {
        logger = LogManager.getLogger();
        field_146348_f = new ResourceLocation("textures/gui/demo_background.png");
    }
    
    @Override
    public void initGui() {
        this.buttonList.clear();
        final int i = -16;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + i, 114, 20, I18n.format("demo.help.buy", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + i, 114, 20, I18n.format("demo.help.later", new Object[0])));
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 1: {
                button.enabled = false;
                try {
                    final Class<?> oclass = Class.forName("java.awt.Desktop");
                    final Object object = oclass.getMethod("getDesktop", (Class<?>[])new Class[0]).invoke(null, new Object[0]);
                    oclass.getMethod("browse", URI.class).invoke(object, new URI("http://www.minecraft.net/store?source=demo"));
                }
                catch (Throwable throwable) {
                    GuiScreenDemo.logger.error("Couldn't open link", throwable);
                }
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(null);
                this.mc.setIngameFocus();
                break;
            }
        }
    }
    
    @Override
    public void updateScreen() {
        super.updateScreen();
    }
    
    @Override
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(GuiScreenDemo.field_146348_f);
        final int i = (this.width - 248) / 2;
        final int j = (this.height - 166) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, 248, 166);
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        final int i = (this.width - 248) / 2 + 10;
        int j = (this.height - 166) / 2 + 8;
        this.fontRendererObj.drawString(I18n.format("demo.help.title", new Object[0]), i, j, 2039583);
        j += 12;
        final GameSettings gamesettings = this.mc.gameSettings;
        this.fontRendererObj.drawString(I18n.format("demo.help.movementShort", GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode())), i, j, 5197647);
        this.fontRendererObj.drawString(I18n.format("demo.help.movementMouse", new Object[0]), i, j + 12, 5197647);
        this.fontRendererObj.drawString(I18n.format("demo.help.jump", GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode())), i, j + 24, 5197647);
        this.fontRendererObj.drawString(I18n.format("demo.help.inventory", GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode())), i, j + 36, 5197647);
        this.fontRendererObj.drawSplitString(I18n.format("demo.help.fullWrapped", new Object[0]), i, j + 68, 218, 2039583);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
