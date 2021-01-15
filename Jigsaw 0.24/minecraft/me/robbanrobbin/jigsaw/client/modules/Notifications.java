package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.CheckBtnSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.module.Module;

public class Notifications extends Module {

	@Override
	public ModSetting[] getModSettings() {
//		final BasicCheckButton box1 = new BasicCheckButton("On Hack Enabled");
//		box1.setSelected(ClientSettings.notificationModulesEnable);
//		box1.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.notificationModulesEnable = box1.isSelected();
//			}
//		});
//		
//		final BasicCheckButton box2 = new BasicCheckButton("On Hack Disabled");
//		box2.setSelected(ClientSettings.notificationModulesDisable);
//		box2.addButtonListener(new ButtonListener() {
//
//			@Override
//			public void onRightButtonPress(Button button) {
//
//			}
//
//			@Override
//			public void onButtonPress(Button button) {
//				ClientSettings.notificationModulesDisable = box2.isSelected();
//			}
//		});
		CheckBtnSetting box1 = new CheckBtnSetting("On Hack Enabled", "notificationModulesEnable");
		CheckBtnSetting box2 = new CheckBtnSetting("On Hack Disable", "notificationModulesDisable");
		return new ModSetting[]{box1, box2};
	}
	
	public Notifications() {
		super("Notifications", Keyboard.KEY_NONE, Category.SETTINGS);
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
