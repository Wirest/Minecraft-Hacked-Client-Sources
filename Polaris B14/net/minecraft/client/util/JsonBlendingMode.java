/*     */ package net.minecraft.client.util;
/*     */ 
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ 
/*     */ public class JsonBlendingMode
/*     */ {
/*  10 */   private static JsonBlendingMode field_148118_a = null;
/*     */   private final int field_148116_b;
/*     */   private final int field_148117_c;
/*     */   private final int field_148114_d;
/*     */   private final int field_148115_e;
/*     */   private final int field_148112_f;
/*     */   private final boolean field_148113_g;
/*     */   private final boolean field_148119_h;
/*     */   
/*     */   private JsonBlendingMode(boolean p_i45084_1_, boolean p_i45084_2_, int p_i45084_3_, int p_i45084_4_, int p_i45084_5_, int p_i45084_6_, int p_i45084_7_)
/*     */   {
/*  21 */     this.field_148113_g = p_i45084_1_;
/*  22 */     this.field_148116_b = p_i45084_3_;
/*  23 */     this.field_148114_d = p_i45084_4_;
/*  24 */     this.field_148117_c = p_i45084_5_;
/*  25 */     this.field_148115_e = p_i45084_6_;
/*  26 */     this.field_148119_h = p_i45084_2_;
/*  27 */     this.field_148112_f = p_i45084_7_;
/*     */   }
/*     */   
/*     */   public JsonBlendingMode()
/*     */   {
/*  32 */     this(false, true, 1, 0, 1, 0, 32774);
/*     */   }
/*     */   
/*     */   public JsonBlendingMode(int p_i45085_1_, int p_i45085_2_, int p_i45085_3_)
/*     */   {
/*  37 */     this(false, false, p_i45085_1_, p_i45085_2_, p_i45085_1_, p_i45085_2_, p_i45085_3_);
/*     */   }
/*     */   
/*     */   public JsonBlendingMode(int p_i45086_1_, int p_i45086_2_, int p_i45086_3_, int p_i45086_4_, int p_i45086_5_)
/*     */   {
/*  42 */     this(true, false, p_i45086_1_, p_i45086_2_, p_i45086_3_, p_i45086_4_, p_i45086_5_);
/*     */   }
/*     */   
/*     */   public void func_148109_a()
/*     */   {
/*  47 */     if (!equals(field_148118_a))
/*     */     {
/*  49 */       if ((field_148118_a == null) || (this.field_148119_h != field_148118_a.func_148111_b()))
/*     */       {
/*  51 */         field_148118_a = this;
/*     */         
/*  53 */         if (this.field_148119_h)
/*     */         {
/*  55 */           GlStateManager.disableBlend();
/*  56 */           return;
/*     */         }
/*     */         
/*  59 */         GlStateManager.enableBlend();
/*     */       }
/*     */       
/*  62 */       org.lwjgl.opengl.GL14.glBlendEquation(this.field_148112_f);
/*     */       
/*  64 */       if (this.field_148113_g)
/*     */       {
/*  66 */         GlStateManager.tryBlendFuncSeparate(this.field_148116_b, this.field_148114_d, this.field_148117_c, this.field_148115_e);
/*     */       }
/*     */       else
/*     */       {
/*  70 */         GlStateManager.blendFunc(this.field_148116_b, this.field_148114_d);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean equals(Object p_equals_1_)
/*     */   {
/*  77 */     if (this == p_equals_1_)
/*     */     {
/*  79 */       return true;
/*     */     }
/*  81 */     if (!(p_equals_1_ instanceof JsonBlendingMode))
/*     */     {
/*  83 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  87 */     JsonBlendingMode jsonblendingmode = (JsonBlendingMode)p_equals_1_;
/*  88 */     return this.field_148112_f == jsonblendingmode.field_148112_f;
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  94 */     int i = this.field_148116_b;
/*  95 */     i = 31 * i + this.field_148117_c;
/*  96 */     i = 31 * i + this.field_148114_d;
/*  97 */     i = 31 * i + this.field_148115_e;
/*  98 */     i = 31 * i + this.field_148112_f;
/*  99 */     i = 31 * i + (this.field_148113_g ? 1 : 0);
/* 100 */     i = 31 * i + (this.field_148119_h ? 1 : 0);
/* 101 */     return i;
/*     */   }
/*     */   
/*     */   public boolean func_148111_b()
/*     */   {
/* 106 */     return this.field_148119_h;
/*     */   }
/*     */   
/*     */   public static JsonBlendingMode func_148110_a(JsonObject p_148110_0_)
/*     */   {
/* 111 */     if (p_148110_0_ == null)
/*     */     {
/* 113 */       return new JsonBlendingMode();
/*     */     }
/*     */     
/*     */ 
/* 117 */     int i = 32774;
/* 118 */     int j = 1;
/* 119 */     int k = 0;
/* 120 */     int l = 1;
/* 121 */     int i1 = 0;
/* 122 */     boolean flag = true;
/* 123 */     boolean flag1 = false;
/*     */     
/* 125 */     if (JsonUtils.isString(p_148110_0_, "func"))
/*     */     {
/* 127 */       i = func_148108_a(p_148110_0_.get("func").getAsString());
/*     */       
/* 129 */       if (i != 32774)
/*     */       {
/* 131 */         flag = false;
/*     */       }
/*     */     }
/*     */     
/* 135 */     if (JsonUtils.isString(p_148110_0_, "srcrgb"))
/*     */     {
/* 137 */       j = func_148107_b(p_148110_0_.get("srcrgb").getAsString());
/*     */       
/* 139 */       if (j != 1)
/*     */       {
/* 141 */         flag = false;
/*     */       }
/*     */     }
/*     */     
/* 145 */     if (JsonUtils.isString(p_148110_0_, "dstrgb"))
/*     */     {
/* 147 */       k = func_148107_b(p_148110_0_.get("dstrgb").getAsString());
/*     */       
/* 149 */       if (k != 0)
/*     */       {
/* 151 */         flag = false;
/*     */       }
/*     */     }
/*     */     
/* 155 */     if (JsonUtils.isString(p_148110_0_, "srcalpha"))
/*     */     {
/* 157 */       l = func_148107_b(p_148110_0_.get("srcalpha").getAsString());
/*     */       
/* 159 */       if (l != 1)
/*     */       {
/* 161 */         flag = false;
/*     */       }
/*     */       
/* 164 */       flag1 = true;
/*     */     }
/*     */     
/* 167 */     if (JsonUtils.isString(p_148110_0_, "dstalpha"))
/*     */     {
/* 169 */       i1 = func_148107_b(p_148110_0_.get("dstalpha").getAsString());
/*     */       
/* 171 */       if (i1 != 0)
/*     */       {
/* 173 */         flag = false;
/*     */       }
/*     */       
/* 176 */       flag1 = true;
/*     */     }
/*     */     
/* 179 */     return flag1 ? new JsonBlendingMode(j, k, l, i1, i) : flag ? new JsonBlendingMode() : new JsonBlendingMode(j, k, i);
/*     */   }
/*     */   
/*     */ 
/*     */   private static int func_148108_a(String p_148108_0_)
/*     */   {
/* 185 */     String s = p_148108_0_.trim().toLowerCase();
/* 186 */     return s.equals("max") ? 32776 : s.equals("min") ? 32775 : s.equals("reverse_subtract") ? 32779 : s.equals("reversesubtract") ? 32779 : s.equals("subtract") ? 32778 : s.equals("add") ? 32774 : 32774;
/*     */   }
/*     */   
/*     */   private static int func_148107_b(String p_148107_0_)
/*     */   {
/* 191 */     String s = p_148107_0_.trim().toLowerCase();
/* 192 */     s = s.replaceAll("_", "");
/* 193 */     s = s.replaceAll("one", "1");
/* 194 */     s = s.replaceAll("zero", "0");
/* 195 */     s = s.replaceAll("minus", "-");
/* 196 */     return s.equals("1-dstalpha") ? 773 : s.equals("dstalpha") ? 772 : s.equals("1-srcalpha") ? 771 : s.equals("srcalpha") ? 770 : s.equals("1-dstcolor") ? 775 : s.equals("dstcolor") ? 774 : s.equals("1-srccolor") ? 769 : s.equals("srccolor") ? 768 : s.equals("1") ? 1 : s.equals("0") ? 0 : -1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\util\JsonBlendingMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */