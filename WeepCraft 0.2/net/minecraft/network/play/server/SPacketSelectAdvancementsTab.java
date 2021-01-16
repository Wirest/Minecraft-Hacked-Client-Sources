package net.minecraft.network.play.server;

import java.io.IOException;
import javax.annotation.Nullable;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.ResourceLocation;

public class SPacketSelectAdvancementsTab implements Packet<INetHandlerPlayClient>
{
    @Nullable
    private ResourceLocation field_194155_a;

    public SPacketSelectAdvancementsTab()
    {
    }

    public SPacketSelectAdvancementsTab(@Nullable ResourceLocation p_i47596_1_)
    {
        this.field_194155_a = p_i47596_1_;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.func_194022_a(this);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        if (buf.readBoolean())
        {
            this.field_194155_a = buf.func_192575_l();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeBoolean(this.field_194155_a != null);

        if (this.field_194155_a != null)
        {
            buf.func_192572_a(this.field_194155_a);
        }
    }

    @Nullable
    public ResourceLocation func_194154_a()
    {
        return this.field_194155_a;
    }
}
