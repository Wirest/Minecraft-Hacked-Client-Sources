package net.minecraft.item;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import org.apache.commons.lang3.StringUtils;

public class ItemSkull extends Item
{
    private static final String[] SKULL_TYPES = new String[] {"skeleton", "wither", "zombie", "char", "creeper", "dragon"};

    public ItemSkull()
    {
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        if (hand == EnumFacing.DOWN)
        {
            return EnumActionResult.FAIL;
        }
        else
        {
            IBlockState iblockstate = playerIn.getBlockState(worldIn);
            Block block = iblockstate.getBlock();
            boolean flag = block.isReplaceable(playerIn, worldIn);

            if (!flag)
            {
                if (!playerIn.getBlockState(worldIn).getMaterial().isSolid())
                {
                    return EnumActionResult.FAIL;
                }

                worldIn = worldIn.offset(hand);
            }

            ItemStack itemstack = stack.getHeldItem(pos);

            if (stack.canPlayerEdit(worldIn, hand, itemstack) && Blocks.SKULL.canPlaceBlockAt(playerIn, worldIn))
            {
                if (playerIn.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    playerIn.setBlockState(worldIn, Blocks.SKULL.getDefaultState().withProperty(BlockSkull.FACING, hand), 11);
                    int i = 0;

                    if (hand == EnumFacing.UP)
                    {
                        i = MathHelper.floor((double)(stack.rotationYaw * 16.0F / 360.0F) + 0.5D) & 15;
                    }

                    TileEntity tileentity = playerIn.getTileEntity(worldIn);

                    if (tileentity instanceof TileEntitySkull)
                    {
                        TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;

                        if (itemstack.getMetadata() == 3)
                        {
                            GameProfile gameprofile = null;

                            if (itemstack.hasTagCompound())
                            {
                                NBTTagCompound nbttagcompound = itemstack.getTagCompound();

                                if (nbttagcompound.hasKey("SkullOwner", 10))
                                {
                                    gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                                }
                                else if (nbttagcompound.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbttagcompound.getString("SkullOwner")))
                                {
                                    gameprofile = new GameProfile((UUID)null, nbttagcompound.getString("SkullOwner"));
                                }
                            }

                            tileentityskull.setPlayerProfile(gameprofile);
                        }
                        else
                        {
                            tileentityskull.setType(itemstack.getMetadata());
                        }

                        tileentityskull.setSkullRotation(i);
                        Blocks.SKULL.checkWitherSpawn(playerIn, worldIn, tileentityskull);
                    }

                    if (stack instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.field_193137_x.func_193173_a((EntityPlayerMP)stack, worldIn, itemstack);
                    }

                    itemstack.func_190918_g(1);
                    return EnumActionResult.SUCCESS;
                }
            }
            else
            {
                return EnumActionResult.FAIL;
            }
        }
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab)
    {
        if (this.func_194125_a(itemIn))
        {
            for (int i = 0; i < SKULL_TYPES.length; ++i)
            {
                tab.add(new ItemStack(this, 1, i));
            }
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
        int i = stack.getMetadata();

        if (i < 0 || i >= SKULL_TYPES.length)
        {
            i = 0;
        }

        return super.getUnlocalizedName() + "." + SKULL_TYPES[i];
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        if (stack.getMetadata() == 3 && stack.hasTagCompound())
        {
            if (stack.getTagCompound().hasKey("SkullOwner", 8))
            {
                return I18n.translateToLocalFormatted("item.skull.player.name", stack.getTagCompound().getString("SkullOwner"));
            }

            if (stack.getTagCompound().hasKey("SkullOwner", 10))
            {
                NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("SkullOwner");

                if (nbttagcompound.hasKey("Name", 8))
                {
                    return I18n.translateToLocalFormatted("item.skull.player.name", nbttagcompound.getString("Name"));
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

        if (nbt.hasKey("SkullOwner", 8) && !StringUtils.isBlank(nbt.getString("SkullOwner")))
        {
            GameProfile gameprofile = new GameProfile((UUID)null, nbt.getString("SkullOwner"));
            gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
            return true;
        }
        else
        {
            return false;
        }
    }
}
