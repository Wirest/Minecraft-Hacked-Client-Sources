// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.scoreboard.Team;
import java.util.UUID;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.IEntityOwnable;

public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable
{
    protected EntityAISit aiSit;
    
    public EntityTameable(final World worldIn) {
        super(worldIn);
        this.aiSit = new EntityAISit(this);
        this.setupTamedAI();
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Byte)0);
        this.dataWatcher.addObject(17, "");
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        if (this.getOwnerId() == null) {
            tagCompound.setString("OwnerUUID", "");
        }
        else {
            tagCompound.setString("OwnerUUID", this.getOwnerId());
        }
        tagCompound.setBoolean("Sitting", this.isSitting());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        String s = "";
        if (tagCompund.hasKey("OwnerUUID", 8)) {
            s = tagCompund.getString("OwnerUUID");
        }
        else {
            final String s2 = tagCompund.getString("Owner");
            s = PreYggdrasilConverter.getStringUUIDFromName(s2);
        }
        if (s.length() > 0) {
            this.setOwnerId(s);
            this.setTamed(true);
        }
        this.aiSit.setSitting(tagCompund.getBoolean("Sitting"));
        this.setSitting(tagCompund.getBoolean("Sitting"));
    }
    
    protected void playTameEffect(final boolean play) {
        EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
        if (!play) {
            enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
        }
        for (int i = 0; i < 7; ++i) {
            final double d0 = this.rand.nextGaussian() * 0.02;
            final double d2 = this.rand.nextGaussian() * 0.02;
            final double d3 = this.rand.nextGaussian() * 0.02;
            this.worldObj.spawnParticle(enumparticletypes, this.posX + this.rand.nextFloat() * this.width * 2.0f - this.width, this.posY + 0.5 + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0f - this.width, d0, d2, d3, new int[0]);
        }
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 7) {
            this.playTameEffect(true);
        }
        else if (id == 6) {
            this.playTameEffect(false);
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    public boolean isTamed() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x4) != 0x0;
    }
    
    public void setTamed(final boolean tamed) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (tamed) {
            this.dataWatcher.updateObject(16, (byte)(b0 | 0x4));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(b0 & 0xFFFFFFFB));
        }
        this.setupTamedAI();
    }
    
    protected void setupTamedAI() {
    }
    
    public boolean isSitting() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setSitting(final boolean sitting) {
        final byte b0 = this.dataWatcher.getWatchableObjectByte(16);
        if (sitting) {
            this.dataWatcher.updateObject(16, (byte)(b0 | 0x1));
        }
        else {
            this.dataWatcher.updateObject(16, (byte)(b0 & 0xFFFFFFFE));
        }
    }
    
    @Override
    public String getOwnerId() {
        return this.dataWatcher.getWatchableObjectString(17);
    }
    
    public void setOwnerId(final String ownerUuid) {
        this.dataWatcher.updateObject(17, ownerUuid);
    }
    
    @Override
    public EntityLivingBase getOwner() {
        try {
            final UUID uuid = UUID.fromString(this.getOwnerId());
            return (uuid == null) ? null : this.worldObj.getPlayerEntityByUUID(uuid);
        }
        catch (IllegalArgumentException var2) {
            return null;
        }
    }
    
    public boolean isOwner(final EntityLivingBase entityIn) {
        return entityIn == this.getOwner();
    }
    
    public EntityAISit getAISit() {
        return this.aiSit;
    }
    
    public boolean shouldAttackEntity(final EntityLivingBase p_142018_1_, final EntityLivingBase p_142018_2_) {
        return true;
    }
    
    @Override
    public Team getTeam() {
        if (this.isTamed()) {
            final EntityLivingBase entitylivingbase = this.getOwner();
            if (entitylivingbase != null) {
                return entitylivingbase.getTeam();
            }
        }
        return super.getTeam();
    }
    
    @Override
    public boolean isOnSameTeam(final EntityLivingBase otherEntity) {
        if (this.isTamed()) {
            final EntityLivingBase entitylivingbase = this.getOwner();
            if (otherEntity == entitylivingbase) {
                return true;
            }
            if (entitylivingbase != null) {
                return entitylivingbase.isOnSameTeam(otherEntity);
            }
        }
        return super.isOnSameTeam(otherEntity);
    }
    
    @Override
    public void onDeath(final DamageSource cause) {
        if (!this.worldObj.isRemote && this.worldObj.getGameRules().getBoolean("showDeathMessages") && this.hasCustomName() && this.getOwner() instanceof EntityPlayerMP) {
            ((EntityPlayerMP)this.getOwner()).addChatMessage(this.getCombatTracker().getDeathMessage());
        }
        super.onDeath(cause);
    }
}
