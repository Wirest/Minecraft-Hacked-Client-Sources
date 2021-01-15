package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;

public abstract class EntityAIDoorInteract extends EntityAIBase
{
    protected EntityLiving theEntity;
    protected BlockPos field_179507_b;

    /** The wooden door block */
    protected BlockDoor doorBlock;

    /**
     * If is true then the Entity has stopped Door Interaction and compoleted the task.
     */
    boolean hasStoppedDoorInteraction;
    float entityPositionX;
    float entityPositionZ;
    private static final String __OBFID = "CL_00001581";

    public EntityAIDoorInteract(EntityLiving p_i1621_1_)
    {
        this.field_179507_b = BlockPos.ORIGIN;
        this.theEntity = p_i1621_1_;

        if (!(p_i1621_1_.getNavigator() instanceof PathNavigateGround))
        {
            throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!this.theEntity.isCollidedHorizontally)
        {
            return false;
        }
        else
        {
            PathNavigateGround var1 = (PathNavigateGround)this.theEntity.getNavigator();
            PathEntity var2 = var1.getPath();

            if (var2 != null && !var2.isFinished() && var1.func_179686_g())
            {
                for (int var3 = 0; var3 < Math.min(var2.getCurrentPathIndex() + 2, var2.getCurrentPathLength()); ++var3)
                {
                    PathPoint var4 = var2.getPathPointFromIndex(var3);
                    this.field_179507_b = new BlockPos(var4.xCoord, var4.yCoord + 1, var4.zCoord);

                    if (this.theEntity.getDistanceSq((double)this.field_179507_b.getX(), this.theEntity.posY, (double)this.field_179507_b.getZ()) <= 2.25D)
                    {
                        this.doorBlock = this.func_179506_a(this.field_179507_b);

                        if (this.doorBlock != null)
                        {
                            return true;
                        }
                    }
                }

                this.field_179507_b = (new BlockPos(this.theEntity)).offsetUp();
                this.doorBlock = this.func_179506_a(this.field_179507_b);
                return this.doorBlock != null;
            }
            else
            {
                return false;
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.hasStoppedDoorInteraction;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.hasStoppedDoorInteraction = false;
        this.entityPositionX = (float)((double)((float)this.field_179507_b.getX() + 0.5F) - this.theEntity.posX);
        this.entityPositionZ = (float)((double)((float)this.field_179507_b.getZ() + 0.5F) - this.theEntity.posZ);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        float var1 = (float)((double)((float)this.field_179507_b.getX() + 0.5F) - this.theEntity.posX);
        float var2 = (float)((double)((float)this.field_179507_b.getZ() + 0.5F) - this.theEntity.posZ);
        float var3 = this.entityPositionX * var1 + this.entityPositionZ * var2;

        if (var3 < 0.0F)
        {
            this.hasStoppedDoorInteraction = true;
        }
    }

    private BlockDoor func_179506_a(BlockPos p_179506_1_)
    {
        Block var2 = this.theEntity.worldObj.getBlockState(p_179506_1_).getBlock();
        return var2 instanceof BlockDoor && var2.getMaterial() == Material.wood ? (BlockDoor)var2 : null;
    }
}
