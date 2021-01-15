package info.spicyclient.modules.movement;

import org.lwjgl.input.Keyboard;

import info.spicyclient.SpicyClient;
import info.spicyclient.events.Event;
import info.spicyclient.events.listeners.EventMotion;
import info.spicyclient.events.listeners.EventUpdate;
import info.spicyclient.modules.Module;
import info.spicyclient.modules.Module.Category;
import info.spicyclient.util.MovementUtils;
import info.spicyclient.util.Timer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.C13PacketPlayerAbilities;
import net.minecraft.util.MathHelper;

public class LongJump extends Module {
	
	public LongJump() {
		super("Long Jump", Keyboard.KEY_NONE, Category.MOVEMENT);
	}

	
	public void onEnable() {
		
        PlayerCapabilities playerCapabilities = new PlayerCapabilities();
        playerCapabilities.isFlying = true;
        playerCapabilities.allowFlying = true;
        playerCapabilities.setFlySpeed((float) ((Math.random() * (9.0 - 0.1)) + 0.1));
        playerCapabilities.isCreativeMode = true;
        mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C13PacketPlayerAbilities(playerCapabilities));
        
	}
	
	public void onDisable() {
		
	}
	
	public static boolean jumped = false;
	public static Timer timer = new Timer();
	
	
	public void onEvent(Event e) {
		
		if (e instanceof EventUpdate && e.isPre()) {
			this.additionalInformation = "Hypixel";
		}
		
		if (e instanceof EventMotion) {
			
			if (e.isPost()) {
				
				if (jumped && mc.thePlayer.onGround) {
					this.toggle();
					jumped = false;
					return;
				}
				
				mc.gameSettings.keyBindJump.pressed = false;

                if (MovementUtils.isOnGround(0.000001)) {
                	
                	MovementUtils.strafe((float) Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ) + 1.25f);
                    mc.thePlayer.jump();
                    e.setCanceled(true);
                    jumped = true;

                }

                mc.thePlayer.setSprinting(true);
				
			}
			
		}
		
	}
	
	
}
