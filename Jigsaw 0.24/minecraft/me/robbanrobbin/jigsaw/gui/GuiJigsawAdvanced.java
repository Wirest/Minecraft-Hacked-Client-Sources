package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawAdvanced extends GuiScreen {
	private int offset = -40;
	private GuiScreen before;
	private boolean closed = false;
	private Minecraft mc = Minecraft.getMinecraft();

	public GuiJigsawAdvanced(GuiScreen before) {
		this.before = before;
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(3, this.width / 2 - 100, height - 50, 200, 20, "Done"));
		this.buttonList.add(new GuiButton(8, this.width / 2 - 80, this.height / 2, 160, 20, "Reload GUI"));
		//this.buttonList.add(new GuiButton(9, this.width / 2 - 80, this.height / 2, 160, 20, "Reset Jigsaw"));

		// this.buttonList.add(new GuiButton(6, this.width / 2 + 2,
		// 94, 198, 20, "Account Hacker"));
	}

	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 3) {
			mc.displayGuiScreen(before);
		}
		if (button.id == 8) {
			Jigsaw.getClickGuiManager().reload();
		}
		if (button.id == 10) {
			
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
		drawCenteredString(fontRendererObj, Jigsaw.headerNoBrackets, this.width / 2 / 4, (this.height / 2 - 67) / 4, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawCenteredString(fontRendererObj, "§7Advanced", this.width / 2 / 2, (this.height / 2 - 25) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 1);
		drawHorizontalLine((this.width / 2 - 60) / 1, (this.width / 2 - 80 + 138) / 1, (this.height / 2 - 5) / 1, 0xffaaaaaa);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
