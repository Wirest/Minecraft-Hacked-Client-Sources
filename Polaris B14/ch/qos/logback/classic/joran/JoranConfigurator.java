/*    */ package ch.qos.logback.classic.joran;
/*    */ 
/*    */ import ch.qos.logback.classic.joran.action.ConfigurationAction;
/*    */ import ch.qos.logback.classic.joran.action.ConsolePluginAction;
/*    */ import ch.qos.logback.classic.joran.action.ContextNameAction;
/*    */ import ch.qos.logback.classic.joran.action.EvaluatorAction;
/*    */ import ch.qos.logback.classic.joran.action.InsertFromJNDIAction;
/*    */ import ch.qos.logback.classic.joran.action.JMXConfiguratorAction;
/*    */ import ch.qos.logback.classic.joran.action.LevelAction;
/*    */ import ch.qos.logback.classic.joran.action.LoggerAction;
/*    */ import ch.qos.logback.classic.joran.action.LoggerContextListenerAction;
/*    */ import ch.qos.logback.classic.joran.action.ReceiverAction;
/*    */ import ch.qos.logback.classic.joran.action.RootLoggerAction;
/*    */ import ch.qos.logback.classic.sift.SiftAction;
/*    */ import ch.qos.logback.classic.spi.PlatformInfo;
/*    */ import ch.qos.logback.classic.util.DefaultNestedComponentRules;
/*    */ import ch.qos.logback.core.joran.JoranConfiguratorBase;
/*    */ import ch.qos.logback.core.joran.action.AppenderRefAction;
/*    */ import ch.qos.logback.core.joran.action.IncludeAction;
/*    */ import ch.qos.logback.core.joran.action.NOPAction;
/*    */ import ch.qos.logback.core.joran.conditional.ElseAction;
/*    */ import ch.qos.logback.core.joran.conditional.IfAction;
/*    */ import ch.qos.logback.core.joran.conditional.ThenAction;
/*    */ import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
/*    */ import ch.qos.logback.core.joran.spi.ElementSelector;
/*    */ import ch.qos.logback.core.joran.spi.RuleStore;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JoranConfigurator
/*    */   extends JoranConfiguratorBase
/*    */ {
/*    */   public void addInstanceRules(RuleStore rs)
/*    */   {
/* 41 */     super.addInstanceRules(rs);
/*    */     
/* 43 */     rs.addRule(new ElementSelector("configuration"), new ConfigurationAction());
/*    */     
/* 45 */     rs.addRule(new ElementSelector("configuration/contextName"), new ContextNameAction());
/*    */     
/* 47 */     rs.addRule(new ElementSelector("configuration/contextListener"), new LoggerContextListenerAction());
/*    */     
/* 49 */     rs.addRule(new ElementSelector("configuration/insertFromJNDI"), new InsertFromJNDIAction());
/*    */     
/* 51 */     rs.addRule(new ElementSelector("configuration/evaluator"), new EvaluatorAction());
/*    */     
/* 53 */     rs.addRule(new ElementSelector("configuration/appender/sift"), new SiftAction());
/* 54 */     rs.addRule(new ElementSelector("configuration/appender/sift/*"), new NOPAction());
/*    */     
/* 56 */     rs.addRule(new ElementSelector("configuration/logger"), new LoggerAction());
/* 57 */     rs.addRule(new ElementSelector("configuration/logger/level"), new LevelAction());
/*    */     
/* 59 */     rs.addRule(new ElementSelector("configuration/root"), new RootLoggerAction());
/* 60 */     rs.addRule(new ElementSelector("configuration/root/level"), new LevelAction());
/* 61 */     rs.addRule(new ElementSelector("configuration/logger/appender-ref"), new AppenderRefAction());
/*    */     
/* 63 */     rs.addRule(new ElementSelector("configuration/root/appender-ref"), new AppenderRefAction());
/*    */     
/*    */ 
/*    */ 
/* 67 */     rs.addRule(new ElementSelector("*/if"), new IfAction());
/* 68 */     rs.addRule(new ElementSelector("*/if/then"), new ThenAction());
/* 69 */     rs.addRule(new ElementSelector("*/if/then/*"), new NOPAction());
/* 70 */     rs.addRule(new ElementSelector("*/if/else"), new ElseAction());
/* 71 */     rs.addRule(new ElementSelector("*/if/else/*"), new NOPAction());
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 76 */     if (PlatformInfo.hasJMXObjectName()) {
/* 77 */       rs.addRule(new ElementSelector("configuration/jmxConfigurator"), new JMXConfiguratorAction());
/*    */     }
/*    */     
/* 80 */     rs.addRule(new ElementSelector("configuration/include"), new IncludeAction());
/*    */     
/* 82 */     rs.addRule(new ElementSelector("configuration/consolePlugin"), new ConsolePluginAction());
/*    */     
/*    */ 
/* 85 */     rs.addRule(new ElementSelector("configuration/receiver"), new ReceiverAction());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry)
/*    */   {
/* 93 */     DefaultNestedComponentRules.addDefaultNestedComponentRegistryRules(registry);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\joran\JoranConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */