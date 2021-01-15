/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public class CombatTracker
/*     */ {
/*  14 */   private final List<CombatEntry> combatEntries = com.google.common.collect.Lists.newArrayList();
/*     */   
/*     */   private final EntityLivingBase fighter;
/*     */   
/*     */   private int field_94555_c;
/*     */   private int field_152775_d;
/*     */   private int field_152776_e;
/*     */   private boolean field_94552_d;
/*     */   private boolean field_94553_e;
/*     */   private String field_94551_f;
/*     */   
/*     */   public CombatTracker(EntityLivingBase fighterIn)
/*     */   {
/*  27 */     this.fighter = fighterIn;
/*     */   }
/*     */   
/*     */   public void func_94545_a()
/*     */   {
/*  32 */     func_94542_g();
/*     */     
/*  34 */     if (this.fighter.isOnLadder())
/*     */     {
/*  36 */       Block block = this.fighter.worldObj.getBlockState(new BlockPos(this.fighter.posX, this.fighter.getEntityBoundingBox().minY, this.fighter.posZ)).getBlock();
/*     */       
/*  38 */       if (block == Blocks.ladder)
/*     */       {
/*  40 */         this.field_94551_f = "ladder";
/*     */       }
/*  42 */       else if (block == Blocks.vine)
/*     */       {
/*  44 */         this.field_94551_f = "vines";
/*     */       }
/*     */     }
/*  47 */     else if (this.fighter.isInWater())
/*     */     {
/*  49 */       this.field_94551_f = "water";
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void trackDamage(DamageSource damageSrc, float healthIn, float damageAmount)
/*     */   {
/*  58 */     reset();
/*  59 */     func_94545_a();
/*  60 */     CombatEntry combatentry = new CombatEntry(damageSrc, this.fighter.ticksExisted, healthIn, damageAmount, this.field_94551_f, this.fighter.fallDistance);
/*  61 */     this.combatEntries.add(combatentry);
/*  62 */     this.field_94555_c = this.fighter.ticksExisted;
/*  63 */     this.field_94553_e = true;
/*     */     
/*  65 */     if ((combatentry.isLivingDamageSrc()) && (!this.field_94552_d) && (this.fighter.isEntityAlive()))
/*     */     {
/*  67 */       this.field_94552_d = true;
/*  68 */       this.field_152775_d = this.fighter.ticksExisted;
/*  69 */       this.field_152776_e = this.field_152775_d;
/*  70 */       this.fighter.sendEnterCombat();
/*     */     }
/*     */   }
/*     */   
/*     */   public IChatComponent getDeathMessage()
/*     */   {
/*  76 */     if (this.combatEntries.size() == 0)
/*     */     {
/*  78 */       return new ChatComponentTranslation("death.attack.generic", new Object[] { this.fighter.getDisplayName() });
/*     */     }
/*     */     
/*     */ 
/*  82 */     CombatEntry combatentry = func_94544_f();
/*  83 */     CombatEntry combatentry1 = (CombatEntry)this.combatEntries.get(this.combatEntries.size() - 1);
/*  84 */     IChatComponent ichatcomponent1 = combatentry1.getDamageSrcDisplayName();
/*  85 */     Entity entity = combatentry1.getDamageSrc().getEntity();
/*     */     IChatComponent ichatcomponent;
/*     */     IChatComponent ichatcomponent;
/*  88 */     if ((combatentry != null) && (combatentry1.getDamageSrc() == DamageSource.fall))
/*     */     {
/*  90 */       IChatComponent ichatcomponent2 = combatentry.getDamageSrcDisplayName();
/*     */       IChatComponent ichatcomponent;
/*  92 */       if ((combatentry.getDamageSrc() != DamageSource.fall) && (combatentry.getDamageSrc() != DamageSource.outOfWorld)) {
/*     */         IChatComponent ichatcomponent;
/*  94 */         if ((ichatcomponent2 != null) && ((ichatcomponent1 == null) || (!ichatcomponent2.equals(ichatcomponent1))))
/*     */         {
/*  96 */           Entity entity1 = combatentry.getDamageSrc().getEntity();
/*  97 */           ItemStack itemstack1 = (entity1 instanceof EntityLivingBase) ? ((EntityLivingBase)entity1).getHeldItem() : null;
/*     */           IChatComponent ichatcomponent;
/*  99 */           if ((itemstack1 != null) && (itemstack1.hasDisplayName()))
/*     */           {
/* 101 */             ichatcomponent = new ChatComponentTranslation("death.fell.assist.item", new Object[] { this.fighter.getDisplayName(), ichatcomponent2, itemstack1.getChatComponent() });
/*     */           }
/*     */           else
/*     */           {
/* 105 */             ichatcomponent = new ChatComponentTranslation("death.fell.assist", new Object[] { this.fighter.getDisplayName(), ichatcomponent2 }); }
/*     */         } else {
/*     */           IChatComponent ichatcomponent;
/* 108 */           if (ichatcomponent1 != null)
/*     */           {
/* 110 */             ItemStack itemstack = (entity instanceof EntityLivingBase) ? ((EntityLivingBase)entity).getHeldItem() : null;
/*     */             IChatComponent ichatcomponent;
/* 112 */             if ((itemstack != null) && (itemstack.hasDisplayName()))
/*     */             {
/* 114 */               ichatcomponent = new ChatComponentTranslation("death.fell.finish.item", new Object[] { this.fighter.getDisplayName(), ichatcomponent1, itemstack.getChatComponent() });
/*     */             }
/*     */             else
/*     */             {
/* 118 */               ichatcomponent = new ChatComponentTranslation("death.fell.finish", new Object[] { this.fighter.getDisplayName(), ichatcomponent1 });
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 123 */             ichatcomponent = new ChatComponentTranslation("death.fell.killer", new Object[] { this.fighter.getDisplayName() });
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 128 */         ichatcomponent = new ChatComponentTranslation("death.fell.accident." + func_94548_b(combatentry), new Object[] { this.fighter.getDisplayName() });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 133 */       ichatcomponent = combatentry1.getDamageSrc().getDeathMessage(this.fighter);
/*     */     }
/*     */     
/* 136 */     return ichatcomponent;
/*     */   }
/*     */   
/*     */ 
/*     */   public EntityLivingBase func_94550_c()
/*     */   {
/* 142 */     EntityLivingBase entitylivingbase = null;
/* 143 */     EntityPlayer entityplayer = null;
/* 144 */     float f = 0.0F;
/* 145 */     float f1 = 0.0F;
/*     */     
/* 147 */     for (CombatEntry combatentry : this.combatEntries)
/*     */     {
/* 149 */       if (((combatentry.getDamageSrc().getEntity() instanceof EntityPlayer)) && ((entityplayer == null) || (combatentry.func_94563_c() > f1)))
/*     */       {
/* 151 */         f1 = combatentry.func_94563_c();
/* 152 */         entityplayer = (EntityPlayer)combatentry.getDamageSrc().getEntity();
/*     */       }
/*     */       
/* 155 */       if (((combatentry.getDamageSrc().getEntity() instanceof EntityLivingBase)) && ((entitylivingbase == null) || (combatentry.func_94563_c() > f)))
/*     */       {
/* 157 */         f = combatentry.func_94563_c();
/* 158 */         entitylivingbase = (EntityLivingBase)combatentry.getDamageSrc().getEntity();
/*     */       }
/*     */     }
/*     */     
/* 162 */     if ((entityplayer != null) && (f1 >= f / 3.0F))
/*     */     {
/* 164 */       return entityplayer;
/*     */     }
/*     */     
/*     */ 
/* 168 */     return entitylivingbase;
/*     */   }
/*     */   
/*     */ 
/*     */   private CombatEntry func_94544_f()
/*     */   {
/* 174 */     CombatEntry combatentry = null;
/* 175 */     CombatEntry combatentry1 = null;
/* 176 */     int i = 0;
/* 177 */     float f = 0.0F;
/*     */     
/* 179 */     for (int j = 0; j < this.combatEntries.size(); j++)
/*     */     {
/* 181 */       CombatEntry combatentry2 = (CombatEntry)this.combatEntries.get(j);
/* 182 */       CombatEntry combatentry3 = j > 0 ? (CombatEntry)this.combatEntries.get(j - 1) : null;
/*     */       
/* 184 */       if (((combatentry2.getDamageSrc() == DamageSource.fall) || (combatentry2.getDamageSrc() == DamageSource.outOfWorld)) && (combatentry2.getDamageAmount() > 0.0F) && ((combatentry == null) || (combatentry2.getDamageAmount() > f)))
/*     */       {
/* 186 */         if (j > 0)
/*     */         {
/* 188 */           combatentry = combatentry3;
/*     */         }
/*     */         else
/*     */         {
/* 192 */           combatentry = combatentry2;
/*     */         }
/*     */         
/* 195 */         f = combatentry2.getDamageAmount();
/*     */       }
/*     */       
/* 198 */       if ((combatentry2.func_94562_g() != null) && ((combatentry1 == null) || (combatentry2.func_94563_c() > i)))
/*     */       {
/* 200 */         combatentry1 = combatentry2;
/*     */       }
/*     */     }
/*     */     
/* 204 */     if ((f > 5.0F) && (combatentry != null))
/*     */     {
/* 206 */       return combatentry;
/*     */     }
/* 208 */     if ((i > 5) && (combatentry1 != null))
/*     */     {
/* 210 */       return combatentry1;
/*     */     }
/*     */     
/*     */ 
/* 214 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private String func_94548_b(CombatEntry p_94548_1_)
/*     */   {
/* 220 */     return p_94548_1_.func_94562_g() == null ? "generic" : p_94548_1_.func_94562_g();
/*     */   }
/*     */   
/*     */   public int func_180134_f()
/*     */   {
/* 225 */     return this.field_94552_d ? this.fighter.ticksExisted - this.field_152775_d : this.field_152776_e - this.field_152775_d;
/*     */   }
/*     */   
/*     */   private void func_94542_g()
/*     */   {
/* 230 */     this.field_94551_f = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/* 238 */     int i = this.field_94552_d ? 300 : 100;
/*     */     
/* 240 */     if ((this.field_94553_e) && ((!this.fighter.isEntityAlive()) || (this.fighter.ticksExisted - this.field_94555_c > i)))
/*     */     {
/* 242 */       boolean flag = this.field_94552_d;
/* 243 */       this.field_94553_e = false;
/* 244 */       this.field_94552_d = false;
/* 245 */       this.field_152776_e = this.fighter.ticksExisted;
/*     */       
/* 247 */       if (flag)
/*     */       {
/* 249 */         this.fighter.sendEndCombat();
/*     */       }
/*     */       
/* 252 */       this.combatEntries.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EntityLivingBase getFighter()
/*     */   {
/* 261 */     return this.fighter;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\CombatTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */