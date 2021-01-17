// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.List;
import net.minecraft.util.Vec3;
import net.minecraft.util.EntitySelectors;
import com.google.common.base.Predicates;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.EntityCreature;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;

public class EntityAIAvoidEntity<T extends Entity> extends EntityAIBase
{
    private final Predicate<Entity> canBeSeenSelector;
    protected EntityCreature theEntity;
    private double farSpeed;
    private double nearSpeed;
    protected T closestLivingEntity;
    private float avoidDistance;
    private PathEntity entityPathEntity;
    private PathNavigate entityPathNavigate;
    private Class<T> field_181064_i;
    private Predicate<? super T> avoidTargetSelector;
    
    public EntityAIAvoidEntity(final EntityCreature p_i46404_1_, final Class<T> p_i46404_2_, final float p_i46404_3_, final double p_i46404_4_, final double p_i46404_6_) {
        this(p_i46404_1_, p_i46404_2_, Predicates.alwaysTrue(), p_i46404_3_, p_i46404_4_, p_i46404_6_);
    }
    
    public EntityAIAvoidEntity(final EntityCreature p_i46405_1_, final Class<T> p_i46405_2_, final Predicate<? super T> p_i46405_3_, final float p_i46405_4_, final double p_i46405_5_, final double p_i46405_7_) {
        this.canBeSeenSelector = new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive() && EntityAIAvoidEntity.this.theEntity.getEntitySenses().canSee(p_apply_1_);
            }
        };
        this.theEntity = p_i46405_1_;
        this.field_181064_i = p_i46405_2_;
        this.avoidTargetSelector = p_i46405_3_;
        this.avoidDistance = p_i46405_4_;
        this.farSpeed = p_i46405_5_;
        this.nearSpeed = p_i46405_7_;
        this.entityPathNavigate = p_i46405_1_.getNavigator();
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final List<T> list = this.theEntity.worldObj.getEntitiesWithinAABB((Class<? extends T>)this.field_181064_i, this.theEntity.getEntityBoundingBox().expand(this.avoidDistance, 3.0, this.avoidDistance), Predicates.and(EntitySelectors.NOT_SPECTATING, this.canBeSeenSelector, this.avoidTargetSelector));
        if (list.isEmpty()) {
            return false;
        }
        this.closestLivingEntity = list.get(0);
        final Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
        if (vec3 == null) {
            return false;
        }
        if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity)) {
            return false;
        }
        this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
        return this.entityPathEntity != null && this.entityPathEntity.isDestinationSame(vec3);
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.entityPathNavigate.noPath();
    }
    
    @Override
    public void startExecuting() {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }
    
    @Override
    public void resetTask() {
        this.closestLivingEntity = null;
    }
    
    @Override
    public void updateTask() {
        if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0) {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        }
        else {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}
