package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.combat.Killaura;
import info.spicyclient.modules.render.TabGUI;
import info.spicyclient.settings.NumberSetting;
import info.spicyclient.util.RotationUtils;
import net.minecraft.util.MathHelper;

public class Back extends Module {
	
	NumberSetting distance = new NumberSetting("Distance", 4, 0, 6, 0.5);
	
	public Back() {
		super("Back", Keyboard.KEY_NONE, Category.MOVEMENT);
		resetSettings();
	}
	
	@Override
	public void resetSettings() {
		this.settings.clear();
		this.addSettings(distance);
	}
	
	public void onEnable() {
		
	}
	
	public void onDisable() {
		
	}
	
	public void onEvent(Event e) {
		
		if (e instanceof EventMotion) {
			
			if (e.isPre()) {
			Killaura k = SpicyClient.config.killaura;
				
				if (k.target == null) {
					
				}else {
					
					EventMotion event = (EventMotion) e;
					
					float f = k.target.rotationYaw * 0.017453292F;
					double x2 = k.target.posX, z2 = k.target.posZ;
		            x2 -= (double)(MathHelper.sin(f) * distance.getValue() * -1);
		            z2 += (double)(MathHelper.cos(f) * distance.getValue() * -1);
		            
		            double x = mc.thePlayer.posX;
		            double z = mc.thePlayer.posZ;
		            
					mc.thePlayer.posX = x2;
					mc.thePlayer.posZ = z2;
					event.x = x2;
					event.z = z2;
					
					event.setYaw(RotationUtils.getRotations(k.target)[0]+10);
					event.setPitch(RotationUtils.getRotations(k.target)[1]);
					
					mc.thePlayer.rotationYawHead = RotationUtils.getRotations(k.target)[0];
					
					k.target = null;
					//mc.thePlayer.posX = x;
					//mc.thePlayer.posZ = z;
					
				}
				
			}
			
		}
		
	}
	
}
