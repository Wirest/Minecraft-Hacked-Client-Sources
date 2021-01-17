// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.boss;

import net.minecraft.block.properties.IProperty;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.player.EntityPlayer;
import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.Explosion;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.EntityLiving;

public class EntityDragon extends EntityLiving implements IBossDisplayData, IEntityMultiPart, IMob
{
    public double targetX;
    public double targetY;
    public double targetZ;
    public double[][] ringBuffer;
    public int ringBufferIndex;
    public EntityDragonPart[] dragonPartArray;
    public EntityDragonPart dragonPartHead;
    public EntityDragonPart dragonPartBody;
    public EntityDragonPart dragonPartTail1;
    public EntityDragonPart dragonPartTail2;
    public EntityDragonPart dragonPartTail3;
    public EntityDragonPart dragonPartWing1;
    public EntityDragonPart dragonPartWing2;
    public float prevAnimTime;
    public float animTime;
    public boolean forceNewTarget;
    public boolean slowed;
    private Entity target;
    public int deathTicks;
    public EntityEnderCrystal healingEnderCrystal;
    
    public EntityDragon(final World worldIn) {
        super(worldIn);
        this.ringBuffer = new double[64][3];
        this.ringBufferIndex = -1;
        this.dragonPartArray = new EntityDragonPart[] { this.dragonPartHead = new EntityDragonPart(this, "head", 6.0f, 6.0f), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0f, 8.0f), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0f, 4.0f), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0f, 4.0f), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0f, 4.0f), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0f, 4.0f), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0f, 4.0f) };
        this.setHealth(this.getMaxHealth());
        this.setSize(16.0f, 8.0f);
        this.noClip = true;
        this.isImmuneToFire = true;
        this.targetY = 100.0;
        this.ignoreFrustumCheck = true;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    public double[] getMovementOffsets(final int p_70974_1_, float p_70974_2_) {
        if (this.getHealth() <= 0.0f) {
            p_70974_2_ = 0.0f;
        }
        p_70974_2_ = 1.0f - p_70974_2_;
        final int i = this.ringBufferIndex - p_70974_1_ * 1 & 0x3F;
        final int j = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 0x3F;
        final double[] adouble = new double[3];
        double d0 = this.ringBuffer[i][0];
        double d2 = MathHelper.wrapAngleTo180_double(this.ringBuffer[j][0] - d0);
        adouble[0] = d0 + d2 * p_70974_2_;
        d0 = this.ringBuffer[i][1];
        d2 = this.ringBuffer[j][1] - d0;
        adouble[1] = d0 + d2 * p_70974_2_;
        adouble[2] = this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * p_70974_2_;
        return adouble;
    }
    
    @Override
    public void onLivingUpdate() {
        if (this.worldObj.isRemote) {
            final float f = MathHelper.cos(this.animTime * 3.1415927f * 2.0f);
            final float f2 = MathHelper.cos(this.prevAnimTime * 3.1415927f * 2.0f);
            if (f2 <= -0.3f && f >= -0.3f && !this.isSilent()) {
                this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0f, 0.8f + this.rand.nextFloat() * 0.3f, false);
            }
        }
        this.prevAnimTime = this.animTime;
        if (this.getHealth() <= 0.0f) {
            final float f3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            final float f4 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            final float f5 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f3, this.posY + 2.0 + f4, this.posZ + f5, 0.0, 0.0, 0.0, new int[0]);
        }
        else {
            this.updateDragonEnderCrystal();
            float f6 = 0.2f / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0f + 1.0f);
            f6 *= (float)Math.pow(2.0, this.motionY);
            if (this.slowed) {
                this.animTime += f6 * 0.5f;
            }
            else {
                this.animTime += f6;
            }
            this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
            if (this.isAIDisabled()) {
                this.animTime = 0.5f;
            }
            else {
                if (this.ringBufferIndex < 0) {
                    for (int i = 0; i < this.ringBuffer.length; ++i) {
                        this.ringBuffer[i][0] = this.rotationYaw;
                        this.ringBuffer[i][1] = this.posY;
                    }
                }
                if (++this.ringBufferIndex == this.ringBuffer.length) {
                    this.ringBufferIndex = 0;
                }
                this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
                this.ringBuffer[this.ringBufferIndex][1] = this.posY;
                if (this.worldObj.isRemote) {
                    if (this.newPosRotationIncrements > 0) {
                        final double d10 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
                        final double d11 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
                        final double d12 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
                        final double d13 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
                        this.rotationYaw += (float)(d13 / this.newPosRotationIncrements);
                        this.rotationPitch += (float)((this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
                        --this.newPosRotationIncrements;
                        this.setPosition(d10, d11, d12);
                        this.setRotation(this.rotationYaw, this.rotationPitch);
                    }
                }
                else {
                    final double d14 = this.targetX - this.posX;
                    double d15 = this.targetY - this.posY;
                    final double d16 = this.targetZ - this.posZ;
                    final double d17 = d14 * d14 + d15 * d15 + d16 * d16;
                    if (this.target != null) {
                        this.targetX = this.target.posX;
                        this.targetZ = this.target.posZ;
                        final double d18 = this.targetX - this.posX;
                        final double d19 = this.targetZ - this.posZ;
                        final double d20 = Math.sqrt(d18 * d18 + d19 * d19);
                        double d21 = 0.4000000059604645 + d20 / 80.0 - 1.0;
                        if (d21 > 10.0) {
                            d21 = 10.0;
                        }
                        this.targetY = this.target.getEntityBoundingBox().minY + d21;
                    }
                    else {
                        this.targetX += this.rand.nextGaussian() * 2.0;
                        this.targetZ += this.rand.nextGaussian() * 2.0;
                    }
                    if (this.forceNewTarget || d17 < 100.0 || d17 > 22500.0 || this.isCollidedHorizontally || this.isCollidedVertically) {
                        this.setNewTarget();
                    }
                    d15 /= MathHelper.sqrt_double(d14 * d14 + d16 * d16);
                    final float f7 = 0.6f;
                    d15 = MathHelper.clamp_double(d15, -f7, f7);
                    this.motionY += d15 * 0.10000000149011612;
                    this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
                    final double d22 = 180.0 - MathHelper.func_181159_b(d14, d16) * 180.0 / 3.141592653589793;
                    double d23 = MathHelper.wrapAngleTo180_double(d22 - this.rotationYaw);
                    if (d23 > 50.0) {
                        d23 = 50.0;
                    }
                    if (d23 < -50.0) {
                        d23 = -50.0;
                    }
                    final Vec3 vec3 = new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ).normalize();
                    final double d24 = -MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f);
                    final Vec3 vec4 = new Vec3(MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f), this.motionY, d24).normalize();
                    float f8 = ((float)vec4.dotProduct(vec3) + 0.5f) / 1.5f;
                    if (f8 < 0.0f) {
                        f8 = 0.0f;
                    }
                    this.randomYawVelocity *= 0.8f;
                    final float f9 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0f + 1.0f;
                    double d25 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0 + 1.0;
                    if (d25 > 40.0) {
                        d25 = 40.0;
                    }
                    this.randomYawVelocity += (float)(d23 * (0.699999988079071 / d25 / f9));
                    this.rotationYaw += this.randomYawVelocity * 0.1f;
                    final float f10 = (float)(2.0 / (d25 + 1.0));
                    final float f11 = 0.06f;
                    this.moveFlying(0.0f, -1.0f, f11 * (f8 * f10 + (1.0f - f10)));
                    if (this.slowed) {
                        this.moveEntity(this.motionX * 0.800000011920929, this.motionY * 0.800000011920929, this.motionZ * 0.800000011920929);
                    }
                    else {
                        this.moveEntity(this.motionX, this.motionY, this.motionZ);
                    }
                    final Vec3 vec5 = new Vec3(this.motionX, this.motionY, this.motionZ).normalize();
                    float f12 = ((float)vec5.dotProduct(vec4) + 1.0f) / 2.0f;
                    f12 = 0.8f + 0.15f * f12;
                    this.motionX *= f12;
                    this.motionZ *= f12;
                    this.motionY *= 0.9100000262260437;
                }
                this.renderYawOffset = this.rotationYaw;
                final EntityDragonPart dragonPartHead = this.dragonPartHead;
                final EntityDragonPart dragonPartHead2 = this.dragonPartHead;
                final float n = 3.0f;
                dragonPartHead2.height = n;
                dragonPartHead.width = n;
                final EntityDragonPart dragonPartTail1 = this.dragonPartTail1;
                final EntityDragonPart dragonPartTail2 = this.dragonPartTail1;
                final float n2 = 2.0f;
                dragonPartTail2.height = n2;
                dragonPartTail1.width = n2;
                final EntityDragonPart dragonPartTail3 = this.dragonPartTail2;
                final EntityDragonPart dragonPartTail4 = this.dragonPartTail2;
                final float n3 = 2.0f;
                dragonPartTail4.height = n3;
                dragonPartTail3.width = n3;
                final EntityDragonPart dragonPartTail5 = this.dragonPartTail3;
                final EntityDragonPart dragonPartTail6 = this.dragonPartTail3;
                final float n4 = 2.0f;
                dragonPartTail6.height = n4;
                dragonPartTail5.width = n4;
                this.dragonPartBody.height = 3.0f;
                this.dragonPartBody.width = 5.0f;
                this.dragonPartWing1.height = 2.0f;
                this.dragonPartWing1.width = 4.0f;
                this.dragonPartWing2.height = 3.0f;
                this.dragonPartWing2.width = 4.0f;
                final float f13 = (float)(this.getMovementOffsets(5, 1.0f)[1] - this.getMovementOffsets(10, 1.0f)[1]) * 10.0f / 180.0f * 3.1415927f;
                final float f14 = MathHelper.cos(f13);
                final float f15 = -MathHelper.sin(f13);
                final float f16 = this.rotationYaw * 3.1415927f / 180.0f;
                final float f17 = MathHelper.sin(f16);
                final float f18 = MathHelper.cos(f16);
                this.dragonPartBody.onUpdate();
                this.dragonPartBody.setLocationAndAngles(this.posX + f17 * 0.5f, this.posY, this.posZ - f18 * 0.5f, 0.0f, 0.0f);
                this.dragonPartWing1.onUpdate();
                this.dragonPartWing1.setLocationAndAngles(this.posX + f18 * 4.5f, this.posY + 2.0, this.posZ + f17 * 4.5f, 0.0f, 0.0f);
                this.dragonPartWing2.onUpdate();
                this.dragonPartWing2.setLocationAndAngles(this.posX - f18 * 4.5f, this.posY + 2.0, this.posZ - f17 * 4.5f, 0.0f, 0.0f);
                if (!this.worldObj.isRemote && this.hurtTime == 0) {
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0, 2.0, 4.0).offset(0.0, -2.0, 0.0)));
                    this.attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.dragonPartHead.getEntityBoundingBox().expand(1.0, 1.0, 1.0)));
                }
                final double[] adouble1 = this.getMovementOffsets(5, 1.0f);
                final double[] adouble2 = this.getMovementOffsets(0, 1.0f);
                final float f19 = MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f - this.randomYawVelocity * 0.01f);
                final float f20 = MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f - this.randomYawVelocity * 0.01f);
                this.dragonPartHead.onUpdate();
                this.dragonPartHead.setLocationAndAngles(this.posX + f19 * 5.5f * f14, this.posY + (adouble2[1] - adouble1[1]) * 1.0 + f15 * 5.5f, this.posZ - f20 * 5.5f * f14, 0.0f, 0.0f);
                for (int j = 0; j < 3; ++j) {
                    EntityDragonPart entitydragonpart = null;
                    if (j == 0) {
                        entitydragonpart = this.dragonPartTail1;
                    }
                    if (j == 1) {
                        entitydragonpart = this.dragonPartTail2;
                    }
                    if (j == 2) {
                        entitydragonpart = this.dragonPartTail3;
                    }
                    final double[] adouble3 = this.getMovementOffsets(12 + j * 2, 1.0f);
                    final float f21 = this.rotationYaw * 3.1415927f / 180.0f + this.simplifyAngle(adouble3[0] - adouble1[0]) * 3.1415927f / 180.0f * 1.0f;
                    final float f22 = MathHelper.sin(f21);
                    final float f23 = MathHelper.cos(f21);
                    final float f24 = 1.5f;
                    final float f25 = (j + 1) * 2.0f;
                    entitydragonpart.onUpdate();
                    entitydragonpart.setLocationAndAngles(this.posX - (f17 * f24 + f22 * f25) * f14, this.posY + (adouble3[1] - adouble1[1]) * 1.0 - (f25 + f24) * f15 + 1.5, this.posZ + (f18 * f24 + f23 * f25) * f14, 0.0f, 0.0f);
                }
                if (!this.worldObj.isRemote) {
                    this.slowed = (this.destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | this.destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox()));
                }
            }
        }
    }
    
    private void updateDragonEnderCrystal() {
        if (this.healingEnderCrystal != null) {
            if (this.healingEnderCrystal.isDead) {
                if (!this.worldObj.isRemote) {
                    this.attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource(null), 10.0f);
                }
                this.healingEnderCrystal = null;
            }
            else if (this.ticksExisted % 10 == 0 && this.getHealth() < this.getMaxHealth()) {
                this.setHealth(this.getHealth() + 1.0f);
            }
        }
        if (this.rand.nextInt(10) == 0) {
            final float f = 32.0f;
            final List<EntityEnderCrystal> list = this.worldObj.getEntitiesWithinAABB((Class<? extends EntityEnderCrystal>)EntityEnderCrystal.class, this.getEntityBoundingBox().expand(f, f, f));
            EntityEnderCrystal entityendercrystal = null;
            double d0 = Double.MAX_VALUE;
            for (final EntityEnderCrystal entityendercrystal2 : list) {
                final double d2 = entityendercrystal2.getDistanceSqToEntity(this);
                if (d2 < d0) {
                    d0 = d2;
                    entityendercrystal = entityendercrystal2;
                }
            }
            this.healingEnderCrystal = entityendercrystal;
        }
    }
    
    private void collideWithEntities(final List<Entity> p_70970_1_) {
        final double d0 = (this.dragonPartBody.getEntityBoundingBox().minX + this.dragonPartBody.getEntityBoundingBox().maxX) / 2.0;
        final double d2 = (this.dragonPartBody.getEntityBoundingBox().minZ + this.dragonPartBody.getEntityBoundingBox().maxZ) / 2.0;
        for (final Entity entity : p_70970_1_) {
            if (entity instanceof EntityLivingBase) {
                final double d3 = entity.posX - d0;
                final double d4 = entity.posZ - d2;
                final double d5 = d3 * d3 + d4 * d4;
                entity.addVelocity(d3 / d5 * 4.0, 0.20000000298023224, d4 / d5 * 4.0);
            }
        }
    }
    
    private void attackEntitiesInList(final List<Entity> p_70971_1_) {
        for (int i = 0; i < p_70971_1_.size(); ++i) {
            final Entity entity = p_70971_1_.get(i);
            if (entity instanceof EntityLivingBase) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), 10.0f);
                this.applyEnchantments(this, entity);
            }
        }
    }
    
    private void setNewTarget() {
        this.forceNewTarget = false;
        final List<EntityPlayer> list = (List<EntityPlayer>)Lists.newArrayList((Iterable<?>)this.worldObj.playerEntities);
        final Iterator<EntityPlayer> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isSpectator()) {
                iterator.remove();
            }
        }
        if (this.rand.nextInt(2) == 0 && !list.isEmpty()) {
            this.target = list.get(this.rand.nextInt(list.size()));
        }
        else {
            boolean flag;
            do {
                this.targetX = 0.0;
                this.targetY = 70.0f + this.rand.nextFloat() * 50.0f;
                this.targetZ = 0.0;
                this.targetX += this.rand.nextFloat() * 120.0f - 60.0f;
                this.targetZ += this.rand.nextFloat() * 120.0f - 60.0f;
                final double d0 = this.posX - this.targetX;
                final double d2 = this.posY - this.targetY;
                final double d3 = this.posZ - this.targetZ;
                flag = (d0 * d0 + d2 * d2 + d3 * d3 > 100.0);
            } while (!flag);
            this.target = null;
        }
    }
    
    private float simplifyAngle(final double p_70973_1_) {
        return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
    }
    
    private boolean destroyBlocksInAABB(final AxisAlignedBB p_70972_1_) {
        final int i = MathHelper.floor_double(p_70972_1_.minX);
        final int j = MathHelper.floor_double(p_70972_1_.minY);
        final int k = MathHelper.floor_double(p_70972_1_.minZ);
        final int l = MathHelper.floor_double(p_70972_1_.maxX);
        final int i2 = MathHelper.floor_double(p_70972_1_.maxY);
        final int j2 = MathHelper.floor_double(p_70972_1_.maxZ);
        boolean flag = false;
        boolean flag2 = false;
        for (int k2 = i; k2 <= l; ++k2) {
            for (int l2 = j; l2 <= i2; ++l2) {
                for (int i3 = k; i3 <= j2; ++i3) {
                    final BlockPos blockpos = new BlockPos(k2, l2, i3);
                    final Block block = this.worldObj.getBlockState(blockpos).getBlock();
                    if (block.getMaterial() != Material.air) {
                        if (block != Blocks.barrier && block != Blocks.obsidian && block != Blocks.end_stone && block != Blocks.bedrock && block != Blocks.command_block && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
                            flag2 = (this.worldObj.setBlockToAir(blockpos) || flag2);
                        }
                        else {
                            flag = true;
                        }
                    }
                }
            }
        }
        if (flag2) {
            final double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * this.rand.nextFloat();
            final double d2 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * this.rand.nextFloat();
            final double d3 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * this.rand.nextFloat();
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d2, d3, 0.0, 0.0, 0.0, new int[0]);
        }
        return flag;
    }
    
    @Override
    public boolean attackEntityFromPart(final EntityDragonPart dragonPart, final DamageSource source, float p_70965_3_) {
        if (dragonPart != this.dragonPartHead) {
            p_70965_3_ = p_70965_3_ / 4.0f + 1.0f;
        }
        final float f = this.rotationYaw * 3.1415927f / 180.0f;
        final float f2 = MathHelper.sin(f);
        final float f3 = MathHelper.cos(f);
        this.targetX = this.posX + f2 * 5.0f + (this.rand.nextFloat() - 0.5f) * 2.0f;
        this.targetY = this.posY + this.rand.nextFloat() * 3.0f + 1.0;
        this.targetZ = this.posZ - f3 * 5.0f + (this.rand.nextFloat() - 0.5f) * 2.0f;
        this.target = null;
        if (source.getEntity() instanceof EntityPlayer || source.isExplosion()) {
            this.attackDragonFrom(source, p_70965_3_);
        }
        return true;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage()) {
            this.attackDragonFrom(source, amount);
        }
        return false;
    }
    
    protected boolean attackDragonFrom(final DamageSource source, final float amount) {
        return super.attackEntityFrom(source, amount);
    }
    
    @Override
    public void onKillCommand() {
        this.setDead();
    }
    
    @Override
    protected void onDeathUpdate() {
        ++this.deathTicks;
        if (this.deathTicks >= 180 && this.deathTicks <= 200) {
            final float f = (this.rand.nextFloat() - 0.5f) * 8.0f;
            final float f2 = (this.rand.nextFloat() - 0.5f) * 4.0f;
            final float f3 = (this.rand.nextFloat() - 0.5f) * 8.0f;
            this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2.0 + f2, this.posZ + f3, 0.0, 0.0, 0.0, new int[0]);
        }
        final boolean flag = this.worldObj.getGameRules().getBoolean("doMobLoot");
        if (!this.worldObj.isRemote) {
            if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag) {
                int i = 1000;
                while (i > 0) {
                    final int k = EntityXPOrb.getXPSplit(i);
                    i -= k;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, k));
                }
            }
            if (this.deathTicks == 1) {
                this.worldObj.playBroadcastSound(1018, new BlockPos(this), 0);
            }
        }
        this.moveEntity(0.0, 0.10000000149011612, 0.0);
        final float n = this.rotationYaw + 20.0f;
        this.rotationYaw = n;
        this.renderYawOffset = n;
        if (this.deathTicks == 200 && !this.worldObj.isRemote) {
            if (flag) {
                int j = 2000;
                while (j > 0) {
                    final int l = EntityXPOrb.getXPSplit(j);
                    j -= l;
                    this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, l));
                }
            }
            this.generatePortal(new BlockPos(this.posX, 64.0, this.posZ));
            this.setDead();
        }
    }
    
    private void generatePortal(final BlockPos pos) {
        final int i = 4;
        final double d0 = 12.25;
        final double d2 = 6.25;
        for (int j = -1; j <= 32; ++j) {
            for (int k = -4; k <= 4; ++k) {
                for (int l = -4; l <= 4; ++l) {
                    final double d3 = k * k + l * l;
                    if (d3 <= 12.25) {
                        final BlockPos blockpos = pos.add(k, j, l);
                        if (j < 0) {
                            if (d3 <= 6.25) {
                                this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
                            }
                        }
                        else if (j > 0) {
                            this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState());
                        }
                        else if (d3 > 6.25) {
                            this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
                        }
                        else {
                            this.worldObj.setBlockState(blockpos, Blocks.end_portal.getDefaultState());
                        }
                    }
                }
            }
        }
        this.worldObj.setBlockState(pos, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(pos.up(), Blocks.bedrock.getDefaultState());
        final BlockPos blockpos2 = pos.up(2);
        this.worldObj.setBlockState(blockpos2, Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(blockpos2.west(), Blocks.torch.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.EAST));
        this.worldObj.setBlockState(blockpos2.east(), Blocks.torch.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.WEST));
        this.worldObj.setBlockState(blockpos2.north(), Blocks.torch.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.SOUTH));
        this.worldObj.setBlockState(blockpos2.south(), Blocks.torch.getDefaultState().withProperty((IProperty<Comparable>)BlockTorch.FACING, EnumFacing.NORTH));
        this.worldObj.setBlockState(pos.up(3), Blocks.bedrock.getDefaultState());
        this.worldObj.setBlockState(pos.up(4), Blocks.dragon_egg.getDefaultState());
    }
    
    @Override
    protected void despawnEntity() {
    }
    
    @Override
    public Entity[] getParts() {
        return this.dragonPartArray;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    
    @Override
    public World getWorld() {
        return this.worldObj;
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.enderdragon.growl";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.enderdragon.hit";
    }
    
    @Override
    protected float getSoundVolume() {
        return 5.0f;
    }
}
