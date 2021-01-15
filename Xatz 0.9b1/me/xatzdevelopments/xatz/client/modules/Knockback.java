package me.xatzdevelopments.xatz.client.modules;

import org.darkstorm.minecraft.gui.component.Component;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent.ValueDisplay;
import org.darkstorm.minecraft.gui.component.basic.BasicSlider;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.lwjgl.input.Keyboard;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.module.state.Category;
import me.xatzdevelopments.xatz.client.settings.ClientSettings;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ModSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.SliderSetting;
import me.xatzdevelopments.xatz.gui.custom.clickgui.ValueFormat;
import me.xatzdevelopments.xatz.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.server.S06PacketUpdateHealth;
import net.minecraft.network.play.server.S12PacketEntityVelocity;

public class Knockback extends Module {
	
	@Override
	public ModSetting[] getModSettings() {
//		Slider vert = new BasicSlider("Vertical Knockback", ClientSettings.KBVertical, 0.0, 1.0, 0.0,
//				ValueDisplay.PERCENTAGE);
//		vert.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.KBVertical = slider.getValue();
//
//			}
//		});
//		
//		Slider hori = new BasicSlider("Horizontal Knockback", ClientSettings.KBHorizontal, 0.0, 1.0, 0.0,
//				ValueDisplay.PERCENTAGE);
//		hori.addSliderListener(new SliderListener() {
//
//			@Override
//			public void onSliderValueChanged(Slider slider) {
//
//				ClientSettings.KBHorizontal = slider.getValue();
//
//			}
//		});
		return new ModSetting[]{new SliderSetting<Number>("Vertical", ClientSettings.KBVertical, 0.0, 1.0, 0.0, ValueFormat.PERCENT), 
				new SliderSetting<Number>("Horizontal", ClientSettings.KBHorizontal, 0.0, 1.0, 0.0, ValueFormat.PERCENT)};
	}

	public Knockback() {
		super("Knockback", Keyboard.KEY_NONE, Category.PLAYER,
				"Modifies your knockback.");
	}
	
	public String[] getModes() {
		return new String[] { "Normal", "AAC"};
		
	}
	
	public String getModeName() {
		return "Mode: ";
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
	
	@Override
	public String getAddonText() {
		return Math.round(ClientSettings.KBVertical * 100d) / 100d + ", " + Math.round(ClientSettings.KBHorizontal * 100d) / 100d;
	}

	@Override
	public void onPacketRecieved(AbstractPacket packetIn) {
		if(this.currentMode.equalsIgnoreCase("Normal")) {
		if (packetIn instanceof S12PacketEntityVelocity) {
			Entity entity = mc.getNetHandler().clientWorldController
					.getEntityByID(((S12PacketEntityVelocity) packetIn).getEntityID());
			if (entity instanceof EntityPlayerSP) {
				S12PacketEntityVelocity vel = ((S12PacketEntityVelocity) packetIn);
				packetIn.cancel();
				mc.thePlayer.addVelocity(((double) vel.getMotionX() / 8000.0D) * ClientSettings.KBHorizontal, 
						((double) vel.getMotionY() / 8000.0D) * ClientSettings.KBVertical,
						((double) vel.getMotionZ() / 8000.0D) * ClientSettings.KBHorizontal);
			}
		}
		super.onPacketRecieved(packetIn);
	  }
		else if(this.currentMode.equalsIgnoreCase("AAC")) {
			if (!mc.gameSettings.keyBindJump.pressed) {
	            if (mc.thePlayer.hurtTime > 0) {
	                mc.thePlayer.onGround = true;
	            }
	        } else {
	            if (mc.thePlayer.hurtTime > 0) {
	                mc.thePlayer.motionX *= 0.15;
	                mc.thePlayer.motionZ *= 0.15;

	            }
	        }
		}
	}

}
