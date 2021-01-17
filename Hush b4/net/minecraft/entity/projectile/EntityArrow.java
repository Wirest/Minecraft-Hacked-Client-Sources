// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import java.util.List;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.block.Block;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.Entity;

public class EntityArrow extends Entity implements IProjectile
{
    private int xTile;
    private int yTile;
    private int zTile;
    private Block inTile;
    private int inData;
    private boolean inGround;
    public int canBePickedUp;
    public int arrowShake;
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage;
    private int knockbackStrength;
    
    public EntityArrow(final World worldIn) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
    }
    
    public EntityArrow(final World worldIn, final double x, final double y, final double z) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.setSize(0.5f, 0.5f);
        this.setPosition(x, y, z);
    }
    
    public EntityArrow(final World worldIn, final EntityLivingBase shooter, final EntityLivingBase p_i1755_3_, final float p_i1755_4_, final float p_i1755_5_) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.shootingEntity = shooter;
        if (shooter instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }
        this.posY = shooter.posY + shooter.getEyeHeight() - 0.10000000149011612;
        final double d0 = p_i1755_3_.posX - shooter.posX;
        final double d2 = p_i1755_3_.getEntityBoundingBox().minY + p_i1755_3_.height / 3.0f - this.posY;
        final double d3 = p_i1755_3_.posZ - shooter.posZ;
        final double d4 = MathHelper.sqrt_double(d0 * d0 + d3 * d3);
        if (d4 >= 1.0E-7) {
            final float f = (float)(MathHelper.func_181159_b(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
            final float f2 = (float)(-(MathHelper.func_181159_b(d2, d4) * 180.0 / 3.141592653589793));
            final double d5 = d0 / d4;
            final double d6 = d3 / d4;
            this.setLocationAndAngles(shooter.posX + d5, this.posY, shooter.posZ + d6, f, f2);
            final float f3 = (float)(d4 * 0.20000000298023224);
            this.setThrowableHeading(d0, d2 + f3, d3, p_i1755_4_, p_i1755_5_);
        }
    }
    
    public EntityArrow(final World worldIn, final EntityLivingBase shooter, final float velocity) {
        super(worldIn);
        this.xTile = -1;
        this.yTile = -1;
        this.zTile = -1;
        this.damage = 2.0;
        this.renderDistanceWeight = 10.0;
        this.shootingEntity = shooter;
        if (shooter instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }
        this.setSize(0.5f, 0.5f);
        this.setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.posX -= MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.posY -= 0.10000000149011612;
        this.posZ -= MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * 0.16f;
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = -MathHelper.sin(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f);
        this.motionZ = MathHelper.cos(this.rotationYaw / 180.0f * 3.1415927f) * MathHelper.cos(this.rotationPitch / 180.0f * 3.1415927f);
        this.motionY = -MathHelper.sin(this.rotationPitch / 180.0f * 3.1415927f);
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5f, 1.0f);
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObject(16, (Byte)0);
    }
    
    @Override
    public void setThrowableHeading(double x, double y, double z, final float velocity, final float inaccuracy) {
        final float f = MathHelper.sqrt_double(x * x + y * y + z * z);
        x /= f;
        y /= f;
        z /= f;
        x += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * inaccuracy;
        y += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * inaccuracy;
        z += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937 * inaccuracy;
        x *= velocity;
        y *= velocity;
        z *= velocity;
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        final float f2 = MathHelper.sqrt_double(x * x + z * z);
        final float n = (float)(MathHelper.func_181159_b(x, z) * 180.0 / 3.141592653589793);
        this.rotationYaw = n;
        this.prevRotationYaw = n;
        final float n2 = (float)(MathHelper.func_181159_b(y, f2) * 180.0 / 3.141592653589793);
        this.rotationPitch = n2;
        this.prevRotationPitch = n2;
        this.ticksInGround = 0;
    }
    
    @Override
    public void setPositionAndRotation2(final double x, final double y, final double z, final float yaw, final float pitch, final int posRotationIncrements, final boolean p_180426_10_) {
        this.setPosition(x, y, z);
        this.setRotation(yaw, pitch);
    }
    
    @Override
    public void setVelocity(final double x, final double y, final double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt_double(x * x + z * z);
            final float n = (float)(MathHelper.func_181159_b(x, z) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float)(MathHelper.func_181159_b(y, f) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.prevRotationPitch == 0.0f && this.prevRotationYaw == 0.0f) {
            final float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            final float n = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
            this.rotationYaw = n;
            this.prevRotationYaw = n;
            final float n2 = (float)(MathHelper.func_181159_b(this.motionY, f) * 180.0 / 3.141592653589793);
            this.rotationPitch = n2;
            this.prevRotationPitch = n2;
        }
        final BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
        final IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
        final Block block = iblockstate.getBlock();
        if (block.getMaterial() != Material.air) {
            block.setBlockBoundsBasedOnState(this.worldObj, blockpos);
            final AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);
            if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }
        if (this.arrowShake > 0) {
            --this.arrowShake;
        }
        if (this.inGround) {
            final int j = block.getMetaFromState(iblockstate);
            if (block == this.inTile && j == this.inData) {
                ++this.ticksInGround;
                if (this.ticksInGround >= 1200) {
                    this.setDead();
                }
            }
            else {
                this.inGround = false;
                this.motionX *= this.rand.nextFloat() * 0.2f;
                this.motionY *= this.rand.nextFloat() * 0.2f;
                this.motionZ *= this.rand.nextFloat() * 0.2f;
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        }
        else {
            ++this.ticksInAir;
            Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 vec32 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec32, false, true, false);
            vec31 = new Vec3(this.posX, this.posY, this.posZ);
            vec32 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            if (movingobjectposition != null) {
                vec32 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }
            Entity entity = null;
            final List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0, 1.0, 1.0));
            double d0 = 0.0;
            for (int i = 0; i < list.size(); ++i) {
                final Entity entity2 = list.get(i);
                if (entity2.canBeCollidedWith() && (entity2 != this.shootingEntity || this.ticksInAir >= 5)) {
                    final float f2 = 0.3f;
                    final AxisAlignedBB axisalignedbb2 = entity2.getEntityBoundingBox().expand(f2, f2, f2);
                    final MovingObjectPosition movingobjectposition2 = axisalignedbb2.calculateIntercept(vec31, vec32);
                    if (movingobjectposition2 != null) {
                        final double d2 = vec31.squareDistanceTo(movingobjectposition2.hitVec);
                        if (d2 < d0 || d0 == 0.0) {
                            entity = entity2;
                            d0 = d2;
                        }
                    }
                }
            }
            if (entity != null) {
                movingobjectposition = new MovingObjectPosition(entity);
            }
            if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
                final EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;
                if (entityplayer.capabilities.disableDamage || (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer))) {
                    movingobjectposition = null;
                }
            }
            if (movingobjectposition != null) {
                if (movingobjectposition.entityHit != null) {
                    final float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int l = MathHelper.ceiling_double_int(f3 * this.damage);
                    if (this.getIsCritical()) {
                        l += this.rand.nextInt(l / 2 + 2);
                    }
                    DamageSource damagesource;
                    if (this.shootingEntity == null) {
                        damagesource = DamageSource.causeArrowDamage(this, this);
                    }
                    else {
                        damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
                    }
                    if (this.isBurning() && !(movingobjectposition.entityHit instanceof EntityEnderman)) {
                        movingobjectposition.entityHit.setFire(5);
                    }
                    if (movingobjectposition.entityHit.attackEntityFrom(damagesource, (float)l)) {
                        if (movingobjectposition.entityHit instanceof EntityLivingBase) {
                            final EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;
                            if (!this.worldObj.isRemote) {
                                entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
                            }
                            if (this.knockbackStrength > 0) {
                                final float f4 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
                                if (f4 > 0.0f) {
                                    movingobjectposition.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579 / f4, 0.1, this.motionZ * this.knockbackStrength * 0.6000000238418579 / f4);
                                }
                            }
                            if (this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
                                EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, entitylivingbase);
                            }
                            if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0f));
                            }
                        }
                        this.playSound("random.bowhit", 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                        if (!(movingobjectposition.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    }
                    else {
                        this.motionX *= -0.10000000149011612;
                        this.motionY *= -0.10000000149011612;
                        this.motionZ *= -0.10000000149011612;
                        this.rotationYaw += 180.0f;
                        this.prevRotationYaw += 180.0f;
                        this.ticksInAir = 0;
                    }
                }
                else {
                    final BlockPos blockpos2 = movingobjectposition.getBlockPos();
                    this.xTile = blockpos2.getX();
                    this.yTile = blockpos2.getY();
                    this.zTile = blockpos2.getZ();
                    final IBlockState iblockstate2 = this.worldObj.getBlockState(blockpos2);
                    this.inTile = iblockstate2.getBlock();
                    this.inData = this.inTile.getMetaFromState(iblockstate2);
                    this.motionX = (float)(movingobjectposition.hitVec.xCoord - this.posX);
                    this.motionY = (float)(movingobjectposition.hitVec.yCoord - this.posY);
                    this.motionZ = (float)(movingobjectposition.hitVec.zCoord - this.posZ);
                    final float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / f5 * 0.05000000074505806;
                    this.posY -= this.motionY / f5 * 0.05000000074505806;
                    this.posZ -= this.motionZ / f5 * 0.05000000074505806;
                    this.playSound("random.bowhit", 1.0f, 1.2f / (this.rand.nextFloat() * 0.2f + 0.9f));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);
                    if (this.inTile.getMaterial() != Material.air) {
                        this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos2, iblockstate2, this);
                    }
                }
            }
            if (this.getIsCritical()) {
                for (int k = 0; k < 4; ++k) {
                    this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * k / 4.0, this.posY + this.motionY * k / 4.0, this.posZ + this.motionZ * k / 4.0, -this.motionX, -this.motionY + 0.2, -this.motionZ, new int[0]);
                }
            }
            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            final float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
            this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f6) * 180.0 / 3.141592653589793);
            while (this.rotationPitch - this.prevRotationPitch < -180.0f) {
                this.prevRotationPitch -= 360.0f;
            }
            while (this.rotationPitch - this.prevRotationPitch >= 180.0f) {
                this.prevRotationPitch += 360.0f;
            }
            while (this.rotationYaw - this.prevRotationYaw < -180.0f) {
                this.prevRotationYaw -= 360.0f;
            }
            while (this.rotationYaw - this.prevRotationYaw >= 180.0f) {
                this.prevRotationYaw += 360.0f;
            }
            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2f;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2f;
            float f7 = 0.99f;
            final float f8 = 0.05f;
            if (this.isInWater()) {
                for (int i2 = 0; i2 < 4; ++i2) {
                    final float f9 = 0.25f;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f9, this.posY - this.motionY * f9, this.posZ - this.motionZ * f9, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
                f7 = 0.6f;
            }
            if (this.isWet()) {
                this.extinguish();
            }
            this.motionX *= f7;
            this.motionY *= f7;
            this.motionZ *= f7;
            this.motionY -= f8;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short)this.xTile);
        tagCompound.setShort("yTile", (short)this.yTile);
        tagCompound.setShort("zTile", (short)this.zTile);
        tagCompound.setShort("life", (short)this.ticksInGround);
        final ResourceLocation resourcelocation = Block.blockRegistry.getNameForObject(this.inTile);
        tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
        tagCompound.setByte("inData", (byte)this.inData);
        tagCompound.setByte("shake", (byte)this.arrowShake);
        tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte)this.canBePickedUp);
        tagCompound.setDouble("damage", this.damage);
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.xTile = tagCompund.getShort("xTile");
        this.yTile = tagCompund.getShort("yTile");
        this.zTile = tagCompund.getShort("zTile");
        this.ticksInGround = tagCompund.getShort("life");
        if (tagCompund.hasKey("inTile", 8)) {
            this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
        }
        else {
            this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
        }
        this.inData = (tagCompund.getByte("inData") & 0xFF);
        this.arrowShake = (tagCompund.getByte("shake") & 0xFF);
        this.inGround = (tagCompund.getByte("inGround") == 1);
        if (tagCompund.hasKey("damage", 99)) {
            this.damage = tagCompund.getDouble("damage");
        }
        if (tagCompund.hasKey("pickup", 99)) {
            this.canBePickedUp = tagCompund.getByte("pickup");
        }
        else if (tagCompund.hasKey("player", 99)) {
            this.canBePickedUp = (tagCompund.getBoolean("player") ? 1 : 0);
        }
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean flag = this.canBePickedUp == 1 || (this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode);
            if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1))) {
                flag = false;
            }
            if (flag) {
                this.playSound("random.pop", 0.2f, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.0f) * 2.0f);
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    public void setDamage(final double damageIn) {
        this.damage = damageIn;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    public void setKnockbackStrength(final int knockbackStrengthIn) {
        this.knockbackStrength = knockbackStrengthIn;
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.0f;
    }
    
    public void setIsCritical(final boolean critical) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (critical) {
            this.dataWatcher.updateObject(16, (byte)(b0 | 0x1));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(b0 & 0xFFFFFFFE));
        }
    }
    
    public boolean getIsCritical() {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        return (b0 & 0x1) != 0x0;
    }
}
