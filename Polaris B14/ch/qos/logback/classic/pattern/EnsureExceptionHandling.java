/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.pattern.Converter;
/*    */ import ch.qos.logback.core.pattern.ConverterUtil;
/*    */ import ch.qos.logback.core.pattern.PostCompileProcessor;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnsureExceptionHandling
/*    */   implements PostCompileProcessor<ILoggingEvent>
/*    */ {
/*    */   public void process(Converter<ILoggingEvent> head)
/*    */   {
/* 40 */     if (head == null)
/*    */     {
/* 42 */       throw new IllegalArgumentException("cannot process empty chain");
/*    */     }
/* 44 */     if (!chainHandlesThrowable(head)) {
/* 45 */       Converter<ILoggingEvent> tail = ConverterUtil.findTail(head);
/* 46 */       Converter<ILoggingEvent> exConverter = new ExtendedThrowableProxyConverter();
/* 47 */       tail.setNext(exConverter);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean chainHandlesThrowable(Converter head)
/*    */   {
/* 60 */     Converter c = head;
/* 61 */     while (c != null) {
/* 62 */       if ((c instanceof ThrowableHandlingConverter)) {
/* 63 */         return true;
/*    */       }
/* 65 */       c = c.getNext();
/*    */     }
/* 67 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\EnsureExceptionHandling.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */