/*     */ package ch.qos.logback.core.rolling.helper;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.pattern.Converter;
/*     */ import ch.qos.logback.core.pattern.ConverterUtil;
/*     */ import ch.qos.logback.core.pattern.LiteralConverter;
/*     */ import ch.qos.logback.core.pattern.parser.Node;
/*     */ import ch.qos.logback.core.pattern.parser.Parser;
/*     */ import ch.qos.logback.core.pattern.util.AlmostAsIsEscapeUtil;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
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
/*     */ public class FileNamePattern
/*     */   extends ContextAwareBase
/*     */ {
/*  40 */   static final Map<String, String> CONVERTER_MAP = new HashMap();
/*     */   
/*  42 */   static { CONVERTER_MAP.put("i", IntegerTokenConverter.class.getName());
/*     */     
/*  44 */     CONVERTER_MAP.put("d", DateTokenConverter.class.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   String pattern;
/*     */   
/*     */   public FileNamePattern(String patternArg, Context contextArg)
/*     */   {
/*  53 */     setPattern(FileFilterUtil.slashify(patternArg));
/*  54 */     setContext(contextArg);
/*  55 */     parse();
/*  56 */     ConverterUtil.startConverters(this.headTokenConverter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void parse()
/*     */   {
/*     */     try
/*     */     {
/*  65 */       String patternForParsing = escapeRightParantesis(this.pattern);
/*  66 */       Parser<Object> p = new Parser(patternForParsing, new AlmostAsIsEscapeUtil());
/*  67 */       p.setContext(this.context);
/*  68 */       Node t = p.parse();
/*  69 */       this.headTokenConverter = p.compile(t, CONVERTER_MAP);
/*     */     }
/*     */     catch (ScanException sce) {
/*  72 */       addError("Failed to parse pattern \"" + this.pattern + "\".", sce);
/*     */     }
/*     */   }
/*     */   
/*     */   String escapeRightParantesis(String in) {
/*  77 */     return this.pattern.replace(")", "\\)");
/*     */   }
/*     */   
/*     */   public String toString() {
/*  81 */     return this.pattern;
/*     */   }
/*     */   
/*     */   public DateTokenConverter getPrimaryDateTokenConverter() {
/*  85 */     Converter p = this.headTokenConverter;
/*     */     
/*  87 */     while (p != null) {
/*  88 */       if ((p instanceof DateTokenConverter)) {
/*  89 */         DateTokenConverter dtc = (DateTokenConverter)p;
/*     */         
/*  91 */         if (dtc.isPrimary()) {
/*  92 */           return dtc;
/*     */         }
/*     */       }
/*  95 */       p = p.getNext();
/*     */     }
/*     */     
/*  98 */     return null;
/*     */   }
/*     */   
/*     */   public IntegerTokenConverter getIntegerTokenConverter() {
/* 102 */     Converter p = this.headTokenConverter;
/*     */     
/* 104 */     while (p != null) {
/* 105 */       if ((p instanceof IntegerTokenConverter)) {
/* 106 */         return (IntegerTokenConverter)p;
/*     */       }
/*     */       
/* 109 */       p = p.getNext();
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   public String convertMultipleArguments(Object... objectList) {
/* 115 */     StringBuilder buf = new StringBuilder();
/* 116 */     Converter<Object> c = this.headTokenConverter;
/* 117 */     while (c != null) {
/* 118 */       if ((c instanceof MonoTypedConverter)) {
/* 119 */         MonoTypedConverter monoTyped = (MonoTypedConverter)c;
/* 120 */         for (Object o : objectList) {
/* 121 */           if (monoTyped.isApplicable(o)) {
/* 122 */             buf.append(c.convert(o));
/*     */           }
/*     */         }
/*     */       } else {
/* 126 */         buf.append(c.convert(objectList));
/*     */       }
/* 128 */       c = c.getNext();
/*     */     }
/* 130 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String convert(Object o) {
/* 134 */     StringBuilder buf = new StringBuilder();
/* 135 */     Converter<Object> p = this.headTokenConverter;
/* 136 */     while (p != null) {
/* 137 */       buf.append(p.convert(o));
/* 138 */       p = p.getNext();
/*     */     }
/* 140 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String convertInt(int i) {
/* 144 */     return convert(Integer.valueOf(i));
/*     */   }
/*     */   
/*     */   public void setPattern(String pattern) {
/* 148 */     if (pattern != null)
/*     */     {
/* 150 */       this.pattern = pattern.trim();
/*     */     }
/*     */   }
/*     */   
/*     */   public String getPattern() {
/* 155 */     return this.pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Converter<Object> headTokenConverter;
/*     */   
/*     */ 
/*     */   public String toRegexForFixedDate(Date date)
/*     */   {
/* 165 */     StringBuilder buf = new StringBuilder();
/* 166 */     Converter<Object> p = this.headTokenConverter;
/* 167 */     while (p != null) {
/* 168 */       if ((p instanceof LiteralConverter)) {
/* 169 */         buf.append(p.convert(null));
/* 170 */       } else if ((p instanceof IntegerTokenConverter)) {
/* 171 */         buf.append("(\\d{1,3})");
/* 172 */       } else if ((p instanceof DateTokenConverter)) {
/* 173 */         buf.append(p.convert(date));
/*     */       }
/* 175 */       p = p.getNext();
/*     */     }
/* 177 */     return buf.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toRegex()
/*     */   {
/* 184 */     StringBuilder buf = new StringBuilder();
/* 185 */     Converter<Object> p = this.headTokenConverter;
/* 186 */     while (p != null) {
/* 187 */       if ((p instanceof LiteralConverter)) {
/* 188 */         buf.append(p.convert(null));
/* 189 */       } else if ((p instanceof IntegerTokenConverter)) {
/* 190 */         buf.append("\\d{1,2}");
/* 191 */       } else if ((p instanceof DateTokenConverter)) {
/* 192 */         DateTokenConverter<Object> dtc = (DateTokenConverter)p;
/* 193 */         buf.append(dtc.toRegex());
/*     */       }
/* 195 */       p = p.getNext();
/*     */     }
/* 197 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\FileNamePattern.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */