package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.AbstractPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S46PacketSetCompressionLevel extends AbstractPacket<INetHandlerPlayClient> {
	private int field_179761_a;

	/**
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.field_179761_a = buf.readVarIntFromBuffer();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeVarIntToBuffer(this.field_179761_a);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient handler) {
		super.processPacket(handler);
		if (cancelled) {
			return;
		}
		handler.handleSetCompressionLevel(this);
	}

	public int func_179760_a() {
		return this.field_179761_a;
	}
}
