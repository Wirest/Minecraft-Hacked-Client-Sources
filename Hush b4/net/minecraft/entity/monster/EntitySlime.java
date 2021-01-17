// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.WorldType;
import net.minecraft.util.BlockPos;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearest;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public class EntitySlime extends EntityLiving implements IMob
{
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;
    
    public EntitySlime(final World worldIn) {
        super(worldIn);
        this.moveHelper = new SlimeMoveHelper(this);
        this.tasks.addTask(1, new AISlimeFloat(this));
        this.tasks.addTask(2, new AISlimeAttack(this));
        this.tasks.addTask(3, new AISlimeFaceRandom(this));
        this.tasks.addTask(5, new AISlimeHop(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
        this.targetTasks.addTask(3, new EntityAIFindEntityNearest(this, EntityIronGolem.class));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Byte)1);
    }
    
    protected void setSlimeSize(final int size) {
        this.dataWatcher.updateObject(16, (byte)size);
        this.setSize(0.51000005f * size, 0.51000005f * size);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(size * size);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.2f + 0.1f * size);
        this.setHealth(this.getMaxHealth());
        this.experienceValue = size;
    }
    
    public int getSlimeSize() {
        return this.dataWatcher.getWatchableObjectByte(16);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("Size", this.getSlimeSize() - 1);
        tagCompound.setBoolean("wasOnGround", this.wasOnGround);
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        int i = tagCompund.getInteger("Size");
        if (i < 0) {
            i = 0;
        }
        this.setSlimeSize(i + 1);
        this.wasOnGround = tagCompund.getBoolean("wasOnGround");
    }
    
    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.SLIME;
    }
    
    protected String getJumpSound() {
        return "mob.slime." + ((this.getSlimeSize() > 1) ? "big" : "small");
    }
    
    @Override
    public void onUpdate() {
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.getSlimeSize() > 0) {
            this.isDead = true;
        }
        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5f;
        this.prevSquishFactor = this.squishFactor;
        super.onUpdate();
        if (this.onGround && !this.wasOnGround) {
            for (int i = this.getSlimeSize(), j = 0; j < i * 8; ++j) {
                final float f = this.rand.nextFloat() * 3.1415927f * 2.0f;
                final float f2 = this.rand.nextFloat() * 0.5f + 0.5f;
                final float f3 = MathHelper.sin(f) * i * 0.5f * f2;
                final float f4 = MathHelper.cos(f) * i * 0.5f * f2;
                final World world = this.worldObj;
                final EnumParticleTypes enumparticletypes = this.getParticleType();
                final double d0 = this.posX + f3;
                final double d2 = this.posZ + f4;
                world.spawnParticle(enumparticletypes, d0, this.getEntityBoundingBox().minY, d2, 0.0, 0.0, 0.0, new int[0]);
            }
            if (this.makesSoundOnLand()) {
                this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f) / 0.8f);
            }
            this.squishAmount = -0.5f;
        }
        else if (!this.onGround && this.wasOnGround) {
            this.squishAmount = 1.0f;
        }
        this.wasOnGround = this.onGround;
        this.alterSquishAmount();
    }
    
    protected void alterSquishAmount() {
        this.squishAmount *= 0.6f;
    }
    
    protected int getJumpDelay() {
        return this.rand.nextInt(20) + 10;
    }
    
    protected EntitySlime createInstance() {
        return new EntitySlime(this.worldObj);
    }
    
    @Override
    public void onDataWatcherUpdate(final int dataID) {
        if (dataID == 16) {
            final int i = this.getSlimeSize();
            this.setSize(0.51000005f * i, 0.51000005f * i);
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if (this.isInWater() && this.rand.nextInt(20) == 0) {
                this.resetHeight();
            }
        }
        super.onDataWatcherUpdate(dataID);
    }
    
    @Override
    public void setDead() {
        final int i = this.getSlimeSize();
        if (!this.worldObj.isRemote && i > 1 && this.getHealth() <= 0.0f) {
            for (int j = 2 + this.rand.nextInt(3), k = 0; k < j; ++k) {
                final float f = (k % 2 - 0.5f) * i / 4.0f;
                final float f2 = (k / 2 - 0.5f) * i / 4.0f;
                final EntitySlime entityslime = this.createInstance();
                if (this.hasCustomName()) {
                    entityslime.setCustomNameTag(this.getCustomNameTag());
                }
                if (this.isNoDespawnRequired()) {
                    entityslime.enablePersistence();
                }
                entityslime.setSlimeSize(i / 2);
                entityslime.setLocationAndAngles(this.posX + f, this.posY + 0.5, this.posZ + f2, this.rand.nextFloat() * 360.0f, 0.0f);
                this.worldObj.spawnEntityInWorld(entityslime);
            }
        }
        super.setDead();
    }
    
    @Override
    public void applyEntityCollision(final Entity entityIn) {
        super.applyEntityCollision(entityIn);
        if (entityIn instanceof EntityIronGolem && this.canDamagePlayer()) {
            this.func_175451_e((EntityLivingBase)entityIn);
        }
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (this.canDamagePlayer()) {
            this.func_175451_e(entityIn);
        }
    }
    
    protected void func_175451_e(final EntityLivingBase p_175451_1_) {
        final int i = this.getSlimeSize();
        if (this.canEntityBeSeen(p_175451_1_) && this.getDistanceSqToEntity(p_175451_1_) < 0.6 * i * 0.6 * i && p_175451_1_.attackEntityFrom(DamageSource.causeMobDamage(this), (float)this.getAttackStrength())) {
            this.playSound("mob.attack", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.applyEnchantments(this, p_175451_1_);
        }
    }
    
    @Override
    public float getEyeHeight() {
        return 0.625f * this.height;
    }
    
    protected boolean canDamagePlayer() {
        return this.getSlimeSize() > 1;
    }
    
    protected int getAttackStrength() {
        return this.getSlimeSize();
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.slime." + ((this.getSlimeSize() > 1) ? "big" : "small");
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.slime." + ((this.getSlimeSize() > 1) ? "big" : "small");
    }
    
    @Override
    protected Item getDropItem() {
        return (this.getSlimeSize() == 1) ? Items.slime_ball : null;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final BlockPos blockpos = new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ));
        final Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
        if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1) {
            return false;
        }
        if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
            final BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
            if (biomegenbase == BiomeGenBase.swampland && this.posY > 50.0 && this.posY < 70.0 && this.rand.nextFloat() < 0.5f && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos(this)) <= this.rand.nextInt(8)) {
                return super.getCanSpawnHere();
            }
            if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0) {
                return super.getCanSpawnHere();
            }
        }
        return false;
    }
    
    @Override
    protected float getSoundVolume() {
        return 0.4f * this.getSlimeSize();
    }
    
    @Override
    public int getVerticalFaceSpeed() {
        return 0;
    }
    
    protected boolean makesSoundOnJump() {
        return this.getSlimeSize() > 0;
    }
    
    protected boolean makesSoundOnLand() {
        return this.getSlimeSize() > 2;
    }
    
    @Override
    protected void jump() {
        this.motionY = 0.41999998688697815;
        this.isAirBorne = true;
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, final IEntityLivingData livingdata) {
        int i = this.rand.nextInt(3);
        if (i < 2 && this.rand.nextFloat() < 0.5f * difficulty.getClampedAdditionalDifficulty()) {
            ++i;
        }
        final int j = 1 << i;
        this.setSlimeSize(j);
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    static class AISlimeAttack extends EntityAIBase
    {
        private EntitySlime slime;
        private int field_179465_b;
        
        public AISlimeAttack(final EntitySlime p_i45824_1_) {
            this.slime = p_i45824_1_;
            this.setMutexBits(2);
        }
        
        @Override
        public boolean shouldExecute() {
            final EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && (!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage);
        }
        
        @Override
        public void startExecuting() {
            this.field_179465_b = 300;
            super.startExecuting();
        }
        
        @Override
        public boolean continueExecuting() {
            final EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() && (!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage) && --this.field_179465_b > 0;
        }
        
        @Override
        public void updateTask() {
            this.slime.faceEntity(this.slime.getAttackTarget(), 10.0f, 10.0f);
            ((SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.rotationYaw, this.slime.canDamagePlayer());
        }
    }
    
    static class AISlimeFaceRandom extends EntityAIBase
    {
        private EntitySlime slime;
        private float field_179459_b;
        private int field_179460_c;
        
        public AISlimeFaceRandom(final EntitySlime p_i45820_1_) {
            this.slime = p_i45820_1_;
            this.setMutexBits(2);
        }
        
        @Override
        public boolean shouldExecute() {
            return this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava());
        }
        
        @Override
        public void updateTask() {
            final int field_179460_c = this.field_179460_c - 1;
            this.field_179460_c = field_179460_c;
            if (field_179460_c <= 0) {
                this.field_179460_c = 40 + this.slime.getRNG().nextInt(60);
                this.field_179459_b = (float)this.slime.getRNG().nextInt(360);
            }
            ((SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
        }
    }
    
    static class AISlimeFloat extends EntityAIBase
    {
        private EntitySlime slime;
        
        public AISlimeFloat(final EntitySlime p_i45823_1_) {
            this.slime = p_i45823_1_;
            this.setMutexBits(5);
            ((PathNavigateGround)p_i45823_1_.getNavigator()).setCanSwim(true);
        }
        
        @Override
        public boolean shouldExecute() {
            return this.slime.isInWater() || this.slime.isInLava();
        }
        
        @Override
        public void updateTask() {
            if (this.slime.getRNG().nextFloat() < 0.8f) {
                this.slime.getJumpHelper().setJumping();
            }
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2);
        }
    }
    
    static class AISlimeHop extends EntityAIBase
    {
        private EntitySlime slime;
        
        public AISlimeHop(final EntitySlime p_i45822_1_) {
            this.slime = p_i45822_1_;
            this.setMutexBits(5);
        }
        
        @Override
        public boolean shouldExecute() {
            return true;
        }
        
        @Override
        public void updateTask() {
            ((SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0);
        }
    }
    
    static class SlimeMoveHelper extends EntityMoveHelper
    {
        private float field_179922_g;
        private int field_179924_h;
        private EntitySlime slime;
        private boolean field_179923_j;
        
        public SlimeMoveHelper(final EntitySlime p_i45821_1_) {
            super(p_i45821_1_);
            this.slime = p_i45821_1_;
        }
        
        public void func_179920_a(final float p_179920_1_, final boolean p_179920_2_) {
            this.field_179922_g = p_179920_1_;
            this.field_179923_j = p_179920_2_;
        }
        
        public void setSpeed(final double speedIn) {
            this.speed = speedIn;
            this.update = true;
        }
        
        @Override
        public void onUpdateMoveHelper() {
            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0f);
            this.entity.rotationYawHead = this.entity.rotationYaw;
            this.entity.renderYawOffset = this.entity.rotationYaw;
            if (!this.update) {
                this.entity.setMoveForward(0.0f);
            }
            else {
                this.update = false;
                if (this.entity.onGround) {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                    if (this.field_179924_h-- <= 0) {
                        this.field_179924_h = this.slime.getJumpDelay();
                        if (this.field_179923_j) {
                            this.field_179924_h /= 3;
                        }
                        this.slime.getJumpHelper().setJumping();
                        if (this.slime.makesSoundOnJump()) {
                            this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2f + 1.0f) * 0.8f);
                        }
                    }
                    else {
                        final EntitySlime slime = this.slime;
                        final EntitySlime slime2 = this.slime;
                        final float n = 0.0f;
                        slime2.moveForward = n;
                        slime.moveStrafing = n;
                        this.entity.setAIMoveSpeed(0.0f);
                    }
                }
                else {
                    this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                }
            }
        }
    }
}
