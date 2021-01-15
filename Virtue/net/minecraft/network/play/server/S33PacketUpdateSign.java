package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

public class S33PacketUpdateSign implements Packet
{
    private World field_179706_a;
    private BlockPos field_179705_b;
    private IChatComponent[] field_149349_d;
    private static final String __OBFID = "CL_00001338";

    public S33PacketUpdateSign() {}

    public S33PacketUpdateSign(World worldIn, BlockPos p_i45951_2_, IChatComponent[] p_i45951_3_)
    {
        this.field_179706_a = worldIn;
        this.field_179705_b = p_i45951_2_;
        this.field_149349_d = new IChatComponent[] {p_i45951_3_[0], p_i45951_3_[1], p_i45951_3_[2], p_i45951_3_[3]};
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_179705_b = data.readBlockPos();
        this.field_149349_d = new IChatComponent[4];

        for (int var2 = 0; var2 < 4; ++var2)
        {
            this.field_149349_d[var2] = data.readChatComponent();
        }
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeBlockPos(this.field_179705_b);

        for (int var2 = 0; var2 < 4; ++var2)
        {
            data.writeChatComponent(this.field_149349_d[var2]);
        }
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandlerPlayClient handler)
    {
        handler.handleUpdateSign(this);
    }

    public BlockPos func_179704_a()
    {
        return this.field_179705_b;
    }

    public IChatComponent[] func_180753_b()
    {
        return this.field_149349_d;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.processPacket((INetHandlerPlayClient)handler);
    }
}
