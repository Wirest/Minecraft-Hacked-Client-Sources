/*     */ package optfine;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class ReflectorMethod
/*     */ {
/*     */   private ReflectorClass reflectorClass;
/*     */   private String targetMethodName;
/*     */   private Class[] targetMethodParameterTypes;
/*     */   private boolean checked;
/*     */   private Method targetMethod;
/*     */   
/*     */   public ReflectorMethod(ReflectorClass p_i59_1_, String p_i59_2_)
/*     */   {
/*  15 */     this(p_i59_1_, p_i59_2_, null);
/*     */   }
/*     */   
/*     */   public ReflectorMethod(ReflectorClass p_i60_1_, String p_i60_2_, Class[] p_i60_3_)
/*     */   {
/*  20 */     this.reflectorClass = null;
/*  21 */     this.targetMethodName = null;
/*  22 */     this.targetMethodParameterTypes = null;
/*  23 */     this.checked = false;
/*  24 */     this.targetMethod = null;
/*  25 */     this.reflectorClass = p_i60_1_;
/*  26 */     this.targetMethodName = p_i60_2_;
/*  27 */     this.targetMethodParameterTypes = p_i60_3_;
/*  28 */     Method method = getTargetMethod();
/*     */   }
/*     */   
/*     */   public Method getTargetMethod()
/*     */   {
/*  33 */     if (this.checked)
/*     */     {
/*  35 */       return this.targetMethod;
/*     */     }
/*     */     
/*     */ 
/*  39 */     this.checked = true;
/*  40 */     Class oclass = this.reflectorClass.getTargetClass();
/*     */     
/*  42 */     if (oclass == null)
/*     */     {
/*  44 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  48 */     Method[] amethod = oclass.getDeclaredMethods();
/*  49 */     int i = 0;
/*     */     
/*     */ 
/*     */     for (;;)
/*     */     {
/*  54 */       if (i >= amethod.length)
/*     */       {
/*  56 */         Config.log("(Reflector) Method not present: " + oclass.getName() + "." + this.targetMethodName);
/*  57 */         return null;
/*     */       }
/*     */       
/*  60 */       Method method = amethod[i];
/*     */       
/*  62 */       if (method.getName().equals(this.targetMethodName))
/*     */       {
/*  64 */         if (this.targetMethodParameterTypes == null) {
/*     */           break;
/*     */         }
/*     */         
/*     */ 
/*  69 */         Class[] aclass = method.getParameterTypes();
/*     */         
/*  71 */         if (Reflector.matchesTypes(this.targetMethodParameterTypes, aclass)) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  77 */       i++;
/*     */     }
/*     */     Method method;
/*  80 */     this.targetMethod = method;
/*     */     
/*  82 */     if (!this.targetMethod.isAccessible())
/*     */     {
/*  84 */       this.targetMethod.setAccessible(true);
/*     */     }
/*     */     
/*  87 */     return this.targetMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean exists()
/*     */   {
/*  94 */     return this.targetMethod != null;
/*     */   }
/*     */   
/*     */   public Class getReturnType()
/*     */   {
/*  99 */     Method method = getTargetMethod();
/* 100 */     return method == null ? null : method.getReturnType();
/*     */   }
/*     */   
/*     */   public void deactivate()
/*     */   {
/* 105 */     this.checked = true;
/* 106 */     this.targetMethod = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\ReflectorMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */