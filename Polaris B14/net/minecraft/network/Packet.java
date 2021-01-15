package net.minecraft.network;

import java.io.IOException;

public abstract interface Packet<T extends INetHandler>
{
  public abstract void readPacketData(PacketBuffer paramPacketBuffer)
    throws IOException;
  
  public abstract void writePacketData(PacketBuffer paramPacketBuffer)
    throws IOException;
  
  public abstract void processPacket(T paramT);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\Packet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */