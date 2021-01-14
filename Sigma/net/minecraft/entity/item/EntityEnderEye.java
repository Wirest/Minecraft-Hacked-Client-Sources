package net.minecraft.entity.item;

import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityEnderEye extends Entity {
    /**
     * 'x' location the eye should float towards.
     */
    private double targetX;

    /**
     * 'y' location the eye should float towards.
     */
    private double targetY;

    /**
     * 'z' location the eye should float towards.
     */
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;
    private static final String __OBFID = "CL_00001716";

    public EntityEnderEye(World worldIn) {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }

    protected void entityInit() {
    }

    /**
     * Checks if the entity is in range to render by using the past in distance and comparing it to its average edge
     * length * 64 * renderDistanceWeight Args: distance
     */
    public boolean isInRangeToRenderDist(double distance) {
        double var3 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
        var3 *= 64.0D;
        return distance < var3 * var3;
    }

    public EntityEnderEye(World worldIn, double p_i1758_2_, double p_i1758_4_, double p_i1758_6_) {
        super(worldIn);
        this.despawnTimer = 0;
        this.setSize(0.25F, 0.25F);
        this.setPosition(p_i1758_2_, p_i1758_4_, p_i1758_6_);
    }

    public void func_180465_a(BlockPos p_180465_1_) {
        double var2 = (double) p_180465_1_.getX();
        int var4 = p_180465_1_.getY();
        double var5 = (double) p_180465_1_.getZ();
        double var7 = var2 - this.posX;
        double var9 = var5 - this.posZ;
        float var11 = MathHelper.sqrt_double(var7 * var7 + var9 * var9);

        if (var11 > 12.0F) {
            this.targetX = this.posX + var7 / (double) var11 * 12.0D;
            this.targetZ = this.posZ + var9 / (double) var11 * 12.0D;
            this.targetY = this.posY + 8.0D;
        } else {
            this.targetX = var2;
            this.targetY = (double) var4;
            this.targetZ = var5;
        }

        this.despawnTimer = 0;
        this.shatterOrDrop = this.rand.nextInt(5) > 0;
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
        }
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        float var1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float) (Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float) (Math.atan2(this.motionY, (double) var1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
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

        if (!this.worldObj.isRemote) {
            double var2 = this.targetX - this.posX;
            double var4 = this.targetZ - this.posZ;
            float var6 = (float) Math.sqrt(var2 * var2 + var4 * var4);
            float var7 = (float) Math.atan2(var4, var2);
            double var8 = (double) var1 + (double) (var6 - var1) * 0.0025D;

            if (var6 < 1.0F) {
                var8 *= 0.8D;
                this.motionY *= 0.8D;
            }

            this.motionX = Math.cos((double) var7) * var8;
            this.motionZ = Math.sin((double) var7) * var8;

            if (this.posY < this.targetY) {
                this.motionY += (1.0D - this.motionY) * 0.014999999664723873D;
            } else {
                this.motionY += (-1.0D - this.motionY) * 0.014999999664723873D;
            }
        }

        float var10 = 0.25F;

        if (this.isInWater()) {
            for (int var3 = 0; var3 < 4; ++var3) {
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * (double) var10, this.posY - this.motionY * (double) var10, this.posZ - this.motionZ * (double) var10, this.motionX, this.motionY, this.motionZ, new int[0]);
            }
        } else {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * (double) var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.posY - this.motionY * (double) var10 - 0.5D, this.posZ - this.motionZ * (double) var10 + this.rand.nextDouble() * 0.6D - 0.3D, this.motionX, this.motionY, this.motionZ, new int[0]);
        }

        if (!this.worldObj.isRemote) {
            this.setPosition(this.posX, this.posY, this.posZ);
            ++this.despawnTimer;

            if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
                this.setDead();

                if (this.shatterOrDrop) {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
                } else {
                    this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
                }
            }
        }
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
    }

    /**
     * Gets how bright this entity is.
     */
    public float getBrightness(float p_70013_1_) {
        return 1.0F;
    }

    public int getBrightnessForRender(float p_70070_1_) {
        return 15728880;
    }

    /**
     * If returns false, the item will not inflict any damage against entities.
     */
    public boolean canAttackWithItem() {
        return false;
    }
}
