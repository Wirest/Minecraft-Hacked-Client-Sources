// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.network;

import net.minecraft.util.IChatComponent;
import net.minecraft.network.INetHandler;
import net.minecraft.server.network.NetHandlerLoginServer;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.handshake.INetHandlerHandshakeServer;

public class NetHandlerHandshakeMemory implements INetHandlerHandshakeServer
{
    private final MinecraftServer mcServer;
    private final NetworkManager networkManager;
    
    public NetHandlerHandshakeMemory(final MinecraftServer p_i45287_1_, final NetworkManager p_i45287_2_) {
        this.mcServer = p_i45287_1_;
        this.networkManager = p_i45287_2_;
    }
    
    @Override
    public void processHandshake(final C00Handshake packetIn) {
        this.networkManager.setConnectionState(packetIn.getRequestedState());
        this.networkManager.setNetHandler(new NetHandlerLoginServer(this.mcServer, this.networkManager));
    }
    
    @Override
    public void onDisconnect(final IChatComponent reason) {
    }
}
