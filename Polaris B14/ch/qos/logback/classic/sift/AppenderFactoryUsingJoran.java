/*    */ package ch.qos.logback.classic.sift;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import ch.qos.logback.core.sift.AbstractAppenderFactoryUsingJoran;
/*    */ import ch.qos.logback.core.sift.SiftingJoranConfiguratorBase;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class AppenderFactoryUsingJoran
/*    */   extends AbstractAppenderFactoryUsingJoran<ILoggingEvent>
/*    */ {
/*    */   AppenderFactoryUsingJoran(List<SaxEvent> eventList, String key, Map<String, String> parentPropertyMap)
/*    */   {
/* 31 */     super(eventList, key, parentPropertyMap);
/*    */   }
/*    */   
/*    */   public SiftingJoranConfiguratorBase<ILoggingEvent> getSiftingJoranConfigurator(String discriminatingValue) {
/* 35 */     return new SiftingJoranConfigurator(this.key, discriminatingValue, this.parentPropertyMap);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\sift\AppenderFactoryUsingJoran.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */