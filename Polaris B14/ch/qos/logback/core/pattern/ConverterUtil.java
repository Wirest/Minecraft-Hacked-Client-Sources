/*    */ package ch.qos.logback.core.pattern;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.spi.ContextAware;
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
/*    */ public class ConverterUtil
/*    */ {
/*    */   public static <E> void startConverters(Converter<E> head)
/*    */   {
/* 27 */     Converter<E> c = head;
/* 28 */     while (c != null)
/*    */     {
/* 30 */       if ((c instanceof CompositeConverter)) {
/* 31 */         CompositeConverter<E> cc = (CompositeConverter)c;
/* 32 */         Converter<E> childConverter = cc.childConverter;
/* 33 */         startConverters(childConverter);
/* 34 */         cc.start();
/* 35 */       } else if ((c instanceof DynamicConverter)) {
/* 36 */         DynamicConverter<E> dc = (DynamicConverter)c;
/* 37 */         dc.start();
/*    */       }
/* 39 */       c = c.getNext();
/*    */     }
/*    */   }
/*    */   
/*    */   public static <E> Converter<E> findTail(Converter<E> head)
/*    */   {
/* 45 */     Converter<E> p = head;
/* 46 */     while (p != null) {
/* 47 */       Converter<E> next = p.getNext();
/* 48 */       if (next == null) {
/*    */         break;
/*    */       }
/* 51 */       p = next;
/*    */     }
/*    */     
/* 54 */     return p;
/*    */   }
/*    */   
/*    */   public static <E> void setContextForConverters(Context context, Converter<E> head) {
/* 58 */     Converter<E> c = head;
/* 59 */     while (c != null) {
/* 60 */       if ((c instanceof ContextAware)) {
/* 61 */         ((ContextAware)c).setContext(context);
/*    */       }
/* 63 */       c = c.getNext();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\ConverterUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */