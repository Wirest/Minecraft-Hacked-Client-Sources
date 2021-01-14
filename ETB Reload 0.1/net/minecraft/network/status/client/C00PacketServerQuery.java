package net.minecraft.network.status.client;

import java.io.IOException;

import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.status.INetHandlerStatusServer;

public class C00PacketServerQuery implements Packet<INetHandlerStatusServer>
{
    /**
     * Reads the raw packet data from the data stream.
     */
    @Override
	public void readPacketData(PacketBuffer buf) throws IOException
    {
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    @Override
	public void writePacketData(PacketBuffer buf) throws IOException
    {
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    @Override
	public void processPacket(INetHandlerStatusServer handler)
    {
        handler.processServerQuery(this);
    }
}
