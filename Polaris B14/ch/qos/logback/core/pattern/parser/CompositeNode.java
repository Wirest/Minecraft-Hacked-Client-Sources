/*    */ package ch.qos.logback.core.pattern.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CompositeNode
/*    */   extends SimpleKeywordNode
/*    */ {
/*    */   Node childNode;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   CompositeNode(String keyword)
/*    */   {
/* 20 */     super(2, keyword);
/*    */   }
/*    */   
/*    */   public Node getChildNode()
/*    */   {
/* 25 */     return this.childNode;
/*    */   }
/*    */   
/*    */   public void setChildNode(Node childNode) {
/* 29 */     this.childNode = childNode;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o) {
/* 33 */     if (!super.equals(o)) {
/* 34 */       return false;
/*    */     }
/* 36 */     if (!(o instanceof CompositeNode)) {
/* 37 */       return false;
/*    */     }
/* 39 */     CompositeNode r = (CompositeNode)o;
/*    */     
/* 41 */     return r.childNode == null ? true : this.childNode != null ? this.childNode.equals(r.childNode) : false;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 47 */     return super.hashCode();
/*    */   }
/*    */   
/*    */   public String toString() {
/* 51 */     StringBuilder buf = new StringBuilder();
/* 52 */     if (this.childNode != null) {
/* 53 */       buf.append("CompositeNode(" + this.childNode + ")");
/*    */     } else {
/* 55 */       buf.append("CompositeNode(no child)");
/*    */     }
/* 57 */     buf.append(printNext());
/* 58 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\parser\CompositeNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */