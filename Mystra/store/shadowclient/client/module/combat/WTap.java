package store.shadowclient.client.module.combat;

import store.shadowclient.client.event.EventTarget;
import store.shadowclient.client.event.events.EventSendPacket;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.network.play.client.C02PacketUseEntity;

public class WTap extends Module {

	public WTap() {
		super("WTap", 0, Category.COMBAT);
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		if(event.getPacket() instanceof C02PacketUseEntity) {
			final C02PacketUseEntity packet = (C02PacketUseEntity)event.getPacket();
			if(packet.getAction() == C02PacketUseEntity.Action.ATTACK && mc.thePlayer.onGround) {
				mc.thePlayer.setSprinting(mc.thePlayer.onGround);
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
