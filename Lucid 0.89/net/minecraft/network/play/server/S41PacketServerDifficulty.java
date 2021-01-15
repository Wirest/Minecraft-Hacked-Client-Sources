package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;

public class S41PacketServerDifficulty implements Packet
{
    private EnumDifficulty difficulty;
    private boolean field_179832_b;

    public S41PacketServerDifficulty() {}

    public S41PacketServerDifficulty(EnumDifficulty difficultyIn, boolean p_i45987_2_)
    {
        this.difficulty = difficultyIn;
        this.field_179832_b = p_i45987_2_;
    }

    public void func_179829_a(INetHandlerPlayClient handler)
    {
        handler.handleServerDifficulty(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.difficulty = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.difficulty.getDifficultyId());
    }

    public boolean func_179830_a()
    {
        return this.field_179832_b;
    }

    public EnumDifficulty getDifficulty()
    {
        return this.difficulty;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_179829_a((INetHandlerPlayClient)handler);
    }
}
