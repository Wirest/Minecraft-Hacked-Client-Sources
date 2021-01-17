// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class EntityFireworkRocket extends Entity
{
    private int fireworkAge;
    private int lifetime;
    
    public EntityFireworkRocket(final World worldIn) {
        super(worldIn);
        this.setSize(0.25f, 0.25f);
    }
    
    @Override
    protected void entityInit() {
        this.dataWatcher.addObjectByDataType(8, 5);
    }
    
    @Override
    public boolean isInRangeToRenderDist(final double distance) {
        return distance < 4096.0;
    }
    
    public EntityFireworkRocket(final World worldIn, final double x, final double y, final double z, final ItemStack givenItem) {
        super(worldIn);
        this.fireworkAge = 0;
        this.setSize(0.25f, 0.25f);
        this.setPosition(x, y, z);
        int i = 1;
        if (givenItem != null && givenItem.hasTagCompound()) {
            this.dataWatcher.updateObject(8, givenItem);
            final NBTTagCompound nbttagcompound = givenItem.getTagCompound();
            final NBTTagCompound nbttagcompound2 = nbttagcompound.getCompoundTag("Fireworks");
            if (nbttagcompound2 != null) {
                i += nbttagcompound2.getByte("Flight");
            }
        }
        this.motionX = this.rand.nextGaussian() * 0.001;
        this.motionZ = this.rand.nextGaussian() * 0.001;
        this.motionY = 0.05;
        this.lifetime = 10 * i + this.rand.nextInt(6) + this.rand.nextInt(7);
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
        this.motionX *= 1.15;
        this.motionZ *= 1.15;
        this.motionY += 0.04;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
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
        if (this.fireworkAge == 0 && !this.isSilent()) {
            this.worldObj.playSoundAtEntity(this, "fireworks.launch", 3.0f, 1.0f);
        }
        ++this.fireworkAge;
        if (this.worldObj.isRemote && this.fireworkAge % 2 < 2) {
            this.worldObj.spawnParticle(EnumParticleTypes.FIREWORKS_SPARK, this.posX, this.posY - 0.3, this.posZ, this.rand.nextGaussian() * 0.05, -this.motionY * 0.5, this.rand.nextGaussian() * 0.05, new int[0]);
        }
        if (!this.worldObj.isRemote && this.fireworkAge > this.lifetime) {
            this.worldObj.setEntityState(this, (byte)17);
            this.setDead();
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 17 && this.worldObj.isRemote) {
            final ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
            NBTTagCompound nbttagcompound = null;
            if (itemstack != null && itemstack.hasTagCompound()) {
                nbttagcompound = itemstack.getTagCompound().getCompoundTag("Fireworks");
            }
            this.worldObj.makeFireworks(this.posX, this.posY, this.posZ, this.motionX, this.motionY, this.motionZ, nbttagcompound);
        }
        super.handleStatusUpdate(id);
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setInteger("Life", this.fireworkAge);
        tagCompound.setInteger("LifeTime", this.lifetime);
        final ItemStack itemstack = this.dataWatcher.getWatchableObjectItemStack(8);
        if (itemstack != null) {
            final NBTTagCompound nbttagcompound = new NBTTagCompound();
            itemstack.writeToNBT(nbttagcompound);
            tagCompound.setTag("FireworksItem", nbttagcompound);
        }
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.fireworkAge = tagCompund.getInteger("Life");
        this.lifetime = tagCompund.getInteger("LifeTime");
        final NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("FireworksItem");
        if (nbttagcompound != null) {
            final ItemStack itemstack = ItemStack.loadItemStackFromNBT(nbttagcompound);
            if (itemstack != null) {
                this.dataWatcher.updateObject(8, itemstack);
            }
        }
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        return super.getBrightness(partialTicks);
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        return super.getBrightnessForRender(partialTicks);
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}
