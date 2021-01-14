package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemBucket extends Item {
    /**
     * field for checking if the bucket has been filled.
     */
    private Block isFull;
    private static final String __OBFID = "CL_00000000";

    public ItemBucket(Block p_i45331_1_) {
        this.maxStackSize = 1;
        this.isFull = p_i45331_1_;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        boolean var4 = this.isFull == Blocks.air;
        MovingObjectPosition var5 = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, var4);

        if (var5 == null) {
            return itemStackIn;
        } else {
            if (var5.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                BlockPos var6 = var5.func_178782_a();

                if (!worldIn.isBlockModifiable(playerIn, var6)) {
                    return itemStackIn;
                }

                if (var4) {
                    if (!playerIn.func_175151_a(var6.offset(var5.field_178784_b), var5.field_178784_b, itemStackIn)) {
                        return itemStackIn;
                    }

                    IBlockState var7 = worldIn.getBlockState(var6);
                    Material var8 = var7.getBlock().getMaterial();

                    if (var8 == Material.water && ((Integer) var7.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
                        worldIn.setBlockToAir(var6);
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return this.func_150910_a(itemStackIn, playerIn, Items.water_bucket);
                    }

                    if (var8 == Material.lava && ((Integer) var7.getValue(BlockLiquid.LEVEL)).intValue() == 0) {
                        worldIn.setBlockToAir(var6);
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return this.func_150910_a(itemStackIn, playerIn, Items.lava_bucket);
                    }
                } else {
                    if (this.isFull == Blocks.air) {
                        return new ItemStack(Items.bucket);
                    }

                    BlockPos var9 = var6.offset(var5.field_178784_b);

                    if (!playerIn.func_175151_a(var9, var5.field_178784_b, itemStackIn)) {
                        return itemStackIn;
                    }

                    if (this.func_180616_a(worldIn, var9) && !playerIn.capabilities.isCreativeMode) {
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return new ItemStack(Items.bucket);
                    }
                }
            }

            return itemStackIn;
        }
    }

    private ItemStack func_150910_a(ItemStack p_150910_1_, EntityPlayer p_150910_2_, Item p_150910_3_) {
        if (p_150910_2_.capabilities.isCreativeMode) {
            return p_150910_1_;
        } else if (--p_150910_1_.stackSize <= 0) {
            return new ItemStack(p_150910_3_);
        } else {
            if (!p_150910_2_.inventory.addItemStackToInventory(new ItemStack(p_150910_3_))) {
                p_150910_2_.dropPlayerItemWithRandomChoice(new ItemStack(p_150910_3_, 1, 0), false);
            }

            return p_150910_1_;
        }
    }

    public boolean func_180616_a(World worldIn, BlockPos p_180616_2_) {
        if (this.isFull == Blocks.air) {
            return false;
        } else {
            Material var3 = worldIn.getBlockState(p_180616_2_).getBlock().getMaterial();
            boolean var4 = !var3.isSolid();

            if (!worldIn.isAirBlock(p_180616_2_) && !var4) {
                return false;
            } else {
                if (worldIn.provider.func_177500_n() && this.isFull == Blocks.flowing_water) {
                    int var5 = p_180616_2_.getX();
                    int var6 = p_180616_2_.getY();
                    int var7 = p_180616_2_.getZ();
                    worldIn.playSoundEffect((double) ((float) var5 + 0.5F), (double) ((float) var6 + 0.5F), (double) ((float) var7 + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int var8 = 0; var8 < 8; ++var8) {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) var5 + Math.random(), (double) var6 + Math.random(), (double) var7 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                } else {
                    if (!worldIn.isRemote && var4 && !var3.isLiquid()) {
                        worldIn.destroyBlock(p_180616_2_, true);
                    }

                    worldIn.setBlockState(p_180616_2_, this.isFull.getDefaultState(), 3);
                }

                return true;
            }
        }
    }
}
