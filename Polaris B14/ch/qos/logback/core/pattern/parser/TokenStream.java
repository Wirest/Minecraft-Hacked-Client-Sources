/*     */ package ch.qos.logback.core.pattern.parser;
/*     */ 
/*     */ import ch.qos.logback.core.pattern.util.IEscapeUtil;
/*     */ import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
/*     */ import ch.qos.logback.core.pattern.util.RestrictedEscapeUtil;
/*     */ import ch.qos.logback.core.spi.ScanException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class TokenStream
/*     */ {
/*     */   final String pattern;
/*     */   final int patternLength;
/*     */   final IEscapeUtil escapeUtil;
/*     */   
/*     */   static enum TokenizerState
/*     */   {
/*  49 */     LITERAL_STATE,  FORMAT_MODIFIER_STATE,  KEYWORD_STATE,  OPTION_STATE,  RIGHT_PARENTHESIS_STATE;
/*     */     
/*     */ 
/*     */     private TokenizerState() {}
/*     */   }
/*     */   
/*  55 */   final IEscapeUtil optionEscapeUtil = new RestrictedEscapeUtil();
/*     */   
/*  57 */   TokenizerState state = TokenizerState.LITERAL_STATE;
/*  58 */   int pointer = 0;
/*     */   
/*     */   TokenStream(String pattern)
/*     */   {
/*  62 */     this(pattern, new RegularEscapeUtil());
/*     */   }
/*     */   
/*     */   TokenStream(String pattern, IEscapeUtil escapeUtil) {
/*  66 */     if ((pattern == null) || (pattern.length() == 0)) {
/*  67 */       throw new IllegalArgumentException("null or empty pattern string not allowed");
/*     */     }
/*     */     
/*  70 */     this.pattern = pattern;
/*  71 */     this.patternLength = pattern.length();
/*  72 */     this.escapeUtil = escapeUtil;
/*     */   }
/*     */   
/*     */   List tokenize() throws ScanException {
/*  76 */     List<Token> tokenList = new ArrayList();
/*  77 */     StringBuffer buf = new StringBuffer();
/*     */     
/*  79 */     while (this.pointer < this.patternLength) {
/*  80 */       char c = this.pattern.charAt(this.pointer);
/*  81 */       this.pointer += 1;
/*     */       
/*  83 */       switch (this.state) {
/*     */       case LITERAL_STATE: 
/*  85 */         handleLiteralState(c, tokenList, buf);
/*  86 */         break;
/*     */       case FORMAT_MODIFIER_STATE: 
/*  88 */         handleFormatModifierState(c, tokenList, buf);
/*  89 */         break;
/*     */       case OPTION_STATE: 
/*  91 */         processOption(c, tokenList, buf);
/*  92 */         break;
/*     */       case KEYWORD_STATE: 
/*  94 */         handleKeywordState(c, tokenList, buf);
/*  95 */         break;
/*     */       case RIGHT_PARENTHESIS_STATE: 
/*  97 */         handleRightParenthesisState(c, tokenList, buf);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 105 */     switch (this.state) {
/*     */     case LITERAL_STATE: 
/* 107 */       addValuedToken(1000, buf, tokenList);
/* 108 */       break;
/*     */     case KEYWORD_STATE: 
/* 110 */       tokenList.add(new Token(1004, buf.toString()));
/* 111 */       break;
/*     */     case RIGHT_PARENTHESIS_STATE: 
/* 113 */       tokenList.add(Token.RIGHT_PARENTHESIS_TOKEN);
/* 114 */       break;
/*     */     
/*     */     case FORMAT_MODIFIER_STATE: 
/*     */     case OPTION_STATE: 
/* 118 */       throw new ScanException("Unexpected end of pattern string");
/*     */     }
/*     */     
/* 121 */     return tokenList;
/*     */   }
/*     */   
/*     */   private void handleRightParenthesisState(char c, List<Token> tokenList, StringBuffer buf) {
/* 125 */     tokenList.add(Token.RIGHT_PARENTHESIS_TOKEN);
/* 126 */     switch (c) {
/*     */     case ')': 
/*     */       break;
/*     */     case '{': 
/* 130 */       this.state = TokenizerState.OPTION_STATE;
/* 131 */       break;
/*     */     case '\\': 
/* 133 */       escape("%{}", buf);
/* 134 */       this.state = TokenizerState.LITERAL_STATE;
/* 135 */       break;
/*     */     default: 
/* 137 */       buf.append(c);
/* 138 */       this.state = TokenizerState.LITERAL_STATE;
/*     */     }
/*     */   }
/*     */   
/*     */   private void processOption(char c, List<Token> tokenList, StringBuffer buf) throws ScanException {
/* 143 */     OptionTokenizer ot = new OptionTokenizer(this);
/* 144 */     ot.tokenize(c, tokenList);
/*     */   }
/*     */   
/*     */   private void handleFormatModifierState(char c, List<Token> tokenList, StringBuffer buf) {
/* 148 */     if (c == '(') {
/* 149 */       addValuedToken(1002, buf, tokenList);
/* 150 */       tokenList.add(Token.BARE_COMPOSITE_KEYWORD_TOKEN);
/* 151 */       this.state = TokenizerState.LITERAL_STATE;
/* 152 */     } else if (Character.isJavaIdentifierStart(c)) {
/* 153 */       addValuedToken(1002, buf, tokenList);
/* 154 */       this.state = TokenizerState.KEYWORD_STATE;
/* 155 */       buf.append(c);
/*     */     } else {
/* 157 */       buf.append(c);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleLiteralState(char c, List<Token> tokenList, StringBuffer buf) {
/* 162 */     switch (c) {
/*     */     case '\\': 
/* 164 */       escape("%()", buf);
/* 165 */       break;
/*     */     
/*     */     case '%': 
/* 168 */       addValuedToken(1000, buf, tokenList);
/* 169 */       tokenList.add(Token.PERCENT_TOKEN);
/* 170 */       this.state = TokenizerState.FORMAT_MODIFIER_STATE;
/* 171 */       break;
/*     */     
/*     */     case ')': 
/* 174 */       addValuedToken(1000, buf, tokenList);
/* 175 */       this.state = TokenizerState.RIGHT_PARENTHESIS_STATE;
/* 176 */       break;
/*     */     
/*     */     default: 
/* 179 */       buf.append(c);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleKeywordState(char c, List<Token> tokenList, StringBuffer buf)
/*     */   {
/* 185 */     if (Character.isJavaIdentifierPart(c)) {
/* 186 */       buf.append(c);
/* 187 */     } else if (c == '{') {
/* 188 */       addValuedToken(1004, buf, tokenList);
/* 189 */       this.state = TokenizerState.OPTION_STATE;
/* 190 */     } else if (c == '(') {
/* 191 */       addValuedToken(1005, buf, tokenList);
/* 192 */       this.state = TokenizerState.LITERAL_STATE;
/* 193 */     } else if (c == '%') {
/* 194 */       addValuedToken(1004, buf, tokenList);
/* 195 */       tokenList.add(Token.PERCENT_TOKEN);
/* 196 */       this.state = TokenizerState.FORMAT_MODIFIER_STATE;
/* 197 */     } else if (c == ')') {
/* 198 */       addValuedToken(1004, buf, tokenList);
/* 199 */       this.state = TokenizerState.RIGHT_PARENTHESIS_STATE;
/*     */     } else {
/* 201 */       addValuedToken(1004, buf, tokenList);
/* 202 */       if (c == '\\') {
/* 203 */         if (this.pointer < this.patternLength) {
/* 204 */           char next = this.pattern.charAt(this.pointer++);
/* 205 */           this.escapeUtil.escape("%()", buf, next, this.pointer);
/*     */         }
/*     */       } else {
/* 208 */         buf.append(c);
/*     */       }
/* 210 */       this.state = TokenizerState.LITERAL_STATE;
/*     */     }
/*     */   }
/*     */   
/*     */   void escape(String escapeChars, StringBuffer buf) {
/* 215 */     if (this.pointer < this.patternLength) {
/* 216 */       char next = this.pattern.charAt(this.pointer++);
/* 217 */       this.escapeUtil.escape(escapeChars, buf, next, this.pointer);
/*     */     }
/*     */   }
/*     */   
/*     */   void optionEscape(String escapeChars, StringBuffer buf) {
/* 222 */     if (this.pointer < this.patternLength) {
/* 223 */       char next = this.pattern.charAt(this.pointer++);
/* 224 */       this.optionEscapeUtil.escape(escapeChars, buf, next, this.pointer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void addValuedToken(int type, StringBuffer buf, List<Token> tokenList)
/*     */   {
/* 232 */     if (buf.length() > 0) {
/* 233 */       tokenList.add(new Token(type, buf.toString()));
/* 234 */       buf.setLength(0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\parser\TokenStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */