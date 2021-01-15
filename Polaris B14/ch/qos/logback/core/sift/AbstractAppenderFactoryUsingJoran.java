/*    */ package ch.qos.logback.core.sift;
/*    */ 
/*    */ import ch.qos.logback.core.Appender;
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.joran.event.SaxEvent;
/*    */ import ch.qos.logback.core.joran.spi.JoranException;
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
/*    */ 
/*    */ public abstract class AbstractAppenderFactoryUsingJoran<E>
/*    */   implements AppenderFactory<E>
/*    */ {
/*    */   final List<SaxEvent> eventList;
/*    */   protected String key;
/*    */   protected Map<String, String> parentPropertyMap;
/*    */   
/*    */   protected AbstractAppenderFactoryUsingJoran(List<SaxEvent> eventList, String key, Map<String, String> parentPropertyMap)
/*    */   {
/* 36 */     this.eventList = removeSiftElement(eventList);
/* 37 */     this.key = key;
/* 38 */     this.parentPropertyMap = parentPropertyMap;
/*    */   }
/*    */   
/*    */   List<SaxEvent> removeSiftElement(List<SaxEvent> eventList)
/*    */   {
/* 43 */     return eventList.subList(1, eventList.size() - 1);
/*    */   }
/*    */   
/*    */   public abstract SiftingJoranConfiguratorBase<E> getSiftingJoranConfigurator(String paramString);
/*    */   
/*    */   public Appender<E> buildAppender(Context context, String discriminatingValue) throws JoranException {
/* 49 */     SiftingJoranConfiguratorBase<E> sjc = getSiftingJoranConfigurator(discriminatingValue);
/* 50 */     sjc.setContext(context);
/* 51 */     sjc.doConfigure(this.eventList);
/* 52 */     return sjc.getAppender();
/*    */   }
/*    */   
/*    */   public List<SaxEvent> getEventList() {
/* 56 */     return this.eventList;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\sift\AbstractAppenderFactoryUsingJoran.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */