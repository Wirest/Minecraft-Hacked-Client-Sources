/*    */ package net.minecraft.entity.projectile;
/*    */ 
/*    */ import net.minecraft.block.BlockFire;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntitySmallFireball extends EntityFireball
/*    */ {
/*    */   public EntitySmallFireball(World worldIn)
/*    */   {
/* 15 */     super(worldIn);
/* 16 */     setSize(0.3125F, 0.3125F);
/*    */   }
/*    */   
/*    */   public EntitySmallFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
/*    */   {
/* 21 */     super(worldIn, shooter, accelX, accelY, accelZ);
/* 22 */     setSize(0.3125F, 0.3125F);
/*    */   }
/*    */   
/*    */   public EntitySmallFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
/*    */   {
/* 27 */     super(worldIn, x, y, z, accelX, accelY, accelZ);
/* 28 */     setSize(0.3125F, 0.3125F);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void onImpact(MovingObjectPosition movingObject)
/*    */   {
/* 36 */     if (!this.worldObj.isRemote)
/*    */     {
/* 38 */       if (movingObject.entityHit != null)
/*    */       {
/* 40 */         boolean flag = movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 5.0F);
/*    */         
/* 42 */         if (flag)
/*    */         {
/* 44 */           applyEnchantments(this.shootingEntity, movingObject.entityHit);
/*    */           
/* 46 */           if (!movingObject.entityHit.isImmuneToFire())
/*    */           {
/* 48 */             movingObject.entityHit.setFire(5);
/*    */           }
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 54 */         boolean flag1 = true;
/*    */         
/* 56 */         if ((this.shootingEntity != null) && ((this.shootingEntity instanceof net.minecraft.entity.EntityLiving)))
/*    */         {
/* 58 */           flag1 = this.worldObj.getGameRules().getBoolean("mobGriefing");
/*    */         }
/*    */         
/* 61 */         if (flag1)
/*    */         {
/* 63 */           BlockPos blockpos = movingObject.getBlockPos().offset(movingObject.sideHit);
/*    */           
/* 65 */           if (this.worldObj.isAirBlock(blockpos))
/*    */           {
/* 67 */             this.worldObj.setBlockState(blockpos, net.minecraft.init.Blocks.fire.getDefaultState());
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 72 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean canBeCollidedWith()
/*    */   {
/* 81 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean attackEntityFrom(DamageSource source, float amount)
/*    */   {
/* 89 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntitySmallFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */