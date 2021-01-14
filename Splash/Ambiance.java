package splash.client.modules.visual;

import me.hippo.systems.lwjeb.annotation.Collect;
import net.minecraft.network.play.server.S03PacketTimeUpdate;
import splash.api.module.Module;
import splash.api.module.category.ModuleCategory;
import splash.api.value.impl.ModeValue;
import splash.client.events.network.EventPacketReceive;
import splash.client.events.player.EventPlayerUpdate;

/**
 * Author: Ice
 * Created: 18:55, 13-Jun-20
 * Project: Client
 */
public class Ambiance extends Module {

    public ModeValue<TOD> todModeValue = new ModeValue<>("Time Of Day", TOD.DAY, this);

	public Ambiance() {
		super("Ambiance", "Changes the time", ModuleCategory.VISUALS);
	}


	@Collect
	public void packetInEvent(EventPacketReceive eventPacketReceive) {
		if (eventPacketReceive.getReceivedPacket() instanceof S03PacketTimeUpdate) {
			eventPacketReceive.setCancelled(true);
		}
	}

	@Collect
	public void onUpdate(EventPlayerUpdate eventPlayerUpdate) {
		mc.theWorld.getWorldInfo().setWorldTime(todModeValue.getValue() == TOD.DAY ? 0 : 20000);
	}


    
    
    public enum TOD {
        DAY, NIGHT;
    }
}
