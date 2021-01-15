/*    */ package ch.qos.logback.classic.joran.action;
/*    */ 
/*    */ import ch.qos.logback.classic.LoggerContext;
/*    */ import ch.qos.logback.classic.jmx.JMXConfigurator;
/*    */ import ch.qos.logback.classic.jmx.MBeanUtil;
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.joran.action.Action;
/*    */ import ch.qos.logback.core.joran.spi.ActionException;
/*    */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*    */ import ch.qos.logback.core.util.OptionHelper;
/*    */ import java.lang.management.ManagementFactory;
/*    */ import javax.management.MBeanServer;
/*    */ import javax.management.ObjectName;
/*    */ import org.xml.sax.Attributes;
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
/*    */ public class JMXConfiguratorAction
/*    */   extends Action
/*    */ {
/*    */   static final String OBJECT_NAME_ATTRIBUTE_NAME = "objectName";
/*    */   static final String CONTEXT_NAME_ATTRIBUTE_NAME = "contextName";
/*    */   static final char JMX_NAME_SEPARATOR = ',';
/*    */   
/*    */   public void begin(InterpretationContext ec, String name, Attributes attributes)
/*    */     throws ActionException
/*    */   {
/* 40 */     addInfo("begin");
/*    */     
/*    */ 
/* 43 */     String contextName = this.context.getName();
/* 44 */     String contextNameAttributeVal = attributes.getValue("contextName");
/*    */     
/* 46 */     if (!OptionHelper.isEmpty(contextNameAttributeVal)) {
/* 47 */       contextName = contextNameAttributeVal;
/*    */     }
/*    */     
/*    */ 
/* 51 */     String objectNameAttributeVal = attributes.getValue("objectName");
/*    */     String objectNameAsStr;
/* 53 */     String objectNameAsStr; if (OptionHelper.isEmpty(objectNameAttributeVal)) {
/* 54 */       objectNameAsStr = MBeanUtil.getObjectNameFor(contextName, JMXConfigurator.class);
/*    */     }
/*    */     else {
/* 57 */       objectNameAsStr = objectNameAttributeVal;
/*    */     }
/*    */     
/* 60 */     ObjectName objectName = MBeanUtil.string2ObjectName(this.context, this, objectNameAsStr);
/*    */     
/* 62 */     if (objectName == null) {
/* 63 */       addError("Failed construct ObjectName for [" + objectNameAsStr + "]");
/* 64 */       return;
/*    */     }
/*    */     
/* 67 */     MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/* 68 */     if (!MBeanUtil.isRegistered(mbs, objectName))
/*    */     {
/*    */ 
/*    */ 
/*    */ 
/* 73 */       JMXConfigurator jmxConfigurator = new JMXConfigurator((LoggerContext)this.context, mbs, objectName);
/*    */       try
/*    */       {
/* 76 */         mbs.registerMBean(jmxConfigurator, objectName);
/*    */       } catch (Exception e) {
/* 78 */         addError("Failed to create mbean", e);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void end(InterpretationContext ec, String name)
/*    */     throws ActionException
/*    */   {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\joran\action\JMXConfiguratorAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */