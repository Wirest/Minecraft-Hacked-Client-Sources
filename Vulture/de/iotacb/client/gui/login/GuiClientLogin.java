package de.iotacb.client.gui.login;

import java.awt.Color;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import org.lwjgl.input.Keyboard;

import de.iotacb.client.Client;
import de.iotacb.client.gui.elements.textfields.GuiTextBox;
import de.iotacb.client.gui.elements.textfields.GuiTextBoxLogin;
import de.iotacb.client.utilities.misc.ClipboardUtil;
import de.iotacb.client.utilities.misc.LoginUtil;
import de.iotacb.client.utilities.render.BlurUtil;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiClientLogin extends GuiScreen {

	GuiTextBoxLogin loginBox;
	
	@Override
	public void initGui() {
		this.loginBox = new GuiTextBoxLogin(0, fontRendererObj, "Username", width / 2 - 150, height / 2 - 16, 300, 30);
		this.buttonList.add(new GuiButton(0, width / 2 - 150, height / 2 + 15, 300, 30, "Login").setBaseColor(new Color(0, 0, 0, 150)));
		this.buttonList.add(new GuiButton(1, width / 2 - 150, height / 2 + 46, 300, 30, "Get HWID").setBaseColor(new Color(0, 0, 0, 150)));
		Keyboard.enableRepeatEvents(true);
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(mouseX, mouseY);
		loginBox.drawTextBox(mouseX, mouseY);
		
		
		Client.RENDER2D.rect(width / 2 - 150, height / 2 - 47, 300, 30, new Color(0, 0, 0, 150));
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow("Login", (width - Client.INSTANCE.getFontManager().getDefaultFont().getWidth("Login")) / 2, height / 2 - 35, Color.white);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		super.onGuiClosed();
	}
	
	@Override
	public void updateScreen() {
		loginBox.updateCursorCounter();
		super.updateScreen();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_RETURN) {
			if (loginBox.getText().isEmpty()) return;
			Client.LOGIN_UTIL.loginClient(loginBox.getText());
		}
		loginBox.textboxKeyTyped(typedChar, keyCode);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		loginBox.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			if (loginBox.getText().isEmpty()) return;
			String login = loginBox.getText();
			if (login.contains(" ")) {
				login = login.replace(" ", "%20");
			}
			Client.LOGIN_UTIL.loginClient(login);
		} else if (button.id == 1) {
			try {
				ClipboardUtil.copyToClipboard(Client.LOGIN_UTIL.getHWID());
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		super.actionPerformed(button);
	}
	
}
