package net.minecraft.entity.ai;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    private static final String __OBFID = "CL_00001620";

    public EntityAINearestAttackableTarget(EntityCreature p_i45878_1_, Class p_i45878_2_, boolean p_i45878_3_)
    {
        this(p_i45878_1_, p_i45878_2_, p_i45878_3_, false);
    }

    public EntityAINearestAttackableTarget(EntityCreature p_i45879_1_, Class p_i45879_2_, boolean p_i45879_3_, boolean p_i45879_4_)
    {
        this(p_i45879_1_, p_i45879_2_, 10, p_i45879_3_, p_i45879_4_, (Predicate)null);
    }

    public EntityAINearestAttackableTarget(EntityCreature p_i45880_1_, Class p_i45880_2_, int p_i45880_3_, boolean p_i45880_4_, boolean p_i45880_5_, final Predicate p_i45880_6_)
    {
        super(p_i45880_1_, p_i45880_4_, p_i45880_5_);
        this.targetClass = p_i45880_2_;
        this.targetChance = p_i45880_3_;
        this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(p_i45880_1_);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate()
        {
            private static final String __OBFID = "CL_00001621";
            public boolean func_179878_a(EntityLivingBase p_179878_1_)
            {
                if (p_i45880_6_ != null && !p_i45880_6_.apply(p_179878_1_))
                {
                    return false;
                }
                else
                {
                    if (p_179878_1_ instanceof EntityPlayer)
                    {
                        double var2 = EntityAINearestAttackableTarget.this.getTargetDistance();

                        if (p_179878_1_.isSneaking())
                        {
                            var2 *= 0.800000011920929D;
                        }

                        if (p_179878_1_.isInvisible())
                        {
                            float var4 = ((EntityPlayer)p_179878_1_).getArmorVisibility();

                            if (var4 < 0.1F)
                            {
                                var4 = 0.1F;
                            }

                            var2 *= (double)(0.7F * var4);
                        }

                        if ((double)p_179878_1_.getDistanceToEntity(EntityAINearestAttackableTarget.this.taskOwner) > var2)
                        {
                            return false;
                        }
                    }

                    return EntityAINearestAttackableTarget.this.isSuitableTarget(p_179878_1_, false);
                }
            }
            public boolean apply(Object p_apply_1_)
            {
                return this.func_179878_a((EntityLivingBase)p_apply_1_);
            }
        };
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
        {
            return false;
        }
        else
        {
            double var1 = this.getTargetDistance();
            List var3 = this.taskOwner.worldObj.func_175647_a(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(var1, 4.0D, var1), Predicates.and(this.targetEntitySelector, IEntitySelector.field_180132_d));
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
    public void startExecuting()
    {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }

    public static class Sorter implements Comparator
    {
        private final Entity theEntity;
        private static final String __OBFID = "CL_00001622";

        public Sorter(Entity p_i1662_1_)
        {
            this.theEntity = p_i1662_1_;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double var3 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
            double var5 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
            return var3 < var5 ? -1 : (var3 > var5 ? 1 : 0);
        }

        public int compare(Object p_compare_1_, Object p_compare_2_)
        {
            return this.compare((Entity)p_compare_1_, (Entity)p_compare_2_);
        }
    }
}
