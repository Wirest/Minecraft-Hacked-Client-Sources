// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityTNTPrimed extends Entity
{
    public int fuse;
    private EntityLivingBase tntPlacedBy;
    
    public EntityTNTPrimed(final World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.98f, 0.98f);
    }
    
    public EntityTNTPrimed(final World worldIn, final double p_i1730_2_, final double p_i1730_4_, final double p_i1730_6_, final EntityLivingBase p_i1730_8_) {
        this(worldIn);
        this.setPosition(p_i1730_2_, p_i1730_4_, p_i1730_6_);
        final float f = (float)(Math.random() * 3.141592653589793 * 2.0);
        this.motionX = -(float)Math.sin(f) * 0.02f;
        this.motionY = 0.20000000298023224;
        this.motionZ = -(float)Math.cos(f) * 0.02f;
        this.fuse = 80;
        this.prevPosX = p_i1730_2_;
        this.prevPosY = p_i1730_4_;
        this.prevPosZ = p_i1730_6_;
        this.tntPlacedBy = p_i1730_8_;
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
    }
    
    @Override
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.03999999910593033;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= 0.9800000190734863;
        if (this.onGround) {
            this.motionX *= 0.699999988079071;
            this.motionZ *= 0.699999988079071;
            this.motionY *= -0.5;
        }
        if (this.fuse-- <= 0) {
            this.setDead();
            if (!this.worldObj.isRemote) {
                this.explode();
            }
        }
        else {
            this.handleWaterMovement();
            this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5, this.posZ, 0.0, 0.0, 0.0, new int[0]);
        }
    }
    
    private void explode() {
        final float f = 4.0f;
        this.worldObj.createExplosion(this, this.posX, this.posY + this.height / 16.0f, this.posZ, f, true);
    }
    
    @Override
    protected void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setByte("Fuse", (byte)this.fuse);
    }
    
    @Override
    protected void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.fuse = tagCompund.getByte("Fuse");
    }
    
    public EntityLivingBase getTntPlacedBy() {
        return this.tntPlacedBy;
    }
    
    @Override
    public float getEyeHeight() {
        return 0.0f;
    }
}
