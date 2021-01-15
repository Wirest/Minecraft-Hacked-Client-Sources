package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.WorldType;

public class S07PacketRespawn implements Packet
{
    private int field_149088_a;
    private EnumDifficulty field_149086_b;
    private WorldSettings.GameType field_149087_c;
    private WorldType field_149085_d;

    public S07PacketRespawn() {}

    public S07PacketRespawn(int p_i45213_1_, EnumDifficulty p_i45213_2_, WorldType p_i45213_3_, WorldSettings.GameType p_i45213_4_)
    {
        this.field_149088_a = p_i45213_1_;
        this.field_149086_b = p_i45213_2_;
        this.field_149087_c = p_i45213_4_;
        this.field_149085_d = p_i45213_3_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleRespawn(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.field_149088_a = buf.readInt();
        this.field_149086_b = EnumDifficulty.getDifficultyEnum(buf.readUnsignedByte());
        this.field_149087_c = WorldSettings.GameType.getByID(buf.readUnsignedByte());
        this.field_149085_d = WorldType.parseWorldType(buf.readStringFromBuffer(16));

        if (this.field_149085_d == null)
        {
            this.field_149085_d = WorldType.DEFAULT;
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeInt(this.field_149088_a);
        buf.writeByte(this.field_149086_b.getDifficultyId());
        buf.writeByte(this.field_149087_c.getID());
        buf.writeString(this.field_149085_d.getWorldTypeName());
    }

    public int func_149082_c()
    {
        return this.field_149088_a;
    }

    public EnumDifficulty func_149081_d()
    {
        return this.field_149086_b;
    }

    public WorldSettings.GameType func_149083_e()
    {
        return this.field_149087_c;
    }

    public WorldType func_149080_f()
    {
        return this.field_149085_d;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
