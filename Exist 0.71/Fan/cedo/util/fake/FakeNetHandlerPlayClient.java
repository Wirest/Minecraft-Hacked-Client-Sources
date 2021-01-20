package cedo.util.fake;

import cedo.altmanager.BetterAltManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.EnumPacketDirection;

import java.util.UUID;

public class FakeNetHandlerPlayClient extends NetHandlerPlayClient {
    private final NetworkPlayerInfo playerInfo;

    public FakeNetHandlerPlayClient(final Minecraft mcIn) {
        super(mcIn, mcIn.currentScreen, new FakeNetworkManager(EnumPacketDirection.CLIENTBOUND), BetterAltManager.profile);
        this.playerInfo = new NetworkPlayerInfo(BetterAltManager.profile);
    }

    public NetworkPlayerInfo getPlayerInfo(final UUID uniqueId) {
        return this.playerInfo;
    }

    public NetworkPlayerInfo getPlayerInfo(final String name) {
        return this.playerInfo;
    }
}
