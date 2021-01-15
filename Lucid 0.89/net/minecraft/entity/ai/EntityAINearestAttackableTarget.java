package net.minecraft.entity.ai;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAINearestAttackableTarget extends EntityAITarget
{
    protected final Class targetClass;
    private final int targetChance;

    /** Instance of EntityAINearestAttackableTargetSorter. */
    protected final EntityAINearestAttackableTarget.Sorter theNearestAttackableTargetSorter;

    /**
     * This filter is applied to the Entity search.  Only matching entities will be targetted.  (null -> no
     * restrictions)
     */
    protected Predicate targetEntitySelector;
    protected EntityLivingBase targetEntity;

    public EntityAINearestAttackableTarget(EntityCreature creature, Class classTarget, boolean checkSight)
    {
        this(creature, classTarget, checkSight, false);
    }

    public EntityAINearestAttackableTarget(EntityCreature creature, Class classTarget, boolean checkSight, boolean onlyNearby)
    {
        this(creature, classTarget, 10, checkSight, onlyNearby, (Predicate)null);
    }

    public EntityAINearestAttackableTarget(EntityCreature creature, Class classTarget, int chance, boolean checkSight, boolean onlyNearby, final Predicate targetSelector)
    {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate()
        {
            public boolean isApplicable(EntityLivingBase entitylivingbaseIn)
            {
                if (targetSelector != null && !targetSelector.apply(entitylivingbaseIn))
                {
                    return false;
                }
                else
                {
                    if (entitylivingbaseIn instanceof EntityPlayer)
                    {
                        double var2 = EntityAINearestAttackableTarget.this.getTargetDistance();

                        if (entitylivingbaseIn.isSneaking())
                        {
                            var2 *= 0.800000011920929D;
                        }

                        if (entitylivingbaseIn.isInvisible())
                        {
                            float var4 = ((EntityPlayer)entitylivingbaseIn).getArmorVisibility();

                            if (var4 < 0.1F)
                            {
                                var4 = 0.1F;
                            }

                            var2 *= 0.7F * var4;
                        }

                        if (entitylivingbaseIn.getDistanceToEntity(EntityAINearestAttackableTarget.this.taskOwner) > var2)
                        {
                            return false;
                        }
                    }

                    return EntityAINearestAttackableTarget.this.isSuitableTarget(entitylivingbaseIn, false);
                }
            }
            @Override
			public boolean apply(Object p_apply_1_)
            {
                return this.isApplicable((EntityLivingBase)p_apply_1_);
            }
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else
        {
            double var1 = this.getTargetDistance();
            List var3 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(var1, 4.0D, var1), Predicates.and(this.targetEntitySelector, IEntitySelector.NOT_SPECTATING));
            Collections.sort(var3, this.theNearestAttackableTargetSorter);

            if (var3.isEmpty())
            {
                return false;
            }
            else
            {
                this.targetEntity = (EntityLivingBase)var3.get(0);
                return true;
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator
    {
        private final Entity theEntity;

        public Sorter(Entity theEntityIn)
        {
            this.theEntity = theEntityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double var3 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
            double var5 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
            return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
        }

        @Override
		public int compare(Object p_compare_1_, Object p_compare_2_)
        {
            return this.compare((Entity)p_compare_1_, (Entity)p_compare_2_);
        }
    }
}
