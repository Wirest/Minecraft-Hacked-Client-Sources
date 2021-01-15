package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;
import java.net.URI;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.tools.IPGetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawUseProxy extends GuiScreen {
	private int offset = 20;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();
	private GuiTextField userField;
	public String result = "";
	private boolean validProxy = false;

	public GuiJigsawUseProxy(GuiScreen before) {
		this.before = before;
		if(Jigsaw.proxy != null) {
			result = "§6Using proxy: §c" + Jigsaw.proxy[0] + ":" + Jigsaw.proxy[1];
		}
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height - 50, 200, 20, "Done"));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 40, this.height / 2 + 10 - offset, 70, 20, "Connect"));
		userField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 110, this.height / 2 + 10 - offset, 220, 20);
		userField.setMaxStringLength(254);
		userField.setFocused(true);
		userField.setText("");
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(before);
		}
		if (button.id == 1) {
			if(button.displayString.equals("Reset Proxy")) {
				System.setProperty("socksProxyHost", "");
				System.setProperty("socksProxyPort", "");
				Jigsaw.proxy = null;
				result = "";
			}
			else {
				String[] ipPort = userField.getText().split(":");
				try {
					System.setProperty("socksProxyHost", ipPort[0]);
					System.setProperty("socksProxyPort", ipPort[1]);
					result = "§6Using proxy: §c" + ipPort[0] + ":" + ipPort[1];
					Jigsaw.proxy = ipPort;
				}
				catch(Exception e) {
					result = e.getMessage();
					e.printStackTrace();
				}
			}
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
		if (closed = false) {
			mc.displayGuiScreen(before);
			closed = true;
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if(keyCode == 28) {
			this.actionPerformed(this.buttonList.get(1));
		}
		userField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		userField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void updateScreen() {
		userField.updateCursorCounter();
		boolean valid = userField.getText().length() > 4 && userField.getText().indexOf(":") != -1;
		int port = -1;
		if(userField.getText().split(":").length > 1) {
			try {
				port = Integer.parseInt(userField.getText().split(":")[1]);
			}
			catch(NumberFormatException e) {
				
			}
		}
		else {
			valid = false;
		}
		if(valid) {
			if(port > 65536 || port < 0) {
				valid = false;
			}
		}
		validProxy = valid;
		this.buttonList.get(1).displayString = valid ? "Connect" : "Reset Proxy";
		super.updateScreen();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, Jigsaw.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - offset) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Use a Poxy", this.width / 2 / 2, (this.height / 2 - 25 - offset) / 2, 0xffffffff);
		drawCenteredString(fontRendererObj, result, (this.width / 2) / 2, (this.height / 2 + 42 - offset) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - offset) / 1, 0xffaaaaaa);
		drawString(fontRendererObj, "§6§lSOCKS §6Proxy §8- §7IP:Port:", this.width / 2 - 110, this.height / 2 - offset, 0xffffffff);
		userField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
