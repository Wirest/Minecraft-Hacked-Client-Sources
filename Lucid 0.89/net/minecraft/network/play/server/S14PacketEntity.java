package net.minecraft.network.play.server;

import java.io.IOException;

import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.world.World;

public class S14PacketEntity implements Packet
{
    protected int entityId;
    protected byte field_149072_b;
    protected byte field_149073_c;
    protected byte field_149070_d;
    protected byte field_149071_e;
    protected byte field_149068_f;
    protected boolean field_179743_g;
    protected boolean field_149069_g;

    public S14PacketEntity() {}

    public S14PacketEntity(int entityIdIn)
    {
        this.entityId = entityIdIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.entityId = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.entityId);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityMovement(this);
    }

    @Override
	public String toString()
    {
        return "Entity_" + super.toString();
    }

    public Entity func_149065_a(World worldIn)
    {
        return worldIn.getEntityByID(this.entityId);
    }

    public byte func_149062_c()
    {
        return this.field_149072_b;
    }

    public byte func_149061_d()
    {
        return this.field_149073_c;
    }

    public byte func_149064_e()
    {
        return this.field_149070_d;
    }

    public byte func_149066_f()
    {
        return this.field_149071_e;
    }

    public byte func_149063_g()
    {
        return this.field_149068_f;
    }

    public boolean func_149060_h()
    {
        return this.field_149069_g;
    }

    public boolean func_179742_g()
    {
        return this.field_179743_g;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }

    public static class S15PacketEntityRelMove extends S14PacketEntity
    {

        public S15PacketEntityRelMove() {}

        public S15PacketEntityRelMove(int p_i45974_1_, byte p_i45974_2_, byte p_i45974_3_, byte p_i45974_4_, boolean p_i45974_5_)
        {
            super(p_i45974_1_);
            this.field_149072_b = p_i45974_2_;
            this.field_149073_c = p_i45974_3_;
            this.field_149070_d = p_i45974_4_;
            this.field_179743_g = p_i45974_5_;
        }

        @Override
		public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            this.field_149072_b = buf.readByte();
            this.field_149073_c = buf.readByte();
            this.field_149070_d = buf.readByte();
            this.field_179743_g = buf.readBoolean();
        }

        @Override
		public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeByte(this.field_149072_b);
            buf.writeByte(this.field_149073_c);
            buf.writeByte(this.field_149070_d);
            buf.writeBoolean(this.field_179743_g);
        }

        @Override
		public void processPacket(INetHandler handler)
        {
            super.processPacket((INetHandlerPlayClient)handler);
        }
    }

    public static class S16PacketEntityLook extends S14PacketEntity
    {

        public S16PacketEntityLook()
        {
            this.field_149069_g = true;
        }

        public S16PacketEntityLook(int p_i45972_1_, byte p_i45972_2_, byte p_i45972_3_, boolean p_i45972_4_)
        {
            super(p_i45972_1_);
            this.field_149071_e = p_i45972_2_;
            this.field_149068_f = p_i45972_3_;
            this.field_149069_g = true;
            this.field_179743_g = p_i45972_4_;
        }

        @Override
		public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            this.field_149071_e = buf.readByte();
            this.field_149068_f = buf.readByte();
            this.field_179743_g = buf.readBoolean();
        }

        @Override
		public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeByte(this.field_149071_e);
            buf.writeByte(this.field_149068_f);
            buf.writeBoolean(this.field_179743_g);
        }

        @Override
		public void processPacket(INetHandler handler)
        {
            super.processPacket((INetHandlerPlayClient)handler);
        }
    }

    public static class S17PacketEntityLookMove extends S14PacketEntity
    {

        public S17PacketEntityLookMove()
        {
            this.field_149069_g = true;
        }

        public S17PacketEntityLookMove(int p_i45973_1_, byte p_i45973_2_, byte p_i45973_3_, byte p_i45973_4_, byte p_i45973_5_, byte p_i45973_6_, boolean p_i45973_7_)
        {
            super(p_i45973_1_);
            this.field_149072_b = p_i45973_2_;
            this.field_149073_c = p_i45973_3_;
            this.field_149070_d = p_i45973_4_;
            this.field_149071_e = p_i45973_5_;
            this.field_149068_f = p_i45973_6_;
            this.field_179743_g = p_i45973_7_;
            this.field_149069_g = true;
        }

        @Override
		public void readPacketData(PacketBuffer buf) throws IOException
        {
            super.readPacketData(buf);
            this.field_149072_b = buf.readByte();
            this.field_149073_c = buf.readByte();
            this.field_149070_d = buf.readByte();
            this.field_149071_e = buf.readByte();
            this.field_149068_f = buf.readByte();
            this.field_179743_g = buf.readBoolean();
        }

        @Override
		public void writePacketData(PacketBuffer buf) throws IOException
        {
            super.writePacketData(buf);
            buf.writeByte(this.field_149072_b);
            buf.writeByte(this.field_149073_c);
            buf.writeByte(this.field_149070_d);
            buf.writeByte(this.field_149071_e);
            buf.writeByte(this.field_149068_f);
            buf.writeBoolean(this.field_179743_g);
        }

        @Override
		public void processPacket(INetHandler handler)
        {
            super.processPacket((INetHandlerPlayClient)handler);
        }
    }
}
