package me.robbanrobbin.jigsaw.client.modules;

import me.robbanrobbin.jigsaw.client.module.state.Category;
import me.robbanrobbin.jigsaw.module.Module;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.play.client.C12PacketUpdateSign;

public class OPSign extends Module {

	public OPSign() {
		super("OPSign", 0, Category.EXPLOITS, "Place a sign and right-click it. Note: Only works for 1.8 - 1.8.5 vanilla servers!");
	}
	
	@Override
	public void onPacketSent(AbstractPacket packet) {
		if(packet instanceof C12PacketUpdateSign) {
			C12PacketUpdateSign signPkt = (C12PacketUpdateSign)packet;
			packet.cancel();
			sendPacketFinal(new C12PacketUpdateSign(signPkt.getPosition(), signPkt.getLines()));
		}
		super.onPacketSent(packet);
	}
	
}
