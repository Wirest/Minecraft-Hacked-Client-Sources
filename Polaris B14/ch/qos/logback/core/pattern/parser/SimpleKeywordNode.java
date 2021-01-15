/*    */ package ch.qos.logback.core.pattern.parser;
/*    */ 
/*    */ import java.util.List;
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
/*    */ public class SimpleKeywordNode
/*    */   extends FormattingNode
/*    */ {
/*    */   List<String> optionList;
/*    */   
/*    */   SimpleKeywordNode(Object value)
/*    */   {
/* 23 */     super(1, value);
/*    */   }
/*    */   
/*    */   protected SimpleKeywordNode(int type, Object value) {
/* 27 */     super(type, value);
/*    */   }
/*    */   
/*    */   public List<String> getOptions() {
/* 31 */     return this.optionList;
/*    */   }
/*    */   
/*    */   public void setOptions(List<String> optionList) {
/* 35 */     this.optionList = optionList;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 39 */     if (!super.equals(o)) {
/* 40 */       return false;
/*    */     }
/*    */     
/* 43 */     if (!(o instanceof SimpleKeywordNode)) {
/* 44 */       return false;
/*    */     }
/* 46 */     SimpleKeywordNode r = (SimpleKeywordNode)o;
/*    */     
/* 48 */     return r.optionList == null ? true : this.optionList != null ? this.optionList.equals(r.optionList) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 54 */     return super.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 58 */     StringBuilder buf = new StringBuilder();
/* 59 */     if (this.optionList == null) {
/* 60 */       buf.append("KeyWord(" + this.value + "," + this.formatInfo + ")");
/*    */     } else {
/* 62 */       buf.append("KeyWord(" + this.value + ", " + this.formatInfo + "," + this.optionList + ")");
/*    */     }
/*    */     
/* 65 */     buf.append(printNext());
/* 66 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\parser\SimpleKeywordNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */