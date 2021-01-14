package net.minecraft.client.gui;

import java.io.IOException;
import java.net.URI;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GuiScreenDemo extends GuiScreen {
    private static final Logger logger = LogManager.getLogger();
    private static final ResourceLocation field_146348_f = new ResourceLocation("textures/gui/demo_background.png");
    private static final String __OBFID = "CL_00000691";

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.clear();
        byte var1 = -16;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 116, this.height / 2 + 62 + var1, 114, 20, I18n.format("demo.help.buy", new Object[0])));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 2, this.height / 2 + 62 + var1, 114, 20, I18n.format("demo.help.later", new Object[0])));
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 1:
                button.enabled = false;

                try {
                    Class var2 = Class.forName("java.awt.Desktop");
                    Object var3 = var2.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
                    var2.getMethod("browse", new Class[]{URI.class}).invoke(var3, new Object[]{new URI("http://www.minecraft.net/store?source=demo")});
                } catch (Throwable var4) {
                    logger.error("Couldn\'t open link", var4);
                }

                break;

            case 2:
                this.mc.displayGuiScreen((GuiScreen) null);
                this.mc.setIngameFocus();
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
    }

    /**
     * Draws either a gradient over the background screen (when it exists) or a flat gradient over background.png
     */
    public void drawDefaultBackground() {
        super.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(field_146348_f);
        int var1 = (this.width - 248) / 2;
        int var2 = (this.height - 166) / 2;
        this.drawTexturedModalRect(var1, var2, 0, 0, 248, 166);
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        int var4 = (this.width - 248) / 2 + 10;
        int var5 = (this.height - 166) / 2 + 8;
        this.fontRendererObj.drawString(I18n.format("demo.help.title", new Object[0]), var4, var5, 2039583);
        var5 += 12;
        GameSettings var6 = this.mc.gameSettings;
        this.fontRendererObj.drawString(I18n.format("demo.help.movementShort", new Object[]{GameSettings.getKeyDisplayString(var6.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(var6.keyBindRight.getKeyCode())}), var4, var5, 5197647);
        this.fontRendererObj.drawString(I18n.format("demo.help.movementMouse", new Object[0]), var4, var5 + 12, 5197647);
        this.fontRendererObj.drawString(I18n.format("demo.help.jump", new Object[]{GameSettings.getKeyDisplayString(var6.keyBindJump.getKeyCode())}), var4, var5 + 24, 5197647);
        this.fontRendererObj.drawString(I18n.format("demo.help.inventory", new Object[]{GameSettings.getKeyDisplayString(var6.keyBindInventory.getKeyCode())}), var4, var5 + 36, 5197647);
        this.fontRendererObj.drawSplitString(I18n.format("demo.help.fullWrapped", new Object[0]), var4, var5 + 68, 218, 2039583);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
