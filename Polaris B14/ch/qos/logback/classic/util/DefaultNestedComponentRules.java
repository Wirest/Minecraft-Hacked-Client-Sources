/*    */ package ch.qos.logback.classic.util;
/*    */ 
/*    */ import ch.qos.logback.classic.PatternLayout;
/*    */ import ch.qos.logback.classic.boolex.JaninoEventEvaluator;
/*    */ import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
/*    */ import ch.qos.logback.core.AppenderBase;
/*    */ import ch.qos.logback.core.UnsynchronizedAppenderBase;
/*    */ import ch.qos.logback.core.filter.EvaluatorFilter;
/*    */ import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
/*    */ import ch.qos.logback.core.net.ssl.SSLNestedComponentRegistryRules;
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
/*    */ 
/*    */ 
/*    */ public class DefaultNestedComponentRules
/*    */ {
/*    */   public static void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry)
/*    */   {
/* 36 */     registry.add(AppenderBase.class, "layout", PatternLayout.class);
/* 37 */     registry.add(UnsynchronizedAppenderBase.class, "layout", PatternLayout.class);
/*    */     
/* 39 */     registry.add(AppenderBase.class, "encoder", PatternLayoutEncoder.class);
/* 40 */     registry.add(UnsynchronizedAppenderBase.class, "encoder", PatternLayoutEncoder.class);
/*    */     
/* 42 */     registry.add(EvaluatorFilter.class, "evaluator", JaninoEventEvaluator.class);
/*    */     
/*    */ 
/* 45 */     SSLNestedComponentRegistryRules.addDefaultNestedComponentRegistryRules(registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\util\DefaultNestedComponentRules.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */