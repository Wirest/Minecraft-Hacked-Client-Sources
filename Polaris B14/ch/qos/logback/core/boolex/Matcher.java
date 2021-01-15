/*     */ package ch.qos.logback.core.boolex;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import java.util.regex.Pattern;
/*     */ import java.util.regex.PatternSyntaxException;
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
/*     */ public class Matcher
/*     */   extends ContextAwareBase
/*     */   implements LifeCycle
/*     */ {
/*     */   private String regex;
/*     */   private String name;
/*  26 */   private boolean caseSensitive = true;
/*  27 */   private boolean canonEq = false;
/*  28 */   private boolean unicodeCase = false;
/*     */   
/*  30 */   private boolean start = false;
/*     */   private Pattern pattern;
/*     */   
/*     */   public String getRegex() {
/*  34 */     return this.regex;
/*     */   }
/*     */   
/*     */   public void setRegex(String regex) {
/*  38 */     this.regex = regex;
/*     */   }
/*     */   
/*     */   public void start() {
/*  42 */     if (this.name == null) {
/*  43 */       addError("All Matcher objects must be named");
/*  44 */       return;
/*     */     }
/*     */     try {
/*  47 */       int code = 0;
/*  48 */       if (!this.caseSensitive) {
/*  49 */         code |= 0x2;
/*     */       }
/*  51 */       if (this.canonEq) {
/*  52 */         code |= 0x80;
/*     */       }
/*  54 */       if (this.unicodeCase) {
/*  55 */         code |= 0x40;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  60 */       this.pattern = Pattern.compile(this.regex, code);
/*  61 */       this.start = true;
/*     */     } catch (PatternSyntaxException pse) {
/*  63 */       addError("Failed to compile regex [" + this.regex + "]", pse);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop() {
/*  68 */     this.start = false;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/*  72 */     return this.start;
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
/*     */   public boolean matches(String input)
/*     */     throws EvaluationException
/*     */   {
/*  86 */     if (this.start) {
/*  87 */       java.util.regex.Matcher matcher = this.pattern.matcher(input);
/*  88 */       return matcher.find();
/*     */     }
/*  90 */     throw new EvaluationException("Matcher [" + this.regex + "] not started");
/*     */   }
/*     */   
/*     */   public boolean isCanonEq()
/*     */   {
/*  95 */     return this.canonEq;
/*     */   }
/*     */   
/*     */   public void setCanonEq(boolean canonEq) {
/*  99 */     this.canonEq = canonEq;
/*     */   }
/*     */   
/*     */   public boolean isCaseSensitive() {
/* 103 */     return this.caseSensitive;
/*     */   }
/*     */   
/*     */   public void setCaseSensitive(boolean caseSensitive) {
/* 107 */     this.caseSensitive = caseSensitive;
/*     */   }
/*     */   
/*     */   public boolean isUnicodeCase() {
/* 111 */     return this.unicodeCase;
/*     */   }
/*     */   
/*     */   public void setUnicodeCase(boolean unicodeCase) {
/* 115 */     this.unicodeCase = unicodeCase;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 119 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 123 */     this.name = name;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\boolex\Matcher.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */