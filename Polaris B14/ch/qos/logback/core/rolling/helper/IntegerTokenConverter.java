/*    */ package ch.qos.logback.core.rolling.helper;
/*    */ 
/*    */ import ch.qos.logback.core.pattern.DynamicConverter;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntegerTokenConverter
/*    */   extends DynamicConverter
/*    */   implements MonoTypedConverter
/*    */ {
/*    */   public static final String CONVERTER_KEY = "i";
/*    */   
/*    */   public String convert(int i)
/*    */   {
/* 29 */     return Integer.toString(i);
/*    */   }
/*    */   
/*    */   public String convert(Object o) {
/* 33 */     if (o == null) {
/* 34 */       throw new IllegalArgumentException("Null argument forbidden");
/*    */     }
/* 36 */     if ((o instanceof Integer)) {
/* 37 */       Integer i = (Integer)o;
/* 38 */       return convert(i.intValue());
/*    */     }
/* 40 */     throw new IllegalArgumentException("Cannot convert " + o + " of type" + o.getClass().getName());
/*    */   }
/*    */   
/*    */   public boolean isApplicable(Object o) {
/* 44 */     return o instanceof Integer;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\IntegerTokenConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */