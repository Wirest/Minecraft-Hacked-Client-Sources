/*    */ package ch.qos.logback.core.joran.event;
/*    */ 
/*    */ import ch.qos.logback.core.joran.spi.ElementPath;
/*    */ import org.xml.sax.Attributes;
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.AttributesImpl;
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
/*    */ public class StartEvent
/*    */   extends SaxEvent
/*    */ {
/*    */   public final Attributes attributes;
/*    */   public final ElementPath elementPath;
/*    */   
/*    */   StartEvent(ElementPath elementPath, String namespaceURI, String localName, String qName, Attributes attributes, Locator locator)
/*    */   {
/* 28 */     super(namespaceURI, localName, qName, locator);
/*    */     
/* 30 */     this.attributes = new AttributesImpl(attributes);
/* 31 */     this.elementPath = elementPath;
/*    */   }
/*    */   
/*    */   public Attributes getAttributes() {
/* 35 */     return this.attributes;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 41 */     return "StartEvent(" + getQName() + ")  [" + this.locator.getLineNumber() + "," + this.locator.getColumnNumber() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\StartEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */