package net.minecraft.item;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class ItemDye extends Item
{
    public static final int[] dyeColors = new int[] {1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320};
    private static final String __OBFID = "CL_00000022";

    public ItemDye()
    {
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        int var2 = stack.getMetadata();
        return super.getUnlocalizedName() + "." + EnumDyeColor.func_176766_a(var2).func_176762_d();
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (!playerIn.func_175151_a(pos.offset(side), side, stack))
        {
            return false;
        }
        else
        {
            EnumDyeColor var9 = EnumDyeColor.func_176766_a(stack.getMetadata());

            if (var9 == EnumDyeColor.WHITE)
            {
                if (func_179234_a(stack, worldIn, pos))
                {
                    if (!worldIn.isRemote)
                    {
                        worldIn.playAuxSFX(2005, pos, 0);
                    }

                    return true;
                }
            }
            else if (var9 == EnumDyeColor.BROWN)
            {
                IBlockState var10 = worldIn.getBlockState(pos);
                Block var11 = var10.getBlock();

                if (var11 == Blocks.log && var10.getValue(BlockPlanks.VARIANT_PROP) == BlockPlanks.EnumType.JUNGLE)
                {
                    if (side == EnumFacing.DOWN)
                    {
                        return false;
                    }

                    if (side == EnumFacing.UP)
                    {
                        return false;
                    }

                    pos = pos.offset(side);

                    if (worldIn.isAirBlock(pos))
                    {
                        IBlockState var12 = Blocks.cocoa.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, playerIn);
                        worldIn.setBlockState(pos, var12, 2);

                        if (!playerIn.capabilities.isCreativeMode)
                        {
                            --stack.stackSize;
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public static boolean func_179234_a(ItemStack p_179234_0_, World worldIn, BlockPos p_179234_2_)
    {
        IBlockState var3 = worldIn.getBlockState(p_179234_2_);

        if (var3.getBlock() instanceof IGrowable)
        {
            IGrowable var4 = (IGrowable)var3.getBlock();

            if (var4.isStillGrowing(worldIn, p_179234_2_, var3, worldIn.isRemote))
            {
                if (!worldIn.isRemote)
                {
                    if (var4.canUseBonemeal(worldIn, worldIn.rand, p_179234_2_, var3))
                    {
                        var4.grow(worldIn, worldIn.rand, p_179234_2_, var3);
                    }

                    --p_179234_0_.stackSize;
                }

                return true;
            }
        }

        return false;
    }

    public static void func_180617_a(World worldIn, BlockPos p_180617_1_, int p_180617_2_)
    {
        if (p_180617_2_ == 0)
        {
            p_180617_2_ = 15;
        }

        Block var3 = worldIn.getBlockState(p_180617_1_).getBlock();

        if (var3.getMaterial() != Material.air)
        {
            var3.setBlockBoundsBasedOnState(worldIn, p_180617_1_);

            for (int var4 = 0; var4 < p_180617_2_; ++var4)
            {
                double var5 = itemRand.nextGaussian() * 0.02D;
                double var7 = itemRand.nextGaussian() * 0.02D;
                double var9 = itemRand.nextGaussian() * 0.02D;
                worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (double)((float)p_180617_1_.getX() + itemRand.nextFloat()), (double)p_180617_1_.getY() + (double)itemRand.nextFloat() * var3.getBlockBoundsMaxY(), (double)((float)p_180617_1_.getZ() + itemRand.nextFloat()), var5, var7, var9, new int[0]);
            }
        }
    }

    /**
     * Returns true if the item can be used on the given entity, e.g. shears on sheep.
     */
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target)
    {
        if (target instanceof EntitySheep)
        {
            EntitySheep var4 = (EntitySheep)target;
            EnumDyeColor var5 = EnumDyeColor.func_176766_a(stack.getMetadata());

            if (!var4.getSheared() && var4.func_175509_cj() != var5)
            {
                var4.func_175512_b(var5);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *  
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        for (int var4 = 0; var4 < 16; ++var4)
        {
            subItems.add(new ItemStack(itemIn, 1, var4));
        }
    }
}
