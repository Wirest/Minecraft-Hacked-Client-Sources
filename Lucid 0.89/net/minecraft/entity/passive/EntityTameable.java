package net.minecraft.entity.passive;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.scoreboard.Team;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable
{
    protected EntityAISit aiSit = new EntityAISit(this);

    public EntityTameable(World worldIn)
    {
        super(worldIn);
        this.setupTamedAI();
    }

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
        this.dataWatcher.addObject(17, "");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);

        if (this.getOwnerId() == null)
        {
            tagCompound.setString("OwnerUUID", "");
        }
        else
        {
            tagCompound.setString("OwnerUUID", this.getOwnerId());
        }

        tagCompound.setBoolean("Sitting", this.isSitting());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        String var2 = "";

        if (tagCompund.hasKey("OwnerUUID", 8))
        {
            var2 = tagCompund.getString("OwnerUUID");
        }
        else
        {
            String var3 = tagCompund.getString("Owner");
            var2 = PreYggdrasilConverter.func_152719_a(var3);
        }

        if (var2.length() > 0)
        {
            this.setOwnerId(var2);
            this.setTamed(true);
        }

        this.aiSit.setSitting(tagCompund.getBoolean("Sitting"));
        this.setSitting(tagCompund.getBoolean("Sitting"));
    }

    /**
     * Play the taming effect, will either be hearts or smoke depending on status
     */
    protected void playTameEffect(boolean play)
    {
        EnumParticleTypes var2 = EnumParticleTypes.HEART;

        if (!play)
        {
            var2 = EnumParticleTypes.SMOKE_NORMAL;
        }

        for (int var3 = 0; var3 < 7; ++var3)
        {
            double var4 = this.rand.nextGaussian() * 0.02D;
            double var6 = this.rand.nextGaussian() * 0.02D;
            double var8 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(var2, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, var4, var6, var8, new int[0]);
        }
    }

    @Override
	public void handleHealthUpdate(byte id)
    {
        if (id == 7)
        {
            this.playTameEffect(true);
        }
        else if (id == 6)
        {
            this.playTameEffect(false);
        }
        else
        {
            super.handleHealthUpdate(id);
        }
    }

    public boolean isTamed()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    public void setTamed(boolean tamed)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (tamed)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 4)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -5)));
        }

        this.setupTamedAI();
    }

    protected void setupTamedAI() {}

    public boolean isSitting()
    {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void setSitting(boolean sitting)
    {
        byte var2 = this.dataWatcher.getWatchableObjectByte(16);

        if (sitting)
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 | 1)));
        }
        else
        {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte)(var2 & -2)));
        }
    }

    @Override
	public String getOwnerId()
    {
        return this.dataWatcher.getWatchableObjectString(17);
    }

    public void setOwnerId(String ownerUuid)
    {
        this.dataWatcher.updateObject(17, ownerUuid);
    }

    public EntityLivingBase getOwnerEntity()
    {
        try
        {
            UUID var1 = UUID.fromString(this.getOwnerId());
            return var1 == null ? null : this.worldObj.getPlayerEntityByUUID(var1);
        }
        catch (IllegalArgumentException var2)
        {
            return null;
        }
    }

    public boolean isOwner(EntityLivingBase entityIn)
    {
        return entityIn == this.getOwnerEntity();
    }

    /**
     * Returns the AITask responsible of the sit logic
     */
    public EntityAISit getAISit()
    {
        return this.aiSit;
    }

    public boolean func_142018_a(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_)
    {
        return true;
    }

    @Override
	public Team getTeam()
    {
        if (this.isTamed())
        {
            EntityLivingBase var1 = this.getOwnerEntity();

            if (var1 != null)
            {
                return var1.getTeam();
            }
        }

        return super.getTeam();
    }

    @Override
	public boolean isOnSameTeam(EntityLivingBase otherEntity)
    {
        if (this.isTamed())
        {
            EntityLivingBase var2 = this.getOwnerEntity();

            if (otherEntity == var2)
            {
                return true;
            }

            if (var2 != null)
            {
                return var2.isOnSameTeam(otherEntity);
            }
        }

        return super.isOnSameTeam(otherEntity);
    }

    /**
     * Called when the mob's health reaches 0.
     */
    @Override
	public void onDeath(DamageSource cause)
    {
        if (!this.worldObj.isRemote && this.worldObj.getGameRules().getGameRuleBooleanValue("showDeathMessages") && this.hasCustomName() && this.getOwnerEntity() instanceof EntityPlayerMP)
        {
            ((EntityPlayerMP)this.getOwnerEntity()).addChatMessage(this.getCombatTracker().getDeathMessage());
        }

        super.onDeath(cause);
    }

    @Override
	public Entity getOwner()
    {
        return this.getOwnerEntity();
    }
}
