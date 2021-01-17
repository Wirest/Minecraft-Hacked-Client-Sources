// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.item;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;

public class EntityXPOrb extends Entity
{
    public int xpColor;
    public int xpOrbAge;
    public int delayBeforeCanPickup;
    private int xpOrbHealth;
    private int xpValue;
    private EntityPlayer closestPlayer;
    private int xpTargetColor;
    
    public EntityXPOrb(final World worldIn, final double x, final double y, final double z, final int expValue) {
        super(worldIn);
        this.xpOrbHealth = 5;
        this.setSize(0.5f, 0.5f);
        this.setPosition(x, y, z);
        this.rotationYaw = (float)(Math.random() * 360.0);
        this.motionX = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612) * 2.0f;
        this.motionY = (float)(Math.random() * 0.2) * 2.0f;
        this.motionZ = (float)(Math.random() * 0.20000000298023224 - 0.10000000149011612) * 2.0f;
        this.xpValue = expValue;
    }
    
    @Override
    protected boolean canTriggerWalking() {
        return false;
    }
    
    public EntityXPOrb(final World worldIn) {
        super(worldIn);
        this.xpOrbHealth = 5;
        this.setSize(0.25f, 0.25f);
    }
    
    @Override
    protected void entityInit() {
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        float f = 0.5f;
        f = MathHelper.clamp_float(f, 0.0f, 1.0f);
        final int i = super.getBrightnessForRender(partialTicks);
        int j = i & 0xFF;
        final int k = i >> 16 & 0xFF;
        j += (int)(f * 15.0f * 16.0f);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.delayBeforeCanPickup > 0) {
            --this.delayBeforeCanPickup;
        }
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.motionY -= 0.029999999329447746;
        if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
            this.motionY = 0.20000000298023224;
            this.motionX = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.motionZ = (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f;
            this.playSound("random.fizz", 0.4f, 2.0f + this.rand.nextFloat() * 0.4f);
        }
        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
        final double d0 = 8.0;
        if (this.xpTargetColor < this.xpColor - 20 + this.getEntityId() % 100) {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSqToEntity(this) > d0 * d0) {
                this.closestPlayer = this.worldObj.getClosestPlayerToEntity(this, d0);
            }
            this.xpTargetColor = this.xpColor;
        }
        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }
        if (this.closestPlayer != null) {
            final double d2 = (this.closestPlayer.posX - this.posX) / d0;
            final double d3 = (this.closestPlayer.posY + this.closestPlayer.getEyeHeight() - this.posY) / d0;
            final double d4 = (this.closestPlayer.posZ - this.posZ) / d0;
            final double d5 = Math.sqrt(d2 * d2 + d3 * d3 + d4 * d4);
            double d6 = 1.0 - d5;
            if (d6 > 0.0) {
                d6 *= d6;
                this.motionX += d2 / d5 * d6 * 0.1;
                this.motionY += d3 / d5 * d6 * 0.1;
                this.motionZ += d4 / d5 * d6 * 0.1;
            }
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
        float f = 0.98f;
        if (this.onGround) {
            f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.98f;
        }
        this.motionX *= f;
        this.motionY *= 0.9800000190734863;
        this.motionZ *= f;
        if (this.onGround) {
            this.motionY *= -0.8999999761581421;
        }
        ++this.xpColor;
        ++this.xpOrbAge;
        if (this.xpOrbAge >= 6000) {
            this.setDead();
        }
    }
    
    @Override
    public boolean handleWaterMovement() {
        return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.water, this);
    }
    
    @Override
    protected void dealFireDamage(final int amount) {
        this.attackEntityFrom(DamageSource.inFire, (float)amount);
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        this.setBeenAttacked();
        this.xpOrbHealth -= (int)amount;
        if (this.xpOrbHealth <= 0) {
            this.setDead();
        }
        return false;
    }
    
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        tagCompound.setShort("Health", (byte)this.xpOrbHealth);
        tagCompound.setShort("Age", (short)this.xpOrbAge);
        tagCompound.setShort("Value", (short)this.xpValue);
    }
    
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        this.xpOrbHealth = (tagCompund.getShort("Health") & 0xFF);
        this.xpOrbAge = tagCompund.getShort("Age");
        this.xpValue = tagCompund.getShort("Value");
    }
    
    @Override
    public void onCollideWithPlayer(final EntityPlayer entityIn) {
        if (!this.worldObj.isRemote && this.delayBeforeCanPickup == 0 && entityIn.xpCooldown == 0) {
            entityIn.xpCooldown = 2;
            this.worldObj.playSoundAtEntity(entityIn, "random.orb", 0.1f, 0.5f * ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7f + 1.8f));
            entityIn.onItemPickup(this, 1);
            entityIn.addExperience(this.xpValue);
            this.setDead();
        }
    }
    
    public int getXpValue() {
        return this.xpValue;
    }
    
    public int getTextureByXP() {
        return (this.xpValue >= 2477) ? 10 : ((this.xpValue >= 1237) ? 9 : ((this.xpValue >= 617) ? 8 : ((this.xpValue >= 307) ? 7 : ((this.xpValue >= 149) ? 6 : ((this.xpValue >= 73) ? 5 : ((this.xpValue >= 37) ? 4 : ((this.xpValue >= 17) ? 3 : ((this.xpValue >= 7) ? 2 : ((this.xpValue >= 3) ? 1 : 0)))))))));
    }
    
    public static int getXPSplit(final int expValue) {
        return (expValue >= 2477) ? 2477 : ((expValue >= 1237) ? 1237 : ((expValue >= 617) ? 617 : ((expValue >= 307) ? 307 : ((expValue >= 149) ? 149 : ((expValue >= 73) ? 73 : ((expValue >= 37) ? 37 : ((expValue >= 17) ? 17 : ((expValue >= 7) ? 7 : ((expValue >= 3) ? 3 : 1)))))))));
    }
    
    @Override
    public boolean canAttackWithItem() {
        return false;
    }
}
