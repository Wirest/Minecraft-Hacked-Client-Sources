package me.xatzdevelopments.xatz.client.modules;

import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.gui.custom.clickgui.DisplayClickGui;
import me.xatzdevelopments.xatz.module.Module;

public class ClickGUI extends Module {

	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_RSHIFT, Category.HIDDEN, "Well, this is kind of self explanatory.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

//		if (!(mc.currentScreen instanceof GuiManagerDisplayScreen) && !Xatz.ghostMode) {
//			mc.displayGuiScreen(new GuiManagerDisplayScreen(Xatz.getGUIMananger()));
//		}
		if(!(mc.currentScreen instanceof DisplayClickGui) && !Xatz.ghostMode) {
			mc.displayGuiScreen(new DisplayClickGui(false));
		}
		setToggled(false, true);
		super.onEnable();
	}

	@Override
	public void onRender() {

	}
}
