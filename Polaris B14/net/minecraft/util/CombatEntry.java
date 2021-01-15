/*    */ package net.minecraft.util;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ 
/*    */ public class CombatEntry
/*    */ {
/*    */   private final DamageSource damageSrc;
/*    */   private final int field_94567_b;
/*    */   private final float damage;
/*    */   private final float health;
/*    */   private final String field_94566_e;
/*    */   private final float fallDistance;
/*    */   
/*    */   public CombatEntry(DamageSource damageSrcIn, int p_i1564_2_, float healthAmount, float damageAmount, String p_i1564_5_, float fallDistanceIn)
/*    */   {
/* 16 */     this.damageSrc = damageSrcIn;
/* 17 */     this.field_94567_b = p_i1564_2_;
/* 18 */     this.damage = damageAmount;
/* 19 */     this.health = healthAmount;
/* 20 */     this.field_94566_e = p_i1564_5_;
/* 21 */     this.fallDistance = fallDistanceIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public DamageSource getDamageSrc()
/*    */   {
/* 29 */     return this.damageSrc;
/*    */   }
/*    */   
/*    */   public float func_94563_c()
/*    */   {
/* 34 */     return this.damage;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean isLivingDamageSrc()
/*    */   {
/* 42 */     return this.damageSrc.getEntity() instanceof net.minecraft.entity.EntityLivingBase;
/*    */   }
/*    */   
/*    */   public String func_94562_g()
/*    */   {
/* 47 */     return this.field_94566_e;
/*    */   }
/*    */   
/*    */   public IChatComponent getDamageSrcDisplayName()
/*    */   {
/* 52 */     return getDamageSrc().getEntity() == null ? null : getDamageSrc().getEntity().getDisplayName();
/*    */   }
/*    */   
/*    */   public float getDamageAmount()
/*    */   {
/* 57 */     return this.damageSrc == DamageSource.outOfWorld ? Float.MAX_VALUE : this.fallDistance;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\CombatEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */