package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIOcelotSit extends EntityAIMoveToBlock {
    private final EntityOcelot field_151493_a;
    private static final String __OBFID = "CL_00001601";

    public EntityAIOcelotSit(EntityOcelot p_i45315_1_, double p_i45315_2_) {
        super(p_i45315_1_, p_i45315_2_, 8);
        this.field_151493_a = p_i45315_1_;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.field_151493_a.isTamed() && !this.field_151493_a.isSitting() && super.shouldExecute();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return super.continueExecuting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        super.startExecuting();
        this.field_151493_a.getAISit().setSitting(false);
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        super.resetTask();
        this.field_151493_a.setSitting(false);
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        super.updateTask();
        this.field_151493_a.getAISit().setSitting(false);

        if (!this.func_179487_f()) {
            this.field_151493_a.setSitting(false);
        } else if (!this.field_151493_a.isSitting()) {
            this.field_151493_a.setSitting(true);
        }
    }

    protected boolean func_179488_a(World worldIn, BlockPos p_179488_2_) {
        if (!worldIn.isAirBlock(p_179488_2_.offsetUp())) {
            return false;
        } else {
            IBlockState var3 = worldIn.getBlockState(p_179488_2_);
            Block var4 = var3.getBlock();

            if (var4 == Blocks.chest) {
                TileEntity var5 = worldIn.getTileEntity(p_179488_2_);

                if (var5 instanceof TileEntityChest && ((TileEntityChest) var5).numPlayersUsing < 1) {
                    return true;
                }
            } else {
                if (var4 == Blocks.lit_furnace) {
                    return true;
                }

                if (var4 == Blocks.bed && var3.getValue(BlockBed.PART_PROP) != BlockBed.EnumPartType.HEAD) {
                    return true;
                }
            }

            return false;
        }
    }
}
