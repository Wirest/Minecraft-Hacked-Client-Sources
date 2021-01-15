package net.minecraft.network.login.server;

import com.mojang.authlib.GameProfile;
import java.io.IOException;
import java.util.UUID;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;

public class S02PacketLoginSuccess implements Packet
{
    private GameProfile profile;
    private static final String __OBFID = "CL_00001375";

    public S02PacketLoginSuccess() {}

    public S02PacketLoginSuccess(GameProfile profileIn)
    {
        this.profile = profileIn;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        String var2 = data.readStringFromBuffer(36);
        String var3 = data.readStringFromBuffer(16);
        UUID var4 = UUID.fromString(var2);
        this.profile = new GameProfile(var4, var3);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        UUID var2 = this.profile.getId();
        data.writeString(var2 == null ? "" : var2.toString());
        data.writeString(this.profile.getName());
    }

    public void func_180771_a(INetHandlerLoginClient p_180771_1_)
    {
        p_180771_1_.handleLoginSuccess(this);
    }

    public GameProfile func_179730_a()
    {
        return this.profile;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180771_a((INetHandlerLoginClient)handler);
    }
}
