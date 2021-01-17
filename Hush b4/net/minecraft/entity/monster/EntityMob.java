// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.world.EnumSkyBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.entity.EntityCreature;

public abstract class EntityMob extends EntityCreature implements IMob
{
    public EntityMob(final World worldIn) {
        super(worldIn);
        this.experienceValue = 5;
    }
    
    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();
        final float f = this.getBrightness(1.0f);
        if (f > 0.5f) {
            this.entityAge += 2;
        }
        super.onLivingUpdate();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.setDead();
        }
    }
    
    @Override
    protected String getSwimSound() {
        return "game.hostile.swim";
    }
    
    @Override
    protected String getSplashSound() {
        return "game.hostile.swim.splash";
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (super.attackEntityFrom(source, amount)) {
            final Entity entity = source.getEntity();
            return this.riddenByEntity == entity || this.ridingEntity == entity || true;
        }
        return false;
    }
    
    @Override
    protected String getHurtSound() {
        return "game.hostile.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "game.hostile.die";
    }
    
    @Override
    protected String getFallSoundString(final int damageValue) {
        return (damageValue > 4) ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
        int i = 0;
        if (entityIn instanceof EntityLivingBase) {
            f += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }
        final boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);
        if (flag) {
            if (i > 0) {
                entityIn.addVelocity(-MathHelper.sin(this.rotationYaw * 3.1415927f / 180.0f) * i * 0.5f, 0.1, MathHelper.cos(this.rotationYaw * 3.1415927f / 180.0f) * i * 0.5f);
                this.motionX *= 0.6;
                this.motionZ *= 0.6;
            }
            final int j = EnchantmentHelper.getFireAspectModifier(this);
            if (j > 0) {
                entityIn.setFire(j * 4);
            }
            this.applyEnchantments(this, entityIn);
        }
        return flag;
    }
    
    @Override
    public float getBlockPathWeight(final BlockPos pos) {
        return 0.5f - this.worldObj.getLightBrightness(pos);
    }
    
    protected boolean isValidLightLevel() {
        final BlockPos blockpos = new BlockPos(this.posX, this.getEntityBoundingBox().minY, this.posZ);
        if (this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32)) {
            return false;
        }
        int i = this.worldObj.getLightFromNeighbors(blockpos);
        if (this.worldObj.isThundering()) {
            final int j = this.worldObj.getSkylightSubtracted();
            this.worldObj.setSkylightSubtracted(10);
            i = this.worldObj.getLightFromNeighbors(blockpos);
            this.worldObj.setSkylightSubtracted(j);
        }
        return i <= this.rand.nextInt(8);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && this.isValidLightLevel() && super.getCanSpawnHere();
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
    }
    
    @Override
    protected boolean canDropLoot() {
        return true;
    }
}
