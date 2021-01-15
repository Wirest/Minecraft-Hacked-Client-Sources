/*    */ package ch.qos.logback.core.joran.event;
/*    */ 
/*    */ import org.xml.sax.Locator;
/*    */ import org.xml.sax.helpers.LocatorImpl;
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
/*    */ public class SaxEvent
/*    */ {
/*    */   public final String namespaceURI;
/*    */   public final String localName;
/*    */   public final String qName;
/*    */   public final Locator locator;
/*    */   
/*    */   SaxEvent(String namespaceURI, String localName, String qName, Locator locator)
/*    */   {
/* 27 */     this.namespaceURI = namespaceURI;
/* 28 */     this.localName = localName;
/* 29 */     this.qName = qName;
/*    */     
/* 31 */     this.locator = new LocatorImpl(locator);
/*    */   }
/*    */   
/*    */   public String getLocalName() {
/* 35 */     return this.localName;
/*    */   }
/*    */   
/*    */   public Locator getLocator() {
/* 39 */     return this.locator;
/*    */   }
/*    */   
/*    */   public String getNamespaceURI() {
/* 43 */     return this.namespaceURI;
/*    */   }
/*    */   
/*    */   public String getQName() {
/* 47 */     return this.qName;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\SaxEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */