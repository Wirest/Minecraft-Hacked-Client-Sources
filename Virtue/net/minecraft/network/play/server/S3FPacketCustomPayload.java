package net.minecraft.network.play.server;

import io.netty.buffer.ByteBuf;
import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;

public class S3FPacketCustomPayload implements Packet
{
    private String channel;
    private PacketBuffer data;
    private static final String __OBFID = "CL_00001297";

    public S3FPacketCustomPayload() {}

    public S3FPacketCustomPayload(String channelName, PacketBuffer dataIn)
    {
        this.channel = channelName;
        this.data = dataIn;

        if (dataIn.writerIndex() > 1048576)
        {
            throw new IllegalArgumentException("Payload may not be larger than 1048576 bytes");
        }
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.channel = data.readStringFromBuffer(20);
        int var2 = data.readableBytes();

        if (var2 >= 0 && var2 <= 1048576)
        {
            this.data = new PacketBuffer(data.readBytes(var2));
        }
        else
        {
            throw new IOException("Payload may not be larger than 1048576 bytes");
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeString(this.channel);
        data.writeBytes((ByteBuf)this.data);
    }

    public void process(INetHandlerPlayClient p_180734_1_)
    {
        p_180734_1_.handleCustomPayload(this);
    }

    public String getChannelName()
    {
        return this.channel;
    }

    public PacketBuffer getBufferData()
    {
        return this.data;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.process((INetHandlerPlayClient)handler);
    }
}
