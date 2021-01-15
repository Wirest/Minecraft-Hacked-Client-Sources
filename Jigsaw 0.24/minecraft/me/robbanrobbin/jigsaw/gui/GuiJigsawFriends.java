package me.robbanrobbin.jigsaw.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiJigsawFriends extends GuiScreen {

	private GuiScreen before;

	public GuiJigsawFriends(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 20, "Done"));
		super.initGui();
	}

}
