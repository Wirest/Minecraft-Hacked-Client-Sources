package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public abstract class EntityWaterMob extends EntityLiving implements IAnimals {
    private static final String __OBFID = "CL_00001653";

    public EntityWaterMob(World worldIn) {
        super(worldIn);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    public boolean getCanSpawnHere() {
        return true;
    }

    /**
     * Whether or not the current entity is in lava
     */
    public boolean handleLavaMovement() {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }

    /**
     * Get number of ticks, at least during which the living entity will be silent.
     */
    public int getTalkInterval() {
        return 120;
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    protected boolean canDespawn() {
        return true;
    }

    /**
     * Get the experience points the entity currently has.
     */
    protected int getExperiencePoints(EntityPlayer p_70693_1_) {
        return 1 + this.worldObj.rand.nextInt(3);
    }

    /**
     * Gets called every tick from main Entity class
     */
    public void onEntityUpdate() {
        int var1 = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater()) {
            --var1;
            this.setAir(var1);

            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.drown, 2.0F);
            }
        } else {
            this.setAir(300);
        }
    }

    public boolean isPushedByWater() {
        return false;
    }
}
