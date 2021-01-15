/*    */ package net.minecraft.entity.item;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityMinecartEmpty extends EntityMinecart
/*    */ {
/*    */   public EntityMinecartEmpty(World worldIn)
/*    */   {
/* 11 */     super(worldIn);
/*    */   }
/*    */   
/*    */   public EntityMinecartEmpty(World worldIn, double p_i1723_2_, double p_i1723_4_, double p_i1723_6_)
/*    */   {
/* 16 */     super(worldIn, p_i1723_2_, p_i1723_4_, p_i1723_6_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean interactFirst(EntityPlayer playerIn)
/*    */   {
/* 24 */     if ((this.riddenByEntity != null) && ((this.riddenByEntity instanceof EntityPlayer)) && (this.riddenByEntity != playerIn))
/*    */     {
/* 26 */       return true;
/*    */     }
/* 28 */     if ((this.riddenByEntity != null) && (this.riddenByEntity != playerIn))
/*    */     {
/* 30 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 34 */     if (!this.worldObj.isRemote)
/*    */     {
/* 36 */       playerIn.mountEntity(this);
/*    */     }
/*    */     
/* 39 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower)
/*    */   {
/* 48 */     if (receivingPower)
/*    */     {
/* 50 */       if (this.riddenByEntity != null)
/*    */       {
/* 52 */         this.riddenByEntity.mountEntity(null);
/*    */       }
/*    */       
/* 55 */       if (getRollingAmplitude() == 0)
/*    */       {
/* 57 */         setRollingDirection(-getRollingDirection());
/* 58 */         setRollingAmplitude(10);
/* 59 */         setDamage(50.0F);
/* 60 */         setBeenAttacked();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public EntityMinecart.EnumMinecartType getMinecartType()
/*    */   {
/* 67 */     return EntityMinecart.EnumMinecartType.RIDEABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\item\EntityMinecartEmpty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */