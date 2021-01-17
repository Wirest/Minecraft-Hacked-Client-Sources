// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import com.google.common.base.Predicates;
import net.minecraft.util.EntitySelectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLivingBase;

public class EntityAINearestAttackableTarget<T extends EntityLivingBase> extends EntityAITarget
{
    protected final Class<T> targetClass;
    private final int targetChance;
    protected final Sorter theNearestAttackableTargetSorter;
    protected Predicate<? super T> targetEntitySelector;
    protected EntityLivingBase targetEntity;
    
    public EntityAINearestAttackableTarget(final EntityCreature creature, final Class<T> classTarget, final boolean checkSight) {
        this(creature, classTarget, checkSight, false);
    }
    
    public EntityAINearestAttackableTarget(final EntityCreature creature, final Class<T> classTarget, final boolean checkSight, final boolean onlyNearby) {
        this(creature, classTarget, 10, checkSight, onlyNearby, null);
    }
    
    public EntityAINearestAttackableTarget(final EntityCreature creature, final Class<T> classTarget, final int chance, final boolean checkSight, final boolean onlyNearby, final Predicate<? super T> targetSelector) {
        super(creature, checkSight, onlyNearby);
        this.targetClass = classTarget;
        this.targetChance = chance;
        this.theNearestAttackableTargetSorter = new Sorter(creature);
        this.setMutexBits(1);
        this.targetEntitySelector = new Predicate<T>() {
            @Override
            public boolean apply(final T p_apply_1_) {
                if (targetSelector != null && !targetSelector.apply(p_apply_1_)) {
                    return false;
                }
                if (p_apply_1_ instanceof EntityPlayer) {
                    double d0 = EntityAINearestAttackableTarget.this.getTargetDistance();
                    if (p_apply_1_.isSneaking()) {
                        d0 *= 0.800000011920929;
                    }
                    if (p_apply_1_.isInvisible()) {
                        float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
                        if (f < 0.1f) {
                            f = 0.1f;
                        }
                        d0 *= 0.7f * f;
                    }
                    if (p_apply_1_.getDistanceToEntity(EntityAINearestAttackableTarget.this.taskOwner) > d0) {
                        return false;
                    }
                }
                return EntityAINearestAttackableTarget.this.isSuitableTarget(p_apply_1_, false);
            }
        };
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0) {
            return false;
        }
        final double d0 = this.getTargetDistance();
        final List<T> list = this.taskOwner.worldObj.getEntitiesWithinAABB((Class<? extends T>)this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0, d0), Predicates.and(this.targetEntitySelector, (Predicate<? super T>)EntitySelectors.NOT_SPECTATING));
        Collections.sort(list, this.theNearestAttackableTargetSorter);
        if (list.isEmpty()) {
            return false;
        }
        this.targetEntity = list.get(0);
        return true;
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.targetEntity);
        super.startExecuting();
    }
    
    public static class Sorter implements Comparator<Entity>
    {
        private final Entity theEntity;
        
        public Sorter(final Entity theEntityIn) {
            this.theEntity = theEntityIn;
        }
        
        @Override
        public int compare(final Entity p_compare_1_, final Entity p_compare_2_) {
            final double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
            final double d2 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
            return (d0 < d2) ? -1 : ((d0 > d2) ? 1 : 0);
        }
    }
}
