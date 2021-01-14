package me.memewaredevs.client.util.packet;

import me.memewaredevs.client.util.misc.MinecraftUtil;
import net.minecraft.network.Packet;

public class PacketUtil implements MinecraftUtil {

    public static void sendPacketSilent(Packet packet) {
        mc.getNetHandler().getNetworkManager().sendPacket(packet);
    }

    public static void sendPacket(Packet packet) {
        mc.thePlayer.sendQueue.addToSendQueue(packet);
    }

}
