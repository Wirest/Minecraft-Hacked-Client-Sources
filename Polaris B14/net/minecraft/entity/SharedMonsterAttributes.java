/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.ai.attributes.RangedAttribute;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SharedMonsterAttributes
/*     */ {
/*  17 */   private static final Logger logger = ;
/*  18 */   public static final IAttribute maxHealth = new RangedAttribute(null, "generic.maxHealth", 20.0D, 0.0D, 1024.0D).setDescription("Max Health").setShouldWatch(true);
/*  19 */   public static final IAttribute followRange = new RangedAttribute(null, "generic.followRange", 32.0D, 0.0D, 2048.0D).setDescription("Follow Range");
/*  20 */   public static final IAttribute knockbackResistance = new RangedAttribute(null, "generic.knockbackResistance", 0.0D, 0.0D, 1.0D).setDescription("Knockback Resistance");
/*  21 */   public static final IAttribute movementSpeed = new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071D, 0.0D, 1024.0D).setDescription("Movement Speed").setShouldWatch(true);
/*  22 */   public static final IAttribute attackDamage = new RangedAttribute(null, "generic.attackDamage", 2.0D, 0.0D, 2048.0D);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static NBTTagList writeBaseAttributeMapToNBT(BaseAttributeMap p_111257_0_)
/*     */   {
/*  29 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/*  31 */     for (IAttributeInstance iattributeinstance : p_111257_0_.getAllAttributes())
/*     */     {
/*  33 */       nbttaglist.appendTag(writeAttributeInstanceToNBT(iattributeinstance));
/*     */     }
/*     */     
/*  36 */     return nbttaglist;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance p_111261_0_)
/*     */   {
/*  44 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  45 */     IAttribute iattribute = p_111261_0_.getAttribute();
/*  46 */     nbttagcompound.setString("Name", iattribute.getAttributeUnlocalizedName());
/*  47 */     nbttagcompound.setDouble("Base", p_111261_0_.getBaseValue());
/*  48 */     Collection<AttributeModifier> collection = p_111261_0_.func_111122_c();
/*     */     
/*  50 */     if ((collection != null) && (!collection.isEmpty()))
/*     */     {
/*  52 */       NBTTagList nbttaglist = new NBTTagList();
/*     */       
/*  54 */       for (AttributeModifier attributemodifier : collection)
/*     */       {
/*  56 */         if (attributemodifier.isSaved())
/*     */         {
/*  58 */           nbttaglist.appendTag(writeAttributeModifierToNBT(attributemodifier));
/*     */         }
/*     */       }
/*     */       
/*  62 */       nbttagcompound.setTag("Modifiers", nbttaglist);
/*     */     }
/*     */     
/*  65 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier p_111262_0_)
/*     */   {
/*  73 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  74 */     nbttagcompound.setString("Name", p_111262_0_.getName());
/*  75 */     nbttagcompound.setDouble("Amount", p_111262_0_.getAmount());
/*  76 */     nbttagcompound.setInteger("Operation", p_111262_0_.getOperation());
/*  77 */     nbttagcompound.setLong("UUIDMost", p_111262_0_.getID().getMostSignificantBits());
/*  78 */     nbttagcompound.setLong("UUIDLeast", p_111262_0_.getID().getLeastSignificantBits());
/*  79 */     return nbttagcompound;
/*     */   }
/*     */   
/*     */   public static void func_151475_a(BaseAttributeMap p_151475_0_, NBTTagList p_151475_1_)
/*     */   {
/*  84 */     for (int i = 0; i < p_151475_1_.tagCount(); i++)
/*     */     {
/*  86 */       NBTTagCompound nbttagcompound = p_151475_1_.getCompoundTagAt(i);
/*  87 */       IAttributeInstance iattributeinstance = p_151475_0_.getAttributeInstanceByName(nbttagcompound.getString("Name"));
/*     */       
/*  89 */       if (iattributeinstance != null)
/*     */       {
/*  91 */         applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
/*     */       }
/*     */       else
/*     */       {
/*  95 */         logger.warn("Ignoring unknown attribute '" + nbttagcompound.getString("Name") + "'");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void applyModifiersToAttributeInstance(IAttributeInstance p_111258_0_, NBTTagCompound p_111258_1_)
/*     */   {
/* 102 */     p_111258_0_.setBaseValue(p_111258_1_.getDouble("Base"));
/*     */     
/* 104 */     if (p_111258_1_.hasKey("Modifiers", 9))
/*     */     {
/* 106 */       NBTTagList nbttaglist = p_111258_1_.getTagList("Modifiers", 10);
/*     */       
/* 108 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/* 110 */         AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));
/*     */         
/* 112 */         if (attributemodifier != null)
/*     */         {
/* 114 */           AttributeModifier attributemodifier1 = p_111258_0_.getModifier(attributemodifier.getID());
/*     */           
/* 116 */           if (attributemodifier1 != null)
/*     */           {
/* 118 */             p_111258_0_.removeModifier(attributemodifier1);
/*     */           }
/*     */           
/* 121 */           p_111258_0_.applyModifier(attributemodifier);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound p_111259_0_)
/*     */   {
/* 132 */     UUID uuid = new UUID(p_111259_0_.getLong("UUIDMost"), p_111259_0_.getLong("UUIDLeast"));
/*     */     
/*     */     try
/*     */     {
/* 136 */       return new AttributeModifier(uuid, p_111259_0_.getString("Name"), p_111259_0_.getDouble("Amount"), p_111259_0_.getInteger("Operation"));
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 140 */       logger.warn("Unable to create attribute: " + exception.getMessage()); }
/* 141 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\SharedMonsterAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */