package me.xatzdevelopments.xatz.client.modules;

import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.client.tools.Utils;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Teleport extends Module {

	@Override
	public ModSetting[] getModSettings() {
		// zoooom range slider

//		Slider zoooomRangeSlider = new BasicSlider("Teleport Range", ClientSettings.TPrange, 0.05, 10, 0.0,
//				ValueDisplay.DECIMAL);
//
//		zoooomRangeSlider.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.TPrange = slider.getValue();
//
//			}
//		});
		SliderSetting<Number> zoooomRangeSlider = new SliderSetting<Number>("Teleport Range", ClientSettings.TPrange, 0.05, 9.9, 0.0, ValueFormat.DECIMAL);
		return new ModSetting[] { zoooomRangeSlider };

	}

	public Teleport() {
		super("Teleport", Keyboard.KEY_NONE, Category.MOVEMENT, "Teleports you forward.");
	}

	@Override
	public void onDisable() {

		super.onDisable();
	}

	@Override
	public void onEnable() {

		double angleA = Math.toRadians(Utils.normalizeAngle(mc.thePlayer.rotationYawHead - 90));
		if (this.currentMode.equalsIgnoreCase("Client")) {
			mc.thePlayer.setPosition(mc.thePlayer.posX - Math.cos(angleA) * ClientSettings.TPrange, mc.thePlayer.posY,
					mc.thePlayer.posZ - Math.sin(angleA) * ClientSettings.TPrange);
		} else {
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(
					mc.thePlayer.posX - Math.cos(angleA) * ClientSettings.TPrange, mc.thePlayer.posY,
					mc.thePlayer.posZ - Math.sin(angleA) * ClientSettings.TPrange, mc.thePlayer.onGround));
		}
		this.setToggled(false, true);
		super.onEnable();
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
	}

	@Override
	public String[] getModes() {
		return new String[] { "Client", "Packet" };
	}
	
	public String getModeName() {
		return "Mode: ";
	}

	@Override
	public String getAddonText() {
		return this.currentMode;
	}

	public static void tpStatic(double range) {
		double angleA = Math.toRadians(Utils.normalizeAngle(mc.thePlayer.rotationYawHead - 90));
		mc.thePlayer.setPosition(mc.thePlayer.posX - Math.cos(angleA) * range, mc.thePlayer.posY,
				mc.thePlayer.posZ - Math.sin(angleA) * range);
	}

}
