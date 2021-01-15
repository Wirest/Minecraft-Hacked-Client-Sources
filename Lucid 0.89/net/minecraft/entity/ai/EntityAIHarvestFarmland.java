package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIHarvestFarmland extends EntityAIMoveToBlock
{
    /** Villager that is harvesting */
    private final EntityVillager theVillager;
    private boolean field_179502_d;
    private boolean field_179503_e;
    private int field_179501_f;

    public EntityAIHarvestFarmland(EntityVillager theVillagerIn, double speedIn)
    {
        super(theVillagerIn, speedIn, 16);
        this.theVillager = theVillagerIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        if (this.runDelay <= 0)
        {
            if (!this.theVillager.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"))
            {
                return false;
            }

            this.field_179501_f = -1;
            this.field_179502_d = this.theVillager.func_175556_cs();
            this.field_179503_e = this.theVillager.func_175557_cr();
        }

        return super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean continueExecuting()
    {
        return this.field_179501_f >= 0 && super.continueExecuting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        super.startExecuting();
    }

    /**
     * Resets the task
     */
    @Override
	public void resetTask()
    {
        super.resetTask();
    }

    /**
     * Updates the task
     */
    @Override
	public void updateTask()
    {
        super.updateTask();
        this.theVillager.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, this.destinationBlock.getY() + 1, this.destinationBlock.getZ() + 0.5D, 10.0F, this.theVillager.getVerticalFaceSpeed());

        if (this.getIsAboveDestination())
        {
            World var1 = this.theVillager.worldObj;
            BlockPos var2 = this.destinationBlock.up();
            IBlockState var3 = var1.getBlockState(var2);
            Block var4 = var3.getBlock();

            if (this.field_179501_f == 0 && var4 instanceof BlockCrops && ((Integer)var3.getValue(BlockCrops.AGE)).intValue() == 7)
            {
                var1.destroyBlock(var2, true);
            }
            else if (this.field_179501_f == 1 && var4 == Blocks.air)
            {
                InventoryBasic var5 = this.theVillager.getVillagerInventory();

                for (int var6 = 0; var6 < var5.getSizeInventory(); ++var6)
                {
                    ItemStack var7 = var5.getStackInSlot(var6);
                    boolean var8 = false;

                    if (var7 != null)
                    {
                        if (var7.getItem() == Items.wheat_seeds)
                        {
                            var1.setBlockState(var2, Blocks.wheat.getDefaultState(), 3);
                            var8 = true;
                        }
                        else if (var7.getItem() == Items.potato)
                        {
                            var1.setBlockState(var2, Blocks.potatoes.getDefaultState(), 3);
                            var8 = true;
                        }
                        else if (var7.getItem() == Items.carrot)
                        {
                            var1.setBlockState(var2, Blocks.carrots.getDefaultState(), 3);
                            var8 = true;
                        }
                    }

                    if (var8)
                    {
                        --var7.stackSize;

                        if (var7.stackSize <= 0)
                        {
                            var5.setInventorySlotContents(var6, (ItemStack)null);
                        }

                        break;
                    }
                }
            }

            this.field_179501_f = -1;
            this.runDelay = 10;
        }
    }

    /**
     * Return true to set given position as destination
     */
    @Override
	protected boolean shouldMoveTo(World worldIn, BlockPos pos)
    {
        Block var3 = worldIn.getBlockState(pos).getBlock();

        if (var3 == Blocks.farmland)
        {
            pos = pos.up();
            IBlockState var4 = worldIn.getBlockState(pos);
            var3 = var4.getBlock();

            if (var3 instanceof BlockCrops && ((Integer)var4.getValue(BlockCrops.AGE)).intValue() == 7 && this.field_179503_e && (this.field_179501_f == 0 || this.field_179501_f < 0))
            {
                this.field_179501_f = 0;
                return true;
            }

            if (var3 == Blocks.air && this.field_179502_d && (this.field_179501_f == 1 || this.field_179501_f < 0))
            {
                this.field_179501_f = 1;
                return true;
            }
        }

        return false;
    }
}
