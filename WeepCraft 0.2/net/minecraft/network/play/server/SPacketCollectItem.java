package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class SPacketCollectItem implements Packet<INetHandlerPlayClient>
{
    private int collectedItemEntityId;
    private int entityId;
    private int field_191209_c;

    public SPacketCollectItem()
    {
    }

    public SPacketCollectItem(int p_i47316_1_, int p_i47316_2_, int p_i47316_3_)
    {
        this.collectedItemEntityId = p_i47316_1_;
        this.entityId = p_i47316_2_;
        this.field_191209_c = p_i47316_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.collectedItemEntityId = buf.readVarIntFromBuffer();
        this.entityId = buf.readVarIntFromBuffer();
        this.field_191209_c = buf.readVarIntFromBuffer();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeVarIntToBuffer(this.collectedItemEntityId);
        buf.writeVarIntToBuffer(this.entityId);
        buf.writeVarIntToBuffer(this.field_191209_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleCollectItem(this);
    }

    public int getCollectedItemEntityID()
    {
        return this.collectedItemEntityId;
    }

    public int getEntityID()
    {
        return this.entityId;
    }

    public int func_191208_c()
    {
        return this.field_191209_c;
    }
}
