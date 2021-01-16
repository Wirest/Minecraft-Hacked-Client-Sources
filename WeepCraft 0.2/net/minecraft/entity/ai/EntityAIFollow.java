package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNodeType;

public class EntityAIFollow extends EntityAIBase
{
    private final EntityLiving field_192372_a;
    private final Predicate<EntityLiving> field_192373_b;
    private EntityLiving field_192374_c;
    private final double field_192375_d;
    private final PathNavigate field_192376_e;
    private int field_192377_f;
    private final float field_192378_g;
    private float field_192379_h;
    private final float field_192380_i;

    public EntityAIFollow(final EntityLiving p_i47417_1_, double p_i47417_2_, float p_i47417_4_, float p_i47417_5_)
    {
        this.field_192372_a = p_i47417_1_;
        this.field_192373_b = new Predicate<EntityLiving>()
        {
            public boolean apply(@Nullable EntityLiving p_apply_1_)
            {
                return p_apply_1_ != null && p_i47417_1_.getClass() != p_apply_1_.getClass();
            }
        };
        this.field_192375_d = p_i47417_2_;
        this.field_192376_e = p_i47417_1_.getNavigator();
        this.field_192378_g = p_i47417_4_;
        this.field_192380_i = p_i47417_5_;
        this.setMutexBits(3);

        if (!(p_i47417_1_.getNavigator() instanceof PathNavigateGround) && !(p_i47417_1_.getNavigator() instanceof PathNavigateFlying))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowMobGoal");
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List<EntityLiving> list = this.field_192372_a.world.<EntityLiving>getEntitiesWithinAABB(EntityLiving.class, this.field_192372_a.getEntityBoundingBox().expandXyz((double)this.field_192380_i), this.field_192373_b);

        if (!list.isEmpty())
        {
            for (EntityLiving entityliving : list)
            {
                if (!entityliving.isInvisible())
                {
                    this.field_192374_c = entityliving;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return this.field_192374_c != null && !this.field_192376_e.noPath() && this.field_192372_a.getDistanceSqToEntity(this.field_192374_c) > (double)(this.field_192378_g * this.field_192378_g);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.field_192377_f = 0;
        this.field_192379_h = this.field_192372_a.getPathPriority(PathNodeType.WATER);
        this.field_192372_a.setPathPriority(PathNodeType.WATER, 0.0F);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.field_192374_c = null;
        this.field_192376_e.clearPathEntity();
        this.field_192372_a.setPathPriority(PathNodeType.WATER, this.field_192379_h);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.field_192374_c != null && !this.field_192372_a.getLeashed())
        {
            this.field_192372_a.getLookHelper().setLookPositionWithEntity(this.field_192374_c, 10.0F, (float)this.field_192372_a.getVerticalFaceSpeed());

            if (--this.field_192377_f <= 0)
            {
                this.field_192377_f = 10;
                double d0 = this.field_192372_a.posX - this.field_192374_c.posX;
                double d1 = this.field_192372_a.posY - this.field_192374_c.posY;
                double d2 = this.field_192372_a.posZ - this.field_192374_c.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > (double)(this.field_192378_g * this.field_192378_g))
                {
                    this.field_192376_e.tryMoveToEntityLiving(this.field_192374_c, this.field_192375_d);
                }
                else
                {
                    this.field_192376_e.clearPathEntity();
                    EntityLookHelper entitylookhelper = this.field_192374_c.getLookHelper();

                    if (d3 <= (double)this.field_192378_g || entitylookhelper.getLookPosX() == this.field_192372_a.posX && entitylookhelper.getLookPosY() == this.field_192372_a.posY && entitylookhelper.getLookPosZ() == this.field_192372_a.posZ)
                    {
                        double d4 = this.field_192374_c.posX - this.field_192372_a.posX;
                        double d5 = this.field_192374_c.posZ - this.field_192372_a.posZ;
                        this.field_192376_e.tryMoveToXYZ(this.field_192372_a.posX - d4, this.field_192372_a.posY, this.field_192372_a.posZ - d5, this.field_192375_d);
                    }
                }
            }
        }
    }
}
