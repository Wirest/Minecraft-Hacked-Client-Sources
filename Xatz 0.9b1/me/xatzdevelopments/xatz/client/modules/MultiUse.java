package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;

public class MultiUse extends Module {
	@Override
	public ModSetting[] getModSettings() {
//		BasicSlider slider1 = new BasicSlider("Amount", ClientSettings.MultiUseamount, 1, 1000, 0,
//				ValueDisplay.INTEGER);
//		slider1.addSliderListener(new SliderListener() {
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//				ClientSettings.MultiUseamount = (int) Math.round(slider.getValue());
//			}
//		});
		SliderSetting<Integer> slider1 = new SliderSetting<Integer>("Amount", ClientSettings.MultiUseamount, 1, 1000, ValueFormat.INT);
		return new ModSetting[] { slider1 };
	}

	public MultiUse() {
		super("MultiUse", Keyboard.KEY_NONE, Category.FUN, "When you use something, it uses it again many times.");
	}

	@Override
	public void onRightClick() {
		for (int i = 0; i < ClientSettings.MultiUseamount; i++) {
			mc.rightClickMouse();
		}
		super.onRightClick();
	}

}
