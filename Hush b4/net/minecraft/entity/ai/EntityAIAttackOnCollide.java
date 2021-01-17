// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.world.World;

public class EntityAIAttackOnCollide extends EntityAIBase
{
    World worldObj;
    protected EntityCreature attacker;
    int attackTick;
    double speedTowardsTarget;
    boolean longMemory;
    PathEntity entityPathEntity;
    Class<? extends Entity> classTarget;
    private int delayCounter;
    private double targetX;
    private double targetY;
    private double targetZ;
    
    public EntityAIAttackOnCollide(final EntityCreature creature, final Class<? extends Entity> targetClass, final double speedIn, final boolean useLongMemory) {
        this(creature, speedIn, useLongMemory);
        this.classTarget = targetClass;
    }
    
    public EntityAIAttackOnCollide(final EntityCreature creature, final double speedIn, final boolean useLongMemory) {
        this.attacker = creature;
        this.worldObj = creature.worldObj;
        this.speedTowardsTarget = speedIn;
        this.longMemory = useLongMemory;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        if (!entitylivingbase.isEntityAlive()) {
            return false;
        }
        if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass())) {
            return false;
        }
        this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entitylivingbase);
        return this.entityPathEntity != null;
    }
    
    @Override
    public boolean continueExecuting() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        return entitylivingbase != null && entitylivingbase.isEntityAlive() && (this.longMemory ? this.attacker.isWithinHomeDistanceFromPosition(new BlockPos(entitylivingbase)) : (!this.attacker.getNavigator().noPath()));
    }
    
    @Override
    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.delayCounter = 0;
    }
    
    @Override
    public void resetTask() {
        this.attacker.getNavigator().clearPathEntity();
    }
    
    @Override
    public void updateTask() {
        final EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0f, 30.0f);
        final double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
        final double d2 = this.func_179512_a(entitylivingbase);
        --this.delayCounter;
        if ((this.longMemory || this.attacker.getEntitySenses().canSee(entitylivingbase)) && this.delayCounter <= 0 && ((this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0) || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.attacker.getRNG().nextFloat() < 0.05f)) {
            this.targetX = entitylivingbase.posX;
            this.targetY = entitylivingbase.getEntityBoundingBox().minY;
            this.targetZ = entitylivingbase.posZ;
            this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
            if (d0 > 1024.0) {
                this.delayCounter += 10;
            }
            else if (d0 > 256.0) {
                this.delayCounter += 5;
            }
            if (!this.attacker.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
                this.delayCounter += 15;
            }
        }
        this.attackTick = Math.max(this.attackTick - 1, 0);
        if (d0 <= d2 && this.attackTick <= 0) {
            this.attackTick = 20;
            if (this.attacker.getHeldItem() != null) {
                this.attacker.swingItem();
            }
            this.attacker.attackEntityAsMob(entitylivingbase);
        }
    }
    
    protected double func_179512_a(final EntityLivingBase attackTarget) {
        return this.attacker.width * 2.0f * this.attacker.width * 2.0f + attackTarget.width;
    }
}
