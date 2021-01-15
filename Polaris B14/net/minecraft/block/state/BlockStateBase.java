/*     */ package net.minecraft.block.state;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.util.RegistryNamespacedDefaultedByKey;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public abstract class BlockStateBase implements IBlockState
/*     */ {
/*  15 */   private static final Joiner COMMA_JOINER = Joiner.on(',');
/*  16 */   private static final com.google.common.base.Function MAP_ENTRY_TO_STRING = new com.google.common.base.Function()
/*     */   {
/*     */     private static final String __OBFID = "CL_00002031";
/*     */     
/*     */     public String apply(Map.Entry p_apply_1_) {
/*  21 */       if (p_apply_1_ == null)
/*     */       {
/*  23 */         return "<NULL>";
/*     */       }
/*     */       
/*     */ 
/*  27 */       IProperty iproperty = (IProperty)p_apply_1_.getKey();
/*  28 */       return iproperty.getName() + "=" + iproperty.getName((Comparable)p_apply_1_.getValue());
/*     */     }
/*     */     
/*     */     public Object apply(Object p_apply_1_)
/*     */     {
/*  33 */       return apply((Map.Entry)p_apply_1_);
/*     */     }
/*     */   };
/*     */   private static final String __OBFID = "CL_00002032";
/*  37 */   private int blockId = -1;
/*  38 */   private int blockStateId = -1;
/*  39 */   private int metadata = -1;
/*  40 */   private ResourceLocation blockLocation = null;
/*     */   
/*     */   public int getBlockId()
/*     */   {
/*  44 */     if (this.blockId < 0)
/*     */     {
/*  46 */       this.blockId = Block.getIdFromBlock(getBlock());
/*     */     }
/*     */     
/*  49 */     return this.blockId;
/*     */   }
/*     */   
/*     */   public int getBlockStateId()
/*     */   {
/*  54 */     if (this.blockStateId < 0)
/*     */     {
/*  56 */       this.blockStateId = Block.getStateId(this);
/*     */     }
/*     */     
/*  59 */     return this.blockStateId;
/*     */   }
/*     */   
/*     */   public int getMetadata()
/*     */   {
/*  64 */     if (this.metadata < 0)
/*     */     {
/*  66 */       this.metadata = getBlock().getMetaFromState(this);
/*     */     }
/*     */     
/*  69 */     return this.metadata;
/*     */   }
/*     */   
/*     */   public ResourceLocation getBlockLocation()
/*     */   {
/*  74 */     if (this.blockLocation == null)
/*     */     {
/*  76 */       this.blockLocation = ((ResourceLocation)Block.blockRegistry.getNameForObject(getBlock()));
/*     */     }
/*     */     
/*  79 */     return this.blockLocation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState cycleProperty(IProperty property)
/*     */   {
/*  88 */     return withProperty(property, (Comparable)cyclePropertyValue(property.getAllowedValues(), getValue(property)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static Object cyclePropertyValue(Collection values, Object currentValue)
/*     */   {
/*  96 */     Iterator iterator = values.iterator();
/*     */     
/*  98 */     while (iterator.hasNext())
/*     */     {
/* 100 */       if (iterator.next().equals(currentValue))
/*     */       {
/* 102 */         if (iterator.hasNext())
/*     */         {
/* 104 */           return iterator.next();
/*     */         }
/*     */         
/* 107 */         return values.iterator().next();
/*     */       }
/*     */     }
/*     */     
/* 111 */     return iterator.next();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 116 */     StringBuilder stringbuilder = new StringBuilder();
/* 117 */     stringbuilder.append(Block.blockRegistry.getNameForObject(getBlock()));
/*     */     
/* 119 */     if (!getProperties().isEmpty())
/*     */     {
/* 121 */       stringbuilder.append("[");
/* 122 */       COMMA_JOINER.appendTo(stringbuilder, com.google.common.collect.Iterables.transform(getProperties().entrySet(), MAP_ENTRY_TO_STRING));
/* 123 */       stringbuilder.append("]");
/*     */     }
/*     */     
/* 126 */     return stringbuilder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\state\BlockStateBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */