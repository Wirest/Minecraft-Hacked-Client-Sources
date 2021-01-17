package me.ihaq.iClient.gui.GuiWindows.altmanager;

import java.io.IOException;

import org.lwjgl.input.Keyboard;


import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

public final class GuiAltLogin extends GuiScreen {
	private PasswordField password;
	private final GuiScreen previousScreen;
	private AltLoginThread thread;
	private GuiTextField username;

	public GuiAltLogin(GuiScreen previousScreen) {
		this.previousScreen = previousScreen;
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			this.mc.displayGuiScreen(this.previousScreen);
			break;
		case 0:
			(this.thread = new AltLoginThread(this.username.getText(), this.password.getText())).start();
		}
	}

	@Override
	public void drawScreen(int x, int y, float z) {
		drawDefaultBackground();
		this.username.drawTextBox();
		this.password.drawTextBox();
		drawCenteredString(this.mc.fontRendererObj, "Alt Login", this.width / 2, 20, -1);
		drawCenteredString(this.mc.fontRendererObj,
				this.thread == null ? EnumChatFormatting.GRAY + "Idle..." : this.thread.getStatus(), this.width / 2, 29,
				-1);
		if (this.username.getText().isEmpty()) {
			drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368);
		}
		if (this.password.getText().isEmpty()) {
			drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368);
		}
		super.drawScreen(x, y, z);
	}

	@Override
	public void initGui() {
		int var3 = this.height / 4 + 24;
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, var3 + 72 + 12, "Login"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, var3 + 72 + 12 + 24, "Back"));
		this.username = new GuiTextField(var3, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
		this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
		this.username.setFocused(true);
		Keyboard.enableRepeatEvents(true);
	}

	@Override
	protected void keyTyped(char character, int key) {
		try {
			super.keyTyped(character, key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (character == '\t') {
			if ((!this.username.isFocused()) && (!this.password.isFocused())) {
				this.username.setFocused(true);
			} else {
				this.username.setFocused(this.password.isFocused());
				this.password.setFocused(!this.username.isFocused());
			}
		}
		if (character == '\r') {
			actionPerformed((GuiButton) this.buttonList.get(0));
		}
		this.username.textboxKeyTyped(character, key);
		this.password.textboxKeyTyped(character, key);
	}

	@Override
	protected void mouseClicked(int x, int y, int button) {
		try {
			super.mouseClicked(x, y, button);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.username.mouseClicked(x, y, button);
		this.password.mouseClicked(x, y, button);
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		this.username.updateCursorCounter();
		this.password.updateCursorCounter();
	}
}