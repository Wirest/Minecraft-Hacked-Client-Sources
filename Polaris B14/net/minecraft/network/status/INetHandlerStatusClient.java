package net.minecraft.network.status;

import net.minecraft.network.INetHandler;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;

public abstract interface INetHandlerStatusClient
  extends INetHandler
{
  public abstract void handleServerInfo(S00PacketServerInfo paramS00PacketServerInfo);
  
  public abstract void handlePong(S01PacketPong paramS01PacketPong);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\status\INetHandlerStatusClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */