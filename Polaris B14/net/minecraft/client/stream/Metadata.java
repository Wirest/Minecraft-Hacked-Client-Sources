/*    */ package net.minecraft.client.stream;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ import com.google.common.base.Objects.ToStringHelper;
/*    */ import com.google.gson.Gson;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Metadata
/*    */ {
/* 10 */   private static final Gson field_152811_a = new Gson();
/*    */   private final String name;
/*    */   private String description;
/*    */   private Map<String, String> payload;
/*    */   
/*    */   public Metadata(String p_i46345_1_, String p_i46345_2_)
/*    */   {
/* 17 */     this.name = p_i46345_1_;
/* 18 */     this.description = p_i46345_2_;
/*    */   }
/*    */   
/*    */   public Metadata(String p_i1030_1_)
/*    */   {
/* 23 */     this(p_i1030_1_, null);
/*    */   }
/*    */   
/*    */   public void func_152807_a(String p_152807_1_)
/*    */   {
/* 28 */     this.description = p_152807_1_;
/*    */   }
/*    */   
/*    */   public String func_152809_a()
/*    */   {
/* 33 */     return this.description == null ? this.name : this.description;
/*    */   }
/*    */   
/*    */   public void func_152808_a(String p_152808_1_, String p_152808_2_)
/*    */   {
/* 38 */     if (this.payload == null)
/*    */     {
/* 40 */       this.payload = com.google.common.collect.Maps.newHashMap();
/*    */     }
/*    */     
/* 43 */     if (this.payload.size() > 50)
/*    */     {
/* 45 */       throw new IllegalArgumentException("Metadata payload is full, cannot add more to it!");
/*    */     }
/* 47 */     if (p_152808_1_ == null)
/*    */     {
/* 49 */       throw new IllegalArgumentException("Metadata payload key cannot be null!");
/*    */     }
/* 51 */     if (p_152808_1_.length() > 255)
/*    */     {
/* 53 */       throw new IllegalArgumentException("Metadata payload key is too long!");
/*    */     }
/* 55 */     if (p_152808_2_ == null)
/*    */     {
/* 57 */       throw new IllegalArgumentException("Metadata payload value cannot be null!");
/*    */     }
/* 59 */     if (p_152808_2_.length() > 255)
/*    */     {
/* 61 */       throw new IllegalArgumentException("Metadata payload value is too long!");
/*    */     }
/*    */     
/*    */ 
/* 65 */     this.payload.put(p_152808_1_, p_152808_2_);
/*    */   }
/*    */   
/*    */ 
/*    */   public String func_152806_b()
/*    */   {
/* 71 */     return (this.payload != null) && (!this.payload.isEmpty()) ? field_152811_a.toJson(this.payload) : null;
/*    */   }
/*    */   
/*    */   public String func_152810_c()
/*    */   {
/* 76 */     return this.name;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 81 */     return Objects.toStringHelper(this).add("name", this.name).add("description", this.description).add("data", func_152806_b()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\stream\Metadata.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */