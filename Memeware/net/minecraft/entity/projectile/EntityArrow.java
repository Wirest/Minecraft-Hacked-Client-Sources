package net.minecraft.entity.projectile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityArrow extends Entity implements IProjectile {
    private int field_145791_d = -1;
    private int field_145792_e = -1;
    private int field_145789_f = -1;
    private Block field_145790_g;
    private int inData;
    private boolean inGround;

    /**
     * 1 if the player can pick up the arrow
     */
    public int canBePickedUp;

    /**
     * Seems to be some sort of timer for animating an arrow.
     */
    public int arrowShake;

    /**
     * The owner of this arrow.
     */
    public Entity shootingEntity;
    private int ticksInGround;
    private int ticksInAir;
    private double damage = 2.0D;

    /**
     * The amount of knockback an arrow applies when it hits a mob.
     */
    private int knockbackStrength;
    private static final String __OBFID = "CL_00001715";

    public EntityArrow(World worldIn) {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
    }

    public EntityArrow(World worldIn, double p_i1754_2_, double p_i1754_4_, double p_i1754_6_) {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.setSize(0.5F, 0.5F);
        this.setPosition(p_i1754_2_, p_i1754_4_, p_i1754_6_);
    }

    public EntityArrow(World worldIn, EntityLivingBase p_i1755_2_, EntityLivingBase p_i1755_3_, float p_i1755_4_, float p_i1755_5_) {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = p_i1755_2_;

        if (p_i1755_2_ instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }

        this.posY = p_i1755_2_.posY + (double) p_i1755_2_.getEyeHeight() - 0.10000000149011612D;
        double var6 = p_i1755_3_.posX - p_i1755_2_.posX;
        double var8 = p_i1755_3_.getEntityBoundingBox().minY + (double) (p_i1755_3_.height / 3.0F) - this.posY;
        double var10 = p_i1755_3_.posZ - p_i1755_2_.posZ;
        double var12 = (double) MathHelper.sqrt_double(var6 * var6 + var10 * var10);

        if (var12 >= 1.0E-7D) {
            float var14 = (float) (Math.atan2(var10, var6) * 180.0D / Math.PI) - 90.0F;
            float var15 = (float) (-(Math.atan2(var8, var12) * 180.0D / Math.PI));
            double var16 = var6 / var12;
            double var18 = var10 / var12;
            this.setLocationAndAngles(p_i1755_2_.posX + var16, this.posY, p_i1755_2_.posZ + var18, var14, var15);
            float var20 = (float) (var12 * 0.20000000298023224D);
            this.setThrowableHeading(var6, var8 + (double) var20, var10, p_i1755_4_, p_i1755_5_);
        }
    }

    public EntityArrow(World worldIn, EntityLivingBase p_i1756_2_, float p_i1756_3_) {
        super(worldIn);
        this.renderDistanceWeight = 10.0D;
        this.shootingEntity = p_i1756_2_;

        if (p_i1756_2_ instanceof EntityPlayer) {
            this.canBePickedUp = 1;
        }

        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(p_i1756_2_.posX, p_i1756_2_.posY + (double) p_i1756_2_.getEyeHeight(), p_i1756_2_.posZ, p_i1756_2_.rotationYaw, p_i1756_2_.rotationPitch);
        this.posX -= (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.posY -= 0.10000000149011612D;
        this.posZ -= (double) (MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.motionX = (double) (-MathHelper.sin(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionZ = (double) (MathHelper.cos(this.rotationYaw / 180.0F * (float) Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float) Math.PI));
        this.motionY = (double) (-MathHelper.sin(this.rotationPitch / 180.0F * (float) Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, p_i1756_3_ * 1.5F, 1.0F);
    }

    protected void entityInit() {
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    /**
     * Similar to setArrowHeading, it's point the throwable entity to a x, y, z direction.
     */
    public void setThrowableHeading(double p_70186_1_, double p_70186_3_, double p_70186_5_, float p_70186_7_, float p_70186_8_) {
        float var9 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_3_ * p_70186_3_ + p_70186_5_ * p_70186_5_);
        p_70186_1_ /= (double) var9;
        p_70186_3_ /= (double) var9;
        p_70186_5_ /= (double) var9;
        p_70186_1_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) p_70186_8_;
        p_70186_3_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) p_70186_8_;
        p_70186_5_ += this.rand.nextGaussian() * (double) (this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double) p_70186_8_;
        p_70186_1_ *= (double) p_70186_7_;
        p_70186_3_ *= (double) p_70186_7_;
        p_70186_5_ *= (double) p_70186_7_;
        this.motionX = p_70186_1_;
        this.motionY = p_70186_3_;
        this.motionZ = p_70186_5_;
        float var10 = MathHelper.sqrt_double(p_70186_1_ * p_70186_1_ + p_70186_5_ * p_70186_5_);
        this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(p_70186_1_, p_70186_5_) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(p_70186_3_, (double) var10) * 180.0D / Math.PI);
        this.ticksInGround = 0;
    }

    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
        this.setRotation(p_180426_7_, p_180426_8_);
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double x, double y, double z) {
        this.motionX = x;
        this.motionY = y;
        this.motionZ = z;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var7 = MathHelper.sqrt_double(x * x + z * z);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(x, z) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(y, (double) var7) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.ticksInGround = 0;
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.prevRotationYaw = this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var1) * 180.0D / Math.PI);
        }

        BlockPos var18 = new BlockPos(this.field_145791_d, this.field_145792_e, this.field_145789_f);
        IBlockState var2 = this.worldObj.getBlockState(var18);
        Block var3 = var2.getBlock();

        if (var3.getMaterial() != Material.air) {
            var3.setBlockBoundsBasedOnState(this.worldObj, var18);
            AxisAlignedBB var4 = var3.getCollisionBoundingBox(this.worldObj, var18, var2);

            if (var4 != null && var4.isVecInside(new Vec3(this.posX, this.posY, this.posZ))) {
                this.inGround = true;
            }
        }

        if (this.arrowShake > 0) {
            --this.arrowShake;
        }

        if (this.inGround) {
            int var20 = var3.getMetaFromState(var2);

            if (var3 == this.field_145790_g && var20 == this.inData) {
                ++this.ticksInGround;

                if (this.ticksInGround >= 1200) {
                    this.setDead();
                }
            } else {
                this.inGround = false;
                this.motionX *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionY *= (double) (this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double) (this.rand.nextFloat() * 0.2F);
                this.ticksInGround = 0;
                this.ticksInAir = 0;
            }
        } else {
            ++this.ticksInAir;
            Vec3 var19 = new Vec3(this.posX, this.posY, this.posZ);
            Vec3 var5 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition var6 = this.worldObj.rayTraceBlocks(var19, var5, false, true, false);
            var19 = new Vec3(this.posX, this.posY, this.posZ);
            var5 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (var6 != null) {
                var5 = new Vec3(var6.hitVec.xCoord, var6.hitVec.yCoord, var6.hitVec.zCoord);
            }

            Entity var7 = null;
            List var8 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double var9 = 0.0D;
            int var11;
            float var13;

            for (var11 = 0; var11 < var8.size(); ++var11) {
                Entity var12 = (Entity) var8.get(var11);

                if (var12.canBeCollidedWith() && (var12 != this.shootingEntity || this.ticksInAir >= 5)) {
                    var13 = 0.3F;
                    AxisAlignedBB var14 = var12.getEntityBoundingBox().expand((double) var13, (double) var13, (double) var13);
                    MovingObjectPosition var15 = var14.calculateIntercept(var19, var5);

                    if (var15 != null) {
                        double var16 = var19.distanceTo(var15.hitVec);

                        if (var16 < var9 || var9 == 0.0D) {
                            var7 = var12;
                            var9 = var16;
                        }
                    }
                }
            }

            if (var7 != null) {
                var6 = new MovingObjectPosition(var7);
            }

            if (var6 != null && var6.entityHit != null && var6.entityHit instanceof EntityPlayer) {
                EntityPlayer var21 = (EntityPlayer) var6.entityHit;

                if (var21.capabilities.disableDamage || this.shootingEntity instanceof EntityPlayer && !((EntityPlayer) this.shootingEntity).canAttackPlayer(var21)) {
                    var6 = null;
                }
            }

            float var22;
            float var25;
            float var29;

            if (var6 != null) {
                if (var6.entityHit != null) {
                    var22 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    int var24 = MathHelper.ceiling_double_int((double) var22 * this.damage);

                    if (this.getIsCritical()) {
                        var24 += this.rand.nextInt(var24 / 2 + 2);
                    }

                    DamageSource var26;

                    if (this.shootingEntity == null) {
                        var26 = DamageSource.causeArrowDamage(this, this);
                    } else {
                        var26 = DamageSource.causeArrowDamage(this, this.shootingEntity);
                    }

                    if (this.isBurning() && !(var6.entityHit instanceof EntityEnderman)) {
                        var6.entityHit.setFire(5);
                    }

                    if (var6.entityHit.attackEntityFrom(var26, (float) var24)) {
                        if (var6.entityHit instanceof EntityLivingBase) {
                            EntityLivingBase var27 = (EntityLivingBase) var6.entityHit;

                            if (!this.worldObj.isRemote) {
                                var27.setArrowCountInEntity(var27.getArrowCountInEntity() + 1);
                            }

                            if (this.knockbackStrength > 0) {
                                var29 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);

                                if (var29 > 0.0F) {
                                    var6.entityHit.addVelocity(this.motionX * (double) this.knockbackStrength * 0.6000000238418579D / (double) var29, 0.1D, this.motionZ * (double) this.knockbackStrength * 0.6000000238418579D / (double) var29);
                                }
                            }

                            if (this.shootingEntity instanceof EntityLivingBase) {
                                EnchantmentHelper.func_151384_a(var27, this.shootingEntity);
                                EnchantmentHelper.func_151385_b((EntityLivingBase) this.shootingEntity, var27);
                            }

                            if (this.shootingEntity != null && var6.entityHit != this.shootingEntity && var6.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP) {
                                ((EntityPlayerMP) this.shootingEntity).playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(6, 0.0F));
                            }
                        }

                        this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

                        if (!(var6.entityHit instanceof EntityEnderman)) {
                            this.setDead();
                        }
                    } else {
                        this.motionX *= -0.10000000149011612D;
                        this.motionY *= -0.10000000149011612D;
                        this.motionZ *= -0.10000000149011612D;
                        this.rotationYaw += 180.0F;
                        this.prevRotationYaw += 180.0F;
                        this.ticksInAir = 0;
                    }
                } else {
                    BlockPos var23 = var6.func_178782_a();
                    this.field_145791_d = var23.getX();
                    this.field_145792_e = var23.getY();
                    this.field_145789_f = var23.getZ();
                    var2 = this.worldObj.getBlockState(var23);
                    this.field_145790_g = var2.getBlock();
                    this.inData = this.field_145790_g.getMetaFromState(var2);
                    this.motionX = (double) ((float) (var6.hitVec.xCoord - this.posX));
                    this.motionY = (double) ((float) (var6.hitVec.yCoord - this.posY));
                    this.motionZ = (double) ((float) (var6.hitVec.zCoord - this.posZ));
                    var25 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double) var25 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double) var25 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double) var25 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inGround = true;
                    this.arrowShake = 7;
                    this.setIsCritical(false);

                    if (this.field_145790_g.getMaterial() != Material.air) {
                        this.field_145790_g.onEntityCollidedWithBlock(this.worldObj, var23, var2, this);
                    }
                }
            }

            if (this.getIsCritical()) {
                for (var11 = 0; var11 < 4; ++var11) {
                    this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * (double) var11 / 4.0D, this.posY + this.motionY * (double) var11 / 4.0D, this.posZ + this.motionZ * (double) var11 / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            var22 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var22) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
                ;
            }

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            var25 = 0.99F;
            var13 = 0.05F;

            if (this.isInWater()) {
                for (int var28 = 0; var28 < 4; ++var28) {
                    var29 = 0.25F;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) var29, this.posY - this.motionY * (double) var29, this.posZ - this.motionZ * (double) var29, this.motionX, this.motionY, this.motionZ, new int[0]);
                }

                var25 = 0.6F;
            }

            if (this.isWet()) {
                this.extinguish();
            }

            this.motionX *= (double) var25;
            this.motionY *= (double) var25;
            this.motionZ *= (double) var25;
            this.motionY -= (double) var13;
            this.setPosition(this.posX, this.posY, this.posZ);
            this.doBlockCollisions();
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        tagCompound.setShort("xTile", (short) this.field_145791_d);
        tagCompound.setShort("yTile", (short) this.field_145792_e);
        tagCompound.setShort("zTile", (short) this.field_145789_f);
        tagCompound.setShort("life", (short) this.ticksInGround);
        ResourceLocation var2 = (ResourceLocation) Block.blockRegistry.getNameForObject(this.field_145790_g);
        tagCompound.setString("inTile", var2 == null ? "" : var2.toString());
        tagCompound.setByte("inData", (byte) this.inData);
        tagCompound.setByte("shake", (byte) this.arrowShake);
        tagCompound.setByte("inGround", (byte) (this.inGround ? 1 : 0));
        tagCompound.setByte("pickup", (byte) this.canBePickedUp);
        tagCompound.setDouble("damage", this.damage);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        this.field_145791_d = tagCompund.getShort("xTile");
        this.field_145792_e = tagCompund.getShort("yTile");
        this.field_145789_f = tagCompund.getShort("zTile");
        this.ticksInGround = tagCompund.getShort("life");

        if (tagCompund.hasKey("inTile", 8)) {
            this.field_145790_g = Block.getBlockFromName(tagCompund.getString("inTile"));
        } else {
            this.field_145790_g = Block.getBlockById(tagCompund.getByte("inTile") & 255);
        }

        this.inData = tagCompund.getByte("inData") & 255;
        this.arrowShake = tagCompund.getByte("shake") & 255;
        this.inGround = tagCompund.getByte("inGround") == 1;

        if (tagCompund.hasKey("damage", 99)) {
            this.damage = tagCompund.getDouble("damage");
        }

        if (tagCompund.hasKey("pickup", 99)) {
            this.canBePickedUp = tagCompund.getByte("pickup");
        } else if (tagCompund.hasKey("player", 99)) {
            this.canBePickedUp = tagCompund.getBoolean("player") ? 1 : 0;
        }
    }

    /**
     * Called by a player entity when they collide with an entity
     */
    public void onCollideWithPlayer(EntityPlayer entityIn) {
        if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
            boolean var2 = this.canBePickedUp == 1 || this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode;

            if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1))) {
                var2 = false;
            }

            if (var2) {
                this.playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                entityIn.onItemPickup(this, 1);
                this.setDead();
            }
        }
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    public void setDamage(double p_70239_1_) {
        this.damage = p_70239_1_;
    }

    public double getDamage() {
        return this.damage;
    }

    /**
     * Sets the amount of knockback the arrow applies when it hits a mob.
     */
    public void setKnockbackStrength(int p_70240_1_) {
        this.knockbackStrength = p_70240_1_;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem() {
        return false;
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public void setIsCritical(boolean p_70243_1_) {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (p_70243_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 | 1)));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) (var2 & -2)));
        }
    }

    /**
     * Whether the arrow has a stream of critical hit particles flying behind it.
     */
    public boolean getIsCritical() {
        byte var1 = this.dataWatcher.getWatchableObjectByte(16);
        return (var1 & 1) != 0;
    }
}
