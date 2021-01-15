/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class EntityDamageSourceIndirect extends EntityDamageSource
/*    */ {
/*    */   private Entity indirectEntity;
/*    */   
/*    */   public EntityDamageSourceIndirect(String p_i1568_1_, Entity p_i1568_2_, Entity indirectEntityIn)
/*    */   {
/* 13 */     super(p_i1568_1_, p_i1568_2_);
/* 14 */     this.indirectEntity = indirectEntityIn;
/*    */   }
/*    */   
/*    */   public Entity getSourceOfDamage()
/*    */   {
/* 19 */     return this.damageSourceEntity;
/*    */   }
/*    */   
/*    */   public Entity getEntity()
/*    */   {
/* 24 */     return this.indirectEntity;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IChatComponent getDeathMessage(EntityLivingBase p_151519_1_)
/*    */   {
/* 32 */     IChatComponent ichatcomponent = this.indirectEntity == null ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
/* 33 */     ItemStack itemstack = (this.indirectEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.indirectEntity).getHeldItem() : null;
/* 34 */     String s = "death.attack." + this.damageType;
/* 35 */     String s1 = s + ".item";
/* 36 */     return (itemstack != null) && (itemstack.hasDisplayName()) && (StatCollector.canTranslate(s1)) ? new ChatComponentTranslation(s1, new Object[] { p_151519_1_.getDisplayName(), ichatcomponent, itemstack.getChatComponent() }) : new ChatComponentTranslation(s, new Object[] { p_151519_1_.getDisplayName(), ichatcomponent });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\EntityDamageSourceIndirect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */