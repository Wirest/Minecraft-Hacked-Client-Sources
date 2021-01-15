package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;

public class ExtendedReach extends Module {

	@Override
	public ModSetting[] getModSettings() {
		// reach range slider

//		Slider reachRangeSlider = new BasicSlider("Extendedreach Range", ClientSettings.ExtendedReachrange, 3.5, 7.0,
//				0.0, ValueDisplay.DECIMAL);
//
//		reachRangeSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.ExtendedReachrange = (float) slider.getValue();
//
//			}
//		});
		SliderSetting<Number> reachRangeSlider = new SliderSetting<Number>("Reach", ClientSettings.ExtendedReachrange, 3.5, 7.0, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { reachRangeSlider };
	}

	public ExtendedReach() {
		super("ExtendedReach", Keyboard.KEY_NONE, Category.COMBAT,
				"Enables you to place blocks and hit entites further away. Warning! Don't mine blocks more than 5 blocks away!");
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
