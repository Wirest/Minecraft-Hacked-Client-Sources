package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawAlert extends GuiScreen {

	public final String text;

	public GuiJigsawAlert(String alert) {
		System.err.println("Got alert message with text: " + alert);
		this.text = alert;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 60, 200, 20, "Okay LET ME PLAY"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();

		GlStateManager.pushMatrix();
		GlStateManager.scale(1.5, 1.5, 1);
		drawCenteredString(fontRendererObj, "§cAlert!", (int) (width / 2 / 1.5), 30, 0xffffff);
		GlStateManager.popMatrix();

		GlStateManager.pushMatrix();
		GlStateManager.scale(0.8, 0.8, 1);
		drawCenteredString(fontRendererObj, text, (int) (width / 2 / 0.8), 170, 0xffffff);
		GlStateManager.popMatrix();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
		if (id == 0) {
			mc.displayGuiScreen(new GuiMainMenu());
		}
		super.actionPerformed(button);
	}

	// No escape here
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {

	}

}
