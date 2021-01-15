package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityPainting;
import net.minecraft.network.AbstractPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class S10PacketSpawnPainting extends AbstractPacket<INetHandlerPlayClient> {
	private int entityID;
	private BlockPos position;
	private EnumFacing facing;
	private String title;

	public S10PacketSpawnPainting() {
	}

	public S10PacketSpawnPainting(EntityPainting painting) {
		this.entityID = painting.getEntityId();
		this.position = painting.getHangingPosition();
		this.facing = painting.facingDirection;
		this.title = painting.art.title;
	}

	/**
	 * Reads the raw packet data from the data stream.
	 */
	public void readPacketData(PacketBuffer buf) throws IOException {
		this.entityID = buf.readVarIntFromBuffer();
		this.title = buf.readStringFromBuffer(EntityPainting.EnumArt.field_180001_A);
		this.position = buf.readBlockPos();
		this.facing = EnumFacing.getHorizontal(buf.readUnsignedByte());
	}

	/**
	 * Writes the raw packet data to the data stream.
	 */
	public void writePacketData(PacketBuffer buf) throws IOException {
		buf.writeVarIntToBuffer(this.entityID);
		buf.writeString(this.title);
		buf.writeBlockPos(this.position);
		buf.writeByte(this.facing.getHorizontalIndex());
	}

	/**
	 * Passes this Packet on to the NetHandler for processing.
	 */
	public void processPacket(INetHandlerPlayClient handler) {
		super.processPacket(handler);
		if (cancelled) {
			return;
		}
		handler.handleSpawnPainting(this);
	}

	public int getEntityID() {
		return this.entityID;
	}

	public BlockPos getPosition() {
		return this.position;
	}

	public EnumFacing getFacing() {
		return this.facing;
	}

	public String getTitle() {
		return this.title;
	}
}
