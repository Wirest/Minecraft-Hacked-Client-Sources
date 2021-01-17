// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.monster;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public class EntityMagmaCube extends EntitySlime
{
    public EntityMagmaCube(final World worldIn) {
        super(worldIn);
        this.isImmuneToFire = true;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224);
    }
    
    @Override
    public boolean getCanSpawnHere() {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }
    
    @Override
    public boolean isNotColliding() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }
    
    @Override
    public int getTotalArmorValue() {
        return this.getSlimeSize() * 3;
    }
    
    @Override
    public int getBrightnessForRender(final float partialTicks) {
        return 15728880;
    }
    
    @Override
    public float getBrightness(final float partialTicks) {
        return 1.0f;
    }
    
    @Override
    protected EnumParticleTypes getParticleType() {
        return EnumParticleTypes.FLAME;
    }
    
    @Override
    protected EntitySlime createInstance() {
        return new EntityMagmaCube(this.worldObj);
    }
    
    @Override
    protected Item getDropItem() {
        return Items.magma_cream;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        final Item item = this.getDropItem();
        if (item != null && this.getSlimeSize() > 1) {
            int i = this.rand.nextInt(4) - 2;
            if (p_70628_2_ > 0) {
                i += this.rand.nextInt(p_70628_2_ + 1);
            }
            for (int j = 0; j < i; ++j) {
                this.dropItem(item, 1);
            }
        }
    }
    
    @Override
    public boolean isBurning() {
        return false;
    }
    
    @Override
    protected int getJumpDelay() {
        return super.getJumpDelay() * 4;
    }
    
    @Override
    protected void alterSquishAmount() {
        this.squishAmount *= 0.9f;
    }
    
    @Override
    protected void jump() {
        this.motionY = 0.42f + this.getSlimeSize() * 0.1f;
        this.isAirBorne = true;
    }
    
    @Override
    protected void handleJumpLava() {
        this.motionY = 0.22f + this.getSlimeSize() * 0.05f;
        this.isAirBorne = true;
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected boolean canDamagePlayer() {
        return true;
    }
    
    @Override
    protected int getAttackStrength() {
        return super.getAttackStrength() + 2;
    }
    
    @Override
    protected String getJumpSound() {
        return (this.getSlimeSize() > 1) ? "mob.magmacube.big" : "mob.magmacube.small";
    }
    
    @Override
    protected boolean makesSoundOnLand() {
        return true;
    }
}
