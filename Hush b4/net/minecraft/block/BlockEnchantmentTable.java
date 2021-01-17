// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IInteractionObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityEnchantmentTable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.init.Blocks;
import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockEnchantmentTable extends BlockContainer
{
    protected BlockEnchantmentTable() {
        super(Material.rock, MapColor.redColor);
        this.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 0.75f, 1.0f);
        this.setLightOpacity(0);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }
    
    @Override
    public boolean isFullCube() {
        return false;
    }
    
    @Override
    public void randomDisplayTick(final World worldIn, final BlockPos pos, final IBlockState state, final Random rand) {
        super.randomDisplayTick(worldIn, pos, state, rand);
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }
                if (rand.nextInt(16) == 0) {
                    for (int k = 0; k <= 1; ++k) {
                        final BlockPos blockpos = pos.add(i, k, j);
                        if (worldIn.getBlockState(blockpos).getBlock() == Blocks.bookshelf) {
                            if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
                                break;
                            }
                            worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX() + 0.5, pos.getY() + 2.0, pos.getZ() + 0.5, i + rand.nextFloat() - 0.5, k - rand.nextFloat() - 1.0f, j + rand.nextFloat() - 0.5, new int[0]);
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public boolean isOpaqueCube() {
        return false;
    }
    
    @Override
    public int getRenderType() {
        return 3;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityEnchantmentTable();
    }
    
    @Override
    public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (worldIn.isRemote) {
            return true;
        }
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityEnchantmentTable) {
            playerIn.displayGui((IInteractionObject)tileentity);
        }
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (stack.hasDisplayName()) {
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntityEnchantmentTable) {
                ((TileEntityEnchantmentTable)tileentity).setCustomName(stack.getDisplayName());
            }
        }
    }
}
