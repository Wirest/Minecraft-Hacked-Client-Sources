package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;

public class NameTags extends Module {
	@Override
	public ModSetting[] getModSettings() {
		// nameTagsSlider

//		Slider nameTagsSlider = new BasicSlider("Size", ClientSettings.Nametagssize, 2.0, 10.0, 0.0,
//				ValueDisplay.INTEGER);
//
//		nameTagsSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.Nametagssize = (float) (slider.getValue());
//
//			}
//		});
		SliderSetting<Number> nameTagsSlider = new SliderSetting<Number>("Tag Size", ClientSettings.Nametagssize, 2.0, 10.0, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { nameTagsSlider };
	}

	public NameTags() {
		super("NameTags", Keyboard.KEY_NONE, Category.RENDER, "Changes nametags.");
	}

	@Override
	public void onDisable() {
		super.onDisable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

}
