/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.base.Predicate;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public final class EntitySelectors
/*    */ {
/* 14 */   public static final Predicate<Entity> selectAnything = new Predicate()
/*    */   {
/*    */     public boolean apply(Entity p_apply_1_)
/*    */     {
/* 18 */       return p_apply_1_.isEntityAlive();
/*    */     }
/*    */   };
/* 21 */   public static final Predicate<Entity> IS_STANDALONE = new Predicate()
/*    */   {
/*    */     public boolean apply(Entity p_apply_1_)
/*    */     {
/* 25 */       return (p_apply_1_.isEntityAlive()) && (p_apply_1_.riddenByEntity == null) && (p_apply_1_.ridingEntity == null);
/*    */     }
/*    */   };
/* 28 */   public static final Predicate<Entity> selectInventories = new Predicate()
/*    */   {
/*    */     public boolean apply(Entity p_apply_1_)
/*    */     {
/* 32 */       return ((p_apply_1_ instanceof IInventory)) && (p_apply_1_.isEntityAlive());
/*    */     }
/*    */   };
/* 35 */   public static final Predicate<Entity> NOT_SPECTATING = new Predicate()
/*    */   {
/*    */     public boolean apply(Entity p_apply_1_)
/*    */     {
/* 39 */       return (!(p_apply_1_ instanceof EntityPlayer)) || (!((EntityPlayer)p_apply_1_).isSpectator());
/*    */     }
/*    */   };
/*    */   
/*    */   public static class ArmoredMob implements Predicate<Entity>
/*    */   {
/*    */     private final ItemStack armor;
/*    */     
/*    */     public ArmoredMob(ItemStack armor)
/*    */     {
/* 49 */       this.armor = armor;
/*    */     }
/*    */     
/*    */     public boolean apply(Entity p_apply_1_)
/*    */     {
/* 54 */       if (!p_apply_1_.isEntityAlive())
/*    */       {
/* 56 */         return false;
/*    */       }
/* 58 */       if (!(p_apply_1_ instanceof EntityLivingBase))
/*    */       {
/* 60 */         return false;
/*    */       }
/*    */       
/*    */ 
/* 64 */       EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
/* 65 */       return (entitylivingbase instanceof EntityArmorStand) ? true : (entitylivingbase instanceof EntityLiving) ? ((EntityLiving)entitylivingbase).canPickUpLoot() : entitylivingbase.getEquipmentInSlot(EntityLiving.getArmorPosition(this.armor)) != null ? false : entitylivingbase instanceof EntityPlayer;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\EntitySelectors.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */