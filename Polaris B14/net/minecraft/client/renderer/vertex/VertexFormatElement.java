/*     */ package net.minecraft.client.renderer.vertex;
/*     */ 
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class VertexFormatElement
/*     */ {
/*   8 */   private static final Logger LOGGER = ;
/*     */   private final EnumType type;
/*     */   private final EnumUsage usage;
/*     */   private int index;
/*     */   private int elementCount;
/*     */   
/*     */   public VertexFormatElement(int indexIn, EnumType typeIn, EnumUsage usageIn, int count)
/*     */   {
/*  16 */     if (!func_177372_a(indexIn, usageIn))
/*     */     {
/*  18 */       LOGGER.warn("Multiple vertex elements of the same type other than UVs are not supported. Forcing type to UV.");
/*  19 */       this.usage = EnumUsage.UV;
/*     */     }
/*     */     else
/*     */     {
/*  23 */       this.usage = usageIn;
/*     */     }
/*     */     
/*  26 */     this.type = typeIn;
/*  27 */     this.index = indexIn;
/*  28 */     this.elementCount = count;
/*     */   }
/*     */   
/*     */   private final boolean func_177372_a(int p_177372_1_, EnumUsage p_177372_2_)
/*     */   {
/*  33 */     return (p_177372_1_ == 0) || (p_177372_2_ == EnumUsage.UV);
/*     */   }
/*     */   
/*     */   public final EnumType getType()
/*     */   {
/*  38 */     return this.type;
/*     */   }
/*     */   
/*     */   public final EnumUsage getUsage()
/*     */   {
/*  43 */     return this.usage;
/*     */   }
/*     */   
/*     */   public final int getElementCount()
/*     */   {
/*  48 */     return this.elementCount;
/*     */   }
/*     */   
/*     */   public final int getIndex()
/*     */   {
/*  53 */     return this.index;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  58 */     return this.elementCount + "," + this.usage.getDisplayName() + "," + this.type.getDisplayName();
/*     */   }
/*     */   
/*     */   public final int getSize()
/*     */   {
/*  63 */     return this.type.getSize() * this.elementCount;
/*     */   }
/*     */   
/*     */   public final boolean isPositionElement()
/*     */   {
/*  68 */     return this.usage == EnumUsage.POSITION;
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  73 */     if (this == p_equals_1_)
/*     */     {
/*  75 */       return true;
/*     */     }
/*  77 */     if ((p_equals_1_ != null) && (getClass() == p_equals_1_.getClass()))
/*     */     {
/*  79 */       VertexFormatElement vertexformatelement = (VertexFormatElement)p_equals_1_;
/*  80 */       return this.elementCount == vertexformatelement.elementCount;
/*     */     }
/*     */     
/*     */ 
/*  84 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  90 */     int i = this.type.hashCode();
/*  91 */     i = 31 * i + this.usage.hashCode();
/*  92 */     i = 31 * i + this.index;
/*  93 */     i = 31 * i + this.elementCount;
/*  94 */     return i;
/*     */   }
/*     */   
/*     */   public static enum EnumType
/*     */   {
/*  99 */     FLOAT(4, "Float", 5126), 
/* 100 */     UBYTE(1, "Unsigned Byte", 5121), 
/* 101 */     BYTE(1, "Byte", 5120), 
/* 102 */     USHORT(2, "Unsigned Short", 5123), 
/* 103 */     SHORT(2, "Short", 5122), 
/* 104 */     UINT(4, "Unsigned Int", 5125), 
/* 105 */     INT(4, "Int", 5124);
/*     */     
/*     */     private final int size;
/*     */     private final String displayName;
/*     */     private final int glConstant;
/*     */     
/*     */     private EnumType(int sizeIn, String displayNameIn, int glConstantIn)
/*     */     {
/* 113 */       this.size = sizeIn;
/* 114 */       this.displayName = displayNameIn;
/* 115 */       this.glConstant = glConstantIn;
/*     */     }
/*     */     
/*     */     public int getSize()
/*     */     {
/* 120 */       return this.size;
/*     */     }
/*     */     
/*     */     public String getDisplayName()
/*     */     {
/* 125 */       return this.displayName;
/*     */     }
/*     */     
/*     */     public int getGlConstant()
/*     */     {
/* 130 */       return this.glConstant;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum EnumUsage
/*     */   {
/* 136 */     POSITION("Position"), 
/* 137 */     NORMAL("Normal"), 
/* 138 */     COLOR("Vertex Color"), 
/* 139 */     UV("UV"), 
/* 140 */     MATRIX("Bone Matrix"), 
/* 141 */     BLEND_WEIGHT("Blend Weight"), 
/* 142 */     PADDING("Padding");
/*     */     
/*     */     private final String displayName;
/*     */     
/*     */     private EnumUsage(String displayNameIn)
/*     */     {
/* 148 */       this.displayName = displayNameIn;
/*     */     }
/*     */     
/*     */     public String getDisplayName()
/*     */     {
/* 153 */       return this.displayName;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\renderer\vertex\VertexFormatElement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */