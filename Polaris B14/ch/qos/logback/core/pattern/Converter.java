/*    */ package ch.qos.logback.core.pattern;
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
/*    */ public abstract class Converter<E>
/*    */ {
/*    */   Converter<E> next;
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
/*    */   public abstract String convert(E paramE);
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
/*    */   public void write(StringBuilder buf, E event)
/*    */   {
/* 42 */     buf.append(convert(event));
/*    */   }
/*    */   
/*    */   public final void setNext(Converter<E> next) {
/* 46 */     if (this.next != null) {
/* 47 */       throw new IllegalStateException("Next converter has been already set");
/*    */     }
/* 49 */     this.next = next;
/*    */   }
/*    */   
/*    */   public final Converter<E> getNext() {
/* 53 */     return this.next;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\Converter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */