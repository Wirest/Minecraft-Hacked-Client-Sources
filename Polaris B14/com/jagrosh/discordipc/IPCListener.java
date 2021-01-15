package com.jagrosh.discordipc;

import com.jagrosh.discordipc.entities.Packet;
import com.jagrosh.discordipc.entities.User;
import org.json.JSONObject;

public abstract interface IPCListener
{
  public void onPacketSent(IPCClient client, Packet packet) {}
  
  public void onPacketReceived(IPCClient client, Packet packet) {}
  
  public void onActivityJoin(IPCClient client, String secret) {}
  
  public void onActivitySpectate(IPCClient client, String secret) {}
  
  public void onActivityJoinRequest(IPCClient client, String secret, User user) {}
  
  public void onReady(IPCClient client) {}
  
  public void onClose(IPCClient client, JSONObject json) {}
  
  public void onDisconnect(IPCClient client, Throwable t) {}
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\com\jagrosh\discordipc\IPCListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */