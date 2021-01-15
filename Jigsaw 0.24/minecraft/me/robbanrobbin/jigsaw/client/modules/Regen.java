package me.robbanrobbin.jigsaw.client.modules;

import org.lwjgl.input.Keyboard;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.client.settings.ClientSettings;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ModSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.SliderSetting;
import me.robbanrobbin.jigsaw.gui.custom.clickgui.ValueFormat;
import me.robbanrobbin.jigsaw.module.Module;
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
				&& !Jigsaw.getModuleByName("Blink").isToggled()) {

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
