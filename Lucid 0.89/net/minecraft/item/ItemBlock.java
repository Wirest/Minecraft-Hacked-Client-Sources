package net.minecraft.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemBlock extends Item
{
    protected final Block block;

    public ItemBlock(Block block)
    {
        this.block = block;
    }

    /**
     * Sets the unlocalized name of this item to the string passed as the parameter, prefixed by "item."
     */
    @Override
	public ItemBlock setUnlocalizedName(String unlocalizedName)
    {
        super.setUnlocalizedName(unlocalizedName);
        return this;
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
        IBlockState var9 = worldIn.getBlockState(pos);
        Block var10 = var9.getBlock();

        if (var10 == Blocks.snow_layer && ((Integer)var9.getValue(BlockSnow.LAYERS)).intValue() < 1)
        {
            side = EnumFacing.UP;
        }
        else if (!var10.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }

        if (stack.stackSize == 0)
        {
            return false;
        }
        else if (!playerIn.canPlayerEdit(pos, side, stack))
        {
            return false;
        }
        else if (pos.getY() == 255 && this.block.getMaterial().isSolid())
        {
            return false;
        }
        else if (worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack))
        {
            int var11 = this.getMetadata(stack.getMetadata());
            IBlockState var12 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, var11, playerIn);

            if (worldIn.setBlockState(pos, var12, 3))
            {
                var12 = worldIn.getBlockState(pos);

                if (var12.getBlock() == this.block)
                {
                    setTileEntityNBT(worldIn, pos, stack);
                    this.block.onBlockPlacedBy(worldIn, pos, var12, playerIn, stack);
                }

                worldIn.playSoundEffect(pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0F) / 2.0F, this.block.stepSound.getFrequency() * 0.8F);
                --stack.stackSize;
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean setTileEntityNBT(World worldIn, BlockPos pos, ItemStack stack)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10))
        {
            TileEntity var3 = worldIn.getTileEntity(pos);

            if (var3 != null)
            {
                NBTTagCompound var4 = new NBTTagCompound();
                NBTTagCompound var5 = (NBTTagCompound)var4.copy();
                var3.writeToNBT(var4);
                NBTTagCompound var6 = (NBTTagCompound)stack.getTagCompound().getTag("BlockEntityTag");
                var4.merge(var6);
                var4.setInteger("x", pos.getX());
                var4.setInteger("y", pos.getY());
                var4.setInteger("z", pos.getZ());

                if (!var4.equals(var5))
                {
                    var3.readFromNBT(var4);
                    var3.markDirty();
                    return true;
                }
            }
        }

        return false;
    }

    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        Block var6 = worldIn.getBlockState(pos).getBlock();

        if (var6 == Blocks.snow_layer)
        {
            side = EnumFacing.UP;
        }
        else if (!var6.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }

        return worldIn.canBlockBePlaced(this.block, pos, false, side, (Entity)null, stack);
    }

    /**
     * Returns the unlocalized name of this item. This version accepts an ItemStack so different stacks can have
     * different names based on their damage or NBT.
     */
    @Override
	public String getUnlocalizedName(ItemStack stack)
    {
        return this.block.getUnlocalizedName();
    }

    /**
     * Returns the unlocalized name of this item.
     */
    @Override
	public String getUnlocalizedName()
    {
        return this.block.getUnlocalizedName();
    }

    /**
     * gets the CreativeTab this item is displayed on
     */
    @Override
	public CreativeTabs getCreativeTab()
    {
        return this.block.getCreativeTabToDisplayOn();
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *  
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    @Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        this.block.getSubBlocks(itemIn, tab, subItems);
    }

    public Block getBlock()
    {
        return this.block;
    }
}
