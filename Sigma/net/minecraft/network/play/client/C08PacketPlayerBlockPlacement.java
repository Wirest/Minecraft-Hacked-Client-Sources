package net.minecraft.network.play.client;

import java.io.IOException;

import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.util.BlockPos;

public class C08PacketPlayerBlockPlacement implements Packet {
    private static final BlockPos field_179726_a = new BlockPos(-1, -1, -1);
    private BlockPos blockPos;
    private int placedBlockDirection;
    private ItemStack stack;
    private float facingX;
    private float facingY;
    private float facingZ;
    private static final String __OBFID = "CL_00001371";

    public C08PacketPlayerBlockPlacement() {
    }

    public C08PacketPlayerBlockPlacement(ItemStack p_i45930_1_) {
        this(field_179726_a, 255, p_i45930_1_, 0.0F, 0.0F, 0.0F);
    }

    public C08PacketPlayerBlockPlacement(BlockPos pos, int direction, ItemStack stack, float x, float y, float z) {
        this.blockPos = pos;
        this.placedBlockDirection = direction;
        this.stack = stack != null ? stack.copy() : null;
        this.facingX = x;
        this.facingY = y;
        this.facingZ = z;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException {
        this.blockPos = data.readBlockPos();
        this.placedBlockDirection = data.readUnsignedByte();
        this.stack = data.readItemStackFromBuffer();
        this.facingX = (float) data.readUnsignedByte() / 16.0F;
        this.facingY = (float) data.readUnsignedByte() / 16.0F;
        this.facingZ = (float) data.readUnsignedByte() / 16.0F;
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException {
        data.writeBlockPos(this.blockPos);
        data.writeByte(this.placedBlockDirection);
        data.writeItemStackToBuffer(this.stack);
        data.writeByte((int) (this.facingX * 16.0F));
        data.writeByte((int) (this.facingY * 16.0F));
        data.writeByte((int) (this.facingZ * 16.0F));
    }

    public void processPlacement(INetHandlerPlayServer handler) {
        handler.processPlayerBlockPlacement(this);
    }

    public BlockPos getBlockPos() {
        return this.blockPos;
    }

    public void setBlockPos(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public int getPlacedBlockDirection() {
        return this.placedBlockDirection;
    }

    public ItemStack getStack() {
        return this.stack;
    }

    /**
     * Returns the offset from xPosition where the actual click took place.
     */
    public float getPlacedBlockOffsetX() {
        return this.facingX;
    }

    /**
     * Returns the offset from yPosition where the actual click took place.
     */
    public float getPlacedBlockOffsetY() {
        return this.facingY;
    }

    /**
     * Returns the offset from zPosition where the actual click took place.
     */
    public float getPlacedBlockOffsetZ() {
        return this.facingZ;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler) {
        this.processPlacement((INetHandlerPlayServer) handler);
    }
}
