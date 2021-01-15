package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Regen extends Module {

	@Override
	public ModSetting[] getModSettings() {
		// regenSpeedSlider

//		Slider regenSpeedSlider = new BasicSlider("Regen Packets", Regen.getSpeed(), 10.0, 1000.0, 0.0,
//				ValueDisplay.INTEGER);
//
//		regenSpeedSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				Regen.setSpeed(slider.getValue());
//
//			}
//		});
		SliderSetting<Number> regenSpeedSlider = new SliderSetting<Number>("Regen Packets", ClientSettings.Regenspeed, 10.0, 1000.0, 0.0, ValueFormat.INT);
		return new ModSetting[] { regenSpeedSlider };
	}

	public Regen() {
		super("Regen", Keyboard.KEY_NONE, Category.PLAYER, "Regens health (and drains food) more quickly");
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

		// if(mc.gameSettings.keyBindUseItem.isKeyDown()) {
		// return;
		// }
		if (!mc.thePlayer.capabilities.isCreativeMode && mc.thePlayer.getFoodStats().getFoodLevel() > 17
				&& mc.thePlayer.getHealth() < mc.thePlayer.getMaxHealth() && mc.thePlayer.getHealth() != 0
				&& !Xatz.getModuleByName("Blink").isToggled()) {

			for (int i = 0; i < ClientSettings.Regenspeed; i++) {
				mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
			}

		}

		super.onUpdate();
	}

	public static void setSpeed(double set) {
		ClientSettings.Regenspeed = set;
	}

	public static double getSpeed() {
		return ClientSettings.Regenspeed;
	}

}
