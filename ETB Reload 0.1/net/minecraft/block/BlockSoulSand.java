package net.minecraft.block;

import com.etb.client.event.events.player.SlowdownEvent;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSoulSand extends Block
{
    public BlockSoulSand()
    {
        super(Material.sand, MapColor.brownColor);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
    {
        float f = 0.125F;
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1 - f, pos.getZ() + 1);
    }

    /**
     * Called When an Entity Collided with the Block
     */
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        final SlowdownEvent event = new SlowdownEvent(SlowdownEvent.Type.SoulSand);
        event.dispatch();
        if (event.isCanceled()) {
            return;
        }
        entityIn.motionX *= 0.4D;
        entityIn.motionZ *= 0.4D;
    }
}
