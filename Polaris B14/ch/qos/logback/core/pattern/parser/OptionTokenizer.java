/*     */ package ch.qos.logback.core.pattern.parser;
/*     */ 
/*     */ import ch.qos.logback.core.pattern.util.AsIsEscapeUtil;
/*     */ import ch.qos.logback.core.pattern.util.IEscapeUtil;
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
/*     */ public class OptionTokenizer
/*     */ {
/*     */   private static final int EXPECTING_STATE = 0;
/*     */   private static final int RAW_COLLECTING_STATE = 1;
/*     */   private static final int QUOTED_COLLECTING_STATE = 2;
/*     */   final IEscapeUtil escapeUtil;
/*     */   final TokenStream tokenStream;
/*     */   final String pattern;
/*     */   final int patternLength;
/*     */   char quoteChar;
/*  47 */   int state = 0;
/*     */   
/*     */   OptionTokenizer(TokenStream tokenStream) {
/*  50 */     this(tokenStream, new AsIsEscapeUtil());
/*     */   }
/*     */   
/*     */   OptionTokenizer(TokenStream tokenStream, IEscapeUtil escapeUtil) {
/*  54 */     this.tokenStream = tokenStream;
/*  55 */     this.pattern = tokenStream.pattern;
/*  56 */     this.patternLength = tokenStream.patternLength;
/*  57 */     this.escapeUtil = escapeUtil;
/*     */   }
/*     */   
/*     */   void tokenize(char firstChar, List<Token> tokenList) throws ScanException {
/*  61 */     StringBuffer buf = new StringBuffer();
/*  62 */     List<String> optionList = new ArrayList();
/*  63 */     char c = firstChar;
/*     */     
/*  65 */     while (this.tokenStream.pointer < this.patternLength) {
/*  66 */       switch (this.state) {
/*     */       case 0: 
/*  68 */         switch (c) {
/*     */         case '\t': 
/*     */         case '\n': 
/*     */         case '\r': 
/*     */         case ' ': 
/*     */         case ',': 
/*     */           break;
/*     */         case '"': 
/*     */         case '\'': 
/*  77 */           this.state = 2;
/*  78 */           this.quoteChar = c;
/*  79 */           break;
/*     */         case '}': 
/*  81 */           emitOptionToken(tokenList, optionList);
/*  82 */           return;
/*     */         default: 
/*  84 */           buf.append(c);
/*  85 */           this.state = 1;
/*     */         }
/*  87 */         break;
/*     */       case 1: 
/*  89 */         switch (c) {
/*     */         case ',': 
/*  91 */           optionList.add(buf.toString().trim());
/*  92 */           buf.setLength(0);
/*  93 */           this.state = 0;
/*  94 */           break;
/*     */         case '}': 
/*  96 */           optionList.add(buf.toString().trim());
/*  97 */           emitOptionToken(tokenList, optionList);
/*  98 */           return;
/*     */         default: 
/* 100 */           buf.append(c);
/*     */         }
/* 102 */         break;
/*     */       case 2: 
/* 104 */         if (c == this.quoteChar) {
/* 105 */           optionList.add(buf.toString());
/* 106 */           buf.setLength(0);
/* 107 */           this.state = 0;
/* 108 */         } else if (c == '\\') {
/* 109 */           escape(String.valueOf(this.quoteChar), buf);
/*     */         } else {
/* 111 */           buf.append(c);
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/* 117 */       c = this.pattern.charAt(this.tokenStream.pointer);
/* 118 */       this.tokenStream.pointer += 1;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 123 */     if (c == '}') {
/* 124 */       if (this.state == 0) {
/* 125 */         emitOptionToken(tokenList, optionList);
/* 126 */       } else if (this.state == 1) {
/* 127 */         optionList.add(buf.toString().trim());
/* 128 */         emitOptionToken(tokenList, optionList);
/*     */       } else {
/* 130 */         throw new ScanException("Unexpected end of pattern string in OptionTokenizer");
/*     */       }
/*     */     } else {
/* 133 */       throw new ScanException("Unexpected end of pattern string in OptionTokenizer");
/*     */     }
/*     */   }
/*     */   
/*     */   void emitOptionToken(List<Token> tokenList, List<String> optionList) {
/* 138 */     tokenList.add(new Token(1006, optionList));
/* 139 */     this.tokenStream.state = TokenStream.TokenizerState.LITERAL_STATE;
/*     */   }
/*     */   
/*     */   void escape(String escapeChars, StringBuffer buf) {
/* 143 */     if (this.tokenStream.pointer < this.patternLength) {
/* 144 */       char next = this.pattern.charAt(this.tokenStream.pointer++);
/* 145 */       this.escapeUtil.escape(escapeChars, buf, next, this.tokenStream.pointer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\parser\OptionTokenizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */