/*     */ package ch.qos.logback.core.joran.util;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
/*     */ import ch.qos.logback.core.status.InfoStatus;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
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
/*     */ public class ConfigurationWatchListUtil
/*     */ {
/*  31 */   static final ConfigurationWatchListUtil origin = new ConfigurationWatchListUtil();
/*     */   
/*     */ 
/*     */ 
/*     */   public static void setMainWatchURL(Context context, URL url)
/*     */   {
/*  37 */     ConfigurationWatchList cwl = getConfigurationWatchList(context);
/*  38 */     if (cwl == null) {
/*  39 */       cwl = new ConfigurationWatchList();
/*  40 */       cwl.setContext(context);
/*  41 */       context.putObject("CONFIGURATION_WATCH_LIST", cwl);
/*     */     } else {
/*  43 */       cwl.clear();
/*     */     }
/*  45 */     setConfigurationWatchListResetFlag(context, true);
/*  46 */     cwl.setMainURL(url);
/*     */   }
/*     */   
/*     */   public static URL getMainWatchURL(Context context) {
/*  50 */     ConfigurationWatchList cwl = getConfigurationWatchList(context);
/*  51 */     if (cwl == null) {
/*  52 */       return null;
/*     */     }
/*  54 */     return cwl.getMainURL();
/*     */   }
/*     */   
/*     */   public static void addToWatchList(Context context, URL url)
/*     */   {
/*  59 */     ConfigurationWatchList cwl = getConfigurationWatchList(context);
/*  60 */     if (cwl == null) {
/*  61 */       addWarn(context, "Null ConfigurationWatchList. Cannot add " + url);
/*     */     } else {
/*  63 */       addInfo(context, "Adding [" + url + "] to configuration watch list.");
/*  64 */       cwl.addToWatchList(url);
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean wasConfigurationWatchListReset(Context context) {
/*  69 */     Object o = context.getObject("CONFIGURATION_WATCH_LIST_RESET");
/*  70 */     if (o == null) {
/*  71 */       return false;
/*     */     }
/*  73 */     return ((Boolean)o).booleanValue();
/*     */   }
/*     */   
/*     */   public static void setConfigurationWatchListResetFlag(Context context, boolean val)
/*     */   {
/*  78 */     context.putObject("CONFIGURATION_WATCH_LIST_RESET", new Boolean(val));
/*     */   }
/*     */   
/*     */   public static ConfigurationWatchList getConfigurationWatchList(Context context) {
/*  82 */     return (ConfigurationWatchList)context.getObject("CONFIGURATION_WATCH_LIST");
/*     */   }
/*     */   
/*     */   static void addStatus(Context context, Status s) {
/*  86 */     if (context == null) {
/*  87 */       System.out.println("Null context in " + ConfigurationWatchList.class.getName());
/*  88 */       return;
/*     */     }
/*  90 */     StatusManager sm = context.getStatusManager();
/*  91 */     if (sm == null) return;
/*  92 */     sm.add(s);
/*     */   }
/*     */   
/*     */   static void addInfo(Context context, String msg) {
/*  96 */     addStatus(context, new InfoStatus(msg, origin));
/*     */   }
/*     */   
/*     */   static void addWarn(Context context, String msg) {
/* 100 */     addStatus(context, new WarnStatus(msg, origin));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\util\ConfigurationWatchListUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */