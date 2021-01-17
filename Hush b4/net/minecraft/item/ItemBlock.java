// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.block.Block;

public class ItemBlock extends Item
{
    protected final Block block;
    
    public ItemBlock(final Block block) {
        this.block = block;
    }
    
    @Override
    public ItemBlock setUnlocalizedName(final String unlocalizedName) {
        super.setUnlocalizedName(unlocalizedName);
        return this;
    }
    
    @Override
    public boolean onItemUse(final ItemStack stack, final EntityPlayer playerIn, final World worldIn, BlockPos pos, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
        final IBlockState iblockstate = worldIn.getBlockState(pos);
        final Block block = iblockstate.getBlock();
        if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        if (stack.stackSize == 0) {
            return false;
        }
        if (!playerIn.canPlayerEdit(pos, side, stack)) {
            return false;
        }
        if (worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack)) {
            final int i = this.getMetadata(stack.getMetadata());
            IBlockState iblockstate2 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, i, playerIn);
            if (worldIn.setBlockState(pos, iblockstate2, 3)) {
                iblockstate2 = worldIn.getBlockState(pos);
                if (iblockstate2.getBlock() == this.block) {
                    setTileEntityNBT(worldIn, playerIn, pos, stack);
                    this.block.onBlockPlacedBy(worldIn, pos, iblockstate2, playerIn, stack);
                }
                worldIn.playSoundEffect(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, this.block.stepSound.getPlaceSound(), (this.block.stepSound.getVolume() + 1.0f) / 2.0f, this.block.stepSound.getFrequency() * 0.8f);
                --stack.stackSize;
            }
            return true;
        }
        return false;
    }
    
    public static boolean setTileEntityNBT(final World worldIn, final EntityPlayer pos, final BlockPos stack, final ItemStack p_179224_3_) {
        final MinecraftServer minecraftserver = MinecraftServer.getServer();
        if (minecraftserver == null) {
            return false;
        }
        if (p_179224_3_.hasTagCompound() && p_179224_3_.getTagCompound().hasKey("BlockEntityTag", 10)) {
            final TileEntity tileentity = worldIn.getTileEntity(stack);
            if (tileentity != null) {
                if (!worldIn.isRemote && tileentity.func_183000_F() && !minecraftserver.getConfigurationManager().canSendCommands(pos.getGameProfile())) {
                    return false;
                }
                final NBTTagCompound nbttagcompound = new NBTTagCompound();
                final NBTTagCompound nbttagcompound2 = (NBTTagCompound)nbttagcompound.copy();
                tileentity.writeToNBT(nbttagcompound);
                final NBTTagCompound nbttagcompound3 = (NBTTagCompound)p_179224_3_.getTagCompound().getTag("BlockEntityTag");
                nbttagcompound.merge(nbttagcompound3);
                nbttagcompound.setInteger("x", stack.getX());
                nbttagcompound.setInteger("y", stack.getY());
                nbttagcompound.setInteger("z", stack.getZ());
                if (!nbttagcompound.equals(nbttagcompound2)) {
                    tileentity.readFromNBT(nbttagcompound);
                    tileentity.markDirty();
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean canPlaceBlockOnSide(final World worldIn, BlockPos pos, EnumFacing side, final EntityPlayer player, final ItemStack stack) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.snow_layer) {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos)) {
            pos = pos.offset(side);
        }
        return worldIn.canBlockBePlaced(this.block, pos, false, side, null, stack);
    }
    
    @Override
    public String getUnlocalizedName(final ItemStack stack) {
        return this.block.getUnlocalizedName();
    }
    
    @Override
    public String getUnlocalizedName() {
        return this.block.getUnlocalizedName();
    }
    
    @Override
    public CreativeTabs getCreativeTab() {
        return this.block.getCreativeTabToDisplayOn();
    }
    
    @Override
    public void getSubItems(final Item itemIn, final CreativeTabs tab, final List<ItemStack> subItems) {
        this.block.getSubBlocks(itemIn, tab, subItems);
    }
    
    public Block getBlock() {
        return this.block;
    }
}
