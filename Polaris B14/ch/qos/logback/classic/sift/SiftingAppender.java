/*    */ package ch.qos.logback.classic.sift;
/*    */ 
/*    */ import ch.qos.logback.classic.ClassicConstants;
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.joran.spi.DefaultClass;
/*    */ import ch.qos.logback.core.sift.Discriminator;
/*    */ import ch.qos.logback.core.sift.SiftingAppenderBase;
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
/*    */ public class SiftingAppender
/*    */   extends SiftingAppenderBase<ILoggingEvent>
/*    */ {
/*    */   protected long getTimestamp(ILoggingEvent event)
/*    */   {
/* 37 */     return event.getTimeStamp();
/*    */   }
/*    */   
/*    */ 
/*    */   @DefaultClass(MDCBasedDiscriminator.class)
/*    */   public void setDiscriminator(Discriminator<ILoggingEvent> discriminator)
/*    */   {
/* 44 */     super.setDiscriminator(discriminator);
/*    */   }
/*    */   
/*    */   protected boolean eventMarksEndOfLife(ILoggingEvent event) {
/* 48 */     Marker marker = event.getMarker();
/* 49 */     if (marker == null) {
/* 50 */       return false;
/*    */     }
/* 52 */     return marker.contains(ClassicConstants.FINALIZE_SESSION_MARKER);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\sift\SiftingAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */