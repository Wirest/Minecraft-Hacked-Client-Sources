// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.status;

import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.INetHandler;

public interface INetHandlerStatusServer extends INetHandler
{
    void processPing(final C01PacketPing p0);
    
    void processServerQuery(final C00PacketServerQuery p0);
}
