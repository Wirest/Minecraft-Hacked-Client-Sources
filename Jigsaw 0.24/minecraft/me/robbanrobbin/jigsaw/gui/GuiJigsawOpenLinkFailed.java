package me.robbanrobbin.jigsaw.gui;

import java.io.IOException;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;

public class GuiJigsawOpenLinkFailed extends GuiScreen {

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + 60, 200, 20, "Okay!"));
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();

		drawCenteredString(fontRendererObj, "Failed to open link!", width / 2, height / 2 - 60, 0xffffff);
		drawCenteredString(fontRendererObj, "You have to download it manually from the website:", width / 2,
				height / 2 - 50, 0xffffff);

		drawCenteredString(fontRendererObj, "http://jigsawclient.weebly.com/download.html", width / 2, height / 2 - 40,
				0xffffff);

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
