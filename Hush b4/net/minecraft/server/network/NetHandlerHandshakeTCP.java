// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.network;

import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.util.IChatComponent;
import net.minecraft.network.login.server.S00PacketDisconnect;
import net.minecraft.util.ChatComponentText;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class NetHandlerHandshakeTCP implements INetHandlerHandshakeServer
{
    private final MinecraftServer server;
    private final NetworkManager networkManager;
    
    public NetHandlerHandshakeTCP(final MinecraftServer serverIn, final NetworkManager netManager) {
        this.server = serverIn;
        this.networkManager = netManager;
    }
    
    @Override
    public void processHandshake(final C00Handshake packetIn) {
        switch (packetIn.getRequestedState()) {
            case LOGIN: {
                this.networkManager.setConnectionState(EnumConnectionState.LOGIN);
                if (packetIn.getProtocolVersion() > 47) {
                    final ChatComponentText chatcomponenttext = new ChatComponentText("Outdated server! I'm still on 1.8.8");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext));
                    this.networkManager.closeChannel(chatcomponenttext);
                    break;
                }
                if (packetIn.getProtocolVersion() < 47) {
                    final ChatComponentText chatcomponenttext2 = new ChatComponentText("Outdated client! Please use 1.8.8");
                    this.networkManager.sendPacket(new S00PacketDisconnect(chatcomponenttext2));
                    this.networkManager.closeChannel(chatcomponenttext2);
                    break;
                }
                this.networkManager.setNetHandler(new NetHandlerLoginServer(this.server, this.networkManager));
                break;
            }
            case STATUS: {
                this.networkManager.setConnectionState(EnumConnectionState.STATUS);
                this.networkManager.setNetHandler(new NetHandlerStatusServer(this.server, this.networkManager));
                break;
            }
            default: {
                throw new UnsupportedOperationException("Invalid intention " + packetIn.getRequestedState());
            }
        }
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
    }
}
