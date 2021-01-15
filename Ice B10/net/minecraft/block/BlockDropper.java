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
    private final IBehaviorDispenseItem field_149947_P = new BehaviorDefaultDispenseItem();
    private static final String __OBFID = "CL_00000233";

    protected IBehaviorDispenseItem func_149940_a(ItemStack p_149940_1_)
    {
        return this.field_149947_P;
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    public TileEntity createNewTileEntity(World worldIn, int meta)
    {
        return new TileEntityDropper();
    }

    protected void func_176439_d(World worldIn, BlockPos p_176439_2_)
    {
        BlockSourceImpl var3 = new BlockSourceImpl(worldIn, p_176439_2_);
        TileEntityDispenser var4 = (TileEntityDispenser)var3.getBlockTileEntity();

        if (var4 != null)
        {
            int var5 = var4.func_146017_i();

            if (var5 < 0)
            {
                worldIn.playAuxSFX(1001, p_176439_2_, 0);
            }
            else
            {
                ItemStack var6 = var4.getStackInSlot(var5);

                if (var6 != null)
                {
                    EnumFacing var7 = (EnumFacing)worldIn.getBlockState(p_176439_2_).getValue(FACING);
                    BlockPos var8 = p_176439_2_.offset(var7);
                    IInventory var9 = TileEntityHopper.func_145893_b(worldIn, (double)var8.getX(), (double)var8.getY(), (double)var8.getZ());
                    ItemStack var10;

                    if (var9 == null)
                    {
                        var10 = this.field_149947_P.dispense(var3, var6);

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
