/*    */ package ch.qos.logback.core.joran.event.stax;
/*    */ 
/*    */ import ch.qos.logback.core.joran.spi.ElementPath;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.Location;
/*    */ import javax.xml.stream.events.Attribute;
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
/*    */   extends StaxEvent
/*    */ {
/*    */   List<Attribute> attributes;
/*    */   public ElementPath elementPath;
/*    */   
/*    */   StartEvent(ElementPath elementPath, String name, Iterator<Attribute> attributeIterator, Location location)
/*    */   {
/* 30 */     super(name, location);
/* 31 */     populateAttributes(attributeIterator);
/* 32 */     this.elementPath = elementPath;
/*    */   }
/*    */   
/*    */   private void populateAttributes(Iterator<Attribute> attributeIterator) {
/* 36 */     while (attributeIterator.hasNext()) {
/* 37 */       if (this.attributes == null) {
/* 38 */         this.attributes = new ArrayList(2);
/*    */       }
/* 40 */       this.attributes.add(attributeIterator.next());
/*    */     }
/*    */   }
/*    */   
/*    */   public ElementPath getElementPath() {
/* 45 */     return this.elementPath;
/*    */   }
/*    */   
/*    */   public List<Attribute> getAttributeList() {
/* 49 */     return this.attributes;
/*    */   }
/*    */   
/*    */   Attribute getAttributeByName(String name) {
/* 53 */     if (this.attributes == null) {
/* 54 */       return null;
/*    */     }
/* 56 */     for (Attribute attr : this.attributes) {
/* 57 */       if (name.equals(attr.getName().getLocalPart()))
/* 58 */         return attr;
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     return "StartEvent(" + getName() + ")  [" + this.location.getLineNumber() + "," + this.location.getColumnNumber() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\stax\StartEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */