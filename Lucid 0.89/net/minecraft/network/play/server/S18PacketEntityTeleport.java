package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.MathHelper;

public class S18PacketEntityTeleport implements Packet
{
    private int entityId;
    private int field_149456_b;
    private int field_149457_c;
    private int field_149454_d;
    private byte yaw;
    private byte pitch;
    private boolean onGround;

    public S18PacketEntityTeleport() {}

    public S18PacketEntityTeleport(Entity entityIn)
    {
        this.entityId = entityIn.getEntityId();
        this.field_149456_b = MathHelper.floor_double(entityIn.posX * 32.0D);
        this.field_149457_c = MathHelper.floor_double(entityIn.posY * 32.0D);
        this.field_149454_d = MathHelper.floor_double(entityIn.posZ * 32.0D);
        this.yaw = (byte)((int)(entityIn.rotationYaw * 256.0F / 360.0F));
        this.pitch = (byte)((int)(entityIn.rotationPitch * 256.0F / 360.0F));
        this.onGround = entityIn.onGround;
    }

    public S18PacketEntityTeleport(int p_i45949_1_, int p_i45949_2_, int p_i45949_3_, int p_i45949_4_, byte p_i45949_5_, byte p_i45949_6_, boolean p_i45949_7_)
    {
        this.entityId = p_i45949_1_;
        this.field_149456_b = p_i45949_2_;
        this.field_149457_c = p_i45949_3_;
        this.field_149454_d = p_i45949_4_;
        this.yaw = p_i45949_5_;
        this.pitch = p_i45949_6_;
        this.onGround = p_i45949_7_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarIntFromBuffer();
        this.field_149456_b = buf.readInt();
        this.field_149457_c = buf.readInt();
        this.field_149454_d = buf.readInt();
        this.yaw = buf.readByte();
        this.pitch = buf.readByte();
        this.onGround = buf.readBoolean();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeInt(this.field_149456_b);
        buf.writeInt(this.field_149457_c);
        buf.writeInt(this.field_149454_d);
        buf.writeByte(this.yaw);
        buf.writeByte(this.pitch);
        buf.writeBoolean(this.onGround);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityTeleport(this);
    }

    public int getEntityId()
    {
        return this.entityId;
    }

    public int func_149449_d()
    {
        return this.field_149456_b;
    }

    public int func_149448_e()
    {
        return this.field_149457_c;
    }

    public int func_149446_f()
    {
        return this.field_149454_d;
    }

    public byte getYaw()
    {
        return this.yaw;
    }

    public byte getPitch()
    {
        return this.pitch;
    }

    public boolean getOnGround()
    {
        return this.onGround;
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
