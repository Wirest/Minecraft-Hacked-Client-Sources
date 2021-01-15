package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemSkull extends Item
{
    private static final String[] skullTypes = new String[] {"skeleton", "wither", "zombie", "char", "creeper"};
    private static final String __OBFID = "CL_00000067";

    public ItemSkull()
    {
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Called when a Block is right-clicked with this Item
     *  
     * @param pos The block being right-clicked
     * @param side The side being right-clicked
     */
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        if (side == EnumFacing.DOWN)
        {
            return false;
        }
        else
        {
            IBlockState var9 = worldIn.getBlockState(pos);
            Block var10 = var9.getBlock();
            boolean var11 = var10.isReplaceable(worldIn, pos);

            if (!var11)
            {
                if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
                {
                    return false;
                }

                pos = pos.offset(side);
            }

            if (!playerIn.func_175151_a(pos, side, stack))
            {
                return false;
            }
            else if (!Blocks.skull.canPlaceBlockAt(worldIn, pos))
            {
                return false;
            }
            else
            {
                if (!worldIn.isRemote)
                {
                    worldIn.setBlockState(pos, Blocks.skull.getDefaultState().withProperty(BlockSkull.field_176418_a, side), 3);
                    int var12 = 0;

                    if (side == EnumFacing.UP)
                    {
                        var12 = MathHelper.floor_double((double)(playerIn.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
                    }

                    TileEntity var13 = worldIn.getTileEntity(pos);

                    if (var13 instanceof TileEntitySkull)
                    {
                        TileEntitySkull var14 = (TileEntitySkull)var13;

                        if (stack.getMetadata() == 3)
                        {
                            GameProfile var15 = null;

                            if (stack.hasTagCompound())
                            {
                                NBTTagCompound var16 = stack.getTagCompound();

                                if (var16.hasKey("SkullOwner", 10))
                                {
                                    var15 = NBTUtil.readGameProfileFromNBT(var16.getCompoundTag("SkullOwner"));
                                }
                                else if (var16.hasKey("SkullOwner", 8) && var16.getString("SkullOwner").length() > 0)
                                {
                                    var15 = new GameProfile((UUID)null, var16.getString("SkullOwner"));
                                }
                            }

                            var14.setPlayerProfile(var15);
                        }
                        else
                        {
                            var14.setType(stack.getMetadata());
                        }

                        var14.setSkullRotation(var12);
                        Blocks.skull.func_180679_a(worldIn, pos, var14);
                    }

                    --stack.stackSize;
                }

                return true;
            }
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     *  
     * @param subItems The List of sub-items. This is a List of ItemStacks.
     */
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        for (int var4 = 0; var4 < skullTypes.length; ++var4)
        {
            subItems.add(new ItemStack(itemIn, 1, var4));
        }
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
        int var2 = stack.getMetadata();

        if (var2 < 0 || var2 >= skullTypes.length)
        {
            var2 = 0;
        }

        return super.getUnlocalizedName() + "." + skullTypes[var2];
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getMetadata() == 3 && stack.hasTagCompound())
        {
            if (stack.getTagCompound().hasKey("SkullOwner", 8))
            {
                return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] {stack.getTagCompound().getString("SkullOwner")});
            }

            if (stack.getTagCompound().hasKey("SkullOwner", 10))
            {
                NBTTagCompound var2 = stack.getTagCompound().getCompoundTag("SkullOwner");

                if (var2.hasKey("Name", 8))
                {
                    return StatCollector.translateToLocalFormatted("item.skull.player.name", new Object[] {var2.getString("Name")});
                }
            }
        }

        return super.getItemStackDisplayName(stack);
    }

    /**
     * Called when an ItemStack with NBT data is read to potentially that ItemStack's NBT data
     */
    public boolean updateItemStackNBT(NBTTagCompound nbt)
    {
        super.updateItemStackNBT(nbt);

        if (nbt.hasKey("SkullOwner", 8) && nbt.getString("SkullOwner").length() > 0)
        {
            GameProfile var2 = new GameProfile((UUID)null, nbt.getString("SkullOwner"));
            var2 = TileEntitySkull.updateGameprofile(var2);
            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), var2));
            return true;
        }
        else
        {
            return false;
        }
    }
}
