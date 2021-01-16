package net.minecraft.network.login.server;

import java.io.IOException;
import java.security.PublicKey;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.login.INetHandlerLoginClient;
import net.minecraft.util.CryptManager;

public class S01PacketEncryptionRequest implements Packet
{
    private String hashedServerId;
    private PublicKey publicKey;
    private byte[] field_149611_c;
    private static final String __OBFID = "CL_00001376";

    public S01PacketEncryptionRequest() {}

    public S01PacketEncryptionRequest(String serverId, PublicKey key, byte[] p_i45268_3_)
    {
        this.hashedServerId = serverId;
        this.publicKey = key;
        this.field_149611_c = p_i45268_3_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.hashedServerId = data.readStringFromBuffer(20);
        this.publicKey = CryptManager.decodePublicKey(data.readByteArray());
        this.field_149611_c = data.readByteArray();
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeString(this.hashedServerId);
        data.writeByteArray(this.publicKey.getEncoded());
        data.writeByteArray(this.field_149611_c);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerLoginClient handler)
    {
        handler.handleEncryptionRequest(this);
    }

    public String func_149609_c()
    {
        return this.hashedServerId;
    }

    public PublicKey func_149608_d()
    {
        return this.publicKey;
    }

    public byte[] func_149607_e()
    {
        return this.field_149611_c;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerLoginClient)handler);
    }
}
