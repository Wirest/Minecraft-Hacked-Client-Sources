package me.robbanrobbin.jigsaw.client.modules;

import org.darkstorm.minecraft.gui.util.GuiManagerDisplayScreen;
import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.DisplayClickGui;
import me.robbanrobbin.jigsaw.module.Module;

public class ClickGUI extends Module {

	public ClickGUI() {
		super("ClickGUI", Keyboard.KEY_LCONTROL, Category.HIDDEN, "Well, this is kind of self explanatory.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

//		if (!(mc.currentScreen instanceof GuiManagerDisplayScreen) && !Jigsaw.ghostMode) {
//			mc.displayGuiScreen(new GuiManagerDisplayScreen(Jigsaw.getGUIMananger()));
//		}
		if(!(mc.currentScreen instanceof DisplayClickGui) && !Jigsaw.ghostMode) {
			mc.displayGuiScreen(new DisplayClickGui(false));
		}
		setToggled(false, true);
		super.onEnable();
	}

	@Override
	public void onRender() {

	}
}
