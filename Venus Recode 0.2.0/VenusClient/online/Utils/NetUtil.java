package VenusClient.online.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetUtil {
	private static Minecraft mc = Minecraft.getMinecraft();
	
    public static void sendPacketNoEvents(Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueueSilent(packet);
    }

    public static void sendPacket(Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }
}
