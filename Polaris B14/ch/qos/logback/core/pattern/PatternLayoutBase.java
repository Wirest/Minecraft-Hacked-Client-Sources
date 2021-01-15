/*     */ package ch.qos.logback.core.pattern;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.LayoutBase;
/*     */ import ch.qos.logback.core.pattern.parser.Node;
/*     */ import ch.qos.logback.core.pattern.parser.Parser;
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.status.StatusManager;
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
/*     */ public abstract class PatternLayoutBase<E>
/*     */   extends LayoutBase<E>
/*     */ {
/*     */   Converter<E> head;
/*     */   String pattern;
/*     */   protected PostCompileProcessor<E> postCompileProcessor;
/*  36 */   Map<String, String> instanceConverterMap = new HashMap();
/*  37 */   protected boolean outputPatternAsHeader = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Map<String, String> getDefaultConverterMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, String> getEffectiveConverterMap()
/*     */   {
/*  52 */     Map<String, String> effectiveMap = new HashMap();
/*     */     
/*     */ 
/*  55 */     Map<String, String> defaultMap = getDefaultConverterMap();
/*  56 */     if (defaultMap != null) {
/*  57 */       effectiveMap.putAll(defaultMap);
/*     */     }
/*     */     
/*     */ 
/*  61 */     Context context = getContext();
/*  62 */     if (context != null)
/*     */     {
/*  64 */       Map<String, String> contextMap = (Map)context.getObject("PATTERN_RULE_REGISTRY");
/*     */       
/*  66 */       if (contextMap != null) {
/*  67 */         effectiveMap.putAll(contextMap);
/*     */       }
/*     */     }
/*     */     
/*  71 */     effectiveMap.putAll(this.instanceConverterMap);
/*  72 */     return effectiveMap;
/*     */   }
/*     */   
/*     */   public void start() {
/*  76 */     if ((this.pattern == null) || (this.pattern.length() == 0)) {
/*  77 */       addError("Empty or null pattern.");
/*  78 */       return;
/*     */     }
/*     */     try {
/*  81 */       Parser<E> p = new Parser(this.pattern);
/*  82 */       if (getContext() != null) {
/*  83 */         p.setContext(getContext());
/*     */       }
/*  85 */       Node t = p.parse();
/*  86 */       this.head = p.compile(t, getEffectiveConverterMap());
/*  87 */       if (this.postCompileProcessor != null) {
/*  88 */         this.postCompileProcessor.process(this.head);
/*     */       }
/*  90 */       ConverterUtil.setContextForConverters(getContext(), this.head);
/*  91 */       ConverterUtil.startConverters(this.head);
/*  92 */       super.start();
/*     */     } catch (ScanException sce) {
/*  94 */       StatusManager sm = getContext().getStatusManager();
/*  95 */       sm.add(new ErrorStatus("Failed to parse pattern \"" + getPattern() + "\".", this, sce));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPostCompileProcessor(PostCompileProcessor<E> postCompileProcessor)
/*     */   {
/* 102 */     this.postCompileProcessor = postCompileProcessor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   protected void setContextForConverters(Converter<E> head)
/*     */   {
/* 112 */     ConverterUtil.setContextForConverters(getContext(), head);
/*     */   }
/*     */   
/*     */   protected String writeLoopOnConverters(E event) {
/* 116 */     StringBuilder buf = new StringBuilder(128);
/* 117 */     Converter<E> c = this.head;
/* 118 */     while (c != null) {
/* 119 */       c.write(buf, event);
/* 120 */       c = c.getNext();
/*     */     }
/* 122 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public String getPattern() {
/* 126 */     return this.pattern;
/*     */   }
/*     */   
/*     */   public void setPattern(String pattern) {
/* 130 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 134 */     return getClass().getName() + "(\"" + getPattern() + "\")";
/*     */   }
/*     */   
/*     */   public Map<String, String> getInstanceConverterMap() {
/* 138 */     return this.instanceConverterMap;
/*     */   }
/*     */   
/*     */   protected String getPresentationHeaderPrefix()
/*     */   {
/* 143 */     return "";
/*     */   }
/*     */   
/*     */   public boolean isOutputPatternAsHeader() {
/* 147 */     return this.outputPatternAsHeader;
/*     */   }
/*     */   
/*     */   public void setOutputPatternAsHeader(boolean outputPatternAsHeader) {
/* 151 */     this.outputPatternAsHeader = outputPatternAsHeader;
/*     */   }
/*     */   
/*     */   public String getPresentationHeader()
/*     */   {
/* 156 */     if (this.outputPatternAsHeader) {
/* 157 */       return getPresentationHeaderPrefix() + this.pattern;
/*     */     }
/* 159 */     return super.getPresentationHeader();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\PatternLayoutBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */