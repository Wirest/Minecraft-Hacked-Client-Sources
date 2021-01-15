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

public class ItemBucket extends Item
{
    /** field for checking if the bucket has been filled. */
    private Block isFull;

    public ItemBucket(Block containedBlock)
    {
        this.maxStackSize = 1;
        this.isFull = containedBlock;
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        boolean var4 = this.isFull == Blocks.air;
        MovingObjectPosition var5 = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, var4);

        if (var5 == null)
        {
            return itemStackIn;
        }
        else
        {
            if (var5.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
            {
                BlockPos var6 = var5.getBlockPos();

                if (!worldIn.isBlockModifiable(playerIn, var6))
                {
                    return itemStackIn;
                }

                if (var4)
                {
                    if (!playerIn.canPlayerEdit(var6.offset(var5.sideHit), var5.sideHit, itemStackIn))
                    {
                        return itemStackIn;
                    }

                    IBlockState var7 = worldIn.getBlockState(var6);
                    Material var8 = var7.getBlock().getMaterial();

                    if (var8 == Material.water && ((Integer)var7.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                    {
                        worldIn.setBlockToAir(var6);
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return this.fillBucket(itemStackIn, playerIn, Items.water_bucket);
                    }

                    if (var8 == Material.lava && ((Integer)var7.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                    {
                        worldIn.setBlockToAir(var6);
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return this.fillBucket(itemStackIn, playerIn, Items.lava_bucket);
                    }
                }
                else
                {
                    if (this.isFull == Blocks.air)
                    {
                        return new ItemStack(Items.bucket);
                    }

                    BlockPos var9 = var6.offset(var5.sideHit);

                    if (!playerIn.canPlayerEdit(var9, var5.sideHit, itemStackIn))
                    {
                        return itemStackIn;
                    }

                    if (this.tryPlaceContainedLiquid(worldIn, var9) && !playerIn.capabilities.isCreativeMode)
                    {
                        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        return new ItemStack(Items.bucket);
                    }
                }
            }

            return itemStackIn;
        }
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket)
    {
        if (player.capabilities.isCreativeMode)
        {
            return emptyBuckets;
        }
        else if (--emptyBuckets.stackSize <= 0)
        {
            return new ItemStack(fullBucket);
        }
        else
        {
            if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket)))
            {
                player.dropPlayerItemWithRandomChoice(new ItemStack(fullBucket, 1, 0), false);
            }

            return emptyBuckets;
        }
    }

    public boolean tryPlaceContainedLiquid(World worldIn, BlockPos pos)
    {
        if (this.isFull == Blocks.air)
        {
            return false;
        }
        else
        {
            Material var3 = worldIn.getBlockState(pos).getBlock().getMaterial();
            boolean var4 = !var3.isSolid();

            if (!worldIn.isAirBlock(pos) && !var4)
            {
                return false;
            }
            else
            {
                if (worldIn.provider.doesWaterVaporize() && this.isFull == Blocks.flowing_water)
                {
                    int var5 = pos.getX();
                    int var6 = pos.getY();
                    int var7 = pos.getZ();
                    worldIn.playSoundEffect(var5 + 0.5F, var6 + 0.5F, var7 + 0.5F, "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int var8 = 0; var8 < 8; ++var8)
                    {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, var5 + Math.random(), var6 + Math.random(), var7 + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
                    }
                }
                else
                {
                    if (!worldIn.isRemote && var4 && !var3.isLiquid())
                    {
                        worldIn.destroyBlock(pos, true);
                    }

                    worldIn.setBlockState(pos, this.isFull.getDefaultState(), 3);
                }

                return true;
            }
        }
    }
}
