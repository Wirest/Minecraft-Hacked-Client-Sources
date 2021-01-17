package net.minecraft.block;

import me.rigamortis.faurax.module.modules.movement.NoSlowdown;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockSoulSand
extends Block {
    private static final String __OBFID = "CL_00000310";

    public BlockSoulSand() {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        float var4 = 0.125f;
        return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, (float)(pos.getY() + 1) - var4, pos.getZ() + 1);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        if (!NoSlowdown.enabled) {
            entityIn.motionX *= 0.4;
            entityIn.motionZ *= 0.4;
        }
    }
}

