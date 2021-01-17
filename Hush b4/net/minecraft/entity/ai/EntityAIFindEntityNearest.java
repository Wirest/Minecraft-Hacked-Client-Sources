// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.List;
import java.util.Comparator;
import java.util.Collections;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import org.apache.logging.log4j.LogManager;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.base.Predicate;
import net.minecraft.entity.EntityLiving;
import org.apache.logging.log4j.Logger;

public class EntityAIFindEntityNearest extends EntityAIBase
{
    private static final Logger field_179444_a;
    private EntityLiving field_179442_b;
    private final Predicate<EntityLivingBase> field_179443_c;
    private final EntityAINearestAttackableTarget.Sorter field_179440_d;
    private EntityLivingBase field_179441_e;
    private Class<? extends EntityLivingBase> field_179439_f;
    
    static {
        field_179444_a = LogManager.getLogger();
    }
    
    public EntityAIFindEntityNearest(final EntityLiving p_i45884_1_, final Class<? extends EntityLivingBase> p_i45884_2_) {
        this.field_179442_b = p_i45884_1_;
        this.field_179439_f = p_i45884_2_;
        if (p_i45884_1_ instanceof EntityCreature) {
            EntityAIFindEntityNearest.field_179444_a.warn("Use NearestAttackableTargetGoal.class for PathfinerMob mobs!");
        }
        this.field_179443_c = new Predicate<EntityLivingBase>() {
            @Override
            public boolean apply(final EntityLivingBase p_apply_1_) {
                double d0 = EntityAIFindEntityNearest.this.func_179438_f();
                if (p_apply_1_.isSneaking()) {
                    d0 *= 0.800000011920929;
                }
                return !p_apply_1_.isInvisible() && p_apply_1_.getDistanceToEntity(EntityAIFindEntityNearest.this.field_179442_b) <= d0 && EntityAITarget.isSuitableTarget(EntityAIFindEntityNearest.this.field_179442_b, p_apply_1_, false, true);
            }
        };
        this.field_179440_d = new EntityAINearestAttackableTarget.Sorter(p_i45884_1_);
    }
    
    @Override
    public boolean shouldExecute() {
        final double d0 = this.func_179438_f();
        final List<EntityLivingBase> list = this.field_179442_b.worldObj.getEntitiesWithinAABB(this.field_179439_f, this.field_179442_b.getEntityBoundingBox().expand(d0, 4.0, d0), (Predicate<? super EntityLivingBase>)this.field_179443_c);
        Collections.sort(list, this.field_179440_d);
        if (list.isEmpty()) {
            return false;
        }
        this.field_179441_e = list.get(0);
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        final EntityLivingBase entitylivingbase = this.field_179442_b.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        final double d0 = this.func_179438_f();
        return this.field_179442_b.getDistanceSqToEntity(entitylivingbase) <= d0 * d0 && (!(entitylivingbase instanceof EntityPlayerMP) || !((EntityPlayerMP)entitylivingbase).theItemInWorldManager.isCreative());
    }
    
    @Override
    public void startExecuting() {
        this.field_179442_b.setAttackTarget(this.field_179441_e);
        super.startExecuting();
    }
    
    @Override
    public void resetTask() {
        this.field_179442_b.setAttackTarget(null);
        super.startExecuting();
    }
    
    protected double func_179438_f() {
        final IAttributeInstance iattributeinstance = this.field_179442_b.getEntityAttribute(SharedMonsterAttributes.followRange);
        return (iattributeinstance == null) ? 16.0 : iattributeinstance.getAttributeValue();
    }
}
