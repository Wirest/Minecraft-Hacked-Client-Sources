package net.minecraft.entity.monster;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityMagmaCube extends EntitySlime
{
    public EntityMagmaCube(World worldIn)
    {
        super(worldIn);
        this.isImmuneToFire = true;
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
	public boolean getCanSpawnHere()
    {
        return this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
    }

    /**
     * Checks that the entity is not colliding with any blocks / liquids
     */
    @Override
	public boolean isNotColliding()
    {
        return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox());
    }

    /**
     * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
     */
    @Override
	public int getTotalArmorValue()
    {
        return this.getSlimeSize() * 3;
    }

    @Override
	public int getBrightnessForRender(float partialTicks)
    {
        return 15728880;
    }

    /**
     * Gets how bright this entity is.
     */
    @Override
	public float getBrightness(float partialTicks)
    {
        return 1.0F;
    }

    @Override
	protected EnumParticleTypes getParticleType()
    {
        return EnumParticleTypes.FLAME;
    }

    @Override
	protected EntitySlime createInstance()
    {
        return new EntityMagmaCube(this.worldObj);
    }

    @Override
	protected Item getDropItem()
    {
        return Items.magma_cream;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        Item item = this.getDropItem();

        if (item != null && this.getSlimeSize() > 1)
        {
            int i = this.rand.nextInt(4) - 2;

            if (p_70628_2_ > 0)
            {
                i += this.rand.nextInt(p_70628_2_ + 1);
            }

            for (int j = 0; j < i; ++j)
            {
                this.dropItem(item, 1);
            }
        }
    }

    /**
     * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
     */
    @Override
	public boolean isBurning()
    {
        return false;
    }

    /**
     * Gets the amount of time the slime needs to wait between jumps.
     */
    @Override
	protected int getJumpDelay()
    {
        return super.getJumpDelay() * 4;
    }

    @Override
	protected void alterSquishAmount()
    {
        this.squishAmount *= 0.9F;
    }

    /**
     * Causes this entity to do an upwards motion (jumping).
     */
    @Override
	protected void jump()
    {
        this.motionY = 0.42F + this.getSlimeSize() * 0.1F;
        this.isAirBorne = true;
    }

    @Override
	protected void handleJumpLava()
    {
        this.motionY = 0.22F + this.getSlimeSize() * 0.05F;
        this.isAirBorne = true;
    }

    @Override
	public void fall(float distance, float damageMultiplier)
    {
    }

    /**
     * Indicates weather the slime is able to damage the player (based upon the slime's size)
     */
    @Override
	protected boolean canDamagePlayer()
    {
        return true;
    }

    /**
     * Gets the amount of damage dealt to the player when "attacked" by the slime.
     */
    @Override
	protected int getAttackStrength()
    {
        return super.getAttackStrength() + 2;
    }

    /**
     * Returns the name of the sound played when the slime jumps.
     */
    @Override
	protected String getJumpSound()
    {
        return this.getSlimeSize() > 1 ? "mob.magmacube.big" : "mob.magmacube.small";
    }

    /**
     * Returns true if the slime makes a sound when it lands after a jump (based upon the slime's size)
     */
    @Override
	protected boolean makesSoundOnLand()
    {
        return true;
    }
}
