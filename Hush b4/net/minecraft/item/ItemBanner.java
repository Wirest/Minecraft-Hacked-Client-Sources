// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.StatCollector;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.block.BlockWallSign;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockStandingSign;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

public class ItemBanner extends ItemBlock
{
    public ItemBanner() {
        super(Blocks.standing_banner);
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        }
        if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()) {
            return false;
        }
        pos = pos.offset(side);
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (!Blocks.standing_banner.canPlaceBlockAt(worldIn, pos)) {
            return false;
        }
        if (worldIn.isRemote) {
            return true;
        }
        if (side == EnumFacing.UP) {
            final int i = MathHelper.floor_double((playerIn.rotationYaw + 180.0f) * 16.0f / 360.0f + 0.5) & 0xF;
            worldIn.setBlockState(pos, Blocks.standing_banner.getDefaultState().withProperty((IProperty<Comparable>)BlockStandingSign.ROTATION, i), 3);
        }
        else {
            worldIn.setBlockState(pos, Blocks.wall_banner.getDefaultState().withProperty((IProperty<Comparable>)BlockWallSign.FACING, side), 3);
        }
        --stack.stackSize;
        final TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof TileEntityBanner) {
            ((TileEntityBanner)tileentity).setItemValues(stack);
        }
        return true;
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        String s = "item.banner.";
        final EnumDyeColor enumdyecolor = this.getBaseColor(stack);
        s = String.valueOf(s) + enumdyecolor.getUnlocalizedName() + ".name";
        return StatCollector.translateToLocal(s);
    }
    
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) {
            final NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
            for (int i = 0; i < nbttaglist.tagCount() && i < 6; ++i) {
                final NBTTagCompound nbttagcompound2 = nbttaglist.getCompoundTagAt(i);
                final EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound2.getInteger("Color"));
                final TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = TileEntityBanner.EnumBannerPattern.getPatternByID(nbttagcompound2.getString("Pattern"));
                if (tileentitybanner$enumbannerpattern != null) {
                    tooltip.add(StatCollector.translateToLocal("item.banner." + tileentitybanner$enumbannerpattern.getPatternName() + "." + enumdyecolor.getUnlocalizedName()));
                }
            }
        }
    }
    
    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass == 0) {
            return 16777215;
        }
        final EnumDyeColor enumdyecolor = this.getBaseColor(stack);
        return enumdyecolor.getMapColor().colorValue;
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        EnumDyeColor[] values;
        for (int length = (values = EnumDyeColor.values()).length, i = 0; i < length; ++i) {
            final EnumDyeColor enumdyecolor = values[i];
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            TileEntityBanner.func_181020_a(nbttagcompound, enumdyecolor.getDyeDamage(), null);
            final NBTTagCompound nbttagcompound2 = new NBTTagCompound();
            nbttagcompound2.setTag("BlockEntityTag", nbttagcompound);
            final ItemStack itemstack = new ItemStack(itemIn, 1, enumdyecolor.getDyeDamage());
            itemstack.setTagCompound(nbttagcompound2);
            subItems.add(itemstack);
        }
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
        return CreativeTabs.tabDecorations;
    }
    
    private EnumDyeColor getBaseColor(final ItemStack stack) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
        EnumDyeColor enumdyecolor = null;
        if (nbttagcompound != null && nbttagcompound.hasKey("Base")) {
            enumdyecolor = EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base"));
        }
        else {
            enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
        }
        return enumdyecolor;
    }
}
