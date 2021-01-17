// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.block;

import net.minecraft.inventory.IInventory;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.util.BlockPos;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;

public class BlockDropper extends BlockDispenser
{
    private final IBehaviorDispenseItem dropBehavior;
    
    public BlockDropper() {
        this.dropBehavior = new BehaviorDefaultDispenseItem();
    }
    
    @Override
    protected IBehaviorDispenseItem getBehavior(final ItemStack stack) {
        return this.dropBehavior;
    }
    
    @Override
    public TileEntity createNewTileEntity(final World worldIn, final int meta) {
        return new TileEntityDropper();
    }
    
    @Override
    protected void dispense(final World worldIn, final BlockPos pos) {
        final BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
        final TileEntityDispenser tileentitydispenser = blocksourceimpl.getBlockTileEntity();
        if (tileentitydispenser != null) {
            final int i = tileentitydispenser.getDispenseSlot();
            if (i < 0) {
                worldIn.playAuxSFX(1001, pos, 0);
            }
            else {
                final ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
                if (itemstack != null) {
                    final EnumFacing enumfacing = worldIn.getBlockState(pos).getValue((IProperty<EnumFacing>)BlockDropper.FACING);
                    final BlockPos blockpos = pos.offset(enumfacing);
                    final IInventory iinventory = TileEntityHopper.getInventoryAtPosition(worldIn, blockpos.getX(), blockpos.getY(), blockpos.getZ());
                    ItemStack itemstack2;
                    if (iinventory == null) {
                        itemstack2 = this.dropBehavior.dispense(blocksourceimpl, itemstack);
                        if (itemstack2 != null && itemstack2.stackSize <= 0) {
                            itemstack2 = null;
                        }
                    }
                    else {
                        itemstack2 = TileEntityHopper.putStackInInventoryAllSlots(iinventory, itemstack.copy().splitStack(1), enumfacing.getOpposite());
                        if (itemstack2 == null) {
                            final ItemStack copy;
                            itemstack2 = (copy = itemstack.copy());
                            if (--copy.stackSize <= 0) {
                                itemstack2 = null;
                            }
                        }
                        else {
                            itemstack2 = itemstack.copy();
                        }
                    }
                    tileentitydispenser.setInventorySlotContents(i, itemstack2);
                }
            }
        }
    }
}
