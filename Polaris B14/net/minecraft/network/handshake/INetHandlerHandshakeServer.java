package net.minecraft.network.handshake;

import net.minecraft.network.INetHandler;
import net.minecraft.network.handshake.client.C00Handshake;

public abstract interface INetHandlerHandshakeServer
  extends INetHandler
{
  public abstract void processHandshake(C00Handshake paramC00Handshake);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\handshake\INetHandlerHandshakeServer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */