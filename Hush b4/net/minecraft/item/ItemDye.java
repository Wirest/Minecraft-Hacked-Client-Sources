// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.block.IGrowable;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemDye extends Item
{
    public static final int[] dyeColors;
    
    static {
        dyeColors = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };
    }
    
    public ItemDye() {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        final int i = stack.getMetadata();
        return String.valueOf(super.getUnlocalizedName()) + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (!playerIn.canPlayerEdit(pos.offset(side), side, stack)) {
            return false;
        }
        final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
        if (enumdyecolor == EnumDyeColor.WHITE) {
            if (applyBonemeal(stack, worldIn, pos)) {
                if (!worldIn.isRemote) {
                    worldIn.playAuxSFX(2005, pos, 0);
                }
                return true;
            }
        }
        else if (enumdyecolor == EnumDyeColor.BROWN) {
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            final Block block = iblockstate.getBlock();
            if (block == Blocks.log && iblockstate.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
                if (side == EnumFacing.DOWN) {
                    return false;
                }
                if (side == EnumFacing.UP) {
                    return false;
                }
                pos = pos.offset(side);
                if (worldIn.isAirBlock(pos)) {
                    final IBlockState iblockstate2 = Blocks.cocoa.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
                    worldIn.setBlockState(pos, iblockstate2, 2);
                    if (!playerIn.capabilities.isCreativeMode) {
                        --stack.stackSize;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    public static boolean applyBonemeal(final ItemStack stack, final World worldIn, final BlockPos target) {
        final IBlockState iblockstate = worldIn.getBlockState(target);
        if (iblockstate.getBlock() instanceof IGrowable) {
            final IGrowable igrowable = (IGrowable)iblockstate.getBlock();
            if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
                if (!worldIn.isRemote) {
                    if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate)) {
                        igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
                    }
                    --stack.stackSize;
                }
                return true;
            }
        }
        return false;
    }
    
    public static void spawnBonemealParticles(final World worldIn, final BlockPos pos, int amount) {
        if (amount == 0) {
            amount = 15;
        }
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(worldIn, pos);
            for (int i = 0; i < amount; ++i) {
                final double d0 = ItemDye.itemRand.nextGaussian() * 0.02;
                final double d2 = ItemDye.itemRand.nextGaussian() * 0.02;
                final double d3 = ItemDye.itemRand.nextGaussian() * 0.02;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, pos.getX() + ItemDye.itemRand.nextFloat(), pos.getY() + ItemDye.itemRand.nextFloat() * block.getBlockBoundsMaxY(), pos.getZ() + ItemDye.itemRand.nextFloat(), d0, d2, d3, new int[0]);
            }
        }
    }
    
    @Override
    public boolean itemInteractionForEntity(final ItemStack stack, final EntityPlayer playerIn, final EntityLivingBase target) {
        if (target instanceof EntitySheep) {
            final EntitySheep entitysheep = (EntitySheep)target;
            final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
            if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor) {
                entitysheep.setFleeceColor(enumdyecolor);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        for (int i = 0; i < 16; ++i) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }
}
