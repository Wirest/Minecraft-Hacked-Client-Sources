package store.shadowclient.client.module.movement;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventUpdate;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.block.material.Material;

public class FastFall extends Module {

	public FastFall() {
		super("FastFall", 0, Category.MOVEMENT);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.thePlayer.isAirBorne && mc.thePlayer.motionY < 0.0D) {
			if (!mc.thePlayer.isOnLadder() && !mc.gameSettings.keyBindJump.isPressed()) {
				if (mc.thePlayer.isInsideOfMaterial(Material.web) && !Shadow.instance.moduleManager.getModuleByName("NoSlowdown").isToggled()) {
					mc.thePlayer.motionY = -7.0D;
	            } else {
	                mc.thePlayer.motionY = -2.0D;
	            }
	         }
	      }
	   }
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
