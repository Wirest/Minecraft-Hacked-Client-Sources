package net.minecraft.block.state.pattern;

import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public class BlockHelper implements Predicate
{
    private final Block block;
    private static final String __OBFID = "CL_00002020";

    private BlockHelper(Block p_i45654_1_)
    {
        this.block = p_i45654_1_;
    }

    public static BlockHelper forBlock(Block p_177642_0_)
    {
        return new BlockHelper(p_177642_0_);
    }

    public boolean isBlockEqualTo(IBlockState p_177643_1_)
    {
        return p_177643_1_ != null && p_177643_1_.getBlock() == this.block;
    }

    public boolean apply(Object p_apply_1_)
    {
        return this.isBlockEqualTo((IBlockState)p_apply_1_);
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer, double height) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
     }

     public static Block getBlock(BlockPos pos) {
        return Minecraft.theWorld.getBlockState(pos).getBlock();
     }
}
