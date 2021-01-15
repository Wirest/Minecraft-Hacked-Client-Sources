package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class GuiJigsawUpdate2 extends GuiScreen {

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 60, 200, 20, "Okay!"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.25, 1.25, 1);
		drawCenteredString(fontRendererObj, "Don't complain if something isn't working because ",
				(int) (width / 2 / 1.25), (int) (height / 2 / 1.25 - 10), 0xffffff);
		drawCenteredString(fontRendererObj, "§cI probably fixed it in the " + "latest update which you just skipped :P",
				(int) (width / 2 / 1.25), (int) (height / 2 / 1.25), 0xffffff);
		GlStateManager.popMatrix();

		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		int id = button.id;
		if (id == 0) {
			mc.displayGuiScreen(new GuiMainMenu());
			Jigsaw.triedConnectToUpdate = true;
		}
		super.actionPerformed(button);
	}

	// No escape here
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {

	}

}
