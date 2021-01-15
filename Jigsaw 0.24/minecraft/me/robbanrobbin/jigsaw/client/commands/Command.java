package me.robbanrobbin.jigsaw.client.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.AbstractPacket;

public abstract class Command {

	protected Minecraft mc = Minecraft.getMinecraft();

	public abstract void run(String[] commands);

	public abstract String getActivator();

	public abstract String getSyntax();

	public abstract String getDesc();

	public String getName() {
		return getActivator().substring(1, getActivator().length());
	}

	public void sendPacketFinal(AbstractPacket packet) {
		mc.getNetHandler().getNetworkManager().sendPacketFinal(packet);
	}

	public void sendPacket(AbstractPacket packet) {
		mc.getNetHandler().getNetworkManager().sendPacket(packet);
	}
}
