package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.AbstractPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S0DPacketCollectItem extends AbstractPacket<INetHandlerPlayClient> {
	private int collectedItemEntityId;
	private int entityId;

	public S0DPacketCollectItem() {
	}

	public S0DPacketCollectItem(int collectedItemEntityIdIn, int entityIdIn) {
		this.collectedItemEntityId = collectedItemEntityIdIn;
		this.entityId = entityIdIn;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.collectedItemEntityId = buf.readVarIntFromBuffer();
		this.entityId = buf.readVarIntFromBuffer();
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeVarIntToBuffer(this.collectedItemEntityId);
		buf.writeVarIntToBuffer(this.entityId);
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient handler) {
		super.processPacket(handler);
		if (cancelled) {
			return;
		}
		handler.handleCollectItem(this);
	}

	public int getCollectedItemEntityID() {
		return this.collectedItemEntityId;
	}

	public int getEntityID() {
		return this.entityId;
	}
}
