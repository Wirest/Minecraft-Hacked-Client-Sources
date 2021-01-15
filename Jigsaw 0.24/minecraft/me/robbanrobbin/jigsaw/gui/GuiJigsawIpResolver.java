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

public class GuiJigsawIpResolver extends GuiScreen {
	private int offset = 40;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();
	private GuiTextField userField;
	public String result = "";

	public GuiJigsawIpResolver(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height - 50, 200, 20, "Done"));
		this.buttonList.add(new GuiButton(1, this.width / 2 + 2, this.height / 2 + 10 - offset, 98, 20, "Resolve"));
		this.buttonList.add(new GuiButton(69, this.width / 2 - 50, this.height / 2 + 30, 100, 20, "Geo-Locate"));
		this.buttonList.get(2).enabled = !result.isEmpty() && !result.contains("related to");
		userField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 2 + 10 - offset, 100, 20);
		userField.setMaxStringLength(254);
		userField.setFocused(true);
		userField.setText("");
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(before);
		}
		if (button.id == 1) {
			button.enabled = false;
			new Thread(new Runnable() {
				@Override
				public void run() {
					result = IPGetter.getIpForPlayer(userField.getText());
					buttonList.get(1).enabled = true;
					buttonList.get(2).enabled = !result.isEmpty() && !result.contains("related to");
				}
			}).start();
		}
		if (button.id == 69) {
			try {
				Class<?> oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
				oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
						new Object[] { new URI("https://geoiptool.com/en/?ip=" + result) });
			} catch (Exception e) {
				Jigsaw.chatMessage("§cCouldn't open link!");
				e.printStackTrace();
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
		super.updateScreen();
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, Jigsaw.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - offset) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7IP Resolver", this.width / 2 / 2, (this.height / 2 - 25 - offset) / 2, 0xffffffff);
		drawCenteredString(fontRendererObj, "§6" + result, (this.width / 2) / 2, (this.height / 2 + 42 - offset) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - offset) / 1, 0xffaaaaaa);
		drawString(fontRendererObj, "§7MC Username:", this.width / 2 - 100, this.height / 2 - offset, 0xffffffff);
		userField.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
