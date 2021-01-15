/*     */ package ch.qos.logback.core.joran.event;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.spi.ElementPath;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.ContextAwareImpl;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SaxEventRecorder
/*     */   extends DefaultHandler
/*     */   implements ContextAware
/*     */ {
/*     */   final ContextAwareImpl cai;
/*     */   
/*     */   public SaxEventRecorder(Context context)
/*     */   {
/*  46 */     this.cai = new ContextAwareImpl(context, this);
/*     */   }
/*     */   
/*  49 */   public List<SaxEvent> saxEventList = new ArrayList();
/*     */   Locator locator;
/*  51 */   ElementPath globalElementPath = new ElementPath();
/*     */   
/*     */   public final void recordEvents(InputStream inputStream) throws JoranException {
/*  54 */     recordEvents(new InputSource(inputStream));
/*     */   }
/*     */   
/*     */   public List<SaxEvent> recordEvents(InputSource inputSource) throws JoranException
/*     */   {
/*  59 */     SAXParser saxParser = buildSaxParser();
/*     */     try {
/*  61 */       saxParser.parse(inputSource, this);
/*  62 */       return this.saxEventList;
/*     */     } catch (IOException ie) {
/*  64 */       handleError("I/O error occurred while parsing xml file", ie);
/*     */     }
/*     */     catch (SAXException se) {
/*  67 */       throw new JoranException("Problem parsing XML document. See previously reported errors.", se);
/*     */     } catch (Exception ex) {
/*  69 */       handleError("Unexpected exception while parsing XML document.", ex);
/*     */     }
/*  71 */     throw new IllegalStateException("This point can never be reached");
/*     */   }
/*     */   
/*     */   private void handleError(String errMsg, Throwable t) throws JoranException {
/*  75 */     addError(errMsg, t);
/*  76 */     throw new JoranException(errMsg, t);
/*     */   }
/*     */   
/*     */   private SAXParser buildSaxParser() throws JoranException {
/*     */     try {
/*  81 */       SAXParserFactory spf = SAXParserFactory.newInstance();
/*  82 */       spf.setValidating(false);
/*  83 */       spf.setNamespaceAware(true);
/*  84 */       return spf.newSAXParser();
/*     */     } catch (Exception pce) {
/*  86 */       String errMsg = "Parser configuration error occurred";
/*  87 */       addError(errMsg, pce);
/*  88 */       throw new JoranException(errMsg, pce);
/*     */     }
/*     */   }
/*     */   
/*     */   public void startDocument() {}
/*     */   
/*     */   public Locator getLocator()
/*     */   {
/*  96 */     return this.locator;
/*     */   }
/*     */   
/*     */   public void setDocumentLocator(Locator l) {
/* 100 */     this.locator = l;
/*     */   }
/*     */   
/*     */ 
/*     */   public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
/*     */   {
/* 106 */     String tagName = getTagName(localName, qName);
/* 107 */     this.globalElementPath.push(tagName);
/* 108 */     ElementPath current = this.globalElementPath.duplicate();
/* 109 */     this.saxEventList.add(new StartEvent(current, namespaceURI, localName, qName, atts, getLocator()));
/*     */   }
/*     */   
/*     */   public void characters(char[] ch, int start, int length)
/*     */   {
/* 114 */     String bodyStr = new String(ch, start, length);
/* 115 */     SaxEvent lastEvent = getLastEvent();
/* 116 */     if ((lastEvent instanceof BodyEvent)) {
/* 117 */       BodyEvent be = (BodyEvent)lastEvent;
/* 118 */       be.append(bodyStr);
/*     */ 
/*     */     }
/* 121 */     else if (!isSpaceOnly(bodyStr)) {
/* 122 */       this.saxEventList.add(new BodyEvent(bodyStr, getLocator()));
/*     */     }
/*     */   }
/*     */   
/*     */   boolean isSpaceOnly(String bodyStr)
/*     */   {
/* 128 */     String bodyTrimmed = bodyStr.trim();
/* 129 */     return bodyTrimmed.length() == 0;
/*     */   }
/*     */   
/*     */   SaxEvent getLastEvent() {
/* 133 */     if (this.saxEventList.isEmpty()) {
/* 134 */       return null;
/*     */     }
/* 136 */     int size = this.saxEventList.size();
/* 137 */     return (SaxEvent)this.saxEventList.get(size - 1);
/*     */   }
/*     */   
/*     */   public void endElement(String namespaceURI, String localName, String qName) {
/* 141 */     this.saxEventList.add(new EndEvent(namespaceURI, localName, qName, getLocator()));
/*     */     
/* 143 */     this.globalElementPath.pop();
/*     */   }
/*     */   
/*     */   String getTagName(String localName, String qName) {
/* 147 */     String tagName = localName;
/* 148 */     if ((tagName == null) || (tagName.length() < 1)) {
/* 149 */       tagName = qName;
/*     */     }
/* 151 */     return tagName;
/*     */   }
/*     */   
/*     */   public void error(SAXParseException spe) throws SAXException {
/* 155 */     addError("XML_PARSING - Parsing error on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber(), spe);
/*     */   }
/*     */   
/*     */   public void fatalError(SAXParseException spe) throws SAXException
/*     */   {
/* 160 */     addError("XML_PARSING - Parsing fatal error on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber(), spe);
/*     */   }
/*     */   
/*     */   public void warning(SAXParseException spe) throws SAXException
/*     */   {
/* 165 */     addWarn("XML_PARSING - Parsing warning on line " + spe.getLineNumber() + " and column " + spe.getColumnNumber(), spe);
/*     */   }
/*     */   
/*     */   public void addError(String msg)
/*     */   {
/* 170 */     this.cai.addError(msg);
/*     */   }
/*     */   
/*     */   public void addError(String msg, Throwable ex) {
/* 174 */     this.cai.addError(msg, ex);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg) {
/* 178 */     this.cai.addInfo(msg);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg, Throwable ex) {
/* 182 */     this.cai.addInfo(msg, ex);
/*     */   }
/*     */   
/*     */   public void addStatus(Status status) {
/* 186 */     this.cai.addStatus(status);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg) {
/* 190 */     this.cai.addWarn(msg);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg, Throwable ex) {
/* 194 */     this.cai.addWarn(msg, ex);
/*     */   }
/*     */   
/*     */   public Context getContext() {
/* 198 */     return this.cai.getContext();
/*     */   }
/*     */   
/*     */   public void setContext(Context context) {
/* 202 */     this.cai.setContext(context);
/*     */   }
/*     */   
/*     */   public List<SaxEvent> getSaxEventList() {
/* 206 */     return this.saxEventList;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\event\SaxEventRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */