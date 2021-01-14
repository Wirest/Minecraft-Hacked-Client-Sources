package store.shadowclient.client.gui.login;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import jdk.nashorn.api.scripting.URLReader;
import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.gui.login.elements.textfields.GuiTextBoxLogin;
import store.shadowclient.client.utils.gui.ClipboardUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class GuiClientLogin extends GuiScreen {

	GuiTextBoxLogin loginBox;
	
	@Override
	public void initGui() {

		this.loginBox = new GuiTextBoxLogin(0, fontRendererObj, "License Key", width / 2 - 150, height / 2 - 16, 300, 30);
		//this.buttonList.add(new GuiButton(0, width / 2 - 150, height / 2 + 15, 300, 30, "Login"));
		this.buttonList.add(new GuiButton(1, width / 2 - 150, height / 2 + 35, 300, 20, "Buy Shadow").setBaseColor(new Color(0, 0, 0, 150)));
		this.buttonList.add(new GuiButton(0, width / 2 - 150, height / 2 + 15, 300, 20, "Login").setBaseColor(new Color(0, 0, 0, 150)));
		Keyboard.enableRepeatEvents(true);
		super.initGui();
	}

	public static boolean isAllowedToRun() throws MalformedURLException, UnsupportedEncodingException, NoSuchAlgorithmException {
		BufferedReader urlReader = new BufferedReader(new URLReader(new URL("https://pastebin.com/raw/nyyviDqX")));
		return urlReader.lines().collect(Collectors.toCollection(ArrayList::new)).contains(getHWID());
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		drawBackground(mouseX);
		loginBox.drawTextBox(mouseX, mouseY);


		Shadow.RENDER2D.rect(width / 2 - 150, height / 2 - 47, 300, 30, new Color(0, 0, 0, 150));
		Shadow.fontManager.getFont("SFL 10").drawStringWithShadow("Login", (width - Shadow.fontManager.getFont("SFL 10").getWidth("Login")) / 2, height / 2 - 35, -1);
		Shadow.fontManager.getFont("SFL 10").drawStringWithShadow("Click (Buy Shadow)", (width - Shadow.fontManager.getFont("SFL 10").getWidth("Click (Buy Shadow)")) / 2, height / 2 - 85, -1);
		Shadow.fontManager.getFont("SFL 10").drawStringWithShadow("To copy the store link", (width - Shadow.fontManager.getFont("SFL 10").getWidth("To copy the store link")) / 2, height / 2 - 75, -1);
		
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
			if (loginBox.getText().isEmpty()) 
				return;
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
		String login = loginBox.getText();	
		if (button.id == 0) {
			if (loginBox.getText().isEmpty()) {
				return;
			} else if(button.id == 0) {
				if (login.contains("TEM8S2-2ET83-CGKP1-DPSI2-EPZO1") || login.contains("QAMIE2-2EUF3-C1521-EP372-PEDO1") || login.contains("GUA8E2-2KUE3-CLMA1-IDUE2-LMAO1")) {
					Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
				}
			}
			
		} else if (button.id == 1) {
				ClipboardUtil.copyToClipboard("shadowclient.store");
		}
		super.actionPerformed(button);
	}
	
	public static final String getHWID() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String s = "";
        final String main = System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name").trim();
        final byte[] bytes = main.getBytes("UTF-8");
        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        final byte[] md5 = messageDigest.digest(bytes);
        int i = 0;
        for (final byte b : md5) {
            s += Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3);
            if (i != md5.length - 1) {
                s += "-";
            }
            i++;
        }
        return s;
    }
	
}
