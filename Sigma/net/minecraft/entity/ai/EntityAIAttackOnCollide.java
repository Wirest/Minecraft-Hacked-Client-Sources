package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityAIAttackOnCollide extends EntityAIBase {
    World worldObj;
    protected EntityCreature attacker;

    /**
     * An amount of decrementing ticks that allows the entity to attack once the tick reaches 0.
     */
    int attackTick;

    /**
     * The speed with which the mob will approach the target
     */
    double speedTowardsTarget;

    /**
     * When true, the mob will continue chasing its target, even if it can't find a path to them right now.
     */
    boolean longMemory;

    /**
     * The PathEntity of our entity.
     */
    PathEntity entityPathEntity;
    Class classTarget;
    private int field_75445_i;
    private double field_151497_i;
    private double field_151495_j;
    private double field_151496_k;
    private static final String __OBFID = "CL_00001595";

    public EntityAIAttackOnCollide(EntityCreature p_i1635_1_, Class p_i1635_2_, double p_i1635_3_, boolean p_i1635_5_) {
        this(p_i1635_1_, p_i1635_3_, p_i1635_5_);
        this.classTarget = p_i1635_2_;
    }

    public EntityAIAttackOnCollide(EntityCreature p_i1636_1_, double p_i1636_2_, boolean p_i1636_4_) {
        this.attacker = p_i1636_1_;
        this.worldObj = p_i1636_1_.worldObj;
        this.speedTowardsTarget = p_i1636_2_;
        this.longMemory = p_i1636_4_;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        EntityLivingBase var1 = this.attacker.getAttackTarget();

        if (var1 == null) {
            return false;
        } else if (!var1.isEntityAlive()) {
            return false;
        } else if (this.classTarget != null && !this.classTarget.isAssignableFrom(var1.getClass())) {
            return false;
        } else {
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(var1);
            return this.entityPathEntity != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        EntityLivingBase var1 = this.attacker.getAttackTarget();
        return var1 == null ? false : (!var1.isEntityAlive() ? false : (!this.longMemory ? !this.attacker.getNavigator().noPath() : this.attacker.func_180485_d(new BlockPos(var1))));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
        this.field_75445_i = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.attacker.getNavigator().clearPathEntity();
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        EntityLivingBase var1 = this.attacker.getAttackTarget();
        this.attacker.getLookHelper().setLookPositionWithEntity(var1, 30.0F, 30.0F);
        double var2 = this.attacker.getDistanceSq(var1.posX, var1.getEntityBoundingBox().minY, var1.posZ);
        double var4 = this.func_179512_a(var1);
        --this.field_75445_i;

        if ((this.longMemory || this.attacker.getEntitySenses().canSee(var1)) && this.field_75445_i <= 0 && (this.field_151497_i == 0.0D && this.field_151495_j == 0.0D && this.field_151496_k == 0.0D || var1.getDistanceSq(this.field_151497_i, this.field_151495_j, this.field_151496_k) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
            this.field_151497_i = var1.posX;
            this.field_151495_j = var1.getEntityBoundingBox().minY;
            this.field_151496_k = var1.posZ;
            this.field_75445_i = 4 + this.attacker.getRNG().nextInt(7);

            if (var2 > 1024.0D) {
                this.field_75445_i += 10;
            } else if (var2 > 256.0D) {
                this.field_75445_i += 5;
            }

            if (!this.attacker.getNavigator().tryMoveToEntityLiving(var1, this.speedTowardsTarget)) {
                this.field_75445_i += 15;
            }
        }

        this.attackTick = Math.max(this.attackTick - 1, 0);

        if (var2 <= var4 && this.attackTick <= 0) {
            this.attackTick = 20;

            if (this.attacker.getHeldItem() != null) {
                this.attacker.swingItem();
            }

            this.attacker.attackEntityAsMob(var1);
        }
    }

    protected double func_179512_a(EntityLivingBase p_179512_1_) {
        return (double) (this.attacker.width * 2.0F * this.attacker.width * 2.0F + p_179512_1_.width);
    }
}
