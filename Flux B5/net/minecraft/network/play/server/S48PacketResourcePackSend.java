package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S48PacketResourcePackSend implements Packet
{
    private String url;
    private String hash;
    private static final String __OBFID = "CL_00002293";

    public S48PacketResourcePackSend() {}

    public S48PacketResourcePackSend(String url, String hash)
    {
        this.url = url;
        this.hash = hash;

        if (hash.length() > 40)
        {
            throw new IllegalArgumentException("Hash is too long (max 40, was " + hash.length() + ")");
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.url = data.readStringFromBuffer(32767);
        this.hash = data.readStringFromBuffer(40);
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeString(this.url);
        data.writeString(this.hash);
    }

    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_175095_a(this);
    }

    public String func_179783_a()
    {
        return this.url;
    }

    public String func_179784_b()
    {
        return this.hash;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
