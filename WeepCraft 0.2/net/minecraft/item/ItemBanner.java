package net.minecraft.item;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

public class ItemBanner extends ItemBlock
{
    public ItemBanner()
    {
        super(Blocks.STANDING_BANNER);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    /**
     * Called when a Block is right-clicked with this Item
     */
    public EnumActionResult onItemUse(EntityPlayer stack, World playerIn, BlockPos worldIn, EnumHand pos, EnumFacing hand, float facing, float hitX, float hitY)
    {
        IBlockState iblockstate = playerIn.getBlockState(worldIn);
        boolean flag = iblockstate.getBlock().isReplaceable(playerIn, worldIn);

        if (hand != EnumFacing.DOWN && (iblockstate.getMaterial().isSolid() || flag) && (!flag || hand == EnumFacing.UP))
        {
            worldIn = worldIn.offset(hand);
            ItemStack itemstack = stack.getHeldItem(pos);

            if (stack.canPlayerEdit(worldIn, hand, itemstack) && Blocks.STANDING_BANNER.canPlaceBlockAt(playerIn, worldIn))
            {
                if (playerIn.isRemote)
                {
                    return EnumActionResult.SUCCESS;
                }
                else
                {
                    worldIn = flag ? worldIn.down() : worldIn;

                    if (hand == EnumFacing.UP)
                    {
                        int i = MathHelper.floor((double)((stack.rotationYaw + 180.0F) * 16.0F / 360.0F) + 0.5D) & 15;
                        playerIn.setBlockState(worldIn, Blocks.STANDING_BANNER.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
                    }
                    else
                    {
                        playerIn.setBlockState(worldIn, Blocks.WALL_BANNER.getDefaultState().withProperty(BlockWallSign.FACING, hand), 3);
                    }

                    TileEntity tileentity = playerIn.getTileEntity(worldIn);

                    if (tileentity instanceof TileEntityBanner)
                    {
                        ((TileEntityBanner)tileentity).setItemValues(itemstack, false);
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
        else
        {
            return EnumActionResult.FAIL;
        }
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = "item.banner.";
        EnumDyeColor enumdyecolor = getBaseColor(stack);
        s = s + enumdyecolor.getUnlocalizedName() + ".name";
        return I18n.translateToLocal(s);
    }

    public static void appendHoverTextFromTileEntityTag(ItemStack stack, List<String> p_185054_1_)
    {
        NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");

        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns"))
        {
            NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);

            for (int i = 0; i < nbttaglist.tagCount() && i < 6; ++i)
            {
                NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
                EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound1.getInteger("Color"));
                BannerPattern bannerpattern = BannerPattern.func_190994_a(nbttagcompound1.getString("Pattern"));

                if (bannerpattern != null)
                {
                    p_185054_1_.add(I18n.translateToLocal("item.banner." + bannerpattern.func_190997_a() + "." + enumdyecolor.getUnlocalizedName()));
                }
            }
        }
    }

    /**
     * allows items to add custom lines of information to the mouseover description
     */
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced)
    {
        appendHoverTextFromTileEntityTag(stack, tooltip);
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    public void getSubItems(CreativeTabs itemIn, NonNullList<ItemStack> tab)
    {
        if (this.func_194125_a(itemIn))
        {
            for (EnumDyeColor enumdyecolor : EnumDyeColor.values())
            {
                tab.add(func_190910_a(enumdyecolor, (NBTTagList)null));
            }
        }
    }

    public static ItemStack func_190910_a(EnumDyeColor p_190910_0_, @Nullable NBTTagList p_190910_1_)
    {
        ItemStack itemstack = new ItemStack(Items.BANNER, 1, p_190910_0_.getDyeDamage());

        if (p_190910_1_ != null && !p_190910_1_.hasNoTags())
        {
            itemstack.func_190925_c("BlockEntityTag").setTag("Patterns", p_190910_1_.copy());
        }

        return itemstack;
    }

    /**
     * gets the CreativeTab this item is displayed on
     */
    public CreativeTabs getCreativeTab()
    {
        return CreativeTabs.DECORATIONS;
    }

    public static EnumDyeColor getBaseColor(ItemStack stack)
    {
        return EnumDyeColor.byDyeDamage(stack.getMetadata() & 15);
    }
}
