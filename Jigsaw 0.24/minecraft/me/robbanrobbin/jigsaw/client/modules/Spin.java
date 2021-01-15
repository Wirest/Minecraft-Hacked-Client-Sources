package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.events.UpdateEvent;
import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.client.tools.Utils;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;

public class Spin extends Module {

	private float spin = 0;

	@Override
	public ModSetting[] getModSettings() {
//		BasicSlider slider1 = new BasicSlider("Spin Speed", ClientSettings.Spinspeed, 10, 180, 0, ValueDisplay.DECIMAL);
//		slider1.addSliderListener(new SliderListener() {
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//				ClientSettings.Spinspeed = slider.getValue();
//			}
//		});
		SliderSetting<Number> slider1 = new SliderSetting<Number>("Spin Speed", ClientSettings.Spinspeed, 10, 180, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { slider1 };
	}

	public Spin() {
		super("Spin", Keyboard.KEY_NONE, Category.FUN, "You do 360s for other players!");
	}
	
	@Override
	public void onUpdate(UpdateEvent event) {
		if(Jigsaw.doDisablePacketSwitch()) {
			return;
		}
		spin += ClientSettings.Spinspeed;
		spin = Utils.normalizeAngle(spin);
		event.yaw = spin;
		super.onUpdate(event);
	}

}
