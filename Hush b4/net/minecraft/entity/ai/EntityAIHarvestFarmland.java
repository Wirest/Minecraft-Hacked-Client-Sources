// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.init.Blocks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
{
    private final EntityVillager theVillager;
    private boolean hasFarmItem;
    private boolean field_179503_e;
    private int field_179501_f;
    
    public EntityAIHarvestFarmland(final EntityVillager theVillagerIn, final double speedIn) {
        super(theVillagerIn, speedIn, 16);
        this.theVillager = theVillagerIn;
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.runDelay <= 0) {
            if (!this.theVillager.worldObj.getGameRules().getBoolean("mobGriefing")) {
                return false;
            }
            this.field_179501_f = -1;
            this.hasFarmItem = this.theVillager.isFarmItemInInventory();
            this.field_179503_e = this.theVillager.func_175557_cr();
        }
        return super.shouldExecute();
    }
    
    @Override
    public boolean continueExecuting() {
        return this.field_179501_f >= 0 && super.continueExecuting();
    }
    
    @Override
    public void startExecuting() {
        super.startExecuting();
    }
    
    @Override
    public void resetTask() {
        super.resetTask();
    }
    
    @Override
    public void updateTask() {
        super.updateTask();
        this.theVillager.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5, 10.0f, (float)this.theVillager.getVerticalFaceSpeed());
        if (this.getIsAboveDestination()) {
            final World world = this.theVillager.worldObj;
            final BlockPos blockpos = this.destinationBlock.up();
            final IBlockState iblockstate = world.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            if (this.field_179501_f == 0 && block instanceof BlockCrops && iblockstate.getValue((IProperty<Integer>)BlockCrops.AGE) == 7) {
                world.destroyBlock(blockpos, true);
            }
            else if (this.field_179501_f == 1 && block == Blocks.air) {
                final InventoryBasic inventorybasic = this.theVillager.getVillagerInventory();
                int i = 0;
                while (i < inventorybasic.getSizeInventory()) {
                    final ItemStack itemstack = inventorybasic.getStackInSlot(i);
                    boolean flag = false;
                    if (itemstack != null) {
                        if (itemstack.getItem() == Items.wheat_seeds) {
                            world.setBlockState(blockpos, Blocks.wheat.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.potato) {
                            world.setBlockState(blockpos, Blocks.potatoes.getDefaultState(), 3);
                            flag = true;
                        }
                        else if (itemstack.getItem() == Items.carrot) {
                            world.setBlockState(blockpos, Blocks.carrots.getDefaultState(), 3);
                            flag = true;
                        }
                    }
                    if (flag) {
                        final ItemStack itemStack = itemstack;
                        --itemStack.stackSize;
                        if (itemstack.stackSize <= 0) {
                            inventorybasic.setInventorySlotContents(i, null);
                            break;
                        }
                        break;
                    }
                    else {
                        ++i;
                    }
                }
            }
            this.field_179501_f = -1;
            this.runDelay = 10;
        }
    }
    
    @Override
    protected boolean shouldMoveTo(final World worldIn, BlockPos pos) {
        Block block = worldIn.getBlockState(pos).getBlock();
        if (block == Blocks.farmland) {
            pos = pos.up();
            final IBlockState iblockstate = worldIn.getBlockState(pos);
            block = iblockstate.getBlock();
            if (block instanceof BlockCrops && iblockstate.getValue((IProperty<Integer>)BlockCrops.AGE) == 7 && this.field_179503_e && (this.field_179501_f == 0 || this.field_179501_f < 0)) {
                this.field_179501_f = 0;
                return true;
            }
            if (block == Blocks.air && this.hasFarmItem && (this.field_179501_f == 1 || this.field_179501_f < 0)) {
                this.field_179501_f = 1;
                return true;
            }
        }
        return false;
    }
}
