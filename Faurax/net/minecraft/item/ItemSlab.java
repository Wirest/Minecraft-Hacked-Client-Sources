package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSlab extends ItemBlock
{
    private final BlockSlab field_150949_c;
    private final BlockSlab field_179226_c;
    private static final String __OBFID = "CL_00000071";

    public ItemSlab(Block p_i45782_1_, BlockSlab p_i45782_2_, BlockSlab p_i45782_3_)
    {
        super(p_i45782_1_);
        this.field_150949_c = p_i45782_2_;
        this.field_179226_c = p_i45782_3_;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    public int getMetadata(int damage)
    {
        return damage;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    public String getUnlocalizedName(ItemStack stack)
    {
        return this.field_150949_c.getFullSlabName(stack.getMetadata());
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!playerIn.func_175151_a(pos.offset(side), side, stack))
        {
            return false;
        }
        else
        {
            Object var9 = this.field_150949_c.func_176553_a(stack);
            IBlockState var10 = worldIn.getBlockState(pos);

            if (var10.getBlock() == this.field_150949_c)
            {
                IProperty var11 = this.field_150949_c.func_176551_l();
                Comparable var12 = var10.getValue(var11);
                BlockSlab.EnumBlockHalf var13 = (BlockSlab.EnumBlockHalf)var10.getValue(BlockSlab.HALF_PROP);

                if ((side == EnumFacing.UP && var13 == BlockSlab.EnumBlockHalf.BOTTOM || side == EnumFacing.DOWN && var13 == BlockSlab.EnumBlockHalf.TOP) && var12 == var9)
                {
                    IBlockState var14 = this.field_179226_c.getDefaultState().withProperty(var11, var12);

                    if (worldIn.checkNoEntityCollision(this.field_179226_c.getCollisionBoundingBox(worldIn, pos, var14)) && worldIn.setBlockState(pos, var14, 3))
                    {
                        worldIn.playSoundEffect((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), this.field_179226_c.stepSound.getPlaceSound(), (this.field_179226_c.stepSound.getVolume() + 1.0F) / 2.0F, this.field_179226_c.stepSound.getFrequency() * 0.8F);
                        --stack.stackSize;
                    }

                    return true;
                }
            }

            return this.func_180615_a(stack, worldIn, pos.offset(side), var9) ? true : super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
        }
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos p_179222_2_, EnumFacing p_179222_3_, EntityPlayer p_179222_4_, ItemStack p_179222_5_)
    {
        BlockPos var6 = p_179222_2_;
        IProperty var7 = this.field_150949_c.func_176551_l();
        Object var8 = this.field_150949_c.func_176553_a(p_179222_5_);
        IBlockState var9 = worldIn.getBlockState(p_179222_2_);

        if (var9.getBlock() == this.field_150949_c)
        {
            boolean var10 = var9.getValue(BlockSlab.HALF_PROP) == BlockSlab.EnumBlockHalf.TOP;

            if ((p_179222_3_ == EnumFacing.UP && !var10 || p_179222_3_ == EnumFacing.DOWN && var10) && var8 == var9.getValue(var7))
            {
                return true;
            }
        }

        p_179222_2_ = p_179222_2_.offset(p_179222_3_);
        IBlockState var11 = worldIn.getBlockState(p_179222_2_);
        return var11.getBlock() == this.field_150949_c && var8 == var11.getValue(var7) ? true : super.canPlaceBlockOnSide(worldIn, var6, p_179222_3_, p_179222_4_, p_179222_5_);
    }

    private boolean func_180615_a(ItemStack p_180615_1_, World worldIn, BlockPos p_180615_3_, Object p_180615_4_)
    {
        IBlockState var5 = worldIn.getBlockState(p_180615_3_);

        if (var5.getBlock() == this.field_150949_c)
        {
            Comparable var6 = var5.getValue(this.field_150949_c.func_176551_l());

            if (var6 == p_180615_4_)
            {
                IBlockState var7 = this.field_179226_c.getDefaultState().withProperty(this.field_150949_c.func_176551_l(), var6);

                if (worldIn.checkNoEntityCollision(this.field_179226_c.getCollisionBoundingBox(worldIn, p_180615_3_, var7)) && worldIn.setBlockState(p_180615_3_, var7, 3))
                {
                    worldIn.playSoundEffect((double)((float)p_180615_3_.getX() + 0.5F), (double)((float)p_180615_3_.getY() + 0.5F), (double)((float)p_180615_3_.getZ() + 0.5F), this.field_179226_c.stepSound.getPlaceSound(), (this.field_179226_c.stepSound.getVolume() + 1.0F) / 2.0F, this.field_179226_c.stepSound.getFrequency() * 0.8F);
                    --p_180615_1_.stackSize;
                }

                return true;
            }
        }

        return false;
    }
}
