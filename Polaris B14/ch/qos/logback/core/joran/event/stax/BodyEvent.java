/*    */ package ch.qos.logback.core.joran.event.stax;
/*    */ 
/*    */ import javax.xml.stream.Location;
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
/*    */   extends StaxEvent
/*    */ {
/*    */   private String text;
/*    */   
/*    */   BodyEvent(String text, Location location)
/*    */   {
/* 24 */     super(null, location);
/* 25 */     this.text = text;
/*    */   }
/*    */   
/*    */   public String getText() {
/* 29 */     return this.text;
/*    */   }
/*    */   
/*    */   void append(String txt) {
/* 33 */     this.text += txt;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 38 */     return "BodyEvent(" + getText() + ")" + this.location.getLineNumber() + "," + this.location.getColumnNumber();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\stax\BodyEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */