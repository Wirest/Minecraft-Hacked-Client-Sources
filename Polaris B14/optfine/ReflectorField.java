/*    */ package optfine;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ 
/*    */ public class ReflectorField
/*    */ {
/*  7 */   private ReflectorClass reflectorClass = null;
/*  8 */   private String targetFieldName = null;
/*  9 */   private boolean checked = false;
/* 10 */   private Field targetField = null;
/*    */   
/*    */   public ReflectorField(ReflectorClass p_i58_1_, String p_i58_2_)
/*    */   {
/* 14 */     this.reflectorClass = p_i58_1_;
/* 15 */     this.targetFieldName = p_i58_2_;
/* 16 */     Field field = getTargetField();
/*    */   }
/*    */   
/*    */   public Field getTargetField()
/*    */   {
/* 21 */     if (this.checked)
/*    */     {
/* 23 */       return this.targetField;
/*    */     }
/*    */     
/*    */ 
/* 27 */     this.checked = true;
/* 28 */     Class oclass = this.reflectorClass.getTargetClass();
/*    */     
/* 30 */     if (oclass == null)
/*    */     {
/* 32 */       return null;
/*    */     }
/*    */     
/*    */ 
/*    */     try
/*    */     {
/* 38 */       this.targetField = oclass.getDeclaredField(this.targetFieldName);
/*    */       
/* 40 */       if (!this.targetField.isAccessible())
/*    */       {
/* 42 */         this.targetField.setAccessible(true);
/*    */       }
/*    */     }
/*    */     catch (SecurityException securityexception)
/*    */     {
/* 47 */       securityexception.printStackTrace();
/*    */     }
/*    */     catch (NoSuchFieldException var4)
/*    */     {
/* 51 */       Config.log("(Reflector) Field not present: " + oclass.getName() + "." + this.targetFieldName);
/*    */     }
/*    */     
/* 54 */     return this.targetField;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object getValue()
/*    */   {
/* 61 */     return Reflector.getFieldValue(null, this);
/*    */   }
/*    */   
/*    */   public void setValue(Object p_setValue_1_)
/*    */   {
/* 66 */     Reflector.setFieldValue(null, this, p_setValue_1_);
/*    */   }
/*    */   
/*    */   public boolean exists()
/*    */   {
/* 71 */     return this.targetField != null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ReflectorField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */