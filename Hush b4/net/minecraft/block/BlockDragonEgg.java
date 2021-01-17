// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.Item;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockDragonEgg extends Block
{
    public BlockDragonEgg() {
        super(Material.dragonEgg, MapColor.blackColor);
        this.setBlockBounds(0.0625f, 0.0f, 0.0625f, 0.9375f, 1.0f, 0.9375f);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void onNeighborBlockChange(final World worldIn, final BlockPos pos, final IBlockState state, final Block neighborBlock) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        this.checkFall(worldIn, pos);
    }
    
    private void checkFall(final World worldIn, final BlockPos pos) {
        if (BlockFalling.canFallInto(worldIn, pos.down()) && pos.getY() >= 0) {
            final int i = 32;
            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
                worldIn.spawnEntityInWorld(new EntityFallingBlock(worldIn, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, this.getDefaultState()));
            }
            else {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;
                for (blockpos = pos; BlockFalling.canFallInto(worldIn, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down()) {}
                if (blockpos.getY() > 0) {
                    worldIn.setBlockState(blockpos, this.getDefaultState(), 2);
                }
            }
        }
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        this.teleport(worldIn, pos);
        return true;
    }
    
    @Override
    public void onBlockClicked(final World worldIn, final BlockPos pos, final EntityPlayer playerIn) {
        this.teleport(worldIn, pos);
    }
    
    private void teleport(final World worldIn, final BlockPos pos) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this) {
            for (int i = 0; i < 1000; ++i) {
                final BlockPos blockpos = pos.add(worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16), worldIn.rand.nextInt(8) - worldIn.rand.nextInt(8), worldIn.rand.nextInt(16) - worldIn.rand.nextInt(16));
                if (worldIn.getBlockState(blockpos).getBlock().blockMaterial == Material.air) {
                    if (worldIn.isRemote) {
                        for (int j = 0; j < 128; ++j) {
                            final double d0 = worldIn.rand.nextDouble();
                            final float f = (worldIn.rand.nextFloat() - 0.5f) * 0.2f;
                            final float f2 = (worldIn.rand.nextFloat() - 0.5f) * 0.2f;
                            final float f3 = (worldIn.rand.nextFloat() - 0.5f) * 0.2f;
                            final double d2 = blockpos.getX() + (pos.getX() - blockpos.getX()) * d0 + (worldIn.rand.nextDouble() - 0.5) * 1.0 + 0.5;
                            final double d3 = blockpos.getY() + (pos.getY() - blockpos.getY()) * d0 + worldIn.rand.nextDouble() * 1.0 - 0.5;
                            final double d4 = blockpos.getZ() + (pos.getZ() - blockpos.getZ()) * d0 + (worldIn.rand.nextDouble() - 0.5) * 1.0 + 0.5;
                            worldIn.spawnParticle(EnumParticleTypes.PORTAL, d2, d3, d4, f, f2, f3, new int[0]);
                        }
                    }
                    else {
                        worldIn.setBlockState(blockpos, iblockstate, 2);
                        worldIn.setBlockToAir(pos);
                    }
                    return;
                }
            }
        }
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 5;
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public boolean shouldSideBeRendered(final IBlockAccess worldIn, final BlockPos pos, final EnumFacing side) {
        return true;
    }
    
    @Override
    public Item getItem(final World worldIn, final BlockPos pos) {
        return null;
    }
}
