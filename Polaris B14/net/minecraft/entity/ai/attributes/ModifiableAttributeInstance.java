/*     */ package net.minecraft.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModifiableAttributeInstance
/*     */   implements IAttributeInstance
/*     */ {
/*     */   private final BaseAttributeMap attributeMap;
/*     */   private final IAttribute genericAttribute;
/*  18 */   private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.newHashMap();
/*  19 */   private final Map<String, Set<AttributeModifier>> mapByName = Maps.newHashMap();
/*  20 */   private final Map<UUID, AttributeModifier> mapByUUID = Maps.newHashMap();
/*     */   private double baseValue;
/*  22 */   private boolean needsUpdate = true;
/*     */   private double cachedValue;
/*     */   
/*     */   public ModifiableAttributeInstance(BaseAttributeMap attributeMapIn, IAttribute genericAttributeIn)
/*     */   {
/*  27 */     this.attributeMap = attributeMapIn;
/*  28 */     this.genericAttribute = genericAttributeIn;
/*  29 */     this.baseValue = genericAttributeIn.getDefaultValue();
/*     */     
/*  31 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  33 */       this.mapByOperation.put(Integer.valueOf(i), Sets.newHashSet());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IAttribute getAttribute()
/*     */   {
/*  42 */     return this.genericAttribute;
/*     */   }
/*     */   
/*     */   public double getBaseValue()
/*     */   {
/*  47 */     return this.baseValue;
/*     */   }
/*     */   
/*     */   public void setBaseValue(double baseValue)
/*     */   {
/*  52 */     if (baseValue != getBaseValue())
/*     */     {
/*  54 */       this.baseValue = baseValue;
/*  55 */       flagForUpdate();
/*     */     }
/*     */   }
/*     */   
/*     */   public Collection<AttributeModifier> getModifiersByOperation(int operation)
/*     */   {
/*  61 */     return (Collection)this.mapByOperation.get(Integer.valueOf(operation));
/*     */   }
/*     */   
/*     */   public Collection<AttributeModifier> func_111122_c()
/*     */   {
/*  66 */     Set<AttributeModifier> set = Sets.newHashSet();
/*     */     
/*  68 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  70 */       set.addAll(getModifiersByOperation(i));
/*     */     }
/*     */     
/*  73 */     return set;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AttributeModifier getModifier(UUID uuid)
/*     */   {
/*  81 */     return (AttributeModifier)this.mapByUUID.get(uuid);
/*     */   }
/*     */   
/*     */   public boolean hasModifier(AttributeModifier modifier)
/*     */   {
/*  86 */     return this.mapByUUID.get(modifier.getID()) != null;
/*     */   }
/*     */   
/*     */   public void applyModifier(AttributeModifier modifier)
/*     */   {
/*  91 */     if (getModifier(modifier.getID()) != null)
/*     */     {
/*  93 */       throw new IllegalArgumentException("Modifier is already applied on this attribute!");
/*     */     }
/*     */     
/*     */ 
/*  97 */     Set<AttributeModifier> set = (Set)this.mapByName.get(modifier.getName());
/*     */     
/*  99 */     if (set == null)
/*     */     {
/* 101 */       set = Sets.newHashSet();
/* 102 */       this.mapByName.put(modifier.getName(), set);
/*     */     }
/*     */     
/* 105 */     ((Set)this.mapByOperation.get(Integer.valueOf(modifier.getOperation()))).add(modifier);
/* 106 */     set.add(modifier);
/* 107 */     this.mapByUUID.put(modifier.getID(), modifier);
/* 108 */     flagForUpdate();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void flagForUpdate()
/*     */   {
/* 114 */     this.needsUpdate = true;
/* 115 */     this.attributeMap.func_180794_a(this);
/*     */   }
/*     */   
/*     */   public void removeModifier(AttributeModifier modifier)
/*     */   {
/* 120 */     for (int i = 0; i < 3; i++)
/*     */     {
/* 122 */       Set<AttributeModifier> set = (Set)this.mapByOperation.get(Integer.valueOf(i));
/* 123 */       set.remove(modifier);
/*     */     }
/*     */     
/* 126 */     Set<AttributeModifier> set1 = (Set)this.mapByName.get(modifier.getName());
/*     */     
/* 128 */     if (set1 != null)
/*     */     {
/* 130 */       set1.remove(modifier);
/*     */       
/* 132 */       if (set1.isEmpty())
/*     */       {
/* 134 */         this.mapByName.remove(modifier.getName());
/*     */       }
/*     */     }
/*     */     
/* 138 */     this.mapByUUID.remove(modifier.getID());
/* 139 */     flagForUpdate();
/*     */   }
/*     */   
/*     */   public void removeAllModifiers()
/*     */   {
/* 144 */     Collection<AttributeModifier> collection = func_111122_c();
/*     */     
/* 146 */     if (collection != null)
/*     */     {
/* 148 */       for (AttributeModifier attributemodifier : Lists.newArrayList(collection))
/*     */       {
/* 150 */         removeModifier(attributemodifier);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public double getAttributeValue()
/*     */   {
/* 157 */     if (this.needsUpdate)
/*     */     {
/* 159 */       this.cachedValue = computeValue();
/* 160 */       this.needsUpdate = false;
/*     */     }
/*     */     
/* 163 */     return this.cachedValue;
/*     */   }
/*     */   
/*     */   private double computeValue()
/*     */   {
/* 168 */     double d0 = getBaseValue();
/*     */     
/* 170 */     for (AttributeModifier attributemodifier : func_180375_b(0))
/*     */     {
/* 172 */       d0 += attributemodifier.getAmount();
/*     */     }
/*     */     
/* 175 */     double d1 = d0;
/*     */     
/* 177 */     for (AttributeModifier attributemodifier1 : func_180375_b(1))
/*     */     {
/* 179 */       d1 += d0 * attributemodifier1.getAmount();
/*     */     }
/*     */     
/* 182 */     for (AttributeModifier attributemodifier2 : func_180375_b(2))
/*     */     {
/* 184 */       d1 *= (1.0D + attributemodifier2.getAmount());
/*     */     }
/*     */     
/* 187 */     return this.genericAttribute.clampValue(d1);
/*     */   }
/*     */   
/*     */   private Collection<AttributeModifier> func_180375_b(int p_180375_1_)
/*     */   {
/* 192 */     Set<AttributeModifier> set = Sets.newHashSet(getModifiersByOperation(p_180375_1_));
/*     */     
/* 194 */     for (IAttribute iattribute = this.genericAttribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d())
/*     */     {
/* 196 */       IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);
/*     */       
/* 198 */       if (iattributeinstance != null)
/*     */       {
/* 200 */         set.addAll(iattributeinstance.getModifiersByOperation(p_180375_1_));
/*     */       }
/*     */     }
/*     */     
/* 204 */     return set;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\attributes\ModifiableAttributeInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */