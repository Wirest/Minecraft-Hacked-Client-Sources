/*    */ package ch.qos.logback.classic.pattern;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*    */ import ch.qos.logback.classic.spi.ThrowableProxyUtil;
/*    */ import ch.qos.logback.core.CoreConstants;
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
/*    */ public class RootCauseFirstThrowableProxyConverter
/*    */   extends ExtendedThrowableProxyConverter
/*    */ {
/*    */   protected String throwableProxyToString(IThrowableProxy tp)
/*    */   {
/* 28 */     StringBuilder buf = new StringBuilder(2048);
/* 29 */     recursiveAppendRootCauseFirst(buf, null, 1, tp);
/* 30 */     return buf.toString();
/*    */   }
/*    */   
/*    */   protected void recursiveAppendRootCauseFirst(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
/* 34 */     if (tp.getCause() != null) {
/* 35 */       recursiveAppendRootCauseFirst(sb, prefix, indent, tp.getCause());
/* 36 */       prefix = null;
/*    */     }
/* 38 */     ThrowableProxyUtil.indent(sb, indent - 1);
/* 39 */     if (prefix != null) {
/* 40 */       sb.append(prefix);
/*    */     }
/* 42 */     ThrowableProxyUtil.subjoinFirstLineRootCauseFirst(sb, tp);
/* 43 */     sb.append(CoreConstants.LINE_SEPARATOR);
/* 44 */     subjoinSTEPArray(sb, indent, tp);
/* 45 */     IThrowableProxy[] suppressed = tp.getSuppressed();
/* 46 */     if (suppressed != null) {
/* 47 */       for (IThrowableProxy current : suppressed) {
/* 48 */         recursiveAppendRootCauseFirst(sb, "Suppressed: ", indent + 1, current);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\RootCauseFirstThrowableProxyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */