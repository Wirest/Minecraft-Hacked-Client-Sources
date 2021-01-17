// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status;

import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.INetHandler;

public interface INetHandlerStatusClient extends INetHandler
{
    void handleServerInfo(final S00PacketServerInfo p0);
    
    void handlePong(final S01PacketPong p0);
}
