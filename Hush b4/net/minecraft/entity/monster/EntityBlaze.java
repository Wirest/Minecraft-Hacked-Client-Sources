// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class EntityBlaze extends EntityMob
{
    private float heightOffset;
    private int heightOffsetUpdateTime;
    
    public EntityBlaze(final World worldIn) {
        super(worldIn);
        this.heightOffset = 0.5f;
        this.isImmuneToFire = true;
        this.experienceValue = 10;
        this.tasks.addTask(4, new AIFireballAttack(this));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte)0));
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.blaze.breathe";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.blaze.hit";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.blaze.death";
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        return 15728880;
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        return 1.0f;
    }
    
    @Override
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        if (this.worldObj.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()) {
                this.worldObj.playSound(this.posX + 0.5, this.posY + 0.5, this.posZ + 0.5, "fire.fire", 1.0f + this.rand.nextFloat(), this.rand.nextFloat() * 0.7f + 0.3f, false);
            }
            for (int i = 0; i < 2; ++i) {
                this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, 0.0, 0.0, 0.0, new int[0]);
            }
        }
        super.onLivingUpdate();
    }
    
    @Override
    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0f);
        }
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5f + (float)this.rand.nextGaussian() * 3.0f;
        }
        final EntityLivingBase entitylivingbase = this.getAttackTarget();
        if (entitylivingbase != null && entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset) {
            this.motionY += (0.30000001192092896 - this.motionY) * 0.30000001192092896;
            this.isAirBorne = true;
        }
        super.updateAITasks();
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected Item getDropItem() {
        return Items.blaze_rod;
    }
    
    @Override
    public boolean isBurning() {
        return this.func_70845_n();
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        if (p_70628_1_) {
            for (int i = this.rand.nextInt(2 + p_70628_2_), j = 0; j < i; ++j) {
                this.dropItem(Items.blaze_rod, 1);
            }
        }
    }
    
    public boolean func_70845_n() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setOnFire(final boolean onFire) {
        byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (onFire) {
            b0 |= 0x1;
        }
        else {
            b0 &= 0xFFFFFFFE;
        }
        this.dataWatcher.updateObject(16, b0);
    }
    
    @Override
    protected boolean isValidLightLevel() {
        return true;
    }
    
    static class AIFireballAttack extends EntityAIBase
    {
        private EntityBlaze blaze;
        private int field_179467_b;
        private int field_179468_c;
        
        public AIFireballAttack(final EntityBlaze p_i45846_1_) {
            this.blaze = p_i45846_1_;
            this.setMutexBits(3);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive();
        }
        
        @Override
        public void startExecuting() {
            this.field_179467_b = 0;
        }
        
        @Override
        public void resetTask() {
            this.blaze.setOnFire(false);
        }
        
        @Override
        public void updateTask() {
            --this.field_179468_c;
            final EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
            final double d0 = this.blaze.getDistanceSqToEntity(entitylivingbase);
            if (d0 < 4.0) {
                if (this.field_179468_c <= 0) {
                    this.field_179468_c = 20;
                    this.blaze.attackEntityAsMob(entitylivingbase);
                }
                this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0);
            }
            else if (d0 < 256.0) {
                final double d2 = entitylivingbase.posX - this.blaze.posX;
                final double d3 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0f - (this.blaze.posY + this.blaze.height / 2.0f);
                final double d4 = entitylivingbase.posZ - this.blaze.posZ;
                if (this.field_179468_c <= 0) {
                    ++this.field_179467_b;
                    if (this.field_179467_b == 1) {
                        this.field_179468_c = 60;
                        this.blaze.setOnFire(true);
                    }
                    else if (this.field_179467_b <= 4) {
                        this.field_179468_c = 6;
                    }
                    else {
                        this.field_179468_c = 100;
                        this.field_179467_b = 0;
                        this.blaze.setOnFire(false);
                    }
                    if (this.field_179467_b > 1) {
                        final float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5f;
                        this.blaze.worldObj.playAuxSFXAtEntity(null, 1009, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
                        for (int i = 0; i < 1; ++i) {
                            final EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.blaze.worldObj, this.blaze, d2 + this.blaze.getRNG().nextGaussian() * f, d3, d4 + this.blaze.getRNG().nextGaussian() * f);
                            entitysmallfireball.posY = this.blaze.posY + this.blaze.height / 2.0f + 0.5;
                            this.blaze.worldObj.spawnEntityInWorld(entitysmallfireball);
                        }
                    }
                }
                this.blaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0f, 10.0f);
            }
            else {
                this.blaze.getNavigator().clearPathEntity();
                this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0);
            }
            super.updateTask();
        }
    }
}
