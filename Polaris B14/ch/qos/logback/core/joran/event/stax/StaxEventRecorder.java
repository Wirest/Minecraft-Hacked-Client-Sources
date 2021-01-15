/*     */ package ch.qos.logback.core.joran.event.stax;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.ElementPath;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLEventReader;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.events.Characters;
/*     */ import javax.xml.stream.events.EndElement;
/*     */ import javax.xml.stream.events.StartElement;
/*     */ import javax.xml.stream.events.XMLEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StaxEventRecorder
/*     */   extends ContextAwareBase
/*     */ {
/*  34 */   List<StaxEvent> eventList = new ArrayList();
/*  35 */   ElementPath globalElementPath = new ElementPath();
/*     */   
/*     */   public StaxEventRecorder(Context context) {
/*  38 */     setContext(context);
/*     */   }
/*     */   
/*     */   public void recordEvents(InputStream inputStream) throws JoranException {
/*     */     try {
/*  43 */       XMLEventReader xmlEventReader = XMLInputFactory.newInstance().createXMLEventReader(inputStream);
/*  44 */       read(xmlEventReader);
/*     */     } catch (XMLStreamException e) {
/*  46 */       throw new JoranException("Problem parsing XML document. See previously reported errors.", e);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<StaxEvent> getEventList() {
/*  51 */     return this.eventList;
/*     */   }
/*     */   
/*     */   private void read(XMLEventReader xmlEventReader) throws XMLStreamException {
/*  55 */     while (xmlEventReader.hasNext()) {
/*  56 */       XMLEvent xmlEvent = xmlEventReader.nextEvent();
/*  57 */       switch (xmlEvent.getEventType()) {
/*     */       case 1: 
/*  59 */         addStartElement(xmlEvent);
/*  60 */         break;
/*     */       case 4: 
/*  62 */         addCharacters(xmlEvent);
/*  63 */         break;
/*     */       case 2: 
/*  65 */         addEndEvent(xmlEvent);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void addStartElement(XMLEvent xmlEvent)
/*     */   {
/*  74 */     StartElement se = xmlEvent.asStartElement();
/*  75 */     String tagName = se.getName().getLocalPart();
/*  76 */     this.globalElementPath.push(tagName);
/*  77 */     ElementPath current = this.globalElementPath.duplicate();
/*  78 */     StartEvent startEvent = new StartEvent(current, tagName, se.getAttributes(), se.getLocation());
/*  79 */     this.eventList.add(startEvent);
/*     */   }
/*     */   
/*     */   private void addCharacters(XMLEvent xmlEvent) {
/*  83 */     Characters characters = xmlEvent.asCharacters();
/*  84 */     StaxEvent lastEvent = getLastEvent();
/*     */     
/*  86 */     if ((lastEvent instanceof BodyEvent)) {
/*  87 */       BodyEvent be = (BodyEvent)lastEvent;
/*  88 */       be.append(characters.getData());
/*     */ 
/*     */     }
/*  91 */     else if (!characters.isWhiteSpace()) {
/*  92 */       BodyEvent bodyEvent = new BodyEvent(characters.getData(), xmlEvent.getLocation());
/*  93 */       this.eventList.add(bodyEvent);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addEndEvent(XMLEvent xmlEvent)
/*     */   {
/*  99 */     EndElement ee = xmlEvent.asEndElement();
/* 100 */     String tagName = ee.getName().getLocalPart();
/* 101 */     EndEvent endEvent = new EndEvent(tagName, ee.getLocation());
/* 102 */     this.eventList.add(endEvent);
/* 103 */     this.globalElementPath.pop();
/*     */   }
/*     */   
/*     */   StaxEvent getLastEvent() {
/* 107 */     if (this.eventList.isEmpty()) {
/* 108 */       return null;
/*     */     }
/* 110 */     int size = this.eventList.size();
/* 111 */     if (size == 0)
/* 112 */       return null;
/* 113 */     return (StaxEvent)this.eventList.get(size - 1);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\stax\StaxEventRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */