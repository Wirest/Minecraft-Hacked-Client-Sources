package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class EntitySlime extends EntityLiving implements IMob
{
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean field_175452_bi;
    private static final String __OBFID = "CL_00001698";

    public EntitySlime(World worldIn)
    {
        super(worldIn);
        this.moveHelper = new EntitySlime.SlimeMoveHelper();
        this.tasks.addTask(1, new EntitySlime.AISlimeFloat());
        this.tasks.addTask(2, new EntitySlime.AISlimeAttack());
        this.tasks.addTask(3, new EntitySlime.AISlimeFaceRandom());
        this.tasks.addTask(5, new EntitySlime.AISlimeHop());
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)1));
    }

    protected void setSlimeSize(int p_70799_1_)
    {
        this.dataWatcher.updateObject(16, Byte.valueOf((byte)p_70799_1_));
        this.setSize(0.51000005F * (float)p_70799_1_, 0.51000005F * (float)p_70799_1_);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((double)(p_70799_1_ * p_70799_1_));
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((double)(0.2F + 0.1F * (float)p_70799_1_));
        this.setHealth(this.getMaxHealth());
        this.experienceValue = p_70799_1_;
    }

    /**
     * Returns the size of the slime.
     */
    public int getSlimeSize()
    {
        return this.dataWatcher.getWatchableObjectByte(16);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Size", this.getSlimeSize() - 1);
        tagCompound.setBoolean("wasOnGround", this.field_175452_bi);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        int var2 = tagCompund.getInteger("Size");

        if (var2 < 0)
        {
            var2 = 0;
        }

        this.setSlimeSize(var2 + 1);
        this.field_175452_bi = tagCompund.getBoolean("wasOnGround");
    }

    protected EnumParticleTypes func_180487_n()
    {
        return EnumParticleTypes.SLIME;
    }

    /**
     * Returns the name of the sound played when the slime jumps.
     */
    protected String getJumpSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate()
    {
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0)
        {
            this.isDead = true;
        }

        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();

        if (this.onGround && !this.field_175452_bi)
        {
            int var1 = this.getSlimeSize();

            for (int var2 = 0; var2 < var1 * 8; ++var2)
            {
                float var3 = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float var4 = this.rand.nextFloat() * 0.5F + 0.5F;
                float var5 = MathHelper.sin(var3) * (float)var1 * 0.5F * var4;
                float var6 = MathHelper.cos(var3) * (float)var1 * 0.5F * var4;
                World var10000 = this.worldObj;
                EnumParticleTypes var10001 = this.func_180487_n();
                double var10002 = this.posX + (double)var5;
                double var10004 = this.posZ + (double)var6;
                var10000.spawnParticle(var10001, var10002, this.getEntityBoundingBox().minY, var10004, 0.0D, 0.0D, 0.0D, new int[0]);
            }

            if (this.makesSoundOnLand())
            {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
            }

            this.squishAmount = -0.5F;
        }
        else if (!this.onGround && this.field_175452_bi)
        {
            this.squishAmount = 1.0F;
        }

        this.field_175452_bi = this.onGround;
        this.alterSquishAmount();
    }

    protected void alterSquishAmount()
    {
        this.squishAmount *= 0.6F;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    protected int getJumpDelay()
    {
        return this.rand.nextInt(20) + 10;
    }

    protected EntitySlime createInstance()
    {
        return new EntitySlime(this.worldObj);
    }

    public void func_145781_i(int p_145781_1_)
    {
        if (p_145781_1_ == 16)
        {
            int var2 = this.getSlimeSize();
            this.setSize(0.51000005F * (float)var2, 0.51000005F * (float)var2);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;

            if (this.isInWater() && this.rand.nextInt(20) == 0)
            {
                this.resetHeight();
            }
        }

        super.func_145781_i(p_145781_1_);
    }

    /**
     * Will get destroyed next tick.
     */
    public void setDead()
    {
        int var1 = this.getSlimeSize();

        if (!this.worldObj.isRemote && var1 > 1 && this.getHealth() <= 0.0F)
        {
            int var2 = 2 + this.rand.nextInt(3);

            for (int var3 = 0; var3 < var2; ++var3)
            {
                float var4 = ((float)(var3 % 2) - 0.5F) * (float)var1 / 4.0F;
                float var5 = ((float)(var3 / 2) - 0.5F) * (float)var1 / 4.0F;
                EntitySlime var6 = this.createInstance();

                if (this.hasCustomName())
                {
                    var6.setCustomNameTag(this.getCustomNameTag());
                }

                if (this.isNoDespawnRequired())
                {
                    var6.enablePersistence();
                }

                var6.setSlimeSize(var1 / 2);
                var6.setLocationAndAngles(this.posX + (double)var4, this.posY + 0.5D, this.posZ + (double)var5, this.rand.nextFloat() * 360.0F, 0.0F);
                this.worldObj.spawnEntityInWorld(var6);
            }
        }

        super.setDead();
    }

    /**
     * Applies a velocity to each of the entities pushing them away from each other. Args: entity
     */
    public void applyEntityCollision(Entity entityIn)
    {
        super.applyEntityCollision(entityIn);

        if (entityIn instanceof EntityIronGolem && this.canDamagePlayer())
        {
            this.func_175451_e((EntityLivingBase)entityIn);
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn)
    {
        if (this.canDamagePlayer())
        {
            this.func_175451_e(entityIn);
        }
    }

    protected void func_175451_e(EntityLivingBase p_175451_1_)
    {
        int var2 = this.getSlimeSize();

        if (this.canEntityBeSeen(p_175451_1_) && this.getDistanceSqToEntity(p_175451_1_) < 0.6D * (double)var2 * 0.6D * (double)var2 && p_175451_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttackStrength()))
        {
            this.playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            this.func_174815_a(this, p_175451_1_);
        }
    }

    public float getEyeHeight()
    {
        return 0.625F * this.height;
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    protected boolean canDamagePlayer()
    {
        return this.getSlimeSize() > 1;
    }

    /**
     * Gets the amount of damage dealt to the player when "attacked" by the slime.
     */
    protected int getAttackStrength()
    {
        return this.getSlimeSize();
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound()
    {
        return "mob.slime." + (this.getSlimeSize() > 1 ? "big" : "small");
    }

    protected Item getDropItem()
    {
        return this.getSlimeSize() == 1 ? Items.slime_ball : null;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere()
    {
        Chunk var1 = this.worldObj.getChunkFromBlockCoords(new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)));

        if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1)
        {
            return false;
        }
        else
        {
            if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
                BiomeGenBase var2 = this.worldObj.getBiomeGenForCoords(new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ)));

                if (var2 == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8))
                {
                    return super.getCanSpawnHere();
                }

                if (this.rand.nextInt(10) == 0 && var1.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D)
                {
                    return super.getCanSpawnHere();
                }
            }

            return false;
        }
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    protected float getSoundVolume()
    {
        return 0.4F * (float)this.getSlimeSize();
    }

    /**
     * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
     * use in wolves.
     */
    public int getVerticalFaceSpeed()
    {
        return 0;
    }

    /**
     * Returns true if the slime makes a sound when it jumps (based upon the slime's size)
     */
    protected boolean makesSoundOnJump()
    {
        return this.getSlimeSize() > 0;
    }

    /**
     * Returns true if the slime makes a sound when it lands after a jump (based upon the slime's size)
     */
    protected boolean makesSoundOnLand()
    {
        return this.getSlimeSize() > 2;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    protected void jump()
    {
        this.motionY = 0.41999998688697815D;
        this.isAirBorne = true;
    }

    public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
    {
        int var3 = this.rand.nextInt(3);

        if (var3 < 2 && this.rand.nextFloat() < 0.5F * p_180482_1_.func_180170_c())
        {
            ++var3;
        }

        int var4 = 1 << var3;
        this.setSlimeSize(var4);
        return super.func_180482_a(p_180482_1_, p_180482_2_);
    }

    class AISlimeAttack extends EntityAIBase
    {
        private EntitySlime field_179466_a = EntitySlime.this;
        private int field_179465_b;
        private static final String __OBFID = "CL_00002202";

        public AISlimeAttack()
        {
            this.setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            EntityLivingBase var1 = this.field_179466_a.getAttackTarget();
            return var1 == null ? false : var1.isEntityAlive();
        }

        public void startExecuting()
        {
            this.field_179465_b = 300;
            super.startExecuting();
        }

        public boolean continueExecuting()
        {
            EntityLivingBase var1 = this.field_179466_a.getAttackTarget();
            return var1 == null ? false : (!var1.isEntityAlive() ? false : --this.field_179465_b > 0);
        }

        public void updateTask()
        {
            this.field_179466_a.faceEntity(this.field_179466_a.getAttackTarget(), 10.0F, 10.0F);
            ((EntitySlime.SlimeMoveHelper)this.field_179466_a.getMoveHelper()).func_179920_a(this.field_179466_a.rotationYaw, this.field_179466_a.canDamagePlayer());
        }
    }

    class AISlimeFaceRandom extends EntityAIBase
    {
        private EntitySlime field_179461_a = EntitySlime.this;
        private float field_179459_b;
        private int field_179460_c;
        private static final String __OBFID = "CL_00002198";

        public AISlimeFaceRandom()
        {
            this.setMutexBits(2);
        }

        public boolean shouldExecute()
        {
            return this.field_179461_a.getAttackTarget() == null && (this.field_179461_a.onGround || this.field_179461_a.isInWater() || this.field_179461_a.func_180799_ab());
        }

        public void updateTask()
        {
            if (--this.field_179460_c <= 0)
            {
                this.field_179460_c = 40 + this.field_179461_a.getRNG().nextInt(60);
                this.field_179459_b = (float)this.field_179461_a.getRNG().nextInt(360);
            }

            ((EntitySlime.SlimeMoveHelper)this.field_179461_a.getMoveHelper()).func_179920_a(this.field_179459_b, false);
        }
    }

    class AISlimeFloat extends EntityAIBase
    {
        private EntitySlime field_179457_a = EntitySlime.this;
        private static final String __OBFID = "CL_00002201";

        public AISlimeFloat()
        {
            this.setMutexBits(5);
            ((PathNavigateGround)EntitySlime.this.getNavigator()).func_179693_d(true);
        }

        public boolean shouldExecute()
        {
            return this.field_179457_a.isInWater() || this.field_179457_a.func_180799_ab();
        }

        public void updateTask()
        {
            if (this.field_179457_a.getRNG().nextFloat() < 0.8F)
            {
                this.field_179457_a.getJumpHelper().setJumping();
            }

            ((EntitySlime.SlimeMoveHelper)this.field_179457_a.getMoveHelper()).func_179921_a(1.2D);
        }
    }

    class AISlimeHop extends EntityAIBase
    {
        private EntitySlime field_179458_a = EntitySlime.this;
        private static final String __OBFID = "CL_00002200";

        public AISlimeHop()
        {
            this.setMutexBits(5);
        }

        public boolean shouldExecute()
        {
            return true;
        }

        public void updateTask()
        {
            ((EntitySlime.SlimeMoveHelper)this.field_179458_a.getMoveHelper()).func_179921_a(1.0D);
        }
    }

    class SlimeMoveHelper extends EntityMoveHelper
    {
        private float field_179922_g;
        private int field_179924_h;
        private EntitySlime field_179925_i = EntitySlime.this;
        private boolean field_179923_j;
        private static final String __OBFID = "CL_00002199";

        public SlimeMoveHelper()
        {
            super(EntitySlime.this);
        }

        public void func_179920_a(float p_179920_1_, boolean p_179920_2_)
        {
            this.field_179922_g = p_179920_1_;
            this.field_179923_j = p_179920_2_;
        }

        public void func_179921_a(double p_179921_1_)
        {
            this.speed = p_179921_1_;
            this.update = true;
        }

        public void onUpdateMoveHelper()
        {
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0F);
            this.entity.rotationYawHead = this.entity.rotationYaw;
            this.entity.renderYawOffset = this.entity.rotationYaw;

            if (!this.update)
            {
                this.entity.setMoveForward(0.0F);
            }
            else
            {
                this.update = false;

                if (this.entity.onGround)
                {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));

                    if (this.field_179924_h-- <= 0)
                    {
                        this.field_179924_h = this.field_179925_i.getJumpDelay();

                        if (this.field_179923_j)
                        {
                            this.field_179924_h /= 3;
                        }

                        this.field_179925_i.getJumpHelper().setJumping();

                        if (this.field_179925_i.makesSoundOnJump())
                        {
                            this.field_179925_i.playSound(this.field_179925_i.getJumpSound(), this.field_179925_i.getSoundVolume(), ((this.field_179925_i.getRNG().nextFloat() - this.field_179925_i.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
                        }
                    }
                    else
                    {
                        this.field_179925_i.moveStrafing = this.field_179925_i.moveForward = 0.0F;
                        this.entity.setAIMoveSpeed(0.0F);
                    }
                }
                else
                {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                }
            }
        }
    }
}
