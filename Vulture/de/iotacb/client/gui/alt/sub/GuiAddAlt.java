package de.iotacb.client.gui.alt.sub;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import de.iotacb.client.Client;
import de.iotacb.client.file.files.AltManagerFile;
import de.iotacb.client.file.files.PrixGenFile;
import de.iotacb.client.file.files.VultureGenFile;
import de.iotacb.client.gui.alt.AltSlot;
import de.iotacb.client.gui.alt.GuiAltManager;
import de.iotacb.client.gui.alt.util.AltLogin;
import de.iotacb.client.gui.elements.textfields.GuiTextBox;
import de.iotacb.client.gui.elements.textfields.GuiTextBoxLogin;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import wriva.core.Writer;

public class GuiAddAlt extends GuiScreen {
	
	private GuiTextBoxLogin emailField, passwordField;
	
	private final GuiAltManager prevScreen;
	
	public GuiAddAlt(GuiAltManager prev) {
		this.prevScreen = prev;
	}
	
	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		
		final int offset = 30;
		
		this.emailField = new GuiTextBoxLogin(0, fontRendererObj, "Email, Vulture or TheAltening Token...", width / 2 - 150, height / 2 - 100 + offset, 300, 30);
		this.passwordField = new GuiTextBoxLogin(1, fontRendererObj, "Password. Leave empty when using token...", width / 2 - 150, height / 2 - 69 + offset, 300, 30);
		
		
		buttonList.add(new GuiButton(0, width / 2 - 150, height / 2 - 38 + offset, 150, 30, "Add"));
		buttonList.add(new GuiButton(1, width / 2 + 1, height / 2 - 38 + offset, 149, 30, "Clipboard"));
		buttonList.add(new GuiButton(2, width / 2 - 150, height / 2 - 7 + offset, 300, 30, "Generate (Prix)", Client.INSTANCE.isPrixGenLoggedIn()));
		buttonList.add(new GuiButton(3, width / 2 - 150, height / 2 + 24 + offset, 300, 30, "Generate (Vulture)", Client.INSTANCE.isVultureGenLoggedIn()));
		buttonList.add(new GuiButton(4, width / 2 - 150, height / 2 + 55 + offset, 300, 30, "Cancel"));
		
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawBackground(mouseX, mouseY);
		
		emailField.drawTextBox(mouseX, mouseY);
		passwordField.drawTextBox(mouseX, mouseY);
		
		Client.RENDER2D.rect(width / 2 - 150, height / 2 - 101, 300, 30, new Color(0, 0, 0, 150));
		Client.INSTANCE.getFontManager().getDefaultFont().drawStringWithShadow("Add alt", (width - Client.INSTANCE.getFontManager().getDefaultFont().getWidth("Add alt")) / 2, height / 2 - 90, Color.white);
		
		Client.INSTANCE.getNotificationManager().draw(0);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		switch (button.id) {
		case 0:
			if (emailField.getText().isEmpty()) return;
			addAlt(emailField.getText(), passwordField.getText());
			mc.displayGuiScreen(prevScreen);
			break;

		case 1:
			final String clipboard = getClipboardString();
			String email = clipboard;
			String password = "";
			
			if (clipboard.contains(":")) {
				email = clipboard.split(":")[0];
				password = clipboard.split(":")[1];
			}
			
			if (email.isEmpty()) return;
			
			addAlt(email, password);
			
			mc.displayGuiScreen(prevScreen);
			break;
			
		case 2:
			final String[] prixGenDetails = ((PrixGenFile) Client.INSTANCE.getFileManager().getFileByClass(PrixGenFile.class)).readPrix();
			if (prixGenDetails != null) {
				final String alt = AltLogin.gerateAccountPrixGen(prixGenDetails[0], prixGenDetails[1]);
				if (alt.contains(":")) {
					addAlt(alt.split(":")[0], alt.split(":")[1]);
					mc.displayGuiScreen(prevScreen);
				} else {
					Client.INSTANCE.getNotificationManager().addNotification("PrixGen", "Something went wrong!");
				}
			}
			break;
			
		case 3:
			final String[] vultureGenDetails = ((VultureGenFile) Client.INSTANCE.getFileManager().getFileByClass(VultureGenFile.class)).readVulture();
			if (vultureGenDetails != null) {
				final String alt = AltLogin.gerateTokenVultureGen(vultureGenDetails[0], vultureGenDetails[1]);
				if (alt.length() > 3) {
					addAlt(alt, "");
					mc.displayGuiScreen(prevScreen);
				} else {
					Client.INSTANCE.getNotificationManager().addNotification("VultureGen", "Something went wrong!");
				}
			}
			break;
			
		case 4:
			mc.displayGuiScreen(prevScreen);
			break;
		}
		super.actionPerformed(button);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		emailField.textboxKeyTyped(typedChar, keyCode);
		passwordField.textboxKeyTyped(typedChar, keyCode);
		
		if (keyCode == Keyboard.KEY_ESCAPE) {
			mc.displayGuiScreen(prevScreen);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		emailField.mouseClicked(mouseX, mouseY, mouseButton);
		passwordField.mouseClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void updateScreen() {
		emailField.updateCursorCounter();
		passwordField.updateCursorCounter();
		super.updateScreen();
	}
	
	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(true);
		super.onGuiClosed();
	}
	
	private void addAlt(String email, String password) {
		String type = AltLogin.getType(email, password);
		if (type == "M") {
			type = "MOJANG";
		} else if (type == "TA") {
			type = "THEALTENING";
		} else if (type == "V") {
			type = "VULTURE";
		} else {
			type = "CRACKED";
		}
		Writer.INSTANCE.appendString(Client.INSTANCE.getFileManager().getFileByClass(AltManagerFile.class).getPath(), email.concat(":").concat(password), -1);
		Client.INSTANCE.getNotificationManager().addNotification("Alt Manager", "The alt has been added");
	}
	
}
