/*    */ package net.minecraft.client.util;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class JsonException extends IOException
/*    */ {
/* 11 */   private final List<Entry> field_151383_a = Lists.newArrayList();
/*    */   private final String field_151382_b;
/*    */   
/*    */   public JsonException(String p_i45279_1_)
/*    */   {
/* 16 */     this.field_151383_a.add(new Entry(null));
/* 17 */     this.field_151382_b = p_i45279_1_;
/*    */   }
/*    */   
/*    */   public JsonException(String p_i45280_1_, Throwable p_i45280_2_)
/*    */   {
/* 22 */     super(p_i45280_2_);
/* 23 */     this.field_151383_a.add(new Entry(null));
/* 24 */     this.field_151382_b = p_i45280_1_;
/*    */   }
/*    */   
/*    */   public void func_151380_a(String p_151380_1_)
/*    */   {
/* 29 */     ((Entry)this.field_151383_a.get(0)).func_151373_a(p_151380_1_);
/*    */   }
/*    */   
/*    */   public void func_151381_b(String p_151381_1_)
/*    */   {
/* 34 */     ((Entry)this.field_151383_a.get(0)).field_151376_a = p_151381_1_;
/* 35 */     this.field_151383_a.add(0, new Entry(null));
/*    */   }
/*    */   
/*    */   public String getMessage()
/*    */   {
/* 40 */     return "Invalid " + ((Entry)this.field_151383_a.get(this.field_151383_a.size() - 1)).toString() + ": " + this.field_151382_b;
/*    */   }
/*    */   
/*    */   public static JsonException func_151379_a(Exception p_151379_0_)
/*    */   {
/* 45 */     if ((p_151379_0_ instanceof JsonException))
/*    */     {
/* 47 */       return (JsonException)p_151379_0_;
/*    */     }
/*    */     
/*    */ 
/* 51 */     String s = p_151379_0_.getMessage();
/*    */     
/* 53 */     if ((p_151379_0_ instanceof FileNotFoundException))
/*    */     {
/* 55 */       s = "File not found";
/*    */     }
/*    */     
/* 58 */     return new JsonException(s, p_151379_0_);
/*    */   }
/*    */   
/*    */ 
/*    */   public static class Entry
/*    */   {
/*    */     private String field_151376_a;
/*    */     private final List<String> field_151375_b;
/*    */     
/*    */     private Entry()
/*    */     {
/* 69 */       this.field_151376_a = null;
/* 70 */       this.field_151375_b = Lists.newArrayList();
/*    */     }
/*    */     
/*    */     private void func_151373_a(String p_151373_1_)
/*    */     {
/* 75 */       this.field_151375_b.add(0, p_151373_1_);
/*    */     }
/*    */     
/*    */     public String func_151372_b()
/*    */     {
/* 80 */       return StringUtils.join(this.field_151375_b, "->");
/*    */     }
/*    */     
/*    */     public String toString()
/*    */     {
/* 85 */       return !this.field_151375_b.isEmpty() ? "(Unknown file) " + func_151372_b() : this.field_151376_a != null ? this.field_151376_a : !this.field_151375_b.isEmpty() ? this.field_151376_a + " " + func_151372_b() : "(Unknown file)";
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\util\JsonException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */