package net.minecraft.network.play.server;

import java.io.IOException;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class S23PacketBlockChange implements Packet
{
    private BlockPos field_179828_a;
    private IBlockState field_148883_d;
    private static final String __OBFID = "CL_00001287";

    public S23PacketBlockChange() {}

    public S23PacketBlockChange(World worldIn, BlockPos p_i45988_2_)
    {
        this.field_179828_a = p_i45988_2_;
        this.field_148883_d = worldIn.getBlockState(p_i45988_2_);
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer data) throws IOException
    {
        this.field_179828_a = data.readBlockPos();
        this.field_148883_d = (IBlockState)Block.BLOCK_STATE_IDS.getByValue(data.readVarIntFromBuffer());
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer data) throws IOException
    {
        data.writeBlockPos(this.field_179828_a);
        data.writeVarIntToBuffer(Block.BLOCK_STATE_IDS.get(this.field_148883_d));
    }

    public void func_180727_a(INetHandlerPlayClient p_180727_1_)
    {
        p_180727_1_.handleBlockChange(this);
    }

    public IBlockState func_180728_a()
    {
        return this.field_148883_d;
    }

    public BlockPos func_179827_b()
    {
        return this.field_179828_a;
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(INetHandler handler)
    {
        this.func_180727_a((INetHandlerPlayClient)handler);
    }
}
