/*    */ package net.minecraft.entity.projectile;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.DamageSource;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityLargeFireball extends EntityFireball
/*    */ {
/* 12 */   public int explosionPower = 1;
/*    */   
/*    */   public EntityLargeFireball(World worldIn)
/*    */   {
/* 16 */     super(worldIn);
/*    */   }
/*    */   
/*    */   public EntityLargeFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ)
/*    */   {
/* 21 */     super(worldIn, x, y, z, accelX, accelY, accelZ);
/*    */   }
/*    */   
/*    */   public EntityLargeFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ)
/*    */   {
/* 26 */     super(worldIn, shooter, accelX, accelY, accelZ);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void onImpact(MovingObjectPosition movingObject)
/*    */   {
/* 34 */     if (!this.worldObj.isRemote)
/*    */     {
/* 36 */       if (movingObject.entityHit != null)
/*    */       {
/* 38 */         movingObject.entityHit.attackEntityFrom(DamageSource.causeFireballDamage(this, this.shootingEntity), 6.0F);
/* 39 */         applyEnchantments(this.shootingEntity, movingObject.entityHit);
/*    */       }
/*    */       
/* 42 */       boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
/* 43 */       this.worldObj.newExplosion(null, this.posX, this.posY, this.posZ, this.explosionPower, flag, flag);
/* 44 */       setDead();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void writeEntityToNBT(NBTTagCompound tagCompound)
/*    */   {
/* 53 */     super.writeEntityToNBT(tagCompound);
/* 54 */     tagCompound.setInteger("ExplosionPower", this.explosionPower);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void readEntityFromNBT(NBTTagCompound tagCompund)
/*    */   {
/* 62 */     super.readEntityFromNBT(tagCompund);
/*    */     
/* 64 */     if (tagCompund.hasKey("ExplosionPower", 99))
/*    */     {
/* 66 */       this.explosionPower = tagCompund.getInteger("ExplosionPower");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\projectile\EntityLargeFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */