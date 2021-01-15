package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PathNavigateClimber extends PathNavigateGround
{
    /** Current path navigation target */
    private BlockPos targetPosition;

    public PathNavigateClimber(EntityLiving entityLivingIn, World worldIn)
    {
        super(entityLivingIn, worldIn);
    }

    /**
     * Returns path to given BlockPos
     */
    @Override
	public PathEntity getPathToPos(BlockPos pos)
    {
        this.targetPosition = pos;
        return super.getPathToPos(pos);
    }

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    @Override
	public PathEntity getPathToEntityLiving(Entity entityIn)
    {
        this.targetPosition = new BlockPos(entityIn);
        return super.getPathToEntityLiving(entityIn);
    }

    /**
     * Try to find and set a path to EntityLiving. Returns true if successful. Args : entity, speed
     */
    @Override
	public boolean tryMoveToEntityLiving(Entity entityIn, double speedIn)
    {
        PathEntity var4 = this.getPathToEntityLiving(entityIn);

        if (var4 != null)
        {
            return this.setPath(var4, speedIn);
        }
        else
        {
            this.targetPosition = new BlockPos(entityIn);
            this.speed = speedIn;
            return true;
        }
    }

    @Override
	public void onUpdateNavigation()
    {
        if (!this.noPath())
        {
            super.onUpdateNavigation();
        }
        else
        {
            if (this.targetPosition != null)
            {
                double var1 = this.theEntity.width * this.theEntity.width;

                if (this.theEntity.getDistanceSqToCenter(this.targetPosition) >= var1 && (this.theEntity.posY <= this.targetPosition.getY() || this.theEntity.getDistanceSqToCenter(new BlockPos(this.targetPosition.getX(), MathHelper.floor_double(this.theEntity.posY), this.targetPosition.getZ())) >= var1))
                {
                    this.theEntity.getMoveHelper().setMoveTo(this.targetPosition.getX(), this.targetPosition.getY(), this.targetPosition.getZ(), this.speed);
                }
                else
                {
                    this.targetPosition = null;
                }
            }
        }
    }
}
