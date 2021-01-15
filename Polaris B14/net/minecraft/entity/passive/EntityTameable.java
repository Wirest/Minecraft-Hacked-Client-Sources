/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.DataWatcher;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityOwnable;
/*     */ import net.minecraft.entity.ai.EntityAISit;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityTameable extends EntityAnimal implements IEntityOwnable
/*     */ {
/*  17 */   protected EntityAISit aiSit = new EntityAISit(this);
/*     */   
/*     */   public EntityTameable(World worldIn)
/*     */   {
/*  21 */     super(worldIn);
/*  22 */     setupTamedAI();
/*     */   }
/*     */   
/*     */   protected void entityInit()
/*     */   {
/*  27 */     super.entityInit();
/*  28 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*  29 */     this.dataWatcher.addObject(17, "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*     */   {
/*  37 */     super.writeEntityToNBT(tagCompound);
/*     */     
/*  39 */     if (getOwnerId() == null)
/*     */     {
/*  41 */       tagCompound.setString("OwnerUUID", "");
/*     */     }
/*     */     else
/*     */     {
/*  45 */       tagCompound.setString("OwnerUUID", getOwnerId());
/*     */     }
/*     */     
/*  48 */     tagCompound.setBoolean("Sitting", isSitting());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*     */   {
/*  56 */     super.readEntityFromNBT(tagCompund);
/*  57 */     String s = "";
/*     */     
/*  59 */     if (tagCompund.hasKey("OwnerUUID", 8))
/*     */     {
/*  61 */       s = tagCompund.getString("OwnerUUID");
/*     */     }
/*     */     else
/*     */     {
/*  65 */       String s1 = tagCompund.getString("Owner");
/*  66 */       s = net.minecraft.server.management.PreYggdrasilConverter.getStringUUIDFromName(s1);
/*     */     }
/*     */     
/*  69 */     if (s.length() > 0)
/*     */     {
/*  71 */       setOwnerId(s);
/*  72 */       setTamed(true);
/*     */     }
/*     */     
/*  75 */     this.aiSit.setSitting(tagCompund.getBoolean("Sitting"));
/*  76 */     setSitting(tagCompund.getBoolean("Sitting"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void playTameEffect(boolean play)
/*     */   {
/*  84 */     EnumParticleTypes enumparticletypes = EnumParticleTypes.HEART;
/*     */     
/*  86 */     if (!play)
/*     */     {
/*  88 */       enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
/*     */     }
/*     */     
/*  91 */     for (int i = 0; i < 7; i++)
/*     */     {
/*  93 */       double d0 = this.rand.nextGaussian() * 0.02D;
/*  94 */       double d1 = this.rand.nextGaussian() * 0.02D;
/*  95 */       double d2 = this.rand.nextGaussian() * 0.02D;
/*  96 */       this.worldObj.spawnParticle(enumparticletypes, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 0.5D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, d0, d1, d2, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void handleStatusUpdate(byte id)
/*     */   {
/* 102 */     if (id == 7)
/*     */     {
/* 104 */       playTameEffect(true);
/*     */     }
/* 106 */     else if (id == 6)
/*     */     {
/* 108 */       playTameEffect(false);
/*     */     }
/*     */     else
/*     */     {
/* 112 */       super.handleStatusUpdate(id);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isTamed()
/*     */   {
/* 118 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x4) != 0;
/*     */   }
/*     */   
/*     */   public void setTamed(boolean tamed)
/*     */   {
/* 123 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 125 */     if (tamed)
/*     */     {
/* 127 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x4)));
/*     */     }
/*     */     else
/*     */     {
/* 131 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFB)));
/*     */     }
/*     */     
/* 134 */     setupTamedAI();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void setupTamedAI() {}
/*     */   
/*     */ 
/*     */   public boolean isSitting()
/*     */   {
/* 143 */     return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0;
/*     */   }
/*     */   
/*     */   public void setSitting(boolean sitting)
/*     */   {
/* 148 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 150 */     if (sitting)
/*     */     {
/* 152 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else
/*     */     {
/* 156 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     }
/*     */   }
/*     */   
/*     */   public String getOwnerId()
/*     */   {
/* 162 */     return this.dataWatcher.getWatchableObjectString(17);
/*     */   }
/*     */   
/*     */   public void setOwnerId(String ownerUuid)
/*     */   {
/* 167 */     this.dataWatcher.updateObject(17, ownerUuid);
/*     */   }
/*     */   
/*     */   public EntityLivingBase getOwner()
/*     */   {
/*     */     try
/*     */     {
/* 174 */       UUID uuid = UUID.fromString(getOwnerId());
/* 175 */       return uuid == null ? null : this.worldObj.getPlayerEntityByUUID(uuid);
/*     */     }
/*     */     catch (IllegalArgumentException var2) {}
/*     */     
/* 179 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isOwner(EntityLivingBase entityIn)
/*     */   {
/* 185 */     return entityIn == getOwner();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityAISit getAISit()
/*     */   {
/* 193 */     return this.aiSit;
/*     */   }
/*     */   
/*     */   public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_)
/*     */   {
/* 198 */     return true;
/*     */   }
/*     */   
/*     */   public net.minecraft.scoreboard.Team getTeam()
/*     */   {
/* 203 */     if (isTamed())
/*     */     {
/* 205 */       EntityLivingBase entitylivingbase = getOwner();
/*     */       
/* 207 */       if (entitylivingbase != null)
/*     */       {
/* 209 */         return entitylivingbase.getTeam();
/*     */       }
/*     */     }
/*     */     
/* 213 */     return super.getTeam();
/*     */   }
/*     */   
/*     */   public boolean isOnSameTeam(EntityLivingBase otherEntity)
/*     */   {
/* 218 */     if (isTamed())
/*     */     {
/* 220 */       EntityLivingBase entitylivingbase = getOwner();
/*     */       
/* 222 */       if (otherEntity == entitylivingbase)
/*     */       {
/* 224 */         return true;
/*     */       }
/*     */       
/* 227 */       if (entitylivingbase != null)
/*     */       {
/* 229 */         return entitylivingbase.isOnSameTeam(otherEntity);
/*     */       }
/*     */     }
/*     */     
/* 233 */     return super.isOnSameTeam(otherEntity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDeath(DamageSource cause)
/*     */   {
/* 241 */     if ((!this.worldObj.isRemote) && (this.worldObj.getGameRules().getBoolean("showDeathMessages")) && (hasCustomName()) && ((getOwner() instanceof EntityPlayerMP)))
/*     */     {
/* 243 */       ((EntityPlayerMP)getOwner()).addChatMessage(getCombatTracker().getDeathMessage());
/*     */     }
/*     */     
/* 246 */     super.onDeath(cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\passive\EntityTameable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */