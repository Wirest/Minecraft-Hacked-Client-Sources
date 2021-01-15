/*     */ package ch.qos.logback.core.pattern.parser;
/*     */ 
/*     */ 
/*     */ 
/*     */ class Token
/*     */ {
/*     */   static final int PERCENT = 37;
/*     */   
/*     */ 
/*     */   static final int RIGHT_PARENTHESIS = 41;
/*     */   
/*     */ 
/*     */   static final int MINUS = 45;
/*     */   
/*     */ 
/*     */   static final int DOT = 46;
/*     */   
/*     */ 
/*     */   static final int CURLY_LEFT = 123;
/*     */   
/*     */   static final int CURLY_RIGHT = 125;
/*     */   
/*     */   static final int LITERAL = 1000;
/*     */   
/*     */   static final int FORMAT_MODIFIER = 1002;
/*     */   
/*     */   static final int SIMPLE_KEYWORD = 1004;
/*     */   
/*     */   static final int COMPOSITE_KEYWORD = 1005;
/*     */   
/*     */   static final int OPTION = 1006;
/*     */   
/*     */   static final int EOF = Integer.MAX_VALUE;
/*     */   
/*  35 */   static Token EOF_TOKEN = new Token(Integer.MAX_VALUE, "EOF");
/*  36 */   static Token RIGHT_PARENTHESIS_TOKEN = new Token(41);
/*  37 */   static Token BARE_COMPOSITE_KEYWORD_TOKEN = new Token(1005, "BARE");
/*  38 */   static Token PERCENT_TOKEN = new Token(37);
/*     */   
/*     */   private final int type;
/*     */   private final Object value;
/*     */   
/*     */   public Token(int type)
/*     */   {
/*  45 */     this(type, null);
/*     */   }
/*     */   
/*     */   public Token(int type, Object value) {
/*  49 */     this.type = type;
/*  50 */     this.value = value;
/*     */   }
/*     */   
/*     */   public int getType() {
/*  54 */     return this.type;
/*     */   }
/*     */   
/*     */   public Object getValue() {
/*  58 */     return this.value;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  63 */     String typeStr = null;
/*  64 */     switch (this.type)
/*     */     {
/*     */     case 37: 
/*  67 */       typeStr = "%";
/*  68 */       break;
/*     */     case 1002: 
/*  70 */       typeStr = "FormatModifier";
/*  71 */       break;
/*     */     case 1000: 
/*  73 */       typeStr = "LITERAL";
/*  74 */       break;
/*     */     case 1006: 
/*  76 */       typeStr = "OPTION";
/*  77 */       break;
/*     */     case 1004: 
/*  79 */       typeStr = "SIMPLE_KEYWORD";
/*  80 */       break;
/*     */     case 1005: 
/*  82 */       typeStr = "COMPOSITE_KEYWORD";
/*  83 */       break;
/*     */     case 41: 
/*  85 */       typeStr = "RIGHT_PARENTHESIS";
/*  86 */       break;
/*     */     default: 
/*  88 */       typeStr = "UNKNOWN";
/*     */     }
/*  90 */     if (this.value == null) {
/*  91 */       return "Token(" + typeStr + ")";
/*     */     }
/*     */     
/*  94 */     return "Token(" + typeStr + ", \"" + this.value + "\")";
/*     */   }
/*     */   
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 100 */     int result = this.type;
/* 101 */     result = 29 * result + (this.value != null ? this.value.hashCode() : 0);
/* 102 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 107 */     if (this == o) return true;
/* 108 */     if (!(o instanceof Token)) { return false;
/*     */     }
/* 110 */     Token token = (Token)o;
/*     */     
/* 112 */     if (this.type != token.type) return false;
/* 113 */     if (this.value != null ? !this.value.equals(token.value) : token.value != null) { return false;
/*     */     }
/* 115 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\parser\Token.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */