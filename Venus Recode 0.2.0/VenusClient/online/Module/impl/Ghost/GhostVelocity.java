package VenusClient.online.Module.impl.Ghost;

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

import org.apache.commons.lang3.event.EventUtils;
import org.lwjgl.input.Keyboard;

public class GhostVelocity extends Module {
    public GhostVelocity() {
        super("GhostVelocity", "GhostVelocity", Category.GHOST, Keyboard.KEY_NONE);
    }

    @Override
    public void setup() {
    	Client.instance.setmgr.rSetting(new Setting("GhostVelocity Horizontal", this, 90, 0, 100, true));
    	Client.instance.setmgr.rSetting(new Setting("GhostVelocity Vertical", this, 100, 0, 100, true));
	}


    @EventTarget
    public void onKB(EventReceivePacket event) {
    	if (mc.thePlayer == null) {
			return;
		}
		float horizontal = (float) Client.instance.setmgr.getSettingByName("GhostVelocity Horizontal").getValDouble();
		float vertical = (float) Client.instance.setmgr.getSettingByName("GhostVelocity Vertical").getValDouble();
		
	if (event.getPacket() instanceof net.minecraft.network.play.server.S12PacketEntityVelocity || event.getPacket() instanceof net.minecraft.network.play.server.S27PacketExplosion){
			if(mc.thePlayer.hurtTime > 0) {
			mc.thePlayer.motionX *= (float) horizontal / 100;
			mc.thePlayer.motionY *= (float) vertical / 100;
			mc.thePlayer.motionZ *= (float) horizontal / 100;
		}
	}
    }
	@Override
	protected void onEnable() {
		super.onEnable();
	}

}
