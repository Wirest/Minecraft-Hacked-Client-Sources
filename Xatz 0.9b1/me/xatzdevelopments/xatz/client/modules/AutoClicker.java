package me.xatzdevelopments.xatz.client.modules;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import me.xatzdevelopments.xatz.utils.WaitTimer;

public class AutoClicker extends Module {

	WaitTimer timer = new WaitTimer();
	Random rand = new Random();

	@Override
	public ModSetting[] getModSettings() {
//		final Slider aps = new BasicSlider("Max CPS", ClientSettings.AutoClickermax, 1.0, 20.0, 0.0,
//				ValueDisplay.INTEGER);
//		aps.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.AutoClickermax = (int) Math.round(slider.getValue());
//
//			}
//		});
//		Slider aps2 = new BasicSlider("Min CPS", ClientSettings.AutoClickermin, 1.0, 20.0, 0.0, ValueDisplay.INTEGER) {
//			@Override
//			public void update() {
//				if (this.getValue() > aps.getValue()) {
//					this.setValue(aps.getValue());
//				}
//				super.update();
//			}
//		};
//		aps2.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.AutoClickermin = (int) Math.round(slider.getValue());
//
//			}
//		});
//
//		Slider aps3 = new BasicSlider("Hit Chance", ClientSettings.AutoClickerhitPercent, 0.0, 1.0, 0.0,
//				ValueDisplay.PERCENTAGE);
//		aps3.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.AutoClickerhitPercent = slider.getValue();
//
//			}
//		});
		return new ModSetting[] { new SliderSetting<Number>("Max CPS", ClientSettings.AutoClickermax, 1.0, 20.0, 0.0, ValueFormat.DECIMAL),
				new SliderSetting<Number>("Min CPS", ClientSettings.AutoClickermin, 1.0, 20.0, 0.0, ValueFormat.DECIMAL), 
				new SliderSetting<Number>("Hit Chance", ClientSettings.AutoClickerhitPercent, 0.0, 1.0, 0.0, ValueFormat.PERCENT) };
	}

	public AutoClicker() {
		super("AutoClicker", Keyboard.KEY_NONE, Category.COMBAT,
				"Automatically clicks when holding down the attack button.");
	}

	@Override
	public void onToggle() {
		timer.reset();
		super.onToggle();
	}

	@Override
	public void onUpdate() {
		if (shouldClick()) {
			int a = 1000 / randInt(ClientSettings.AutoClickermin, ClientSettings.AutoClickermax);
			// Xatz.chatMessage(a);
			if (!timer.hasTimeElapsed(a, true)) {
				return;
			}
			// Xatz.chatMessage("click");
			if (rand.nextDouble() <= ClientSettings.AutoClickerhitPercent) {
				Xatz.click();
			} else {
				mc.thePlayer.swingItem();
			}

		} else {
			timer.reset();
		}
		super.onUpdate();
	}

	public static boolean shouldClick() {
		return mc.gameSettings.keyBindAttack.pressed && Xatz.getModuleByName("AutoClicker").isToggled();
	}

	public int randInt(int min, int max) {
		return rand.nextInt((max - min) + 1) + min;
	}
}
