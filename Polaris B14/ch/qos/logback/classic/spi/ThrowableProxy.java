/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
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
/*     */ public class ThrowableProxy
/*     */   implements IThrowableProxy
/*     */ {
/*     */   private Throwable throwable;
/*     */   private String className;
/*     */   private String message;
/*     */   StackTraceElementProxy[] stackTraceElementProxyArray;
/*     */   int commonFrames;
/*     */   private ThrowableProxy cause;
/*  31 */   private ThrowableProxy[] suppressed = NO_SUPPRESSED;
/*     */   
/*     */   private transient PackagingDataCalculator packagingDataCalculator;
/*  34 */   private boolean calculatedPackageData = false;
/*     */   private static final Method GET_SUPPRESSED_METHOD;
/*     */   
/*     */   static
/*     */   {
/*  39 */     Method method = null;
/*     */     try {
/*  41 */       method = Throwable.class.getMethod("getSuppressed", new Class[0]);
/*     */     }
/*     */     catch (NoSuchMethodException e) {}
/*     */     
/*  45 */     GET_SUPPRESSED_METHOD = method;
/*     */   }
/*     */   
/*  48 */   private static final ThrowableProxy[] NO_SUPPRESSED = new ThrowableProxy[0];
/*     */   
/*     */   public ThrowableProxy(Throwable throwable)
/*     */   {
/*  52 */     this.throwable = throwable;
/*  53 */     this.className = throwable.getClass().getName();
/*  54 */     this.message = throwable.getMessage();
/*  55 */     this.stackTraceElementProxyArray = ThrowableProxyUtil.steArrayToStepArray(throwable.getStackTrace());
/*     */     
/*     */ 
/*  58 */     Throwable nested = throwable.getCause();
/*     */     
/*  60 */     if (nested != null) {
/*  61 */       this.cause = new ThrowableProxy(nested);
/*  62 */       this.cause.commonFrames = ThrowableProxyUtil.findNumberOfCommonFrames(nested.getStackTrace(), this.stackTraceElementProxyArray);
/*     */     }
/*     */     
/*     */ 
/*  66 */     if (GET_SUPPRESSED_METHOD != null) {
/*     */       try
/*     */       {
/*  69 */         Object obj = GET_SUPPRESSED_METHOD.invoke(throwable, new Object[0]);
/*  70 */         if ((obj instanceof Throwable[])) {
/*  71 */           Throwable[] throwableSuppressed = (Throwable[])obj;
/*  72 */           if (throwableSuppressed.length > 0) {
/*  73 */             this.suppressed = new ThrowableProxy[throwableSuppressed.length];
/*  74 */             for (int i = 0; i < throwableSuppressed.length; i++) {
/*  75 */               this.suppressed[i] = new ThrowableProxy(throwableSuppressed[i]);
/*  76 */               this.suppressed[i].commonFrames = ThrowableProxyUtil.findNumberOfCommonFrames(throwableSuppressed[i].getStackTrace(), this.stackTraceElementProxyArray);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IllegalAccessException e) {}catch (InvocationTargetException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Throwable getThrowable()
/*     */   {
/*  93 */     return this.throwable;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  97 */     return this.message;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 106 */     return this.className;
/*     */   }
/*     */   
/*     */   public StackTraceElementProxy[] getStackTraceElementProxyArray() {
/* 110 */     return this.stackTraceElementProxyArray;
/*     */   }
/*     */   
/*     */   public int getCommonFrames() {
/* 114 */     return this.commonFrames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IThrowableProxy getCause()
/*     */   {
/* 123 */     return this.cause;
/*     */   }
/*     */   
/*     */   public IThrowableProxy[] getSuppressed() {
/* 127 */     return this.suppressed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PackagingDataCalculator getPackagingDataCalculator()
/*     */   {
/* 134 */     if ((this.throwable != null) && (this.packagingDataCalculator == null)) {
/* 135 */       this.packagingDataCalculator = new PackagingDataCalculator();
/*     */     }
/* 137 */     return this.packagingDataCalculator;
/*     */   }
/*     */   
/*     */   public void calculatePackagingData() {
/* 141 */     if (this.calculatedPackageData) {
/* 142 */       return;
/*     */     }
/* 144 */     PackagingDataCalculator pdc = getPackagingDataCalculator();
/* 145 */     if (pdc != null) {
/* 146 */       this.calculatedPackageData = true;
/* 147 */       pdc.calculate(this);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void fullDump()
/*     */   {
/* 154 */     StringBuilder builder = new StringBuilder();
/* 155 */     for (StackTraceElementProxy step : this.stackTraceElementProxyArray) {
/* 156 */       String string = step.toString();
/* 157 */       builder.append('\t').append(string);
/* 158 */       ThrowableProxyUtil.subjoinPackagingData(builder, step);
/* 159 */       builder.append(CoreConstants.LINE_SEPARATOR);
/*     */     }
/* 161 */     System.out.println(builder.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\ThrowableProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */