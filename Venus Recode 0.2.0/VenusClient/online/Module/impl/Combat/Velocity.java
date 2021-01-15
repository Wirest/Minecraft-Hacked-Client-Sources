package VenusClient.online.Module.impl.Combat;

import VenusClient.online.Client;
import VenusClient.online.Event.EventTarget;
import VenusClient.online.Event.impl.EventChat;
import VenusClient.online.Event.impl.EventMotionUpdate;
import VenusClient.online.Event.impl.EventReceivePacket;
import VenusClient.online.Module.Category;
import VenusClient.online.Module.Module;
import de.Hero.settings.Setting;
import net.minecraft.network.Packet;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

public class Velocity extends Module {
    public Velocity() {
        super("Velocity", "Velocity", Category.COMBAT, Keyboard.KEY_NONE);
    }

    @Override
    public void setup() {
        ArrayList<String> flyOptions = new ArrayList<>();
        flyOptions.add("Cancel");
        Client.instance.setmgr.rSetting(new Setting("Velocity Mode", this, "Cancel", flyOptions));
    }


    @EventTarget
    public void onSendPacket(EventReceivePacket event) {
    	if(Client.instance.moduleManager.getModuleByName("GhostMode").isEnabled()) {
			toggle();
    		EventChat.addchatmessage("Ghost Mode Enabled Please Disable GhostMode First");
      		return;
    	}
        String velocityModeSelected = Client.instance.setmgr.getSettingByName("Velocity Mode").getValString();
        Packet p = event.getPacket();
        if (p instanceof net.minecraft.network.play.server.S12PacketEntityVelocity || p instanceof net.minecraft.network.play.server.S27PacketExplosion){
            event.setCancelled(true);
            
    }
    }
 
	@Override
	protected void onEnable() {
		super.onEnable();
	}

}
