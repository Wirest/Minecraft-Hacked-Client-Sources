package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.events.UpdateEvent;
import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;

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
		if(Xatz.doDisablePacketSwitch()) {
			return;
		}
		spin += ClientSettings.Spinspeed;
		spin = Utils.normalizeAngle(spin);
		event.yaw = spin;
		super.onUpdate(event);
	}

}
