// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.stats.StatList;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemEnderEye extends Item
{
    public ItemEnderEye() {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, final BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        if (!playerIn.canPlayerEdit(pos.offset(side), side, stack) || iblockstate.getBlock() != Blocks.end_portal_frame || iblockstate.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        worldIn.setBlockState(pos, iblockstate.withProperty((IProperty<Comparable>)BlockEndPortalFrame.EYE, true), 2);
        worldIn.updateComparatorOutputLevel(pos, Blocks.end_portal_frame);
        --stack.stackSize;
        for (int i = 0; i < 16; ++i) {
            final double d0 = pos.getX() + (5.0f + ItemEnderEye.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double d2 = pos.getY() + 0.8125f;
            final double d3 = pos.getZ() + (5.0f + ItemEnderEye.itemRand.nextFloat() * 6.0f) / 16.0f;
            final double d4 = 0.0;
            final double d5 = 0.0;
            final double d6 = 0.0;
            worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d2, d3, d4, d5, d6, new int[0]);
        }
        final EnumFacing enumfacing = iblockstate.getValue((IProperty<EnumFacing>)BlockEndPortalFrame.FACING);
        int l = 0;
        int j = 0;
        boolean flag1 = false;
        boolean flag2 = true;
        final EnumFacing enumfacing2 = enumfacing.rotateY();
        for (int k = -2; k <= 2; ++k) {
            final BlockPos blockpos1 = pos.offset(enumfacing2, k);
            final IBlockState iblockstate2 = worldIn.getBlockState(blockpos1);
            if (iblockstate2.getBlock() == Blocks.end_portal_frame) {
                if (!iblockstate2.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
                    flag2 = false;
                    break;
                }
                j = k;
                if (!flag1) {
                    l = k;
                    flag1 = true;
                }
            }
        }
        if (flag2 && j == l + 2) {
            BlockPos blockpos2 = pos.offset(enumfacing, 4);
            for (int i2 = l; i2 <= j; ++i2) {
                final BlockPos blockpos3 = blockpos2.offset(enumfacing2, i2);
                final IBlockState iblockstate3 = worldIn.getBlockState(blockpos3);
                if (iblockstate3.getBlock() != Blocks.end_portal_frame || !iblockstate3.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
                    flag2 = false;
                    break;
                }
            }
            for (int j2 = l - 1; j2 <= j + 1; j2 += 4) {
                blockpos2 = pos.offset(enumfacing2, j2);
                for (int l2 = 1; l2 <= 3; ++l2) {
                    final BlockPos blockpos4 = blockpos2.offset(enumfacing, l2);
                    final IBlockState iblockstate4 = worldIn.getBlockState(blockpos4);
                    if (iblockstate4.getBlock() != Blocks.end_portal_frame || !iblockstate4.getValue((IProperty<Boolean>)BlockEndPortalFrame.EYE)) {
                        flag2 = false;
                        break;
                    }
                }
            }
            if (flag2) {
                for (int k2 = l; k2 <= j; ++k2) {
                    blockpos2 = pos.offset(enumfacing2, k2);
                    for (int i3 = 1; i3 <= 3; ++i3) {
                        final BlockPos blockpos5 = blockpos2.offset(enumfacing, i3);
                        worldIn.setBlockState(blockpos5, Blocks.end_portal.getDefaultState(), 2);
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public ItemStack onItemRightClick(final ItemStack itemStackIn, final World worldIn, final EntityPlayer playerIn) {
        final MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
        if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && worldIn.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.end_portal_frame) {
            return itemStackIn;
        }
        if (!worldIn.isRemote) {
            final BlockPos blockpos = worldIn.getStrongholdPos("Stronghold", new BlockPos(playerIn));
            if (blockpos != null) {
                final EntityEnderEye entityendereye = new EntityEnderEye(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
                entityendereye.moveTowards(blockpos);
                worldIn.spawnEntityInWorld(entityendereye);
                worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5f, 0.4f / (ItemEnderEye.itemRand.nextFloat() * 0.4f + 0.8f));
                worldIn.playAuxSFXAtEntity(null, 1002, new BlockPos(playerIn), 0);
                if (!playerIn.capabilities.isCreativeMode) {
                    --itemStackIn.stackSize;
                }
                playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
            }
        }
        return itemStackIn;
    }
}
