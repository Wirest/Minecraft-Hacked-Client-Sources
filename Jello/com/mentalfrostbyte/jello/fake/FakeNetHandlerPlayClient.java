package com.mentalfrostbyte.jello.fake;

import net.minecraft.client.network.*;
import net.minecraft.client.*;
import net.minecraft.network.*;
import java.util.*;
import javax.annotation.*;

public class FakeNetHandlerPlayClient extends NetHandlerPlayClient
{
    private NetworkPlayerInfo playerInfo;
    
    public FakeNetHandlerPlayClient(final Minecraft mcIn) {
        super(mcIn, mcIn.currentScreen, (NetworkManager)new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND), mcIn.getSession().getProfile());
        this.playerInfo = new NetworkPlayerInfo(mcIn.getSession().getProfile());
    }
    
    public NetworkPlayerInfo getPlayerInfo(final UUID uniqueId) {
        return this.playerInfo;
    }
    
    public NetworkPlayerInfo getPlayerInfo(final String name) {
        return this.playerInfo;
    }
}
