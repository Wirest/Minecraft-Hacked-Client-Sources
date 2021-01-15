/*    */ package ch.qos.logback.classic.boolex;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.boolex.EvaluationException;
/*    */ import ch.qos.logback.core.boolex.EventEvaluatorBase;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class OnMarkerEvaluator
/*    */   extends EventEvaluatorBase<ILoggingEvent>
/*    */ {
/* 33 */   List<String> markerList = new ArrayList();
/*    */   
/*    */   public void addMarker(String markerStr) {
/* 36 */     this.markerList.add(markerStr);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean evaluate(ILoggingEvent event)
/*    */     throws NullPointerException, EvaluationException
/*    */   {
/* 46 */     Marker eventsMarker = event.getMarker();
/* 47 */     if (eventsMarker == null) {
/* 48 */       return false;
/*    */     }
/*    */     
/* 51 */     for (String markerStr : this.markerList) {
/* 52 */       if (eventsMarker.contains(markerStr)) {
/* 53 */         return true;
/*    */       }
/*    */     }
/* 56 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\boolex\OnMarkerEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */