/*     */ package ch.qos.logback.core.joran.util;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.UnsupportedCharsetException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringToObjectConverter
/*     */ {
/*  31 */   private static final Class<?>[] STING_CLASS_PARAMETER = { String.class };
/*     */   
/*     */   public static boolean canBeBuiltFromSimpleString(Class<?> parameterClass) {
/*  34 */     Package p = parameterClass.getPackage();
/*  35 */     if (parameterClass.isPrimitive())
/*  36 */       return true;
/*  37 */     if ((p != null) && ("java.lang".equals(p.getName())))
/*  38 */       return true;
/*  39 */     if (followsTheValueOfConvention(parameterClass))
/*  40 */       return true;
/*  41 */     if (parameterClass.isEnum())
/*  42 */       return true;
/*  43 */     if (isOfTypeCharset(parameterClass)) {
/*  44 */       return true;
/*     */     }
/*  46 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object convertArg(ContextAware ca, String val, Class<?> type)
/*     */   {
/*  54 */     if (val == null) {
/*  55 */       return null;
/*     */     }
/*  57 */     String v = val.trim();
/*  58 */     if (String.class.isAssignableFrom(type))
/*  59 */       return v;
/*  60 */     if (Integer.TYPE.isAssignableFrom(type))
/*  61 */       return new Integer(v);
/*  62 */     if (Long.TYPE.isAssignableFrom(type))
/*  63 */       return new Long(v);
/*  64 */     if (Float.TYPE.isAssignableFrom(type))
/*  65 */       return new Float(v);
/*  66 */     if (Double.TYPE.isAssignableFrom(type))
/*  67 */       return new Double(v);
/*  68 */     if (Boolean.TYPE.isAssignableFrom(type)) {
/*  69 */       if ("true".equalsIgnoreCase(v))
/*  70 */         return Boolean.TRUE;
/*  71 */       if ("false".equalsIgnoreCase(v))
/*  72 */         return Boolean.FALSE;
/*     */     } else {
/*  74 */       if (type.isEnum())
/*  75 */         return convertToEnum(ca, v, type);
/*  76 */       if (followsTheValueOfConvention(type))
/*  77 */         return convertByValueOfMethod(ca, type, v);
/*  78 */       if (isOfTypeCharset(type)) {
/*  79 */         return convertToCharset(ca, val);
/*     */       }
/*     */     }
/*  82 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean isOfTypeCharset(Class<?> type) {
/*  86 */     return Charset.class.isAssignableFrom(type);
/*     */   }
/*     */   
/*     */   private static Charset convertToCharset(ContextAware ca, String val) {
/*     */     try {
/*  91 */       return Charset.forName(val);
/*     */     } catch (UnsupportedCharsetException e) {
/*  93 */       ca.addError("Failed to get charset [" + val + "]", e); }
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   private static boolean followsTheValueOfConvention(Class<?> parameterClass)
/*     */   {
/*     */     try {
/* 100 */       Method valueOfMethod = parameterClass.getMethod("valueOf", STING_CLASS_PARAMETER);
/*     */       
/* 102 */       int mod = valueOfMethod.getModifiers();
/* 103 */       if (Modifier.isStatic(mod)) {
/* 104 */         return true;
/*     */       }
/*     */     }
/*     */     catch (SecurityException e) {}catch (NoSuchMethodException e) {}
/*     */     
/*     */ 
/*     */ 
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   private static Object convertByValueOfMethod(ContextAware ca, Class<?> type, String val)
/*     */   {
/*     */     try {
/* 117 */       Method valueOfMethod = type.getMethod("valueOf", STING_CLASS_PARAMETER);
/*     */       
/* 119 */       return valueOfMethod.invoke(null, new Object[] { val });
/*     */     } catch (Exception e) {
/* 121 */       ca.addError("Failed to invoke valueOf{} method in class [" + type.getName() + "] with value [" + val + "]");
/*     */     }
/*     */     
/* 124 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Object convertToEnum(ContextAware ca, String val, Class<? extends Enum> enumType)
/*     */   {
/* 131 */     return Enum.valueOf(enumType, val);
/*     */   }
/*     */   
/*     */   boolean isBuildableFromSimpleString() {
/* 135 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\util\StringToObjectConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */