/*    */ package net.minecraft.entity.item;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.projectile.EntityThrowable;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityExpBottle extends EntityThrowable
/*    */ {
/*    */   public EntityExpBottle(World worldIn)
/*    */   {
/* 13 */     super(worldIn);
/*    */   }
/*    */   
/*    */   public EntityExpBottle(World worldIn, EntityLivingBase p_i1786_2_)
/*    */   {
/* 18 */     super(worldIn, p_i1786_2_);
/*    */   }
/*    */   
/*    */   public EntityExpBottle(World worldIn, double p_i1787_2_, double p_i1787_4_, double p_i1787_6_)
/*    */   {
/* 23 */     super(worldIn, p_i1787_2_, p_i1787_4_, p_i1787_6_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected float getGravityVelocity()
/*    */   {
/* 31 */     return 0.07F;
/*    */   }
/*    */   
/*    */   protected float getVelocity()
/*    */   {
/* 36 */     return 0.7F;
/*    */   }
/*    */   
/*    */   protected float getInaccuracy()
/*    */   {
/* 41 */     return -20.0F;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void onImpact(MovingObjectPosition p_70184_1_)
/*    */   {
/* 49 */     if (!this.worldObj.isRemote)
/*    */     {
/* 51 */       this.worldObj.playAuxSFX(2002, new net.minecraft.util.BlockPos(this), 0);
/* 52 */       int i = 3 + this.worldObj.rand.nextInt(5) + this.worldObj.rand.nextInt(5);
/*    */       
/* 54 */       while (i > 0)
/*    */       {
/* 56 */         int j = EntityXPOrb.getXPSplit(i);
/* 57 */         i -= j;
/* 58 */         this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, j));
/*    */       }
/*    */       
/* 61 */       setDead();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityExpBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */