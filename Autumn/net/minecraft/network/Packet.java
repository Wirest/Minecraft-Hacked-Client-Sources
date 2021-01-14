package net.minecraft.network;

import java.io.IOException;

public interface Packet {
   void readPacketData(PacketBuffer var1) throws IOException;

   void writePacketData(PacketBuffer var1) throws IOException;

   void processPacket(INetHandler var1);
}
