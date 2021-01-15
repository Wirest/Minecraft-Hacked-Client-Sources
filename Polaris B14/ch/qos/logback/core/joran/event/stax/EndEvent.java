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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EndEvent
/*    */   extends StaxEvent
/*    */ {
/*    */   public EndEvent(String name, Location location)
/*    */   {
/* 28 */     super(name, location);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 33 */     return "EndEvent(" + getName() + ")  [" + this.location.getLineNumber() + "," + this.location.getColumnNumber() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\stax\EndEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */