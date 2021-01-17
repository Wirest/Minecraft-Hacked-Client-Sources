// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class EntityBoat extends Entity
{
    private boolean isBoatEmpty;
    private double speedMultiplier;
    private int boatPosRotationIncrements;
    private double boatX;
    private double boatY;
    private double boatZ;
    private double boatYaw;
    private double boatPitch;
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    
    public EntityBoat(final World worldIn) {
        super(worldIn);
        this.isBoatEmpty = true;
        this.speedMultiplier = 0.07;
        this.preventEntitySpawning = true;
        this.setSize(1.5f, 0.6f);
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0f));
    }
    
    @Override
    public AxisAlignedBB getCollisionBox(final Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }
    
    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return this.getEntityBoundingBox();
    }
    
    @Override
    public boolean canBePushed() {
        return true;
    }
    
    public EntityBoat(final World worldIn, final double p_i1705_2_, final double p_i1705_4_, final double p_i1705_6_) {
        this(worldIn);
        this.setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.motionX = 0.0;
        this.motionY = 0.0;
        this.motionZ = 0.0;
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }
    
    @Override
    public double getMountedYOffset() {
        return -0.3;
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.worldObj.isRemote || this.isDead) {
            return true;
        }
        if (this.riddenByEntity != null && this.riddenByEntity == source.getEntity() && source instanceof EntityDamageSourceIndirect) {
            return false;
        }
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() + amount * 10.0f);
        this.setBeenAttacked();
        final boolean flag = source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode;
        if (flag || this.getDamageTaken() > 40.0f) {
            if (this.riddenByEntity != null) {
                this.riddenByEntity.mountEntity(this);
            }
            if (!flag && this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                this.dropItemWithOffset(Items.boat, 1, 0.0f);
            }
            this.setDead();
        }
        return true;
    }
    
    @Override
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0f);
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void setPositionAndRotation2(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        if (p_180426_10_ && this.riddenByEntity != null) {
            this.posX = x;
            this.prevPosX = x;
            this.posY = y;
            this.prevPosY = y;
            this.posZ = z;
            this.prevPosZ = z;
            this.rotationYaw = yaw;
            this.rotationPitch = pitch;
            this.boatPosRotationIncrements = 0;
            this.setPosition(x, y, z);
            final double n = 0.0;
            this.velocityX = n;
            this.motionX = n;
            final double n2 = 0.0;
            this.velocityY = n2;
            this.motionY = n2;
            final double n3 = 0.0;
            this.velocityZ = n3;
            this.motionZ = n3;
        }
        else {
            if (this.isBoatEmpty) {
                this.boatPosRotationIncrements = posRotationIncrements + 5;
            }
            else {
                final double d0 = x - this.posX;
                final double d2 = y - this.posY;
                final double d3 = z - this.posZ;
                final double d4 = d0 * d0 + d2 * d2 + d3 * d3;
                if (d4 <= 1.0) {
                    return;
                }
                this.boatPosRotationIncrements = 3;
            }
            this.boatX = x;
            this.boatY = y;
            this.boatZ = z;
            this.boatYaw = yaw;
            this.boatPitch = pitch;
            this.motionX = this.velocityX;
            this.motionY = this.velocityY;
            this.motionZ = this.velocityZ;
        }
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.velocityX = x;
        this.motionY = y;
        this.velocityY = y;
        this.motionZ = z;
        this.velocityZ = z;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }
        if (this.getDamageTaken() > 0.0f) {
            this.setDamageTaken(this.getDamageTaken() - 1.0f);
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        final int i = 5;
        double d0 = 0.0;
        for (int j = 0; j < i; ++j) {
            final double d2 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (j + 0) / i - 0.125;
            final double d3 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (j + 1) / i - 0.125;
            final AxisAlignedBB axisalignedbb = new AxisAlignedBB(this.getEntityBoundingBox().minX, d2, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, d3, this.getEntityBoundingBox().maxZ);
            if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water)) {
                d0 += 1.0 / i;
            }
        }
        final double d4 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        if (d4 > 0.2975) {
            final double d5 = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0);
            final double d6 = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0);
            for (int k = 0; k < 1.0 + d4 * 60.0; ++k) {
                final double d7 = this.rand.nextFloat() * 2.0f - 1.0f;
                final double d8 = (this.rand.nextInt(2) * 2 - 1) * 0.7;
                if (this.rand.nextBoolean()) {
                    final double d9 = this.posX - d5 * d7 * 0.8 + d6 * d8;
                    final double d10 = this.posZ - d6 * d7 * 0.8 - d5 * d8;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d9, this.posY - 0.125, d10, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                else {
                    final double d11 = this.posX + d5 + d6 * d7 * 0.7;
                    final double d12 = this.posZ + d6 - d5 * d7 * 0.7;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d11, this.posY - 0.125, d12, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }
        if (this.worldObj.isRemote && this.isBoatEmpty) {
            if (this.boatPosRotationIncrements > 0) {
                final double d13 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
                final double d14 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
                final double d15 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
                final double d16 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
                this.rotationYaw += (float)(d16 / this.boatPosRotationIncrements);
                this.rotationPitch += (float)((this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(d13, d14, d15);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            }
            else {
                final double d17 = this.posX + this.motionX;
                final double d18 = this.posY + this.motionY;
                final double d19 = this.posZ + this.motionZ;
                this.setPosition(d17, d18, d19);
                if (this.onGround) {
                    this.motionX *= 0.5;
                    this.motionY *= 0.5;
                    this.motionZ *= 0.5;
                }
                this.motionX *= 0.9900000095367432;
                this.motionY *= 0.949999988079071;
                this.motionZ *= 0.9900000095367432;
            }
        }
        else {
            if (d0 < 1.0) {
                final double d20 = d0 * 2.0 - 1.0;
                this.motionY += 0.03999999910593033 * d20;
            }
            else {
                if (this.motionY < 0.0) {
                    this.motionY /= 2.0;
                }
                this.motionY += 0.007000000216066837;
            }
            if (this.riddenByEntity instanceof EntityLivingBase) {
                final EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
                final float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0f;
                this.motionX += -Math.sin(f * 3.1415927f / 180.0f) * this.speedMultiplier * entitylivingbase.moveForward * 0.05000000074505806;
                this.motionZ += Math.cos(f * 3.1415927f / 180.0f) * this.speedMultiplier * entitylivingbase.moveForward * 0.05000000074505806;
            }
            double d21 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
            if (d21 > 0.35) {
                final double d22 = 0.35 / d21;
                this.motionX *= d22;
                this.motionZ *= d22;
                d21 = 0.35;
            }
            if (d21 > d4 && this.speedMultiplier < 0.35) {
                this.speedMultiplier += (0.35 - this.speedMultiplier) / 35.0;
                if (this.speedMultiplier > 0.35) {
                    this.speedMultiplier = 0.35;
                }
            }
            else {
                this.speedMultiplier -= (this.speedMultiplier - 0.07) / 35.0;
                if (this.speedMultiplier < 0.07) {
                    this.speedMultiplier = 0.07;
                }
            }
            for (int i2 = 0; i2 < 4; ++i2) {
                final int l1 = MathHelper.floor_double(this.posX + (i2 % 2 - 0.5) * 0.8);
                final int i3 = MathHelper.floor_double(this.posZ + (i2 / 2 - 0.5) * 0.8);
                for (int j2 = 0; j2 < 2; ++j2) {
                    final int m = MathHelper.floor_double(this.posY) + j2;
                    final BlockPos blockpos = new BlockPos(l1, m, i3);
                    final Block block = this.worldObj.getBlockState(blockpos).getBlock();
                    if (block == Blocks.snow_layer) {
                        this.worldObj.setBlockToAir(blockpos);
                        this.isCollidedHorizontally = false;
                    }
                    else if (block == Blocks.waterlily) {
                        this.worldObj.destroyBlock(blockpos, true);
                        this.isCollidedHorizontally = false;
                    }
                }
            }
            if (this.onGround) {
                this.motionX *= 0.5;
                this.motionY *= 0.5;
                this.motionZ *= 0.5;
            }
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            if (this.isCollidedHorizontally && d4 > 0.2975) {
                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();
                    if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                        for (int j3 = 0; j3 < 3; ++j3) {
                            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0f);
                        }
                        for (int k2 = 0; k2 < 2; ++k2) {
                            this.dropItemWithOffset(Items.stick, 1, 0.0f);
                        }
                    }
                }
            }
            else {
                this.motionX *= 0.9900000095367432;
                this.motionY *= 0.949999988079071;
                this.motionZ *= 0.9900000095367432;
            }
            this.rotationPitch = 0.0f;
            double d23 = this.rotationYaw;
            final double d24 = this.prevPosX - this.posX;
            final double d25 = this.prevPosZ - this.posZ;
            if (d24 * d24 + d25 * d25 > 0.001) {
                d23 = (float)(MathHelper.func_181159_b(d25, d24) * 180.0 / 3.141592653589793);
            }
            double d26 = MathHelper.wrapAngleTo180_double(d23 - this.rotationYaw);
            if (d26 > 20.0) {
                d26 = 20.0;
            }
            if (d26 < -20.0) {
                d26 = -20.0;
            }
            this.setRotation(this.rotationYaw += (float)d26, this.rotationPitch);
            if (!this.worldObj.isRemote) {
                final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224, 0.0, 0.20000000298023224));
                if (list != null && !list.isEmpty()) {
                    for (int k3 = 0; k3 < list.size(); ++k3) {
                        final Entity entity = list.get(k3);
                        if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat) {
                            entity.applyEntityCollision(this);
                        }
                    }
                }
                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }
    
    @Override
    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            final double d0 = Math.cos(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            final double d2 = Math.sin(this.rotationYaw * 3.141592653589793 / 180.0) * 0.4;
            this.riddenByEntity.setPosition(this.posX + d0, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d2);
        }
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    public boolean interactFirst(final EntityPlayer playerIn) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
            return true;
        }
        if (!this.worldObj.isRemote) {
            playerIn.mountEntity(this);
        }
        return true;
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final Block blockIn, final BlockPos pos) {
        if (onGroundIn) {
            if (this.fallDistance > 3.0f) {
                this.fall(this.fallDistance, 1.0f);
                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();
                    if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
                        for (int i = 0; i < 3; ++i) {
                            this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0f);
                        }
                        for (int j = 0; j < 2; ++j) {
                            this.dropItemWithOffset(Items.stick, 1, 0.0f);
                        }
                    }
                }
                this.fallDistance = 0.0f;
            }
        }
        else if (this.worldObj.getBlockState(new BlockPos(this).down()).getBlock().getMaterial() != Material.water && y < 0.0) {
            this.fallDistance -= (float)y;
        }
    }
    
    public void setDamageTaken(final float p_70266_1_) {
        this.dataWatcher.updateObject(19, p_70266_1_);
    }
    
    public float getDamageTaken() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }
    
    public void setTimeSinceHit(final int p_70265_1_) {
        this.dataWatcher.updateObject(17, p_70265_1_);
    }
    
    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }
    
    public void setForwardDirection(final int p_70269_1_) {
        this.dataWatcher.updateObject(18, p_70269_1_);
    }
    
    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }
    
    public void setIsBoatEmpty(final boolean p_70270_1_) {
        this.isBoatEmpty = p_70270_1_;
    }
}
