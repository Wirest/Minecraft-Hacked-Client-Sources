package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.cracker.gui.GuiJigsawAccHacker;
import me.robbanrobbin.jigsaw.hackerdetect.gui.GuiJigsawHackerDetect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawTools extends GuiScreen {
	private int offset = 22;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiJigsawTools(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, height - 50, 200, 20, "Done"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 80, this.height / 2 - offset, 160, 20, "Account Cracker"));
		this.buttonList.add(new GuiButton(2, this.width / 2 - 80, this.height / 2 + 22 - offset, 160, 20, "IP Resolver"));
		this.buttonList.add(new GuiButton(7, this.width / 2 - 80, this.height / 2 + 44 - offset, 160, 20, "HackerDetector"));
		//this.buttonList.add(new GuiButton(8, this.width / 2 - 80, this.height / 2 + 66 - offset, 160, 20, "Use a Proxy"));
		this.buttonList.get(1).enabled = false;
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 0) {
			mc.displayGuiScreen(before);
		}
		if (button.id == 1) {
			mc.displayGuiScreen(new GuiJigsawAccHacker(this));
		}
		if (button.id == 2) {
			mc.displayGuiScreen(new GuiJigsawIpResolver(this));
		}
		if (button.id == 7) {
			mc.displayGuiScreen(new GuiJigsawHackerDetect(this));
		}
		if (button.id == 8) {
			mc.displayGuiScreen(new GuiJigsawUseProxy(this));
		}
	}

	public boolean doesGuiPauseGame() {
		return true;
	}

	public void onGuiClosed() {
		if (closed = false) {
			mc.displayGuiScreen(before);
			closed = true;
		}
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		GlStateManager.scale(4, 4, 1);
		drawCenteredString(fontRendererObj, Jigsaw.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67 - offset) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Tools", this.width / 2 / 2, (this.height / 2 - 25 - offset) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5 - offset) / 1, 0xffaaaaaa);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
