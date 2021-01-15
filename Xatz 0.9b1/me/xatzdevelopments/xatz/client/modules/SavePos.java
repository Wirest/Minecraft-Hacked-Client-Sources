package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;

public class SavePos extends Module {

	double lastX = 0;
	double lastY = 0;
	double lastZ = 0;

	boolean back = false;

	@Override
	public ModSetting[] getModSettings() {
//		final Slider slider1 = new BasicSlider("Max fall distance", ClientSettings.savePosHeight, 1.0, 10.0, 0.0,
//				ValueDisplay.DECIMAL);
//
//		slider1.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.savePosHeight = slider.getValue();
//			}
//		});
		SliderSetting<Number> slider1 = new SliderSetting<Number>("Fall Distance", ClientSettings.savePosHeight, 1.0, 10.0, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { slider1 };
	}

	public SavePos() {
		super("SavePos", Keyboard.KEY_NONE, Category.EXPLOITS,
				"Teleports you back up if you fall to far. This can use AAC and NCP to teleport you back up.");
	}

	@Override
	public void onUpdate() {
		if (mc.thePlayer.fallDistance > ClientSettings.savePosHeight) {
			if (this.currentMode.equals("AAC")) {
				mc.thePlayer.motionY = 2;
			}
			if (this.currentMode.equals("NCP")) {
				mc.thePlayer.motionY = -0.01;
			}
			if (this.currentMode.equals("Vanilla")) {
				mc.thePlayer.setPosition(lastX, lastY, lastZ);
			}
			back = true;
		}
		if (mc.thePlayer.fallDistance == 0 && mc.thePlayer.onGround) {
			lastX = mc.thePlayer.posX;
			lastY = mc.thePlayer.posY;
			lastZ = mc.thePlayer.posZ;
		}
		super.onUpdate();
	}

	@Override
	public void onLateUpdate() {
		if (back) {
			if(this.currentMode.equals("NCP")) {
				mc.thePlayer.setPosition(lastX, lastY, lastZ);
			}
			back = false;
		}
		super.onLateUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "AAC", "NCP", "Vanilla" };
	}
	
	public String getModeName() {
		return "Mode: ";
	}

}
