package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S11PacketSpawnExperienceOrb implements Packet
{
    private int entityID;
    private int field_148990_b;
    private int field_148991_c;
    private int field_148988_d;
    private int xpValue;

    public S11PacketSpawnExperienceOrb() {}

    public S11PacketSpawnExperienceOrb(EntityXPOrb xpOrb)
    {
        this.entityID = xpOrb.getEntityId();
        this.field_148990_b = MathHelper.floor_double(xpOrb.posX * 32.0D);
        this.field_148991_c = MathHelper.floor_double(xpOrb.posY * 32.0D);
        this.field_148988_d = MathHelper.floor_double(xpOrb.posZ * 32.0D);
        this.xpValue = xpOrb.getXpValue();
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityID = buf.readVarIntFromBuffer();
        this.field_148990_b = buf.readInt();
        this.field_148991_c = buf.readInt();
        this.field_148988_d = buf.readInt();
        this.xpValue = buf.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeInt(this.field_148990_b);
        buf.writeInt(this.field_148991_c);
        buf.writeInt(this.field_148988_d);
        buf.writeShort(this.xpValue);
    }

    public void func_180719_a(INetHandlerPlayClient handler)
    {
        handler.handleSpawnExperienceOrb(this);
    }

    public int getEntityID()
    {
        return this.entityID;
    }

    public int func_148984_d()
    {
        return this.field_148990_b;
    }

    public int func_148983_e()
    {
        return this.field_148991_c;
    }

    public int func_148982_f()
    {
        return this.field_148988_d;
    }

    public int getXPValue()
    {
        return this.xpValue;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.func_180719_a((INetHandlerPlayClient)handler);
    }
}
