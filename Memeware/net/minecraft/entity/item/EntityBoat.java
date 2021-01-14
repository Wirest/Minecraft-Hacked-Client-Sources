package net.minecraft.entity.item;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityBoat extends Entity {
    /**
     * true if no player in boat
     */
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
    private static final String __OBFID = "CL_00001667";

    public EntityBoat(World worldIn) {
        super(worldIn);
        this.isBoatEmpty = true;
        this.speedMultiplier = 0.07D;
        this.preventEntitySpawning = true;
        this.setSize(1.5F, 0.6F);
    }

    /**
     * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
     * prevent them from trampling crops
     */
    protected boolean canTriggerWalking() {
        return false;
    }

    protected void entityInit() {
        this.dataWatcher.addObject(17, new Integer(0));
        this.dataWatcher.addObject(18, new Integer(1));
        this.dataWatcher.addObject(19, new Float(0.0F));
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be
     * pushable on contact, like boats or minecarts.
     */
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return entityIn.getEntityBoundingBox();
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox() {
        return this.getEntityBoundingBox();
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed() {
        return true;
    }

    public EntityBoat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
        this(worldIn);
        this.setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
        this.prevPosX = p_i1705_2_;
        this.prevPosY = p_i1705_4_;
        this.prevPosZ = p_i1705_6_;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset() {
        return (double) this.height * 0.0D - 0.30000001192092896D;
    }

    /**
     * Called when the entity is attacked.
     */
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.func_180431_b(source)) {
            return false;
        } else if (!this.worldObj.isRemote && !this.isDead) {
            if (this.riddenByEntity != null && this.riddenByEntity == source.getEntity() && source instanceof EntityDamageSourceIndirect) {
                return false;
            } else {
                this.setForwardDirection(-this.getForwardDirection());
                this.setTimeSinceHit(10);
                this.setDamageTaken(this.getDamageTaken() + amount * 10.0F);
                this.setBeenAttacked();
                boolean var3 = source.getEntity() instanceof EntityPlayer && ((EntityPlayer) source.getEntity()).capabilities.isCreativeMode;

                if (var3 || this.getDamageTaken() > 40.0F) {
                    if (this.riddenByEntity != null) {
                        this.riddenByEntity.mountEntity(this);
                    }

                    if (!var3) {
                        this.dropItemWithOffset(Items.boat, 1, 0.0F);
                    }

                    this.setDead();
                }

                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * Setups the entity to do the hurt animation. Only used by packets in multiplayer.
     */
    public void performHurtAnimation() {
        this.setForwardDirection(-this.getForwardDirection());
        this.setTimeSinceHit(10);
        this.setDamageTaken(this.getDamageTaken() * 11.0F);
    }

    /**
     * Returns true if other Entities should be prevented from moving through this Entity.
     */
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }

    public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_) {
        if (p_180426_10_ && this.riddenByEntity != null) {
            this.prevPosX = this.posX = p_180426_1_;
            this.prevPosY = this.posY = p_180426_3_;
            this.prevPosZ = this.posZ = p_180426_5_;
            this.rotationYaw = p_180426_7_;
            this.rotationPitch = p_180426_8_;
            this.boatPosRotationIncrements = 0;
            this.setPosition(p_180426_1_, p_180426_3_, p_180426_5_);
            this.motionX = this.velocityX = 0.0D;
            this.motionY = this.velocityY = 0.0D;
            this.motionZ = this.velocityZ = 0.0D;
        } else {
            if (this.isBoatEmpty) {
                this.boatPosRotationIncrements = p_180426_9_ + 5;
            } else {
                double var11 = p_180426_1_ - this.posX;
                double var13 = p_180426_3_ - this.posY;
                double var15 = p_180426_5_ - this.posZ;
                double var17 = var11 * var11 + var13 * var13 + var15 * var15;

                if (var17 <= 1.0D) {
                    return;
                }

                this.boatPosRotationIncrements = 3;
            }

            this.boatX = p_180426_1_;
            this.boatY = p_180426_3_;
            this.boatZ = p_180426_5_;
            this.boatYaw = (double) p_180426_7_;
            this.boatPitch = (double) p_180426_8_;
            this.motionX = this.velocityX;
            this.motionY = this.velocityY;
            this.motionZ = this.velocityZ;
        }
    }

    /**
     * Sets the velocity to the args. Args: x, y, z
     */
    public void setVelocity(double x, double y, double z) {
        this.velocityX = this.motionX = x;
        this.velocityY = this.motionY = y;
        this.velocityZ = this.motionZ = z;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        super.onUpdate();

        if (this.getTimeSinceHit() > 0) {
            this.setTimeSinceHit(this.getTimeSinceHit() - 1);
        }

        if (this.getDamageTaken() > 0.0F) {
            this.setDamageTaken(this.getDamageTaken() - 1.0F);
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        byte var1 = 5;
        double var2 = 0.0D;

        for (int var4 = 0; var4 < var1; ++var4) {
            double var5 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double) (var4 + 0) / (double) var1 - 0.125D;
            double var7 = this.getEntityBoundingBox().minY + (this.getEntityBoundingBox().maxY - this.getEntityBoundingBox().minY) * (double) (var4 + 1) / (double) var1 - 0.125D;
            AxisAlignedBB var9 = new AxisAlignedBB(this.getEntityBoundingBox().minX, var5, this.getEntityBoundingBox().minZ, this.getEntityBoundingBox().maxX, var7, this.getEntityBoundingBox().maxZ);

            if (this.worldObj.isAABBInMaterial(var9, Material.water)) {
                var2 += 1.0D / (double) var1;
            }
        }

        double var19 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        double var6;
        double var8;
        int var10;

        if (var19 > 0.2975D) {
            var6 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D);
            var8 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D);

            for (var10 = 0; (double) var10 < 1.0D + var19 * 60.0D; ++var10) {
                double var11 = (double) (this.rand.nextFloat() * 2.0F - 1.0F);
                double var13 = (double) (this.rand.nextInt(2) * 2 - 1) * 0.7D;
                double var15;
                double var17;

                if (this.rand.nextBoolean()) {
                    var15 = this.posX - var6 * var11 * 0.8D + var8 * var13;
                    var17 = this.posZ - var8 * var11 * 0.8D - var6 * var13;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var15, this.posY - 0.125D, var17, this.motionX, this.motionY, this.motionZ, new int[0]);
                } else {
                    var15 = this.posX + var6 + var8 * var11 * 0.7D;
                    var17 = this.posZ + var8 - var6 * var11 * 0.7D;
                    this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, var15, this.posY - 0.125D, var17, this.motionX, this.motionY, this.motionZ, new int[0]);
                }
            }
        }

        double var24;
        double var26;

        if (this.worldObj.isRemote && this.isBoatEmpty) {
            if (this.boatPosRotationIncrements > 0) {
                var6 = this.posX + (this.boatX - this.posX) / (double) this.boatPosRotationIncrements;
                var8 = this.posY + (this.boatY - this.posY) / (double) this.boatPosRotationIncrements;
                var24 = this.posZ + (this.boatZ - this.posZ) / (double) this.boatPosRotationIncrements;
                var26 = MathHelper.wrapAngleTo180_double(this.boatYaw - (double) this.rotationYaw);
                this.rotationYaw = (float) ((double) this.rotationYaw + var26 / (double) this.boatPosRotationIncrements);
                this.rotationPitch = (float) ((double) this.rotationPitch + (this.boatPitch - (double) this.rotationPitch) / (double) this.boatPosRotationIncrements);
                --this.boatPosRotationIncrements;
                this.setPosition(var6, var8, var24);
                this.setRotation(this.rotationYaw, this.rotationPitch);
            } else {
                var6 = this.posX + this.motionX;
                var8 = this.posY + this.motionY;
                var24 = this.posZ + this.motionZ;
                this.setPosition(var6, var8, var24);

                if (this.onGround) {
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;
                }

                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }
        } else {
            if (var2 < 1.0D) {
                var6 = var2 * 2.0D - 1.0D;
                this.motionY += 0.03999999910593033D * var6;
            } else {
                if (this.motionY < 0.0D) {
                    this.motionY /= 2.0D;
                }

                this.motionY += 0.007000000216066837D;
            }

            if (this.riddenByEntity instanceof EntityLivingBase) {
                EntityLivingBase var20 = (EntityLivingBase) this.riddenByEntity;
                float var21 = this.riddenByEntity.rotationYaw + -var20.moveStrafing * 90.0F;
                this.motionX += -Math.sin((double) (var21 * (float) Math.PI / 180.0F)) * this.speedMultiplier * (double) var20.moveForward * 0.05000000074505806D;
                this.motionZ += Math.cos((double) (var21 * (float) Math.PI / 180.0F)) * this.speedMultiplier * (double) var20.moveForward * 0.05000000074505806D;
            }

            var6 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);

            if (var6 > 0.35D) {
                var8 = 0.35D / var6;
                this.motionX *= var8;
                this.motionZ *= var8;
                var6 = 0.35D;
            }

            if (var6 > var19 && this.speedMultiplier < 0.35D) {
                this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;

                if (this.speedMultiplier > 0.35D) {
                    this.speedMultiplier = 0.35D;
                }
            } else {
                this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;

                if (this.speedMultiplier < 0.07D) {
                    this.speedMultiplier = 0.07D;
                }
            }

            int var22;

            for (var22 = 0; var22 < 4; ++var22) {
                int var23 = MathHelper.floor_double(this.posX + ((double) (var22 % 2) - 0.5D) * 0.8D);
                var10 = MathHelper.floor_double(this.posZ + ((double) (var22 / 2) - 0.5D) * 0.8D);

                for (int var25 = 0; var25 < 2; ++var25) {
                    int var12 = MathHelper.floor_double(this.posY) + var25;
                    BlockPos var27 = new BlockPos(var23, var12, var10);
                    Block var14 = this.worldObj.getBlockState(var27).getBlock();

                    if (var14 == Blocks.snow_layer) {
                        this.worldObj.setBlockToAir(var27);
                        this.isCollidedHorizontally = false;
                    } else if (var14 == Blocks.waterlily) {
                        this.worldObj.destroyBlock(var27, true);
                        this.isCollidedHorizontally = false;
                    }
                }
            }

            if (this.onGround) {
                this.motionX *= 0.5D;
                this.motionY *= 0.5D;
                this.motionZ *= 0.5D;
            }

            this.moveEntity(this.motionX, this.motionY, this.motionZ);

            if (this.isCollidedHorizontally && var19 > 0.2D) {
                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();

                    for (var22 = 0; var22 < 3; ++var22) {
                        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
                    }

                    for (var22 = 0; var22 < 2; ++var22) {
                        this.dropItemWithOffset(Items.stick, 1, 0.0F);
                    }
                }
            } else {
                this.motionX *= 0.9900000095367432D;
                this.motionY *= 0.949999988079071D;
                this.motionZ *= 0.9900000095367432D;
            }

            this.rotationPitch = 0.0F;
            var8 = (double) this.rotationYaw;
            var24 = this.prevPosX - this.posX;
            var26 = this.prevPosZ - this.posZ;

            if (var24 * var24 + var26 * var26 > 0.001D) {
                var8 = (double) ((float) (Math.atan2(var26, var24) * 180.0D / Math.PI));
            }

            double var28 = MathHelper.wrapAngleTo180_double(var8 - (double) this.rotationYaw);

            if (var28 > 20.0D) {
                var28 = 20.0D;
            }

            if (var28 < -20.0D) {
                var28 = -20.0D;
            }

            this.rotationYaw = (float) ((double) this.rotationYaw + var28);
            this.setRotation(this.rotationYaw, this.rotationPitch);

            if (!this.worldObj.isRemote) {
                List var16 = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));

                if (var16 != null && !var16.isEmpty()) {
                    for (int var29 = 0; var29 < var16.size(); ++var29) {
                        Entity var18 = (Entity) var16.get(var29);

                        if (var18 != this.riddenByEntity && var18.canBePushed() && var18 instanceof EntityBoat) {
                            var18.applyEntityCollision(this);
                        }
                    }
                }

                if (this.riddenByEntity != null && this.riddenByEntity.isDead) {
                    this.riddenByEntity = null;
                }
            }
        }
    }

    public void updateRiderPosition() {
        if (this.riddenByEntity != null) {
            double var1 = Math.cos((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            double var3 = Math.sin((double) this.rotationYaw * Math.PI / 180.0D) * 0.4D;
            this.riddenByEntity.setPosition(this.posX + var1, this.posY + this.getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + var3);
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {
    }

    /**
     * First layer of player interaction
     */
    public boolean interactFirst(EntityPlayer playerIn) {
        if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn) {
            return true;
        } else {
            if (!this.worldObj.isRemote) {
                playerIn.mountEntity(this);
            }

            return true;
        }
    }

    protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) {
        if (p_180433_3_) {
            if (this.fallDistance > 3.0F) {
                this.fall(this.fallDistance, 1.0F);

                if (!this.worldObj.isRemote && !this.isDead) {
                    this.setDead();
                    int var6;

                    for (var6 = 0; var6 < 3; ++var6) {
                        this.dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
                    }

                    for (var6 = 0; var6 < 2; ++var6) {
                        this.dropItemWithOffset(Items.stick, 1, 0.0F);
                    }
                }

                this.fallDistance = 0.0F;
            }
        } else if (this.worldObj.getBlockState((new BlockPos(this)).offsetDown()).getBlock().getMaterial() != Material.water && p_180433_1_ < 0.0D) {
            this.fallDistance = (float) ((double) this.fallDistance - p_180433_1_);
        }
    }

    /**
     * Sets the damage taken from the last hit.
     */
    public void setDamageTaken(float p_70266_1_) {
        this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
    }

    /**
     * Gets the damage taken from the last hit.
     */
    public float getDamageTaken() {
        return this.dataWatcher.getWatchableObjectFloat(19);
    }

    /**
     * Sets the time to count down from since the last time entity was hit.
     */
    public void setTimeSinceHit(int p_70265_1_) {
        this.dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
    }

    /**
     * Gets the time since the last hit.
     */
    public int getTimeSinceHit() {
        return this.dataWatcher.getWatchableObjectInt(17);
    }

    /**
     * Sets the forward direction of the entity.
     */
    public void setForwardDirection(int p_70269_1_) {
        this.dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
    }

    /**
     * Gets the forward direction of the entity.
     */
    public int getForwardDirection() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    /**
     * true if no player in boat
     */
    public void setIsBoatEmpty(boolean p_70270_1_) {
        this.isBoatEmpty = p_70270_1_;
    }
}
