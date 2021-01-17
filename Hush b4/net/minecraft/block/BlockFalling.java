// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.Material;

public class BlockFalling extends Block
{
    public static boolean fallInstantly;
    
    public BlockFalling() {
        super(Material.sand);
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    public BlockFalling(final Material materialIn) {
        super(materialIn);
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
        if (!worldIn.isRemote) {
            this.checkFallable(worldIn, pos);
        }
    }
    
    private void checkFallable(final World worldIn, final BlockPos pos) {
        if (canFallInto(worldIn, pos.down()) && pos.getY() >= 0) {
            final int i = 32;
            if (!BlockFalling.fallInstantly && worldIn.isAreaLoaded(pos.add(-i, -i, -i), pos.add(i, i, i))) {
                if (!worldIn.isRemote) {
                    final EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos));
                    this.onStartFalling(entityfallingblock);
                    worldIn.spawnEntityInWorld(entityfallingblock);
                }
            }
            else {
                worldIn.setBlockToAir(pos);
                BlockPos blockpos;
                for (blockpos = pos.down(); canFallInto(worldIn, blockpos) && blockpos.getY() > 0; blockpos = blockpos.down()) {}
                if (blockpos.getY() > 0) {
                    worldIn.setBlockState(blockpos.up(), this.getDefaultState());
                }
            }
        }
    }
    
    protected void onStartFalling(final EntityFallingBlock fallingEntity) {
    }
    
    @Override
    public int tickRate(final World worldIn) {
        return 2;
    }
    
    public static boolean canFallInto(final World worldIn, final BlockPos pos) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        final Material material = block.blockMaterial;
        return block == Blocks.fire || material == Material.air || material == Material.water || material == Material.lava;
    }
    
    public void onEndFalling(final World worldIn, final BlockPos pos) {
    }
}
