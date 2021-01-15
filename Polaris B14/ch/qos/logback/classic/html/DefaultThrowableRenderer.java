/*    */ package ch.qos.logback.classic.html;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*    */ import ch.qos.logback.classic.spi.StackTraceElementProxy;
/*    */ import ch.qos.logback.core.CoreConstants;
/*    */ import ch.qos.logback.core.helpers.Transform;
/*    */ import ch.qos.logback.core.html.IThrowableRenderer;
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
/*    */ public class DefaultThrowableRenderer
/*    */   implements IThrowableRenderer<ILoggingEvent>
/*    */ {
/*    */   static final String TRACE_PREFIX = "<br />&nbsp;&nbsp;&nbsp;&nbsp;";
/*    */   
/*    */   public void render(StringBuilder sbuf, ILoggingEvent event)
/*    */   {
/* 29 */     IThrowableProxy tp = event.getThrowableProxy();
/* 30 */     sbuf.append("<tr><td class=\"Exception\" colspan=\"6\">");
/* 31 */     while (tp != null) {
/* 32 */       render(sbuf, tp);
/* 33 */       tp = tp.getCause();
/*    */     }
/* 35 */     sbuf.append("</td></tr>");
/*    */   }
/*    */   
/*    */   void render(StringBuilder sbuf, IThrowableProxy tp) {
/* 39 */     printFirstLine(sbuf, tp);
/*    */     
/* 41 */     int commonFrames = tp.getCommonFrames();
/* 42 */     StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
/*    */     
/* 44 */     for (int i = 0; i < stepArray.length - commonFrames; i++) {
/* 45 */       StackTraceElementProxy step = stepArray[i];
/* 46 */       sbuf.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
/* 47 */       sbuf.append(Transform.escapeTags(step.toString()));
/* 48 */       sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     }
/*    */     
/* 51 */     if (commonFrames > 0) {
/* 52 */       sbuf.append("<br />&nbsp;&nbsp;&nbsp;&nbsp;");
/* 53 */       sbuf.append("\t... ").append(commonFrames).append(" common frames omitted").append(CoreConstants.LINE_SEPARATOR);
/*    */     }
/*    */   }
/*    */   
/*    */   public void printFirstLine(StringBuilder sb, IThrowableProxy tp)
/*    */   {
/* 59 */     int commonFrames = tp.getCommonFrames();
/* 60 */     if (commonFrames > 0) {
/* 61 */       sb.append("<br />").append("Caused by: ");
/*    */     }
/* 63 */     sb.append(tp.getClassName()).append(": ").append(Transform.escapeTags(tp.getMessage()));
/*    */     
/* 65 */     sb.append(CoreConstants.LINE_SEPARATOR);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\html\DefaultThrowableRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */