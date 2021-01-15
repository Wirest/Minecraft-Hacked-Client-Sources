/*    */ package ch.qos.logback.core.joran.event;
/*    */ 
/*    */ import org.xml.sax.Locator;
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
/*    */ public class BodyEvent
/*    */   extends SaxEvent
/*    */ {
/*    */   private String text;
/*    */   
/*    */   BodyEvent(String text, Locator locator)
/*    */   {
/* 24 */     super(null, null, null, locator);
/* 25 */     this.text = text;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getText()
/*    */   {
/* 34 */     if (this.text != null) {
/* 35 */       return this.text.trim();
/*    */     }
/* 37 */     return this.text;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 42 */     return "BodyEvent(" + getText() + ")" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber();
/*    */   }
/*    */   
/*    */   public void append(String str)
/*    */   {
/* 47 */     this.text += str;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\BodyEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */