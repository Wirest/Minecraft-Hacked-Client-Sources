package net.minecraft.item;

import net.minecraft.block.BlockEndPortalFrame;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityEnderEye;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class ItemEnderEye extends Item
{
    private static final String __OBFID = "CL_00000026";

    public ItemEnderEye()
    {
        this.setCreativeTab(CreativeTabs.tabMisc);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState var9 = worldIn.getBlockState(pos);

        if (playerIn.func_175151_a(pos.offset(side), side, stack) && var9.getBlock() == Blocks.end_portal_frame && !((Boolean)var9.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue())
        {
            if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                worldIn.setBlockState(pos, var9.withProperty(BlockEndPortalFrame.field_176507_b, Boolean.valueOf(true)), 2);
                worldIn.updateComparatorOutputLevel(pos, Blocks.end_portal_frame);
                --stack.stackSize;

                for (int var10 = 0; var10 < 16; ++var10)
                {
                    double var11 = (double)((float)pos.getX() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double var13 = (double)((float)pos.getY() + 0.8125F);
                    double var15 = (double)((float)pos.getZ() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
                    double var17 = 0.0D;
                    double var19 = 0.0D;
                    double var21 = 0.0D;
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, var11, var13, var15, var17, var19, var21, new int[0]);
                }

                EnumFacing var23 = (EnumFacing)var9.getValue(BlockEndPortalFrame.field_176508_a);
                int var24 = 0;
                int var12 = 0;
                boolean var25 = false;
                boolean var14 = true;
                EnumFacing var26 = var23.rotateY();

                for (int var16 = -2; var16 <= 2; ++var16)
                {
                    BlockPos var28 = pos.offset(var26, var16);
                    IBlockState var18 = worldIn.getBlockState(var28);

                    if (var18.getBlock() == Blocks.end_portal_frame)
                    {
                        if (!((Boolean)var18.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue())
                        {
                            var14 = false;
                            break;
                        }

                        var12 = var16;

                        if (!var25)
                        {
                            var24 = var16;
                            var25 = true;
                        }
                    }
                }

                if (var14 && var12 == var24 + 2)
                {
                    BlockPos var27 = pos.offset(var23, 4);
                    int var29;

                    for (var29 = var24; var29 <= var12; ++var29)
                    {
                        BlockPos var30 = var27.offset(var26, var29);
                        IBlockState var32 = worldIn.getBlockState(var30);

                        if (var32.getBlock() != Blocks.end_portal_frame || !((Boolean)var32.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue())
                        {
                            var14 = false;
                            break;
                        }
                    }

                    int var31;
                    BlockPos var33;

                    for (var29 = var24 - 1; var29 <= var12 + 1; var29 += 4)
                    {
                        var27 = pos.offset(var26, var29);

                        for (var31 = 1; var31 <= 3; ++var31)
                        {
                            var33 = var27.offset(var23, var31);
                            IBlockState var20 = worldIn.getBlockState(var33);

                            if (var20.getBlock() != Blocks.end_portal_frame || !((Boolean)var20.getValue(BlockEndPortalFrame.field_176507_b)).booleanValue())
                            {
                                var14 = false;
                                break;
                            }
                        }
                    }

                    if (var14)
                    {
                        for (var29 = var24; var29 <= var12; ++var29)
                        {
                            var27 = pos.offset(var26, var29);

                            for (var31 = 1; var31 <= 3; ++var31)
                            {
                                var33 = var27.offset(var23, var31);
                                worldIn.setBlockState(var33, Blocks.end_portal.getDefaultState(), 2);
                            }
                        }
                    }
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn)
    {
        MovingObjectPosition var4 = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, false);

        if (var4 != null && var4.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && worldIn.getBlockState(var4.func_178782_a()).getBlock() == Blocks.end_portal_frame)
        {
            return itemStackIn;
        }
        else
        {
            if (!worldIn.isRemote)
            {
                BlockPos var5 = worldIn.func_180499_a("Stronghold", new BlockPos(playerIn));

                if (var5 != null)
                {
                    EntityEnderEye var6 = new EntityEnderEye(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
                    var6.func_180465_a(var5);
                    worldIn.spawnEntityInWorld(var6);
                    worldIn.playSoundAtEntity(playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                    worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1002, new BlockPos(playerIn), 0);

                    if (!playerIn.capabilities.isCreativeMode)
                    {
                        --itemStackIn.stackSize;
                    }

                    playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                }
            }

            return itemStackIn;
        }
    }
}
