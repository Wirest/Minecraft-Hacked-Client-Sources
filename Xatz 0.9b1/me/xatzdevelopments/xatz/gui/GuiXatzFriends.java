package me.xatzdevelopments.xatz.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class GuiXatzFriends extends GuiScreen {

	private GuiScreen before;

	public GuiXatzFriends(GuiScreen before) {
		this.before = before;
	}

	@Override
	public void initGui() {
		this.buttonList.add(new GuiButton(0, width / 2 - 100, height - 20, "Done"));
		super.initGui();
	}

}
