/*    */ package ch.qos.logback.core.pattern.parser;
/*    */ 
/*    */ import ch.qos.logback.core.pattern.FormatInfo;
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
/*    */ public class FormattingNode
/*    */   extends Node
/*    */ {
/*    */   FormatInfo formatInfo;
/*    */   
/*    */   FormattingNode(int type)
/*    */   {
/* 23 */     super(type);
/*    */   }
/*    */   
/*    */   FormattingNode(int type, Object value) {
/* 27 */     super(type, value);
/*    */   }
/*    */   
/*    */   public FormatInfo getFormatInfo() {
/* 31 */     return this.formatInfo;
/*    */   }
/*    */   
/*    */   public void setFormatInfo(FormatInfo formatInfo) {
/* 35 */     this.formatInfo = formatInfo;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 39 */     if (!super.equals(o)) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     if (!(o instanceof FormattingNode)) {
/* 44 */       return false;
/*    */     }
/* 46 */     FormattingNode r = (FormattingNode)o;
/*    */     
/* 48 */     return r.formatInfo == null ? true : this.formatInfo != null ? this.formatInfo.equals(r.formatInfo) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 54 */     int result = super.hashCode();
/* 55 */     result = 31 * result + (this.formatInfo != null ? this.formatInfo.hashCode() : 0);
/* 56 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\parser\FormattingNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */