// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;

public class ItemSlab extends ItemBlock
{
    private final BlockSlab singleSlab;
    private final BlockSlab doubleSlab;
    
    public ItemSlab(final Block block, final BlockSlab singleSlab, final BlockSlab doubleSlab) {
        super(block);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return this.singleSlab.getUnlocalizedName(stack.getMetadata());
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (stack.stackSize == 0) {
            return false;
        }
        if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) {
            return false;
        }
        final Object object = this.singleSlab.getVariant(stack);
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this.singleSlab) {
            final IProperty iproperty = this.singleSlab.getVariantProperty();
            final Comparable comparable = iblockstate.getValue((IProperty<Comparable>)iproperty);
            final BlockSlab.EnumBlockHalf blockslab$enumblockhalf = iblockstate.getValue(BlockSlab.HALF);
            if (((side == EnumFacing.UP && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.BOTTOM) || (side == EnumFacing.DOWN && blockslab$enumblockhalf == BlockSlab.EnumBlockHalf.TOP)) && comparable == object) {
                final IBlockState iblockstate2 = this.doubleSlab.getDefaultState().withProperty((IProperty<Comparable>)iproperty, comparable);
                if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate2)) && worldIn.setBlockState(pos, iblockstate2, 3)) {
                    worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0f) / 2.0f, this.doubleSlab.stepSound.getFrequency() * 0.8f);
                    --stack.stackSize;
                }
                return true;
            }
        }
        return this.tryPlace(stack, worldIn, pos.offset(side), object) || super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
    }
    
    @Override
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos pos, final EnumFacing side, final EntityPlayer player, final ItemStack stack) {
        final BlockPos blockpos = pos;
        final IProperty iproperty = this.singleSlab.getVariantProperty();
        final Object object = this.singleSlab.getVariant(stack);
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this.singleSlab) {
            final boolean flag = iblockstate.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;
            if (((side == EnumFacing.UP && !flag) || (side == EnumFacing.DOWN && flag)) && object == iblockstate.getValue((IProperty<Comparable>)iproperty)) {
                return true;
            }
        }
        pos = pos.offset(side);
        final IBlockState iblockstate2 = worldIn.getBlockState(pos);
        return (iblockstate2.getBlock() == this.singleSlab && object == iblockstate2.getValue((IProperty<Comparable>)iproperty)) || super.canPlaceBlockOnSide(worldIn, blockpos, side, player, stack);
    }
    
    private boolean tryPlace(final ItemStack stack, final World worldIn, final BlockPos pos, final Object variantInStack) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (iblockstate.getBlock() == this.singleSlab) {
            final Comparable comparable = (Comparable)iblockstate.getValue(this.singleSlab.getVariantProperty());
            if (comparable == variantInStack) {
                final IBlockState iblockstate2 = this.doubleSlab.getDefaultState().withProperty(this.singleSlab.getVariantProperty(), comparable);
                if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, iblockstate2)) && worldIn.setBlockState(pos, iblockstate2, 3)) {
                    worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0f) / 2.0f, this.doubleSlab.stepSound.getFrequency() * 0.8f);
                    --stack.stackSize;
                }
                return true;
            }
        }
        return false;
    }
}
