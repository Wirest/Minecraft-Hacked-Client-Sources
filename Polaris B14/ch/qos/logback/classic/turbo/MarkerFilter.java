/*    */ package ch.qos.logback.classic.turbo;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import ch.qos.logback.core.spi.FilterReply;
/*    */ import org.slf4j.Marker;
/*    */ import org.slf4j.MarkerFactory;
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
/*    */ public class MarkerFilter
/*    */   extends MatchingFilter
/*    */ {
/*    */   Marker markerToMatch;
/*    */   
/*    */   public void start()
/*    */   {
/* 33 */     if (this.markerToMatch != null) {
/* 34 */       super.start();
/*    */     } else {
/* 36 */       addError("The marker property must be set for [" + getName() + "]");
/*    */     }
/*    */   }
/*    */   
/*    */   public FilterReply decide(Marker marker, Logger logger, Level level, String format, Object[] params, Throwable t)
/*    */   {
/* 42 */     if (!isStarted()) {
/* 43 */       return FilterReply.NEUTRAL;
/*    */     }
/*    */     
/* 46 */     if (marker == null) {
/* 47 */       return this.onMismatch;
/*    */     }
/*    */     
/* 50 */     if (marker.contains(this.markerToMatch)) {
/* 51 */       return this.onMatch;
/*    */     }
/* 53 */     return this.onMismatch;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setMarker(String markerStr)
/*    */   {
/* 63 */     if (markerStr != null) {
/* 64 */       this.markerToMatch = MarkerFactory.getMarker(markerStr);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\turbo\MarkerFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */