package net.minecraft.client.gui;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.Iterator;

public class GuiGameOver extends GuiScreen implements GuiYesNoCallback {
    private int field_146347_a;
    private boolean field_146346_f = false;
    private static final String __OBFID = "CL_00000690";

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui() {
        this.buttonList.clear();

        if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
            if (this.mc.isIntegratedServerRunning()) {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.deleteWorld", new Object[0])));
            } else {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.leaveServer", new Object[0])));
            }
        } else {
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72, I18n.format("deathScreen.respawn", new Object[0])));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96, I18n.format("deathScreen.titleScreen", new Object[0])));

            if (this.mc.getSession() == null) {
                ((GuiButton) this.buttonList.get(1)).enabled = false;
            }
        }

        GuiButton var2;

        for (Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = false) {
            var2 = (GuiButton) var1.next();
        }
    }

    /**
     * Fired when a key is typed (except F11 who toggle full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id) {
            case 0:
                this.mc.thePlayer.respawnPlayer();
                this.mc.displayGuiScreen((GuiScreen) null);
                break;

            case 1:
                GuiYesNo var2 = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "", I18n.format("deathScreen.titleScreen", new Object[0]), I18n.format("deathScreen.respawn", new Object[0]), 0);
                this.mc.displayGuiScreen(var2);
                var2.setButtonDelay(20);
        }
    }

    public void confirmClicked(boolean result, int id) {
        if (result) {
            this.mc.theWorld.sendQuittingDisconnectingPacket();
            this.mc.loadWorld((WorldClient) null);
            this.mc.displayGuiScreen(new GuiMainMenu());
        } else {
            this.mc.thePlayer.respawnPlayer();
            this.mc.displayGuiScreen((GuiScreen) null);
        }
    }

    /**
     * Draws the screen and all the components in it. Args : mouseX, mouseY, renderPartialTicks
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
        GlStateManager.pushMatrix();
        GlStateManager.scale(2.0F, 2.0F, 2.0F);
        boolean var4 = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        String var5 = var4 ? I18n.format("deathScreen.title.hardcore", new Object[0]) : I18n.format("deathScreen.title", new Object[0]);
        this.drawCenteredString(this.fontRendererObj, var5, this.width / 2 / 2, 30, 16777215);
        GlStateManager.popMatrix();

        if (var4) {
            this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo", new Object[0]), this.width / 2, 144, 16777215);
        }

        this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": " + EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen() {
        super.updateScreen();
        ++this.field_146347_a;
        GuiButton var2;

        if (this.field_146347_a == 20) {
            for (Iterator var1 = this.buttonList.iterator(); var1.hasNext(); var2.enabled = true) {
                var2 = (GuiButton) var1.next();
            }
        }
    }
}
