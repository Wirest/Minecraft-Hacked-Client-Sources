/*     */ package ch.qos.logback.core.joran.action;
/*     */ 
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.joran.event.SaxEventRecorder;
/*     */ import ch.qos.logback.core.joran.spi.ActionException;
/*     */ import ch.qos.logback.core.joran.spi.EventPlayer;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.joran.spi.Interpreter;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
/*     */ import ch.qos.logback.core.util.Loader;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.List;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class IncludeAction
/*     */   extends Action
/*     */ {
/*     */   private static final String INCLUDED_TAG = "included";
/*     */   private static final String FILE_ATTR = "file";
/*     */   private static final String URL_ATTR = "url";
/*     */   private static final String RESOURCE_ATTR = "resource";
/*     */   private static final String OPTIONAL_ATTR = "optional";
/*     */   private String attributeInUse;
/*     */   private boolean optional;
/*     */   
/*     */   public void begin(InterpretationContext ec, String name, Attributes attributes)
/*     */     throws ActionException
/*     */   {
/*  51 */     SaxEventRecorder recorder = new SaxEventRecorder(this.context);
/*     */     
/*  53 */     this.attributeInUse = null;
/*  54 */     this.optional = OptionHelper.toBoolean(attributes.getValue("optional"), false);
/*     */     
/*  56 */     if (!checkAttributes(attributes)) {
/*  57 */       return;
/*     */     }
/*     */     
/*  60 */     InputStream in = getInputStream(ec, attributes);
/*     */     try
/*     */     {
/*  63 */       if (in != null) {
/*  64 */         parseAndRecord(in, recorder);
/*     */         
/*  66 */         trimHeadAndTail(recorder);
/*     */         
/*     */ 
/*  69 */         ec.getJoranInterpreter().getEventPlayer().addEventsDynamically(recorder.saxEventList, 2);
/*     */       }
/*     */     } catch (JoranException e) {
/*  72 */       addError("Error while parsing  " + this.attributeInUse, e);
/*     */     } finally {
/*  74 */       close(in);
/*     */     }
/*     */   }
/*     */   
/*     */   void close(InputStream in)
/*     */   {
/*  80 */     if (in != null) {
/*     */       try {
/*  82 */         in.close();
/*     */       }
/*     */       catch (IOException e) {}
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean checkAttributes(Attributes attributes) {
/*  89 */     String fileAttribute = attributes.getValue("file");
/*  90 */     String urlAttribute = attributes.getValue("url");
/*  91 */     String resourceAttribute = attributes.getValue("resource");
/*     */     
/*  93 */     int count = 0;
/*     */     
/*  95 */     if (!OptionHelper.isEmpty(fileAttribute)) {
/*  96 */       count++;
/*     */     }
/*  98 */     if (!OptionHelper.isEmpty(urlAttribute)) {
/*  99 */       count++;
/*     */     }
/* 101 */     if (!OptionHelper.isEmpty(resourceAttribute)) {
/* 102 */       count++;
/*     */     }
/*     */     
/* 105 */     if (count == 0) {
/* 106 */       addError("One of \"path\", \"resource\" or \"url\" attributes must be set.");
/* 107 */       return false; }
/* 108 */     if (count > 1) {
/* 109 */       addError("Only one of \"file\", \"url\" or \"resource\" attributes should be set.");
/* 110 */       return false; }
/* 111 */     if (count == 1) {
/* 112 */       return true;
/*     */     }
/* 114 */     throw new IllegalStateException("Count value [" + count + "] is not expected");
/*     */   }
/*     */   
/*     */   private InputStream getInputStreamByFilePath(String pathToFile)
/*     */   {
/*     */     try {
/* 120 */       return new FileInputStream(pathToFile);
/*     */     } catch (IOException ioe) {
/* 122 */       optionalWarning("File [" + pathToFile + "] does not exist."); }
/* 123 */     return null;
/*     */   }
/*     */   
/*     */   URL attributeToURL(String urlAttribute)
/*     */   {
/*     */     try {
/* 129 */       return new URL(urlAttribute);
/*     */     } catch (MalformedURLException mue) {
/* 131 */       String errMsg = "URL [" + urlAttribute + "] is not well formed.";
/* 132 */       addError(errMsg, mue); }
/* 133 */     return null;
/*     */   }
/*     */   
/*     */   private InputStream getInputStreamByUrl(URL url)
/*     */   {
/* 138 */     return openURL(url);
/*     */   }
/*     */   
/*     */   InputStream openURL(URL url) {
/*     */     try {
/* 143 */       return url.openStream();
/*     */     } catch (IOException e) {
/* 145 */       optionalWarning("Failed to open [" + url.toString() + "]"); }
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   URL resourceAsURL(String resourceAttribute)
/*     */   {
/* 151 */     URL url = Loader.getResourceBySelfClassLoader(resourceAttribute);
/* 152 */     if (url == null) {
/* 153 */       optionalWarning("Could not find resource corresponding to [" + resourceAttribute + "]");
/* 154 */       return null;
/*     */     }
/* 156 */     return url;
/*     */   }
/*     */   
/*     */   private void optionalWarning(String msg) {
/* 160 */     if (!this.optional) {
/* 161 */       addWarn(msg);
/*     */     }
/*     */   }
/*     */   
/*     */   URL filePathAsURL(String path) {
/* 166 */     URI uri = new File(path).toURI();
/*     */     try {
/* 168 */       return uri.toURL();
/*     */     }
/*     */     catch (MalformedURLException e) {
/* 171 */       e.printStackTrace(); }
/* 172 */     return null;
/*     */   }
/*     */   
/*     */   private InputStream getInputStreamByResource(URL url)
/*     */   {
/* 177 */     return openURL(url);
/*     */   }
/*     */   
/*     */   URL getInputURL(InterpretationContext ec, Attributes attributes) {
/* 181 */     String fileAttribute = attributes.getValue("file");
/* 182 */     String urlAttribute = attributes.getValue("url");
/* 183 */     String resourceAttribute = attributes.getValue("resource");
/*     */     
/* 185 */     if (!OptionHelper.isEmpty(fileAttribute)) {
/* 186 */       this.attributeInUse = ec.subst(fileAttribute);
/* 187 */       return filePathAsURL(this.attributeInUse);
/*     */     }
/*     */     
/* 190 */     if (!OptionHelper.isEmpty(urlAttribute)) {
/* 191 */       this.attributeInUse = ec.subst(urlAttribute);
/* 192 */       return attributeToURL(this.attributeInUse);
/*     */     }
/*     */     
/* 195 */     if (!OptionHelper.isEmpty(resourceAttribute)) {
/* 196 */       this.attributeInUse = ec.subst(resourceAttribute);
/* 197 */       return resourceAsURL(this.attributeInUse);
/*     */     }
/*     */     
/* 200 */     throw new IllegalStateException("A URL stream should have been returned");
/*     */   }
/*     */   
/*     */   InputStream getInputStream(InterpretationContext ec, Attributes attributes)
/*     */   {
/* 205 */     URL inputURL = getInputURL(ec, attributes);
/* 206 */     if (inputURL == null) {
/* 207 */       return null;
/*     */     }
/* 209 */     ConfigurationWatchListUtil.addToWatchList(this.context, inputURL);
/* 210 */     return openURL(inputURL);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void trimHeadAndTail(SaxEventRecorder recorder)
/*     */   {
/* 217 */     List<SaxEvent> saxEventList = recorder.saxEventList;
/*     */     
/* 219 */     if (saxEventList.size() == 0) {
/* 220 */       return;
/*     */     }
/*     */     
/* 223 */     SaxEvent first = (SaxEvent)saxEventList.get(0);
/* 224 */     if ((first != null) && (first.qName.equalsIgnoreCase("included"))) {
/* 225 */       saxEventList.remove(0);
/*     */     }
/*     */     
/* 228 */     SaxEvent last = (SaxEvent)saxEventList.get(recorder.saxEventList.size() - 1);
/* 229 */     if ((last != null) && (last.qName.equalsIgnoreCase("included"))) {
/* 230 */       saxEventList.remove(recorder.saxEventList.size() - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private void parseAndRecord(InputStream inputSource, SaxEventRecorder recorder) throws JoranException
/*     */   {
/* 236 */     recorder.setContext(this.context);
/* 237 */     recorder.recordEvents(inputSource);
/*     */   }
/*     */   
/*     */   public void end(InterpretationContext ec, String name)
/*     */     throws ActionException
/*     */   {}
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\action\IncludeAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */