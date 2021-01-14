package store.shadowclient.client.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.clickgui.settings.Setting;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventReceivePacket;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module{
	
    public Velocity() {
        super("Velocity", Keyboard.KEY_K, Category.MOVEMENT);
        
        ArrayList<String> options = new ArrayList<>();
        options.add("AAC");
        options.add("Hypixel");
        options.add("HypixelNew");
        
        Shadow.instance.settingsManager.rSetting(new Setting("Velocity Mode", this, "Hypixel", options));
    }
    
    @EventTarget
    public void onUpdate(EventReceivePacket event) {
    	String mode = Shadow.instance.settingsManager.getSettingByName("Velocity Mode").getValString();
    	
    	if(mode.equalsIgnoreCase("AAC")) {
    		if(mc.thePlayer.hurtTime > 0 & mc.thePlayer.hurtTime <= 7) {
    			mc.thePlayer.motionX *= 0.5;
    		    mc.thePlayer.motionZ *= 0.5;
    		}
    		
    		if(mc.thePlayer.hurtTime > 0 & mc.thePlayer.hurtTime < 6) {
    		    mc.thePlayer.motionX = 0.0;
    		    mc.thePlayer.motionZ = 0.0;
    		}
    	}
    	
    	if(mode.equalsIgnoreCase("HypixelNew")) {
    		Packet packet = event.getPacket();
    		if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
    			event.setCancelled(true);
        	}
    	}
    }
    
    public final void onReceivePacket(EventReceivePacket event) {
    	String mode = Shadow.instance.settingsManager.getSettingByName("Velocity Mode").getValString();
    	if(mode.equalsIgnoreCase("HypixelNew")) {
    		Packet packet = event.getPacket();
    		if (packet instanceof S12PacketEntityVelocity || packet instanceof S27PacketExplosion) {
    			event.setCancelled(true);
        	}
    	}
    }
    
    public void onEnable() {
        super.onEnable();
    }

    public void onDisable() {
        super.onDisable();
    }
}