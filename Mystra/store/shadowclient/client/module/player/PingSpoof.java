package store.shadowclient.client.module.player;

import store.shadowclient.client.event.EventManager;
import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.network.status.client.C01PacketPing;

public class PingSpoof extends Module {

	public PingSpoof() {
		super("PingSpoof", 0, Category.PLAYER);
	}
	
	@Override
    public void onEnable() {
        EventManager.register(this);
    }
    
    @Override
    public void onDisable() {
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (event.packet instanceof C01PacketPing) {
            final C01PacketPing packet = (C01PacketPing)event.packet;
            packet.clientTime = 0L;
            event.packet = packet;
        }
    }

}
