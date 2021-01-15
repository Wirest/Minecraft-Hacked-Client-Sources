package net.minecraft.client.gui;

import java.io.IOException;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

public class GuiGameOver extends GuiScreen implements GuiYesNoCallback {
	/**
	 * The integer value containing the number of ticks that have passed since
	 * the player's death
	 */
	private int enableButtonsTimer;
	private boolean field_146346_f = false;

	/**
	 * Adds the buttons (and other controls) to the screen in question. Called
	 * when the GUI is displayed and when the window resizes, the buttonList is
	 * cleared beforehand.
	 */
	public void initGui() {
		this.buttonList.clear();

		if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
			if (this.mc.isIntegratedServerRunning()) {
				this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96,
						I18n.format("deathScreen.deleteWorld", new Object[0])));
			} else {
				this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96,
						I18n.format("deathScreen.leaveServer", new Object[0])));
			}
		} else {
			this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 72,
					I18n.format("deathScreen.respawn", new Object[0])));
			this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 96,
					I18n.format("deathScreen.titleScreen", new Object[0])));

			if (this.mc.getSession() == null) {
				((GuiButton) this.buttonList.get(1)).enabled = false;
			}
		}
		
		for (GuiButton guibutton : this.buttonList) {
			guibutton.enabled = false;
		}
		if(Jigsaw.ghostMode) {
			return;
		}
		this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72 - 24, "Try GodMode (100)"));
	}

	/**
	 * Fired when a key is typed (except F11 which toggles full screen). This is
	 * the equivalent of KeyListener.keyTyped(KeyEvent e). Args : character
	 * (character on the key), keyCode (lwjgl Keyboard key code)
	 */
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
	}

	/**
	 * Called by the controls from the buttonList when activated. (Mouse pressed
	 * for buttons)
	 */
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			this.mc.thePlayer.respawnPlayer();
			this.mc.displayGuiScreen((GuiScreen) null);
			break;

		case 1:
			if (this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled()) {
				this.mc.displayGuiScreen(new GuiMainMenu());
			} else {
				GuiYesNo guiyesno = new GuiYesNo(this, I18n.format("deathScreen.quit.confirm", new Object[0]), "",
						I18n.format("deathScreen.titleScreen", new Object[0]),
						I18n.format("deathScreen.respawn", new Object[0]), 0);
				this.mc.displayGuiScreen(guiyesno);
				guiyesno.setButtonDelay(20);
			}
		case 2:
			Jigsaw.getModuleByName("GodMode").setToggled(true, true);
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
	 * Draws the screen and all the components in it. Args : mouseX, mouseY,
	 * renderPartialTicks
	 */
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawGradientRect(0, 0, this.width, this.height, 1615855616, -1602211792);
		GlStateManager.pushMatrix();
		GlStateManager.scale(2.0F, 2.0F, 2.0F);
		boolean flag = this.mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
		String s = flag ? I18n.format("deathScreen.title.hardcore", new Object[0])
				: I18n.format("deathScreen.title", new Object[0]);
		this.drawCenteredString(this.fontRendererObj, s, this.width / 2 / 2, 30, 16777215);
		GlStateManager.popMatrix();

		if (flag) {
			this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.hardcoreInfo", new Object[0]),
					this.width / 2, 144, 16777215);
		}
		if(!Jigsaw.ghostMode) {
			drawCenteredString(fontRendererObj, enableButtonsTimer > 100 ? "§7GodMode is only for Vanilla servers!" : "GodMode is only for Vanilla servers!", this.width / 2, this.height / 2 - 72 - 24, 0xffffffff);
		}
		this.drawCenteredString(this.fontRendererObj, I18n.format("deathScreen.score", new Object[0]) + ": "
				+ EnumChatFormatting.YELLOW + this.mc.thePlayer.getScore(), this.width / 2, 100, 16777215);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	/**
	 * Returns true if this GUI should pause the game when it is displayed in
	 * single-player
	 */
	public boolean doesGuiPauseGame() {
		return false;
	}

	/**
	 * Called from the main game loop to update the screen.
	 */
	public void updateScreen() {
		super.updateScreen();
		++this.enableButtonsTimer;
		for (GuiButton guibutton : this.buttonList) {
			if(guibutton.displayString.startsWith("Try GodMode (")) {
				guibutton.displayString = "Try GodMode (" + (-(Math.min(this.enableButtonsTimer, 100) - 100)) + ")";
			}
		}
		if (this.enableButtonsTimer == 20) { //TODO this.enableButtonsTimer == 20 var den f§rst
			for (GuiButton guibutton : this.buttonList) {
				guibutton.enabled = true;
			}
		}
		if (this.enableButtonsTimer == 100) { //TODO this.enableButtonsTimer == 20 var den f§rst
			for (GuiButton guibutton : this.buttonList) {
				if(guibutton.displayString.startsWith("Try GodMode (")) {
					guibutton.enabled = false;
				}
			}
		}
	}
}
