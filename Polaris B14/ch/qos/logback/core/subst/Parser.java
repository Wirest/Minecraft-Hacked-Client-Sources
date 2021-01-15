/*     */ package ch.qos.logback.core.subst;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.spi.ScanException;
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
/*     */ public class Parser
/*     */ {
/*     */   final List<Token> tokenList;
/*  39 */   int pointer = 0;
/*     */   
/*     */   public Parser(List<Token> tokenList) {
/*  42 */     this.tokenList = tokenList;
/*     */   }
/*     */   
/*     */   public Node parse() throws ScanException {
/*  46 */     if ((this.tokenList == null) || (this.tokenList.isEmpty()))
/*  47 */       return null;
/*  48 */     return E();
/*     */   }
/*     */   
/*     */   private Node E() throws ScanException {
/*  52 */     Node t = T();
/*  53 */     if (t == null) {
/*  54 */       return null;
/*     */     }
/*  56 */     Node eOpt = Eopt();
/*  57 */     if (eOpt != null) {
/*  58 */       t.append(eOpt);
/*     */     }
/*  60 */     return t;
/*     */   }
/*     */   
/*     */   private Node Eopt() throws ScanException
/*     */   {
/*  65 */     Token next = peekAtCurentToken();
/*  66 */     if (next == null) {
/*  67 */       return null;
/*     */     }
/*  69 */     return E();
/*     */   }
/*     */   
/*     */   private Node T()
/*     */     throws ScanException
/*     */   {
/*  75 */     Token t = peekAtCurentToken();
/*     */     
/*  77 */     switch (t.type) {
/*     */     case LITERAL: 
/*  79 */       advanceTokenPointer();
/*  80 */       return makeNewLiteralNode(t.payload);
/*     */     case CURLY_LEFT: 
/*  82 */       advanceTokenPointer();
/*  83 */       Node innerNode = C();
/*  84 */       Token right = peekAtCurentToken();
/*  85 */       expectCurlyRight(right);
/*  86 */       advanceTokenPointer();
/*  87 */       Node curlyLeft = makeNewLiteralNode(CoreConstants.LEFT_ACCOLADE);
/*  88 */       curlyLeft.append(innerNode);
/*  89 */       curlyLeft.append(makeNewLiteralNode(CoreConstants.RIGHT_ACCOLADE));
/*  90 */       return curlyLeft;
/*     */     case START: 
/*  92 */       advanceTokenPointer();
/*  93 */       Node v = V();
/*  94 */       Token w = peekAtCurentToken();
/*  95 */       expectCurlyRight(w);
/*  96 */       advanceTokenPointer();
/*  97 */       return v;
/*     */     }
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   private Node makeNewLiteralNode(String s)
/*     */   {
/* 104 */     return new Node(Node.Type.LITERAL, s);
/*     */   }
/*     */   
/*     */   private Node V() throws ScanException
/*     */   {
/* 109 */     Node e = E();
/* 110 */     Node variable = new Node(Node.Type.VARIABLE, e);
/* 111 */     Token t = peekAtCurentToken();
/* 112 */     if (isDefaultToken(t)) {
/* 113 */       advanceTokenPointer();
/* 114 */       Node def = E();
/* 115 */       variable.defaultPart = def;
/*     */     }
/* 117 */     return variable;
/*     */   }
/*     */   
/*     */   private Node C() throws ScanException
/*     */   {
/* 122 */     Node e0 = E();
/* 123 */     Token t = peekAtCurentToken();
/* 124 */     if (isDefaultToken(t)) {
/* 125 */       advanceTokenPointer();
/* 126 */       Node literal = makeNewLiteralNode(":-");
/* 127 */       e0.append(literal);
/* 128 */       Node e1 = E();
/* 129 */       e0.append(e1);
/*     */     }
/* 131 */     return e0;
/*     */   }
/*     */   
/*     */   private boolean isDefaultToken(Token t) {
/* 135 */     return (t != null) && (t.type == Token.Type.DEFAULT);
/*     */   }
/*     */   
/*     */   void advanceTokenPointer() {
/* 139 */     this.pointer += 1;
/*     */   }
/*     */   
/*     */   void expectNotNull(Token t, String expected) {
/* 143 */     if (t == null) {
/* 144 */       throw new IllegalArgumentException("All tokens consumed but was expecting \"" + expected + "\"");
/*     */     }
/*     */   }
/*     */   
/*     */   void expectCurlyRight(Token t) throws ScanException
/*     */   {
/* 150 */     expectNotNull(t, "}");
/* 151 */     if (t.type != Token.Type.CURLY_RIGHT) {
/* 152 */       throw new ScanException("Expecting }");
/*     */     }
/*     */   }
/*     */   
/*     */   Token peekAtCurentToken() {
/* 157 */     if (this.pointer < this.tokenList.size()) {
/* 158 */       return (Token)this.tokenList.get(this.pointer);
/*     */     }
/* 160 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\subst\Parser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */