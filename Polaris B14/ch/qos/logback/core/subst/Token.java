/*    */ package ch.qos.logback.core.subst;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Token
/*    */ {
/* 18 */   public static final Token START_TOKEN = new Token(Type.START, null);
/* 19 */   public static final Token CURLY_LEFT_TOKEN = new Token(Type.CURLY_LEFT, null);
/* 20 */   public static final Token CURLY_RIGHT_TOKEN = new Token(Type.CURLY_RIGHT, null);
/* 21 */   public static final Token DEFAULT_SEP_TOKEN = new Token(Type.DEFAULT, null);
/*    */   
/* 23 */   public static enum Type { LITERAL,  START,  CURLY_LEFT,  CURLY_RIGHT,  DEFAULT;
/*    */     
/*    */     private Type() {} }
/*    */   
/*    */   Type type;
/*    */   String payload;
/* 29 */   public Token(Type type, String payload) { this.type = type;
/* 30 */     this.payload = payload;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 35 */     if (this == o) return true;
/* 36 */     if ((o == null) || (getClass() != o.getClass())) { return false;
/*    */     }
/* 38 */     Token token = (Token)o;
/*    */     
/* 40 */     if (this.type != token.type) return false;
/* 41 */     if (this.payload != null ? !this.payload.equals(token.payload) : token.payload != null) { return false;
/*    */     }
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 48 */     int result = this.type != null ? this.type.hashCode() : 0;
/* 49 */     result = 31 * result + (this.payload != null ? this.payload.hashCode() : 0);
/* 50 */     return result;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 55 */     String result = "Token{type=" + this.type;
/*    */     
/* 57 */     if (this.payload != null) {
/* 58 */       result = result + ", payload='" + this.payload + '\'';
/*    */     }
/* 60 */     result = result + '}';
/* 61 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\subst\Token.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */