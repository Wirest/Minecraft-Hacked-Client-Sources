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
    private final BlockSlab singleSlab;
    private final BlockSlab doubleSlab;

    public ItemSlab(Block block, BlockSlab singleSlab, BlockSlab doubleSlab)
    {
        super(block);
        this.singleSlab = singleSlab;
        this.doubleSlab = doubleSlab;
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Converts the given ItemStack damage value into a metadata value to be placed in the world when this Item is
     * placed as a Block (mostly used with ItemBlocks).
     */
    @Override
	public int getMetadata(int damage)
    {
        return damage;
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return this.singleSlab.getUnlocalizedName(stack.getMetadata());
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    @Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
        {
            return false;
        }
        else
        {
            Object var9 = this.singleSlab.getVariant(stack);
            IBlockState var10 = worldIn.getBlockState(pos);

            if (var10.getBlock() == this.singleSlab)
            {
                IProperty var11 = this.singleSlab.getVariantProperty();
                Comparable var12 = var10.getValue(var11);
                BlockSlab.EnumBlockHalf var13 = (BlockSlab.EnumBlockHalf)var10.getValue(BlockSlab.HALF);

                if ((side == EnumFacing.UP && var13 == BlockSlab.EnumBlockHalf.BOTTOM || side == EnumFacing.DOWN && var13 == BlockSlab.EnumBlockHalf.TOP) && var12 == var9)
                {
                    IBlockState var14 = this.doubleSlab.getDefaultState().withProperty(var11, var12);

                    if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, var14)) && worldIn.setBlockState(pos, var14, 3))
                    {
                        worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
                        --stack.stackSize;
                    }

                    return true;
                }
            }

            return this.tryPlace(stack, worldIn, pos.offset(side), var9) ? true : super.onItemUse(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ);
        }
    }

    @Override
	public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        BlockPos var6 = pos;
        IProperty var7 = this.singleSlab.getVariantProperty();
        Object var8 = this.singleSlab.getVariant(stack);
        IBlockState var9 = worldIn.getBlockState(pos);

        if (var9.getBlock() == this.singleSlab)
        {
            boolean var10 = var9.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP;

            if ((side == EnumFacing.UP && !var10 || side == EnumFacing.DOWN && var10) && var8 == var9.getValue(var7))
            {
                return true;
            }
        }

        pos = pos.offset(side);
        IBlockState var11 = worldIn.getBlockState(pos);
        return var11.getBlock() == this.singleSlab && var8 == var11.getValue(var7) ? true : super.canPlaceBlockOnSide(worldIn, var6, side, player, stack);
    }

    private boolean tryPlace(ItemStack stack, World worldIn, BlockPos pos, Object variantInStack)
    {
        IBlockState var5 = worldIn.getBlockState(pos);

        if (var5.getBlock() == this.singleSlab)
        {
            Comparable var6 = var5.getValue(this.singleSlab.getVariantProperty());

            if (var6 == variantInStack)
            {
                IBlockState var7 = this.doubleSlab.getDefaultState().withProperty(this.singleSlab.getVariantProperty(), var6);

                if (worldIn.checkNoEntityCollision(this.doubleSlab.getCollisionBoundingBox(worldIn, pos, var7)) && worldIn.setBlockState(pos, var7, 3))
                {
                    worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.doubleSlab.stepSound.getPlaceSound(), (this.doubleSlab.stepSound.getVolume() + 1.0F) / 2.0F, this.doubleSlab.stepSound.getFrequency() * 0.8F);
                    --stack.stackSize;
                }

                return true;
            }
        }

        return false;
    }
}
