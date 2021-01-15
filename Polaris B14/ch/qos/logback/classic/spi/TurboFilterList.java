/*    */ package ch.qos.logback.classic.spi;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import ch.qos.logback.classic.turbo.TurboFilter;
/*    */ import ch.qos.logback.core.spi.FilterReply;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ import org.slf4j.Marker;
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
/*    */ public final class TurboFilterList
/*    */   extends CopyOnWriteArrayList<TurboFilter>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public FilterReply getTurboFilterChainDecision(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
/*    */   {
/* 44 */     int size = size();
/*    */     
/*    */ 
/*    */ 
/* 48 */     if (size == 1) {
/*    */       try {
/* 50 */         TurboFilter tf = (TurboFilter)get(0);
/* 51 */         return tf.decide(marker, logger, level, format, params, t);
/*    */       } catch (IndexOutOfBoundsException iobe) {
/* 53 */         return FilterReply.NEUTRAL;
/*    */       }
/*    */     }
/*    */     
/* 57 */     Object[] tfa = toArray();
/* 58 */     int len = tfa.length;
/* 59 */     for (int i = 0; i < len; i++)
/*    */     {
/* 61 */       TurboFilter tf = (TurboFilter)tfa[i];
/* 62 */       FilterReply r = tf.decide(marker, logger, level, format, params, t);
/* 63 */       if ((r == FilterReply.DENY) || (r == FilterReply.ACCEPT)) {
/* 64 */         return r;
/*    */       }
/*    */     }
/* 67 */     return FilterReply.NEUTRAL;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\TurboFilterList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */