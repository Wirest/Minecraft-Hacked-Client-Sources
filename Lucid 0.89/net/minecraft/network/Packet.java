package net.minecraft.network;

import java.io.IOException;

public interface Packet
{
    /**
     * Reads the raw packet data from the data stream.
     */
    void readPacketData(PacketBuffer var1) throws IOException;

    /**
     * Writes the raw packet data to the data stream.
     */
    void writePacketData(PacketBuffer var1) throws IOException;

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    void processPacket(INetHandler var1);
}
