package me.onlyeli.ice.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;

public class NetUtils {

	public static void sendPacket(Packet packet) {
		Minecraft.getMinecraft().getNetHandler().getNetworkManager().dispatchPacket(packet, null);
	}

}
