/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.HashMap;
/*     */ import sun.reflect.Reflection;
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
/*     */ public class PackagingDataCalculator
/*     */ {
/*  30 */   static final StackTraceElementProxy[] STEP_ARRAY_TEMPLATE = new StackTraceElementProxy[0];
/*     */   
/*  32 */   HashMap<String, ClassPackagingData> cache = new HashMap();
/*     */   
/*  34 */   private static boolean GET_CALLER_CLASS_METHOD_AVAILABLE = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  43 */       Reflection.getCallerClass(2);
/*  44 */       GET_CALLER_CLASS_METHOD_AVAILABLE = true;
/*     */ 
/*     */     }
/*     */     catch (NoClassDefFoundError e) {}catch (NoSuchMethodError e) {}catch (UnsupportedOperationException e) {}catch (Throwable e)
/*     */     {
/*  49 */       System.err.println("Unexpected exception");
/*  50 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void calculate(IThrowableProxy tp)
/*     */   {
/*  56 */     while (tp != null) {
/*  57 */       populateFrames(tp.getStackTraceElementProxyArray());
/*  58 */       IThrowableProxy[] suppressed = tp.getSuppressed();
/*  59 */       if (suppressed != null) {
/*  60 */         for (IThrowableProxy current : suppressed) {
/*  61 */           populateFrames(current.getStackTraceElementProxyArray());
/*     */         }
/*     */       }
/*  64 */       tp = tp.getCause();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void populateFrames(StackTraceElementProxy[] stepArray)
/*     */   {
/*  71 */     Throwable t = new Throwable("local stack reference");
/*  72 */     StackTraceElement[] localteSTEArray = t.getStackTrace();
/*  73 */     int commonFrames = STEUtil.findNumberOfCommonFrames(localteSTEArray, stepArray);
/*     */     
/*  75 */     int localFirstCommon = localteSTEArray.length - commonFrames;
/*  76 */     int stepFirstCommon = stepArray.length - commonFrames;
/*     */     
/*  78 */     ClassLoader lastExactClassLoader = null;
/*  79 */     ClassLoader firsExactClassLoader = null;
/*     */     
/*  81 */     int missfireCount = 0;
/*  82 */     for (int i = 0; i < commonFrames; i++) {
/*  83 */       Class callerClass = null;
/*  84 */       if (GET_CALLER_CLASS_METHOD_AVAILABLE) {
/*  85 */         callerClass = Reflection.getCallerClass(localFirstCommon + i - missfireCount + 1);
/*     */       }
/*     */       
/*  88 */       StackTraceElementProxy step = stepArray[(stepFirstCommon + i)];
/*  89 */       String stepClassname = step.ste.getClassName();
/*     */       
/*  91 */       if ((callerClass != null) && (stepClassname.equals(callerClass.getName())))
/*     */       {
/*  93 */         lastExactClassLoader = callerClass.getClassLoader();
/*  94 */         if (firsExactClassLoader == null) {
/*  95 */           firsExactClassLoader = lastExactClassLoader;
/*     */         }
/*  97 */         ClassPackagingData pi = calculateByExactType(callerClass);
/*  98 */         step.setClassPackagingData(pi);
/*     */       } else {
/* 100 */         missfireCount++;
/* 101 */         ClassPackagingData pi = computeBySTEP(step, lastExactClassLoader);
/* 102 */         step.setClassPackagingData(pi);
/*     */       }
/*     */     }
/* 105 */     populateUncommonFrames(commonFrames, stepArray, firsExactClassLoader);
/*     */   }
/*     */   
/*     */   void populateUncommonFrames(int commonFrames, StackTraceElementProxy[] stepArray, ClassLoader firstExactClassLoader)
/*     */   {
/* 110 */     int uncommonFrames = stepArray.length - commonFrames;
/* 111 */     for (int i = 0; i < uncommonFrames; i++) {
/* 112 */       StackTraceElementProxy step = stepArray[i];
/* 113 */       ClassPackagingData pi = computeBySTEP(step, firstExactClassLoader);
/* 114 */       step.setClassPackagingData(pi);
/*     */     }
/*     */   }
/*     */   
/*     */   private ClassPackagingData calculateByExactType(Class type) {
/* 119 */     String className = type.getName();
/* 120 */     ClassPackagingData cpd = (ClassPackagingData)this.cache.get(className);
/* 121 */     if (cpd != null) {
/* 122 */       return cpd;
/*     */     }
/* 124 */     String version = getImplementationVersion(type);
/* 125 */     String codeLocation = getCodeLocation(type);
/* 126 */     cpd = new ClassPackagingData(codeLocation, version);
/* 127 */     this.cache.put(className, cpd);
/* 128 */     return cpd;
/*     */   }
/*     */   
/*     */   private ClassPackagingData computeBySTEP(StackTraceElementProxy step, ClassLoader lastExactClassLoader)
/*     */   {
/* 133 */     String className = step.ste.getClassName();
/* 134 */     ClassPackagingData cpd = (ClassPackagingData)this.cache.get(className);
/* 135 */     if (cpd != null) {
/* 136 */       return cpd;
/*     */     }
/* 138 */     Class type = bestEffortLoadClass(lastExactClassLoader, className);
/* 139 */     String version = getImplementationVersion(type);
/* 140 */     String codeLocation = getCodeLocation(type);
/* 141 */     cpd = new ClassPackagingData(codeLocation, version, false);
/* 142 */     this.cache.put(className, cpd);
/* 143 */     return cpd;
/*     */   }
/*     */   
/*     */   String getImplementationVersion(Class type) {
/* 147 */     if (type == null) {
/* 148 */       return "na";
/*     */     }
/* 150 */     Package aPackage = type.getPackage();
/* 151 */     if (aPackage != null) {
/* 152 */       String v = aPackage.getImplementationVersion();
/* 153 */       if (v == null) {
/* 154 */         return "na";
/*     */       }
/* 156 */       return v;
/*     */     }
/*     */     
/* 159 */     return "na";
/*     */   }
/*     */   
/*     */   String getCodeLocation(Class type)
/*     */   {
/*     */     try {
/* 165 */       if (type != null)
/*     */       {
/* 167 */         CodeSource codeSource = type.getProtectionDomain().getCodeSource();
/* 168 */         if (codeSource != null) {
/* 169 */           URL resource = codeSource.getLocation();
/* 170 */           if (resource != null) {
/* 171 */             String locationStr = resource.toString();
/*     */             
/* 173 */             String result = getCodeLocation(locationStr, '/');
/* 174 */             if (result != null) {
/* 175 */               return result;
/*     */             }
/* 177 */             return getCodeLocation(locationStr, '\\');
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception e) {}
/*     */     
/* 184 */     return "na";
/*     */   }
/*     */   
/*     */   private String getCodeLocation(String locationStr, char separator) {
/* 188 */     int idx = locationStr.lastIndexOf(separator);
/* 189 */     if (isFolder(idx, locationStr)) {
/* 190 */       idx = locationStr.lastIndexOf(separator, idx - 1);
/* 191 */       return locationStr.substring(idx + 1); }
/* 192 */     if (idx > 0) {
/* 193 */       return locationStr.substring(idx + 1);
/*     */     }
/* 195 */     return null;
/*     */   }
/*     */   
/*     */   private boolean isFolder(int idx, String text) {
/* 199 */     return (idx != -1) && (idx + 1 == text.length());
/*     */   }
/*     */   
/*     */   private Class loadClass(ClassLoader cl, String className) {
/* 203 */     if (cl == null) {
/* 204 */       return null;
/*     */     }
/*     */     try {
/* 207 */       return cl.loadClass(className);
/*     */     } catch (ClassNotFoundException e1) {
/* 209 */       return null;
/*     */     } catch (NoClassDefFoundError e1) {
/* 211 */       return null;
/*     */     } catch (Exception e) {
/* 213 */       e.printStackTrace(); }
/* 214 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class bestEffortLoadClass(ClassLoader lastGuaranteedClassLoader, String className)
/*     */   {
/* 226 */     Class result = loadClass(lastGuaranteedClassLoader, className);
/* 227 */     if (result != null) {
/* 228 */       return result;
/*     */     }
/* 230 */     ClassLoader tccl = Thread.currentThread().getContextClassLoader();
/* 231 */     if (tccl != lastGuaranteedClassLoader) {
/* 232 */       result = loadClass(tccl, className);
/*     */     }
/* 234 */     if (result != null) {
/* 235 */       return result;
/*     */     }
/*     */     try
/*     */     {
/* 239 */       return Class.forName(className);
/*     */     } catch (ClassNotFoundException e1) {
/* 241 */       return null;
/*     */     } catch (NoClassDefFoundError e1) {
/* 243 */       return null;
/*     */     } catch (Exception e) {
/* 245 */       e.printStackTrace(); }
/* 246 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\PackagingDataCalculator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */