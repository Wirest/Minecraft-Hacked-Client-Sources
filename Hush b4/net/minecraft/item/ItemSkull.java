// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.StatCollector;
import java.util.List;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.MathHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockSkull;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.creativetab.CreativeTabs;

public class ItemSkull extends Item
{
    private static final String[] skullTypes;
    
    static {
        skullTypes = new String[] { "skeleton", "wither", "zombie", "char", "creeper" };
    }
    
    public ItemSkull() {
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        if (side == EnumFacing.DOWN) {
            return false;
        }
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        final boolean flag = block.isReplaceable(worldIn, pos);
        if (!flag) {
            if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid()) {
                return false;
            }
            pos = pos.offset(side);
        }
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (!Blocks.skull.canPlaceBlockAt(worldIn, pos)) {
            return false;
        }
        if (!worldIn.isRemote) {
            worldIn.setBlockState(pos, Blocks.skull.getDefaultState().withProperty((IProperty<Comparable>)BlockSkull.FACING, side), 3);
            int i = 0;
            if (side == EnumFacing.UP) {
                i = (MathHelper.floor_double(playerIn.rotationYaw * 16.0f / 360.0f + 0.5) & 0xF);
            }
            final TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof TileEntitySkull) {
                final TileEntitySkull tileentityskull = (TileEntitySkull)tileentity;
                if (stack.getMetadata() == 3) {
                    GameProfile gameprofile = null;
                    if (stack.hasTagCompound()) {
                        final NBTTagCompound nbttagcompound = stack.getTagCompound();
                        if (nbttagcompound.hasKey("SkullOwner", 10)) {
                            gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag("SkullOwner"));
                        }
                        else if (nbttagcompound.hasKey("SkullOwner", 8) && nbttagcompound.getString("SkullOwner").length() > 0) {
                            gameprofile = new GameProfile(null, nbttagcompound.getString("SkullOwner"));
                        }
                    }
                    tileentityskull.setPlayerProfile(gameprofile);
                }
                else {
                    tileentityskull.setType(stack.getMetadata());
                }
                tileentityskull.setSkullRotation(i);
                Blocks.skull.checkWitherSpawn(worldIn, pos, tileentityskull);
            }
            --stack.stackSize;
        }
        return true;
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        for (int i = 0; i < ItemSkull.skullTypes.length; ++i) {
            subItems.add(new ItemStack(itemIn, 1, i));
        }
    }
    
    @Override
    public int getMetadata(final int damage) {
        return damage;
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        int i = stack.getMetadata();
        if (i < 0 || i >= ItemSkull.skullTypes.length) {
            i = 0;
        }
        return String.valueOf(super.getUnlocalizedName()) + "." + ItemSkull.skullTypes[i];
    }
    
    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        if (stack.getMetadata() == 3 && stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("SkullOwner", 8)) {
                return StatCollector.translateToLocalFormatted("item.skull.player.name", stack.getTagCompound().getString("SkullOwner"));
            }
            if (stack.getTagCompound().hasKey("SkullOwner", 10)) {
                final NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("SkullOwner");
                if (nbttagcompound.hasKey("Name", 8)) {
                    return StatCollector.translateToLocalFormatted("item.skull.player.name", nbttagcompound.getString("Name"));
                }
            }
        }
        return super.getItemStackDisplayName(stack);
    }
    
    @Override
    public boolean updateItemStackNBT(final NBTTagCompound nbt) {
        super.updateItemStackNBT(nbt);
        if (nbt.hasKey("SkullOwner", 8) && nbt.getString("SkullOwner").length() > 0) {
            GameProfile gameprofile = new GameProfile(null, nbt.getString("SkullOwner"));
            gameprofile = TileEntitySkull.updateGameprofile(gameprofile);
            nbt.setTag("SkullOwner", NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
            return true;
        }
        return false;
    }
}
