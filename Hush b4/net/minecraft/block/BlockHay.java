// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class BlockHay extends BlockRotatedPillar
{
    public BlockHay() {
        super(Material.grass, MapColor.yellowColor);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockHay.AXIS, EnumFacing.Axis.Y));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Y;
        final int i = meta & 0xC;
        if (i == 4) {
            enumfacing$axis = EnumFacing.Axis.X;
        }
        else if (i == 8) {
            enumfacing$axis = EnumFacing.Axis.Z;
        }
        return this.getDefaultState().withProperty(BlockHay.AXIS, enumfacing$axis);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        int i = 0;
        final EnumFacing.Axis enumfacing$axis = state.getValue(BlockHay.AXIS);
        if (enumfacing$axis == EnumFacing.Axis.X) {
            i |= 0x4;
        }
        else if (enumfacing$axis == EnumFacing.Axis.Z) {
            i |= 0x8;
        }
        return i;
    }
    
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[] { BlockHay.AXIS });
    }
    
    @Override
    protected ItemStack createStackedBlock(final IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }
    
    @Override
    public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer) {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(BlockHay.AXIS, facing.getAxis());
    }
}
