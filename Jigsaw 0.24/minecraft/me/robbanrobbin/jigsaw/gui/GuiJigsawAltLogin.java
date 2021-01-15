package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.alts.Alt;
import me.robbanrobbin.jigsaw.client.alts.Login;
import me.robbanrobbin.jigsaw.gui.animations.Animation;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiErrorScreen;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class GuiJigsawAltLogin extends GuiScreen {

	private GuiScreen before;
	private GuiTextField emailField;
	private GuiPasswordField passwordField;
	
	public GuiJigsawAltLogin(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + 60, "Log in"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 85, "Back"));
		emailField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 2 - 10, 200, 20);
		emailField.setMaxStringLength(254);
		emailField.setFocused(true);

		passwordField = new GuiPasswordField(3, this.fontRendererObj, this.width / 2 - 100, this.height / 2 + 25, 200, 20);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if (button.id == 0) {
			Alt loginAlt = new Alt(emailField.getText(), passwordField.getText());
			if (loginAlt.cracked) {
				Login.changeName(loginAlt.name);
				mc.displayGuiScreen(new GuiErrorScreen("Success!", "You now switched accounts! (cracked)"));
			} else {
				try {
					Login.login(loginAlt.email, loginAlt.password);
					mc.displayGuiScreen(new GuiErrorScreen("Success!", "You now switched accounts! (premium)"));
				} catch (Exception e) {
					e.printStackTrace();
					mc.displayGuiScreen(new GuiErrorScreen("Error", "Could not log in..."));
				}
			}

		}
		if (button.id == 1) {
			mc.displayGuiScreen(before);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		emailField.textboxKeyTyped(typedChar, keyCode);
		passwordField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		emailField.mouseClicked(mouseX, mouseY, mouseButton);
		passwordField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		emailField.updateCursorCounter();
		passwordField.updateCursorCounter();
		super.updateScreen();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		emailField.drawTextBox();
		passwordField.drawTextBox();
		this.fontRendererObj.drawString("§7E-mail / Username: ", this.width / 2 - 100, this.height / 2 - 21, 0xffffff);
		this.fontRendererObj.drawString("§7Password: ", this.width / 2 - 100, this.height / 2 + 14, 0xffffff);
		drawCenteredString(fontRendererObj, "§c(Leave password blank for cracked login)", this.width / 2,
				this.height / 2 + 50, 0xffffff);
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, "Alt Login", this.width / 2 / 4, (this.height / 2 - 100) / 4, 0xffffff);
		GlStateManager.scale(0.25, 0.25, 1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
