/*    */ package ch.qos.logback.classic.gaffer;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.core.status.ErrorStatus;
/*    */ import ch.qos.logback.core.status.StatusManager;
/*    */ import java.io.File;
/*    */ import java.lang.reflect.Constructor;
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.net.URL;
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
/*    */ 
/*    */ 
/*    */ public class GafferUtil
/*    */ {
/* 31 */   private static String ERROR_MSG = "Failed to instantiate ch.qos.logback.classic.gaffer.GafferConfigurator";
/*    */   
/*    */   public static void runGafferConfiguratorOn(LoggerContext loggerContext, Object origin, File configFile) {
/* 34 */     GafferConfigurator gafferConfigurator = newGafferConfiguratorInstance(loggerContext, origin);
/* 35 */     if (gafferConfigurator != null) {
/* 36 */       gafferConfigurator.run(configFile);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void runGafferConfiguratorOn(LoggerContext loggerContext, Object origin, URL configFile) {
/* 41 */     GafferConfigurator gafferConfigurator = newGafferConfiguratorInstance(loggerContext, origin);
/* 42 */     if (gafferConfigurator != null) {
/* 43 */       gafferConfigurator.run(configFile);
/*    */     }
/*    */   }
/*    */   
/*    */   private static GafferConfigurator newGafferConfiguratorInstance(LoggerContext loggerContext, Object origin)
/*    */   {
/*    */     try {
/* 50 */       Class gcClass = Class.forName("ch.qos.logback.classic.gaffer.GafferConfigurator");
/* 51 */       Constructor c = gcClass.getConstructor(new Class[] { LoggerContext.class });
/* 52 */       return (GafferConfigurator)c.newInstance(new Object[] { loggerContext });
/*    */     } catch (ClassNotFoundException e) {
/* 54 */       addError(loggerContext, origin, ERROR_MSG, e);
/*    */     } catch (NoSuchMethodException e) {
/* 56 */       addError(loggerContext, origin, ERROR_MSG, e);
/*    */     } catch (InvocationTargetException e) {
/* 58 */       addError(loggerContext, origin, ERROR_MSG, e);
/*    */     } catch (InstantiationException e) {
/* 60 */       addError(loggerContext, origin, ERROR_MSG, e);
/*    */     } catch (IllegalAccessException e) {
/* 62 */       addError(loggerContext, origin, ERROR_MSG, e);
/*    */     }
/* 64 */     return null;
/*    */   }
/*    */   
/*    */   private static void addError(LoggerContext context, Object origin, String msg) {
/* 68 */     addError(context, origin, msg, null);
/*    */   }
/*    */   
/*    */   private static void addError(LoggerContext context, Object origin, String msg, Throwable t) {
/* 72 */     StatusManager sm = context.getStatusManager();
/* 73 */     if (sm == null) {
/* 74 */       return;
/*    */     }
/* 76 */     sm.add(new ErrorStatus(msg, origin, t));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\gaffer\GafferUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */