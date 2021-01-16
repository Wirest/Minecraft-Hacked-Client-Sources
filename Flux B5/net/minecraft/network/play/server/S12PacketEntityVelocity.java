package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S12PacketEntityVelocity implements Packet
{
	public int entityId; //TODO --> ENTITYID
    public int x; //TODO --> X
    public int y; //TODO --> Y
    public int z;  //TODO --> Z
    private static final String __OBFID = "CL_00001328";

    public S12PacketEntityVelocity() {}

    public S12PacketEntityVelocity(Entity p_i45219_1_)
    {
        this(p_i45219_1_.getEntityId(), p_i45219_1_.motionX, p_i45219_1_.motionY, p_i45219_1_.motionZ);
    }

    public S12PacketEntityVelocity(int p_i45220_1_, double p_i45220_2_, double p_i45220_4_, double p_i45220_6_)
    {
        this.entityId = p_i45220_1_;
        double var8 = 3.9D;

        if (p_i45220_2_ < -var8)
        {
            p_i45220_2_ = -var8;
        }

        if (p_i45220_4_ < -var8)
        {
            p_i45220_4_ = -var8;
        }

        if (p_i45220_6_ < -var8)
        {
            p_i45220_6_ = -var8;
        }

        if (p_i45220_2_ > var8)
        {
            p_i45220_2_ = var8;
        }

        if (p_i45220_4_ > var8)
        {
            p_i45220_4_ = var8;
        }

        if (p_i45220_6_ > var8)
        {
            p_i45220_6_ = var8;
        }

        this.x = (int)(p_i45220_2_ * 8000.0D);
        this.y = (int)(p_i45220_4_ * 8000.0D);
        this.z = (int)(p_i45220_6_ * 8000.0D);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.entityId = data.readVarIntFromBuffer();
        this.x = data.readShort();
        this.y = data.readShort();
        this.z = data.readShort();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeVarIntToBuffer(this.entityId);
        data.writeShort(this.x);
        data.writeShort(this.y);
        data.writeShort(this.z);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleEntityVelocity(this);
    }

    public int func_149412_c()
    {
        return this.entityId;
    }

    public int func_149411_d()
    {
        return this.x;
    }

    public int func_149410_e()
    {
        return this.y;
    }

    public int func_149409_f()
    {
        return this.z;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
