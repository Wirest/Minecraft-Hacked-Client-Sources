package net.minecraft.entity.monster;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBlaze extends EntityMob {
    /**
     * Random offset used in floating behaviour
     */
    private float heightOffset = 0.5F;

    /**
     * ticks until heightOffset is randomized
     */
    private int heightOffsetUpdateTime;
    private static final String __OBFID = "CL_00001682";

    public EntityBlaze(World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
        this.experienceValue = 10;
        this.tasks.addTask(4, new EntityBlaze.AIFireballAttack());
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[0]));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0D);
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, new Byte((byte) 0));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return "mob.blaze.breathe";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "mob.blaze.hit";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "mob.blaze.death";
    }

    public int getBrightnessForRender(float p_70070_1_) {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_) {
        return 1.0F;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate() {
        if (!this.onGround && this.motionY < 0.0D) {
            this.motionY *= 0.6D;
        }

        if (this.worldObj.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSlient()) {
                this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }

            for (int var1 = 0; var1 < 2; ++var1) {
                this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * (double) this.width, this.posY + this.rand.nextDouble() * (double) this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double) this.width, 0.0D, 0.0D, 0.0D, new int[0]);
            }
        }

        super.onLivingUpdate();
    }

    protected void updateAITasks() {
        if (this.isWet()) {
            this.attackEntityFrom(DamageSource.drown, 1.0F);
        }

        --this.heightOffsetUpdateTime;

        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float) this.rand.nextGaussian() * 3.0F;
        }

        EntityLivingBase var1 = this.getAttackTarget();

        if (var1 != null && var1.posY + (double) var1.getEyeHeight() > this.posY + (double) this.getEyeHeight() + (double) this.heightOffset) {
            this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
            this.isAirBorne = true;
        }

        super.updateAITasks();
    }

    public void fall(float distance, float damageMultiplier) {
    }

    protected Item getDropItem() {
        return Items.blaze_rod;
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    public boolean isBurning() {
        return this.func_70845_n();
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        if (p_70628_1_) {
            int var3 = this.rand.nextInt(2 + p_70628_2_);

            for (int var4 = 0; var4 < var3; ++var4) {
                this.dropItem(Items.blaze_rod, 1);
            }
        }
    }

    public boolean func_70845_n() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void func_70844_e(boolean p_70844_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70844_1_) {
            var2 = (byte) (var2 | 1);
        } else {
            var2 &= -2;
        }

        this.dataWatcher.updateObject(16, Byte.valueOf(var2));
    }

    /**
     * Checks to make sure the light is not too bright where the mob is spawning
     */
    protected boolean isValidLightLevel() {
        return true;
    }

    class AIFireballAttack extends EntityAIBase {
        private EntityBlaze field_179469_a = EntityBlaze.this;
        private int field_179467_b;
        private int field_179468_c;
        private static final String __OBFID = "CL_00002225";

        public AIFireballAttack() {
            this.setMutexBits(3);
        }

        public boolean shouldExecute() {
            EntityLivingBase var1 = this.field_179469_a.getAttackTarget();
            return var1 != null && var1.isEntityAlive();
        }

        public void startExecuting() {
            this.field_179467_b = 0;
        }

        public void resetTask() {
            this.field_179469_a.func_70844_e(false);
        }

        public void updateTask() {
            --this.field_179468_c;
            EntityLivingBase var1 = this.field_179469_a.getAttackTarget();
            double var2 = this.field_179469_a.getDistanceSqToEntity(var1);

            if (var2 < 4.0D) {
                if (this.field_179468_c <= 0) {
                    this.field_179468_c = 20;
                    this.field_179469_a.attackEntityAsMob(var1);
                }

                this.field_179469_a.getMoveHelper().setMoveTo(var1.posX, var1.posY, var1.posZ, 1.0D);
            } else if (var2 < 256.0D) {
                double var4 = var1.posX - this.field_179469_a.posX;
                double var6 = var1.getEntityBoundingBox().minY + (double) (var1.height / 2.0F) - (this.field_179469_a.posY + (double) (this.field_179469_a.height / 2.0F));
                double var8 = var1.posZ - this.field_179469_a.posZ;

                if (this.field_179468_c <= 0) {
                    ++this.field_179467_b;

                    if (this.field_179467_b == 1) {
                        this.field_179468_c = 60;
                        this.field_179469_a.func_70844_e(true);
                    } else if (this.field_179467_b <= 4) {
                        this.field_179468_c = 6;
                    } else {
                        this.field_179468_c = 100;
                        this.field_179467_b = 0;
                        this.field_179469_a.func_70844_e(false);
                    }

                    if (this.field_179467_b > 1) {
                        float var10 = MathHelper.sqrt_float(MathHelper.sqrt_double(var2)) * 0.5F;
                        this.field_179469_a.worldObj.playAuxSFXAtEntity((EntityPlayer) null, 1009, new BlockPos((int) this.field_179469_a.posX, (int) this.field_179469_a.posY, (int) this.field_179469_a.posZ), 0);

                        for (int var11 = 0; var11 < 1; ++var11) {
                            EntitySmallFireball var12 = new EntitySmallFireball(this.field_179469_a.worldObj, this.field_179469_a, var4 + this.field_179469_a.getRNG().nextGaussian() * (double) var10, var6, var8 + this.field_179469_a.getRNG().nextGaussian() * (double) var10);
                            var12.posY = this.field_179469_a.posY + (double) (this.field_179469_a.height / 2.0F) + 0.5D;
                            this.field_179469_a.worldObj.spawnEntityInWorld(var12);
                        }
                    }
                }

                this.field_179469_a.getLookHelper().setLookPositionWithEntity(var1, 10.0F, 10.0F);
            } else {
                this.field_179469_a.getNavigator().clearPathEntity();
                this.field_179469_a.getMoveHelper().setMoveTo(var1.posX, var1.posY, var1.posZ, 1.0D);
            }

            super.updateTask();
        }
    }
}
