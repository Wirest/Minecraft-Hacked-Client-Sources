/*     */ package ch.qos.logback.core.pattern.parser;
/*     */ 
/*     */ import ch.qos.logback.core.pattern.CompositeConverter;
/*     */ import ch.qos.logback.core.pattern.Converter;
/*     */ import ch.qos.logback.core.pattern.DynamicConverter;
/*     */ import ch.qos.logback.core.pattern.LiteralConverter;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.util.OptionHelper;
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
/*     */ class Compiler<E>
/*     */   extends ContextAwareBase
/*     */ {
/*     */   Converter<E> head;
/*     */   Converter<E> tail;
/*     */   final Node top;
/*     */   final Map converterMap;
/*     */   
/*     */   Compiler(Node top, Map converterMap)
/*     */   {
/*  34 */     this.top = top;
/*  35 */     this.converterMap = converterMap;
/*     */   }
/*     */   
/*     */   Converter<E> compile() {
/*  39 */     this.head = (this.tail = null);
/*  40 */     for (Node n = this.top; n != null; n = n.next) {
/*  41 */       switch (n.type) {
/*     */       case 0: 
/*  43 */         addToList(new LiteralConverter((String)n.getValue()));
/*  44 */         break;
/*     */       case 2: 
/*  46 */         CompositeNode cn = (CompositeNode)n;
/*  47 */         CompositeConverter<E> compositeConverter = createCompositeConverter(cn);
/*  48 */         if (compositeConverter == null) {
/*  49 */           addError("Failed to create converter for [%" + cn.getValue() + "] keyword");
/*  50 */           addToList(new LiteralConverter("%PARSER_ERROR[" + cn.getValue() + "]"));
/*     */         }
/*     */         else {
/*  53 */           compositeConverter.setFormattingInfo(cn.getFormatInfo());
/*  54 */           compositeConverter.setOptionList(cn.getOptions());
/*  55 */           Compiler<E> childCompiler = new Compiler(cn.getChildNode(), this.converterMap);
/*     */           
/*  57 */           childCompiler.setContext(this.context);
/*  58 */           Converter<E> childConverter = childCompiler.compile();
/*  59 */           compositeConverter.setChildConverter(childConverter);
/*  60 */           addToList(compositeConverter); }
/*  61 */         break;
/*     */       case 1: 
/*  63 */         SimpleKeywordNode kn = (SimpleKeywordNode)n;
/*  64 */         DynamicConverter<E> dynaConverter = createConverter(kn);
/*  65 */         if (dynaConverter != null) {
/*  66 */           dynaConverter.setFormattingInfo(kn.getFormatInfo());
/*  67 */           dynaConverter.setOptionList(kn.getOptions());
/*  68 */           addToList(dynaConverter);
/*     */         }
/*     */         else
/*     */         {
/*  72 */           Converter<E> errConveter = new LiteralConverter("%PARSER_ERROR[" + kn.getValue() + "]");
/*     */           
/*  74 */           addStatus(new ErrorStatus("[" + kn.getValue() + "] is not a valid conversion word", this));
/*     */           
/*  76 */           addToList(errConveter);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*  81 */     return this.head;
/*     */   }
/*     */   
/*     */   private void addToList(Converter<E> c) {
/*  85 */     if (this.head == null) {
/*  86 */       this.head = (this.tail = c);
/*     */     } else {
/*  88 */       this.tail.setNext(c);
/*  89 */       this.tail = c;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   DynamicConverter<E> createConverter(SimpleKeywordNode kn)
/*     */   {
/* 102 */     String keyword = (String)kn.getValue();
/* 103 */     String converterClassStr = (String)this.converterMap.get(keyword);
/*     */     
/* 105 */     if (converterClassStr != null) {
/*     */       try {
/* 107 */         return (DynamicConverter)OptionHelper.instantiateByClassName(converterClassStr, DynamicConverter.class, this.context);
/*     */       }
/*     */       catch (Exception e) {
/* 110 */         addError("Failed to instantiate converter class [" + converterClassStr + "] for keyword [" + keyword + "]", e);
/*     */         
/* 112 */         return null;
/*     */       }
/*     */     }
/* 115 */     addError("There is no conversion class registered for conversion word [" + keyword + "]");
/*     */     
/* 117 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   CompositeConverter<E> createCompositeConverter(CompositeNode cn)
/*     */   {
/* 130 */     String keyword = (String)cn.getValue();
/* 131 */     String converterClassStr = (String)this.converterMap.get(keyword);
/*     */     
/* 133 */     if (converterClassStr != null) {
/*     */       try {
/* 135 */         return (CompositeConverter)OptionHelper.instantiateByClassName(converterClassStr, CompositeConverter.class, this.context);
/*     */       }
/*     */       catch (Exception e) {
/* 138 */         addError("Failed to instantiate converter class [" + converterClassStr + "] as a composite converter for keyword [" + keyword + "]", e);
/*     */         
/* 140 */         return null;
/*     */       }
/*     */     }
/* 143 */     addError("There is no conversion class registered for composite conversion word [" + keyword + "]");
/*     */     
/* 145 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\parser\Compiler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */