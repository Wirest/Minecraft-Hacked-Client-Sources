package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.CheckBtnSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;

public class HUDColor extends Module {

	public ModSetting[] getModSettings() {
    	SliderSetting<Number> hudcolorred = new SliderSetting<Number>("Red", ClientSettings.hudcolorred, 0, 1, 0.00, ValueFormat.DECIMAL);
    	SliderSetting<Number> hudcolorgreen = new SliderSetting<Number>("Green", ClientSettings.hudcolorgreen, 0, 1, 0.00, ValueFormat.DECIMAL);
    	SliderSetting<Number> hudcolorblue = new SliderSetting<Number>("Blue", ClientSettings.hudcolorblue, 0, 1, 0.00, ValueFormat.DECIMAL);
		return new ModSetting[] { hudcolorred,  hudcolorgreen, hudcolorblue};
    }
	
	public HUDColor() {
		super("HUDColor", Keyboard.KEY_NONE, Category.SETTINGS);
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {
		setToggled(false, true);
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
