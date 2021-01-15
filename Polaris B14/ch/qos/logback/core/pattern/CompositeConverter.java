/*    */ package ch.qos.logback.core.pattern;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class CompositeConverter<E>
/*    */   extends DynamicConverter<E>
/*    */ {
/*    */   Converter<E> childConverter;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String convert(E event)
/*    */   {
/* 21 */     StringBuilder buf = new StringBuilder();
/*    */     
/* 23 */     for (Converter<E> c = this.childConverter; c != null; c = c.next) {
/* 24 */       c.write(buf, event);
/*    */     }
/* 26 */     String intermediary = buf.toString();
/* 27 */     return transform(event, intermediary);
/*    */   }
/*    */   
/*    */   protected abstract String transform(E paramE, String paramString);
/*    */   
/*    */   public Converter<E> getChildConverter() {
/* 33 */     return this.childConverter;
/*    */   }
/*    */   
/*    */   public void setChildConverter(Converter<E> child) {
/* 37 */     this.childConverter = child;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 41 */     StringBuilder buf = new StringBuilder();
/* 42 */     buf.append("CompositeConverter<");
/*    */     
/* 44 */     if (this.formattingInfo != null) {
/* 45 */       buf.append(this.formattingInfo);
/*    */     }
/* 47 */     if (this.childConverter != null) {
/* 48 */       buf.append(", children: ").append(this.childConverter);
/*    */     }
/* 50 */     buf.append(">");
/* 51 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\CompositeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */