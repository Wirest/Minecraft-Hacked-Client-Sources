// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class EntityEnderEye extends Entity
{
    private double targetX;
    private double targetY;
    private double targetZ;
    private int despawnTimer;
    private boolean shatterOrDrop;
    
    public EntityEnderEye(final World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0;
        if (Double.isNaN(d0)) {
            d0 = 4.0;
        }
        d0 *= 64.0;
        return distance < d0 * d0;
    }
    
    public EntityEnderEye(final World worldIn, final double x, final double y, final double z) {
        super(worldIn);
        this.despawnTimer = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(x, y, z);
    }
    
    public void moveTowards(final BlockPos p_180465_1_) {
        final double d0 = p_180465_1_.getX();
        final int i = p_180465_1_.getY();
        final double d2 = p_180465_1_.getZ();
        final double d3 = d0 - this.posX;
        final double d4 = d2 - this.posZ;
        final float f = MathHelper.sqrt_double(d3 * d3 + d4 * d4);
        if (f > 12.0f) {
            this.targetX = this.posX + d3 / f * 12.0;
            this.targetZ = this.posZ + d4 / f * 12.0;
            this.targetY = this.posY + 8.0;
        }
        else {
            this.targetX = d0;
            this.targetY = i;
            this.targetZ = d2;
        }
        this.despawnTimer = 0;
        this.shatterOrDrop = (this.rand.nextInt(5) > 0);
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
        }
    }
    
    @Override
    public void onUpdate() {
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        final float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(MathHelper.func_181159_b(this.motionX, this.motionZ) * 180.0 / 3.141592653589793);
        this.rotationPitch = (float)(MathHelper.func_181159_b(this.motionY, f) * 180.0 / 3.141592653589793);
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
        if (!this.worldObj.isRemote) {
            final double d0 = this.targetX - this.posX;
            final double d2 = this.targetZ - this.posZ;
            final float f2 = (float)Math.sqrt(d0 * d0 + d2 * d2);
            final float f3 = (float)MathHelper.func_181159_b(d2, d0);
            double d3 = f + (f2 - f) * 0.0025;
            if (f2 < 1.0f) {
                d3 *= 0.8;
                this.motionY *= 0.8;
            }
            this.motionX = Math.cos(f3) * d3;
            this.motionZ = Math.sin(f3) * d3;
            if (this.posY < this.targetY) {
                this.motionY += (1.0 - this.motionY) * 0.014999999664723873;
            }
            else {
                this.motionY += (-1.0 - this.motionY) * 0.014999999664723873;
            }
        }
        final float f4 = 0.25f;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ, new int[0]);
            }
        }
        else {
            this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX - this.motionX * f4 + this.rand.nextDouble() * 0.6 - 0.3, this.posY - this.motionY * f4 - 0.5, this.posZ - this.motionZ * f4 + this.rand.nextDouble() * 0.6 - 0.3, this.motionX, this.motionY, this.motionZ, new int[0]);
        }
        if (!this.worldObj.isRemote) {
            this.setPosition(this.posX, this.posY, this.posZ);
            ++this.despawnTimer;
            if (this.despawnTimer > 80 && !this.worldObj.isRemote) {
                this.setDead();
                if (this.shatterOrDrop) {
                    this.worldObj.spawnEntityInWorld(new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, new ItemStack(Items.ender_eye)));
                }
                else {
                    this.worldObj.playAuxSFX(2003, new BlockPos(this), 0);
                }
            }
        }
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        return 1.0f;
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        return 15728880;
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}
