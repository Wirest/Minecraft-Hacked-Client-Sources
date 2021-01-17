// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.util.AxisAlignedBB;
import java.util.Random;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraft.entity.EntityFlying;

public class EntityGhast extends EntityFlying implements IMob
{
    private int explosionStrength;
    
    public EntityGhast(final World worldIn) {
        super(worldIn);
        this.explosionStrength = 1;
        this.setSize(4.0f, 4.0f);
        this.isImmuneToFire = true;
        this.experienceValue = 5;
        this.moveHelper = new GhastMoveHelper(this);
        this.tasks.addTask(5, new AIRandomFly(this));
        this.tasks.addTask(7, new AILookAround(this));
        this.tasks.addTask(7, new AIFireballAttack(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }
    
    public boolean isAttacking() {
        return this.dataWatcher.getWatchableObjectByte(16) != 0;
    }
    
    public void setAttacking(final boolean p_175454_1_) {
        this.dataWatcher.updateObject(16, (byte)(p_175454_1_ ? 1 : 0));
    }
    
    public int getFireballStrength() {
        return this.explosionStrength;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if ("fireball".equals(source.getDamageType()) && source.getEntity() instanceof EntityPlayer) {
            super.attackEntityFrom(source, 1000.0f);
            ((EntityPlayer)source.getEntity()).triggerAchievement(AchievementList.ghast);
            return true;
        }
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Byte)0);
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0);
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.ghast.moan";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.ghast.scream";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }
    
    @Override
    protected Item getDropItem() {
        return Items.gunpowder;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_), j = 0; j < i; ++j) {
            this.dropItem(Items.ghast_tear, 1);
        }
        for (int i = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_), k = 0; k < i; ++k) {
            this.dropItem(Items.gunpowder, 1);
        }
    }
    
    @Override
    protected float getSoundVolume() {
        return 10.0f;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
    
    @Override
    public int getMaxSpawnedInChunk() {
        return 1;
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("ExplosionPower", this.explosionStrength);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ExplosionPower", 99)) {
            this.explosionStrength = tagCompund.getInteger("ExplosionPower");
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 2.6f;
    }
    
    static class AIFireballAttack extends EntityAIBase
    {
        private EntityGhast parentEntity;
        public int attackTimer;
        
        public AIFireballAttack(final EntityGhast p_i45837_1_) {
            this.parentEntity = p_i45837_1_;
        }
        
        @Override
        public boolean shouldExecute() {
            return this.parentEntity.getAttackTarget() != null;
        }
        
        @Override
        public void startExecuting() {
            this.attackTimer = 0;
        }
        
        @Override
        public void resetTask() {
            this.parentEntity.setAttacking(false);
        }
        
        @Override
        public void updateTask() {
            final EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
            final double d0 = 64.0;
            if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0 && this.parentEntity.canEntityBeSeen(entitylivingbase)) {
                final World world = this.parentEntity.worldObj;
                ++this.attackTimer;
                if (this.attackTimer == 10) {
                    world.playAuxSFXAtEntity(null, 1007, new BlockPos(this.parentEntity), 0);
                }
                if (this.attackTimer == 20) {
                    final double d2 = 4.0;
                    final Vec3 vec3 = this.parentEntity.getLook(1.0f);
                    final double d3 = entitylivingbase.posX - (this.parentEntity.posX + vec3.xCoord * d2);
                    final double d4 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0f - (0.5 + this.parentEntity.posY + this.parentEntity.height / 2.0f);
                    final double d5 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3.zCoord * d2);
                    world.playAuxSFXAtEntity(null, 1008, new BlockPos(this.parentEntity), 0);
                    final EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, this.parentEntity, d3, d4, d5);
                    entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
                    entitylargefireball.posX = this.parentEntity.posX + vec3.xCoord * d2;
                    entitylargefireball.posY = this.parentEntity.posY + this.parentEntity.height / 2.0f + 0.5;
                    entitylargefireball.posZ = this.parentEntity.posZ + vec3.zCoord * d2;
                    world.spawnEntityInWorld(entitylargefireball);
                    this.attackTimer = -40;
                }
            }
            else if (this.attackTimer > 0) {
                --this.attackTimer;
            }
            this.parentEntity.setAttacking(this.attackTimer > 10);
        }
    }
    
    static class AILookAround extends EntityAIBase
    {
        private EntityGhast parentEntity;
        
        public AILookAround(final EntityGhast p_i45839_1_) {
            this.parentEntity = p_i45839_1_;
            this.setMutexBits(2);
        }
        
        @Override
        public boolean shouldExecute() {
            return true;
        }
        
        @Override
        public void updateTask() {
            if (this.parentEntity.getAttackTarget() == null) {
                final EntityGhast parentEntity = this.parentEntity;
                final EntityGhast parentEntity2 = this.parentEntity;
                final float n = -(float)MathHelper.func_181159_b(this.parentEntity.motionX, this.parentEntity.motionZ) * 180.0f / 3.1415927f;
                parentEntity2.rotationYaw = n;
                parentEntity.renderYawOffset = n;
            }
            else {
                final EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
                final double d0 = 64.0;
                if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0) {
                    final double d2 = entitylivingbase.posX - this.parentEntity.posX;
                    final double d3 = entitylivingbase.posZ - this.parentEntity.posZ;
                    final EntityGhast parentEntity3 = this.parentEntity;
                    final EntityGhast parentEntity4 = this.parentEntity;
                    final float n2 = -(float)MathHelper.func_181159_b(d2, d3) * 180.0f / 3.1415927f;
                    parentEntity4.rotationYaw = n2;
                    parentEntity3.renderYawOffset = n2;
                }
            }
        }
    }
    
    static class AIRandomFly extends EntityAIBase
    {
        private EntityGhast parentEntity;
        
        public AIRandomFly(final EntityGhast p_i45836_1_) {
            this.parentEntity = p_i45836_1_;
            this.setMutexBits(1);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
            if (!entitymovehelper.isUpdating()) {
                return true;
            }
            final double d0 = entitymovehelper.getX() - this.parentEntity.posX;
            final double d2 = entitymovehelper.getY() - this.parentEntity.posY;
            final double d3 = entitymovehelper.getZ() - this.parentEntity.posZ;
            final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
            return d4 < 1.0 || d4 > 3600.0;
        }
        
        @Override
        public boolean continueExecuting() {
            return false;
        }
        
        @Override
        public void startExecuting() {
            final Random random = this.parentEntity.getRNG();
            final double d0 = this.parentEntity.posX + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double d2 = this.parentEntity.posY + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            final double d3 = this.parentEntity.posZ + (random.nextFloat() * 2.0f - 1.0f) * 16.0f;
            this.parentEntity.getMoveHelper().setMoveTo(d0, d2, d3, 1.0);
        }
    }
    
    static class GhastMoveHelper extends EntityMoveHelper
    {
        private EntityGhast parentEntity;
        private int courseChangeCooldown;
        
        public GhastMoveHelper(final EntityGhast p_i45838_1_) {
            super(p_i45838_1_);
            this.parentEntity = p_i45838_1_;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            if (this.update) {
                final double d0 = this.posX - this.parentEntity.posX;
                final double d2 = this.posY - this.parentEntity.posY;
                final double d3 = this.posZ - this.parentEntity.posZ;
                double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (this.courseChangeCooldown-- <= 0) {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    d4 = MathHelper.sqrt_double(d4);
                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d4)) {
                        final EntityGhast parentEntity = this.parentEntity;
                        parentEntity.motionX += d0 / d4 * 0.1;
                        final EntityGhast parentEntity2 = this.parentEntity;
                        parentEntity2.motionY += d2 / d4 * 0.1;
                        final EntityGhast parentEntity3 = this.parentEntity;
                        parentEntity3.motionZ += d3 / d4 * 0.1;
                    }
                    else {
                        this.update = false;
                    }
                }
            }
        }
        
        private boolean isNotColliding(final double p_179926_1_, final double p_179926_3_, final double p_179926_5_, final double p_179926_7_) {
            final double d0 = (p_179926_1_ - this.parentEntity.posX) / p_179926_7_;
            final double d2 = (p_179926_3_ - this.parentEntity.posY) / p_179926_7_;
            final double d3 = (p_179926_5_ - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
            for (int i = 1; i < p_179926_7_; ++i) {
                axisalignedbb = axisalignedbb.offset(d0, d2, d3);
                if (!this.parentEntity.worldObj.getCollidingBoundingBoxes(this.parentEntity, axisalignedbb).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }
}
