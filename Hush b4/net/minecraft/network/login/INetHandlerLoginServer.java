// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network.login;

import net.minecraft.network.login.client.C01PacketEncryptionResponse;
import net.minecraft.network.login.client.C00PacketLoginStart;
import net.minecraft.network.INetHandler;

public interface INetHandlerLoginServer extends INetHandler
{
    void processLoginStart(final C00PacketLoginStart p0);
    
    void processEncryptionResponse(final C01PacketEncryptionResponse p0);
}
