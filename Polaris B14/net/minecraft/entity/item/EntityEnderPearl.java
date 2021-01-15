/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.EntityEndermite;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.projectile.EntityThrowable;
/*     */ import net.minecraft.network.NetHandlerPlayServer;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityEnderPearl extends EntityThrowable
/*     */ {
/*     */   private EntityLivingBase field_181555_c;
/*     */   
/*     */   public EntityEnderPearl(World p_i46455_1_)
/*     */   {
/*  20 */     super(p_i46455_1_);
/*     */   }
/*     */   
/*     */   public EntityEnderPearl(World worldIn, EntityLivingBase p_i1783_2_)
/*     */   {
/*  25 */     super(worldIn, p_i1783_2_);
/*  26 */     this.field_181555_c = p_i1783_2_;
/*     */   }
/*     */   
/*     */   public EntityEnderPearl(World worldIn, double p_i1784_2_, double p_i1784_4_, double p_i1784_6_)
/*     */   {
/*  31 */     super(worldIn, p_i1784_2_, p_i1784_4_, p_i1784_6_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onImpact(MovingObjectPosition p_70184_1_)
/*     */   {
/*  39 */     EntityLivingBase entitylivingbase = getThrower();
/*     */     
/*  41 */     if (p_70184_1_.entityHit != null)
/*     */     {
/*  43 */       if (p_70184_1_.entityHit == this.field_181555_c)
/*     */       {
/*  45 */         return;
/*     */       }
/*     */       
/*  48 */       p_70184_1_.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, entitylivingbase), 0.0F);
/*     */     }
/*     */     
/*  51 */     for (int i = 0; i < 32; i++)
/*     */     {
/*  53 */       this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
/*     */     }
/*     */     
/*  56 */     if (!this.worldObj.isRemote)
/*     */     {
/*  58 */       if ((entitylivingbase instanceof EntityPlayerMP))
/*     */       {
/*  60 */         EntityPlayerMP entityplayermp = (EntityPlayerMP)entitylivingbase;
/*     */         
/*  62 */         if ((entityplayermp.playerNetServerHandler.getNetworkManager().isChannelOpen()) && (entityplayermp.worldObj == this.worldObj) && (!entityplayermp.isPlayerSleeping()))
/*     */         {
/*  64 */           if ((this.rand.nextFloat() < 0.05F) && (this.worldObj.getGameRules().getBoolean("doMobSpawning")))
/*     */           {
/*  66 */             EntityEndermite entityendermite = new EntityEndermite(this.worldObj);
/*  67 */             entityendermite.setSpawnedByPlayer(true);
/*  68 */             entityendermite.setLocationAndAngles(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, entitylivingbase.rotationYaw, entitylivingbase.rotationPitch);
/*  69 */             this.worldObj.spawnEntityInWorld(entityendermite);
/*     */           }
/*     */           
/*  72 */           if (entitylivingbase.isRiding())
/*     */           {
/*  74 */             entitylivingbase.mountEntity(null);
/*     */           }
/*     */           
/*  77 */           entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
/*  78 */           entitylivingbase.fallDistance = 0.0F;
/*  79 */           entitylivingbase.attackEntityFrom(DamageSource.fall, 5.0F);
/*     */         }
/*     */       }
/*  82 */       else if (entitylivingbase != null)
/*     */       {
/*  84 */         entitylivingbase.setPositionAndUpdate(this.posX, this.posY, this.posZ);
/*  85 */         entitylivingbase.fallDistance = 0.0F;
/*     */       }
/*     */       
/*  88 */       setDead();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onUpdate()
/*     */   {
/*  97 */     EntityLivingBase entitylivingbase = getThrower();
/*     */     
/*  99 */     if ((entitylivingbase != null) && ((entitylivingbase instanceof net.minecraft.entity.player.EntityPlayer)) && (!entitylivingbase.isEntityAlive()))
/*     */     {
/* 101 */       setDead();
/*     */     }
/*     */     else
/*     */     {
/* 105 */       super.onUpdate();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityEnderPearl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */