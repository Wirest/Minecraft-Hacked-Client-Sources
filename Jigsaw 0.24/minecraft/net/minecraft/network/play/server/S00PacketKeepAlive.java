package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.AbstractPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S00PacketKeepAlive extends AbstractPacket<INetHandlerPlayClient> {
	private int id;

	public S00PacketKeepAlive() {
	}

	public S00PacketKeepAlive(int idIn) {
		this.id = idIn;
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient handler) {
		super.processPacket(handler);
		if (cancelled) {
			return;
		}
		handler.handleKeepAlive(this);
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.id = buf.readVarIntFromBuffer();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeVarIntToBuffer(this.id);
	}

	public int func_149134_c() {
		return this.id;
	}
}
