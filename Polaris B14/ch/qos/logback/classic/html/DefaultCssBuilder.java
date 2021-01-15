/*    */ package ch.qos.logback.classic.html;
/*    */ 
/*    */ import ch.qos.logback.core.CoreConstants;
/*    */ import ch.qos.logback.core.html.CssBuilder;
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
/*    */ public class DefaultCssBuilder
/*    */   implements CssBuilder
/*    */ {
/*    */   public void addCss(StringBuilder sbuf)
/*    */   {
/* 31 */     sbuf.append("<style  type=\"text/css\">");
/* 32 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 33 */     sbuf.append("table { margin-left: 2em; margin-right: 2em; border-left: 2px solid #AAA; }");
/*    */     
/* 35 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 37 */     sbuf.append("TR.even { background: #FFFFFF; }");
/* 38 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 40 */     sbuf.append("TR.odd { background: #EAEAEA; }");
/* 41 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 43 */     sbuf.append("TR.warn TD.Level, TR.error TD.Level, TR.fatal TD.Level {font-weight: bold; color: #FF4040 }");
/*    */     
/* 45 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 47 */     sbuf.append("TD { padding-right: 1ex; padding-left: 1ex; border-right: 2px solid #AAA; }");
/*    */     
/* 49 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 51 */     sbuf.append("TD.Time, TD.Date { text-align: right; font-family: courier, monospace; font-size: smaller; }");
/*    */     
/* 53 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 55 */     sbuf.append("TD.Thread { text-align: left; }");
/* 56 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 58 */     sbuf.append("TD.Level { text-align: right; }");
/* 59 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 61 */     sbuf.append("TD.Logger { text-align: left; }");
/* 62 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 64 */     sbuf.append("TR.header { background: #596ED5; color: #FFF; font-weight: bold; font-size: larger; }");
/*    */     
/* 66 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 68 */     sbuf.append("TD.Exception { background: #A2AEE8; font-family: courier, monospace;}");
/*    */     
/* 70 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */     
/* 72 */     sbuf.append("</style>");
/* 73 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\html\DefaultCssBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */