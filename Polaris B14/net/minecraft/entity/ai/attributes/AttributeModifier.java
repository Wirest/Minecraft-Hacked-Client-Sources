/*     */ package net.minecraft.entity.ai.attributes;
/*     */ 
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.apache.commons.lang3.Validate;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AttributeModifier
/*     */ {
/*     */   private final double amount;
/*     */   private final int operation;
/*     */   private final String name;
/*     */   private final UUID id;
/*     */   private boolean isSaved;
/*     */   
/*     */   public AttributeModifier(String nameIn, double amountIn, int operationIn)
/*     */   {
/*  22 */     this(MathHelper.getRandomUuid(ThreadLocalRandom.current()), nameIn, amountIn, operationIn);
/*     */   }
/*     */   
/*     */   public AttributeModifier(UUID idIn, String nameIn, double amountIn, int operationIn)
/*     */   {
/*  27 */     this.isSaved = true;
/*  28 */     this.id = idIn;
/*  29 */     this.name = nameIn;
/*  30 */     this.amount = amountIn;
/*  31 */     this.operation = operationIn;
/*  32 */     Validate.notEmpty(nameIn, "Modifier name cannot be empty", new Object[0]);
/*  33 */     Validate.inclusiveBetween(0L, 2L, operationIn, "Invalid operation");
/*     */   }
/*     */   
/*     */   public UUID getID()
/*     */   {
/*  38 */     return this.id;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  43 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getOperation()
/*     */   {
/*  48 */     return this.operation;
/*     */   }
/*     */   
/*     */   public double getAmount()
/*     */   {
/*  53 */     return this.amount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSaved()
/*     */   {
/*  61 */     return this.isSaved;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AttributeModifier setSaved(boolean saved)
/*     */   {
/*  69 */     this.isSaved = saved;
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  75 */     if (this == p_equals_1_)
/*     */     {
/*  77 */       return true;
/*     */     }
/*  79 */     if ((p_equals_1_ != null) && (getClass() == p_equals_1_.getClass()))
/*     */     {
/*  81 */       AttributeModifier attributemodifier = (AttributeModifier)p_equals_1_;
/*     */       
/*  83 */       if (this.id != null)
/*     */       {
/*  85 */         if (!this.id.equals(attributemodifier.id))
/*     */         {
/*  87 */           return false;
/*     */         }
/*     */       }
/*  90 */       else if (attributemodifier.id != null)
/*     */       {
/*  92 */         return false;
/*     */       }
/*     */       
/*  95 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  99 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 105 */     return this.id != null ? this.id.hashCode() : 0;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 110 */     return "AttributeModifier{amount=" + this.amount + ", operation=" + this.operation + ", name='" + this.name + '\'' + ", id=" + this.id + ", serialize=" + this.isSaved + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\attributes\AttributeModifier.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */