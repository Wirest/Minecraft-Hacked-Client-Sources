/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.PropertyContainer;
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import ch.qos.logback.core.subst.NodeToStringTransformer;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.Properties;
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
/*     */ public class OptionHelper
/*     */ {
/*     */   static final String DELIM_START = "${";
/*     */   static final char DELIM_STOP = '}';
/*     */   static final String DELIM_DEFAULT = ":-";
/*     */   static final int DELIM_START_LEN = 2;
/*     */   static final int DELIM_STOP_LEN = 1;
/*     */   static final int DELIM_DEFAULT_LEN = 2;
/*     */   static final String _IS_UNDEFINED = "_IS_UNDEFINED";
/*     */   
/*     */   public static Object instantiateByClassName(String className, Class<?> superClass, Context context)
/*     */     throws IncompatibleClassException, DynamicClassLoadingException
/*     */   {
/*  34 */     ClassLoader classLoader = Loader.getClassLoaderOfObject(context);
/*  35 */     return instantiateByClassName(className, superClass, classLoader);
/*     */   }
/*     */   
/*     */   public static Object instantiateByClassNameAndParameter(String className, Class<?> superClass, Context context, Class<?> type, Object param)
/*     */     throws IncompatibleClassException, DynamicClassLoadingException
/*     */   {
/*  41 */     ClassLoader classLoader = Loader.getClassLoaderOfObject(context);
/*  42 */     return instantiateByClassNameAndParameter(className, superClass, classLoader, type, param);
/*     */   }
/*     */   
/*     */   public static Object instantiateByClassName(String className, Class<?> superClass, ClassLoader classLoader)
/*     */     throws IncompatibleClassException, DynamicClassLoadingException
/*     */   {
/*  48 */     return instantiateByClassNameAndParameter(className, superClass, classLoader, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static Object instantiateByClassNameAndParameter(String className, Class<?> superClass, ClassLoader classLoader, Class<?> type, Object parameter)
/*     */     throws IncompatibleClassException, DynamicClassLoadingException
/*     */   {
/*  55 */     if (className == null) {
/*  56 */       throw new NullPointerException();
/*     */     }
/*     */     try {
/*  59 */       Class<?> classObj = null;
/*  60 */       classObj = classLoader.loadClass(className);
/*  61 */       if (!superClass.isAssignableFrom(classObj)) {
/*  62 */         throw new IncompatibleClassException(superClass, classObj);
/*     */       }
/*  64 */       if (type == null) {
/*  65 */         return classObj.newInstance();
/*     */       }
/*  67 */       Constructor<?> constructor = classObj.getConstructor(new Class[] { type });
/*  68 */       return constructor.newInstance(new Object[] { parameter });
/*     */     }
/*     */     catch (IncompatibleClassException ice) {
/*  71 */       throw ice;
/*     */     } catch (Throwable t) {
/*  73 */       throw new DynamicClassLoadingException("Failed to instantiate type " + className, t);
/*     */     }
/*     */   }
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
/*     */   public static String substVars(String val, PropertyContainer pc1)
/*     */   {
/* 109 */     return substVars(val, pc1, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public static String substVars(String input, PropertyContainer pc0, PropertyContainer pc1)
/*     */   {
/*     */     try
/*     */     {
/* 117 */       return NodeToStringTransformer.substituteVariable(input, pc0, pc1);
/*     */     } catch (ScanException e) {
/* 119 */       throw new IllegalArgumentException("Failed to parse input [" + input + "]", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String propertyLookup(String key, PropertyContainer pc1, PropertyContainer pc2)
/*     */   {
/* 125 */     String value = null;
/*     */     
/* 127 */     value = pc1.getProperty(key);
/*     */     
/*     */ 
/* 130 */     if ((value == null) && (pc2 != null)) {
/* 131 */       value = pc2.getProperty(key);
/*     */     }
/*     */     
/* 134 */     if (value == null) {
/* 135 */       value = getSystemProperty(key, null);
/*     */     }
/* 137 */     if (value == null) {
/* 138 */       value = getEnv(key);
/*     */     }
/* 140 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getSystemProperty(String key, String def)
/*     */   {
/*     */     try
/*     */     {
/* 154 */       return System.getProperty(key, def);
/*     */     } catch (SecurityException e) {}
/* 156 */     return def;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getEnv(String key)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       return System.getenv(key);
/*     */     } catch (SecurityException e) {}
/* 170 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getSystemProperty(String key)
/*     */   {
/*     */     try
/*     */     {
/* 184 */       return System.getProperty(key);
/*     */     } catch (SecurityException e) {}
/* 186 */     return null;
/*     */   }
/*     */   
/*     */   public static void setSystemProperties(ContextAware contextAware, Properties props)
/*     */   {
/* 191 */     for (Object o : props.keySet()) {
/* 192 */       String key = (String)o;
/* 193 */       String value = props.getProperty(key);
/* 194 */       setSystemProperty(contextAware, key, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setSystemProperty(ContextAware contextAware, String key, String value) {
/*     */     try {
/* 200 */       System.setProperty(key, value);
/*     */     } catch (SecurityException e) {
/* 202 */       contextAware.addError("Failed to set system property [" + key + "]", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Properties getSystemProperties()
/*     */   {
/*     */     try
/*     */     {
/* 214 */       return System.getProperties();
/*     */     } catch (SecurityException e) {}
/* 216 */     return new Properties();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] extractDefaultReplacement(String key)
/*     */   {
/* 229 */     String[] result = new String[2];
/* 230 */     if (key == null) {
/* 231 */       return result;
/*     */     }
/* 233 */     result[0] = key;
/* 234 */     int d = key.indexOf(":-");
/* 235 */     if (d != -1) {
/* 236 */       result[0] = key.substring(0, d);
/* 237 */       result[1] = key.substring(d + 2);
/*     */     }
/* 239 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean toBoolean(String value, boolean dEfault)
/*     */   {
/* 250 */     if (value == null) {
/* 251 */       return dEfault;
/*     */     }
/*     */     
/* 254 */     String trimmedVal = value.trim();
/*     */     
/* 256 */     if ("true".equalsIgnoreCase(trimmedVal)) {
/* 257 */       return true;
/*     */     }
/*     */     
/* 260 */     if ("false".equalsIgnoreCase(trimmedVal)) {
/* 261 */       return false;
/*     */     }
/*     */     
/* 264 */     return dEfault;
/*     */   }
/*     */   
/*     */   public static boolean isEmpty(String str) {
/* 268 */     return (str == null) || ("".equals(str));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\OptionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */