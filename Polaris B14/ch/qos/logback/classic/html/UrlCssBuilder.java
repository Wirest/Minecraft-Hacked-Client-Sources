/*    */ package ch.qos.logback.classic.html;
/*    */ 
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
/*    */ public class UrlCssBuilder
/*    */   implements CssBuilder
/*    */ {
/* 28 */   String url = "http://logback.qos.ch/css/classic.css";
/*    */   
/*    */   public String getUrl() {
/* 31 */     return this.url;
/*    */   }
/*    */   
/*    */   public void setUrl(String url) {
/* 35 */     this.url = url;
/*    */   }
/*    */   
/*    */   public void addCss(StringBuilder sbuf) {
/* 39 */     sbuf.append("<link REL=StyleSheet HREF=\"");
/* 40 */     sbuf.append(this.url);
/* 41 */     sbuf.append("\" TITLE=\"Basic\" />");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\html\UrlCssBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */