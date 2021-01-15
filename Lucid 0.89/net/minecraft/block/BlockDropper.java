package net.minecraft.block;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityDispenser;
import net.minecraft.tileentity.TileEntityDropper;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDropper extends BlockDispenser
{
    private final IBehaviorDispenseItem dropBehavior = new BehaviorDefaultDispenseItem();

    @Override
	protected IBehaviorDispenseItem getBehavior(ItemStack stack)
    {
        return this.dropBehavior;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDropper();
    }

    @Override
	protected void dispense(World worldIn, BlockPos pos)
    {
        BlockSourceImpl var3 = new BlockSourceImpl(worldIn, pos);
        TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();

        if (var4 != null)
        {
            int var5 = var4.getDispenseSlot();

            if (var5 < 0)
            {
                worldIn.playAuxSFX(1001, pos, 0);
            }
            else
            {
                ItemStack var6 = var4.getStackInSlot(var5);

                if (var6 != null)
                {
                    EnumFacing var7 = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
                    BlockPos var8 = pos.offset(var7);
                    IInventory var9 = TileEntityHopper.func_145893_b(worldIn, var8.getX(), var8.getY(), var8.getZ());
                    ItemStack var10;

                    if (var9 == null)
                    {
                        var10 = this.dropBehavior.dispense(var3, var6);

                        if (var10 != null && var10.stackSize == 0)
                        {
                            var10 = null;
                        }
                    }
                    else
                    {
                        var10 = TileEntityHopper.func_174918_a(var9, var6.copy().splitStack(1), var7.getOpposite());

                        if (var10 == null)
                        {
                            var10 = var6.copy();

                            if (--var10.stackSize == 0)
                            {
                                var10 = null;
                            }
                        }
                        else
                        {
                            var10 = var6.copy();
                        }
                    }

                    var4.setInventorySlotContents(var5, var10);
                }
            }
        }
    }
}
