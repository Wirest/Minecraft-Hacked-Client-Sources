// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.handshake;

import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.INetHandler;

public interface INetHandlerHandshakeServer extends INetHandler
{
    void processHandshake(final C00Handshake p0);
}
