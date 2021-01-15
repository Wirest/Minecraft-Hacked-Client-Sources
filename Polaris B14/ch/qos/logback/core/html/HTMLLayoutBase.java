/*     */ package ch.qos.logback.core.html;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.LayoutBase;
/*     */ import ch.qos.logback.core.pattern.Converter;
/*     */ import ch.qos.logback.core.pattern.ConverterUtil;
/*     */ import ch.qos.logback.core.pattern.parser.Node;
/*     */ import ch.qos.logback.core.pattern.parser.Parser;
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HTMLLayoutBase<E>
/*     */   extends LayoutBase<E>
/*     */ {
/*     */   protected String pattern;
/*     */   protected Converter<E> head;
/*  42 */   protected String title = "Logback Log Messages";
/*     */   
/*     */ 
/*     */ 
/*     */   protected CssBuilder cssBuilder;
/*     */   
/*     */ 
/*  49 */   protected long counter = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPattern(String conversionPattern)
/*     */   {
/*  57 */     this.pattern = conversionPattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getPattern()
/*     */   {
/*  64 */     return this.pattern;
/*     */   }
/*     */   
/*     */   public CssBuilder getCssBuilder() {
/*  68 */     return this.cssBuilder;
/*     */   }
/*     */   
/*     */   public void setCssBuilder(CssBuilder cssBuilder) {
/*  72 */     this.cssBuilder = cssBuilder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  80 */     int errorCount = 0;
/*     */     try
/*     */     {
/*  83 */       Parser<E> p = new Parser(this.pattern);
/*  84 */       p.setContext(getContext());
/*  85 */       Node t = p.parse();
/*  86 */       this.head = p.compile(t, getEffectiveConverterMap());
/*  87 */       ConverterUtil.startConverters(this.head);
/*     */     } catch (ScanException ex) {
/*  89 */       addError("Incorrect pattern found", ex);
/*  90 */       errorCount++;
/*     */     }
/*     */     
/*  93 */     if (errorCount == 0) {
/*  94 */       this.started = true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract Map<String, String> getDefaultConverterMap();
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, String> getEffectiveConverterMap()
/*     */   {
/* 106 */     Map<String, String> effectiveMap = new HashMap();
/*     */     
/*     */ 
/* 109 */     Map<String, String> defaultMap = getDefaultConverterMap();
/* 110 */     if (defaultMap != null) {
/* 111 */       effectiveMap.putAll(defaultMap);
/*     */     }
/*     */     
/*     */ 
/* 115 */     Context context = getContext();
/* 116 */     if (context != null)
/*     */     {
/* 118 */       Map<String, String> contextMap = (Map)context.getObject("PATTERN_RULE_REGISTRY");
/*     */       
/* 120 */       if (contextMap != null) {
/* 121 */         effectiveMap.putAll(contextMap);
/*     */       }
/*     */     }
/* 124 */     return effectiveMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTitle(String title)
/*     */   {
/* 135 */     this.title = title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTitle()
/*     */   {
/* 142 */     return this.title;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getContentType()
/*     */   {
/* 150 */     return "text/html";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFileHeader()
/*     */   {
/* 158 */     StringBuilder sbuf = new StringBuilder();
/* 159 */     sbuf.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
/* 160 */     sbuf.append(" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
/* 161 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 162 */     sbuf.append("<html>");
/* 163 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 164 */     sbuf.append("  <head>");
/* 165 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 166 */     sbuf.append("    <title>");
/* 167 */     sbuf.append(this.title);
/* 168 */     sbuf.append("</title>");
/* 169 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*     */     
/* 171 */     this.cssBuilder.addCss(sbuf);
/*     */     
/* 173 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 174 */     sbuf.append("  </head>");
/* 175 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 176 */     sbuf.append("<body>");
/* 177 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*     */     
/* 179 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   public String getPresentationHeader() {
/* 183 */     StringBuilder sbuf = new StringBuilder();
/* 184 */     sbuf.append("<hr/>");
/* 185 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 186 */     sbuf.append("<p>Log session start time ");
/* 187 */     sbuf.append(new Date());
/* 188 */     sbuf.append("</p><p></p>");
/* 189 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 190 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 191 */     sbuf.append("<table cellspacing=\"0\">");
/* 192 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*     */     
/* 194 */     buildHeaderRowForTable(sbuf);
/*     */     
/* 196 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   private void buildHeaderRowForTable(StringBuilder sbuf)
/*     */   {
/* 201 */     Converter c = this.head;
/*     */     
/* 203 */     sbuf.append("<tr class=\"header\">");
/* 204 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 205 */     while (c != null) {
/* 206 */       String name = computeConverterName(c);
/* 207 */       if (name == null) {
/* 208 */         c = c.getNext();
/*     */       }
/*     */       else {
/* 211 */         sbuf.append("<td class=\"");
/* 212 */         sbuf.append(computeConverterName(c));
/* 213 */         sbuf.append("\">");
/* 214 */         sbuf.append(computeConverterName(c));
/* 215 */         sbuf.append("</td>");
/* 216 */         sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 217 */         c = c.getNext();
/*     */       } }
/* 219 */     sbuf.append("</tr>");
/* 220 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/*     */   }
/*     */   
/*     */   public String getPresentationFooter() {
/* 224 */     StringBuilder sbuf = new StringBuilder();
/* 225 */     sbuf.append("</table>");
/* 226 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFileFooter()
/*     */   {
/* 234 */     StringBuilder sbuf = new StringBuilder();
/* 235 */     sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 236 */     sbuf.append("</body></html>");
/* 237 */     return sbuf.toString();
/*     */   }
/*     */   
/*     */   protected void startNewTableIfLimitReached(StringBuilder sbuf) {
/* 241 */     if (this.counter >= 10000L) {
/* 242 */       this.counter = 0L;
/* 243 */       sbuf.append("</table>");
/* 244 */       sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 245 */       sbuf.append("<p></p>");
/* 246 */       sbuf.append("<table cellspacing=\"0\">");
/* 247 */       sbuf.append(CoreConstants.LINE_SEPARATOR);
/* 248 */       buildHeaderRowForTable(sbuf);
/*     */     }
/*     */   }
/*     */   
/*     */   protected String computeConverterName(Converter c) {
/* 253 */     String className = c.getClass().getSimpleName();
/* 254 */     int index = className.indexOf("Converter");
/* 255 */     if (index == -1) {
/* 256 */       return className;
/*     */     }
/* 258 */     return className.substring(0, index);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\html\HTMLLayoutBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */