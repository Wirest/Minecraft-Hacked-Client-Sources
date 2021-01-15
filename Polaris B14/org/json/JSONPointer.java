/*     */ package org.json;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.net.URLDecoder;
/*     */ import java.net.URLEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
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
/*     */ public class JSONPointer
/*     */ {
/*     */   private static final String ENCODING = "utf-8";
/*     */   private final List<String> refTokens;
/*     */   
/*     */   public static class Builder
/*     */   {
/*  66 */     private final List<String> refTokens = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public JSONPointer build()
/*     */     {
/*  73 */       return new JSONPointer(this.refTokens);
/*     */     }
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
/*     */     public Builder append(String token)
/*     */     {
/*  89 */       if (token == null) {
/*  90 */         throw new NullPointerException("token cannot be null");
/*     */       }
/*  92 */       this.refTokens.add(token);
/*  93 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder append(int arrayIndex)
/*     */     {
/* 104 */       this.refTokens.add(String.valueOf(arrayIndex));
/* 105 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder builder()
/*     */   {
/* 125 */     return new Builder();
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
/*     */ 
/*     */ 
/*     */   public JSONPointer(String pointer)
/*     */   {
/* 140 */     if (pointer == null) {
/* 141 */       throw new NullPointerException("pointer cannot be null");
/*     */     }
/* 143 */     if ((pointer.isEmpty()) || (pointer.equals("#"))) {
/* 144 */       this.refTokens = Collections.emptyList();
/* 145 */       return;
/*     */     }
/*     */     
/* 148 */     if (pointer.startsWith("#/")) {
/* 149 */       String refs = pointer.substring(2);
/*     */       try {
/* 151 */         refs = URLDecoder.decode(refs, "utf-8");
/*     */       } catch (UnsupportedEncodingException e) {
/* 153 */         throw new RuntimeException(e);
/*     */       } } else { String refs;
/* 155 */       if (pointer.startsWith("/")) {
/* 156 */         refs = pointer.substring(1);
/*     */       } else
/* 158 */         throw new IllegalArgumentException("a JSON pointer should start with '/' or '#/'"); }
/*     */     String refs;
/* 160 */     this.refTokens = new ArrayList();
/* 161 */     int slashIdx = -1;
/* 162 */     int prevSlashIdx = 0;
/*     */     do {
/* 164 */       prevSlashIdx = slashIdx + 1;
/* 165 */       slashIdx = refs.indexOf('/', prevSlashIdx);
/* 166 */       if ((prevSlashIdx == slashIdx) || (prevSlashIdx == refs.length()))
/*     */       {
/*     */ 
/* 169 */         this.refTokens.add("");
/* 170 */       } else if (slashIdx >= 0) {
/* 171 */         String token = refs.substring(prevSlashIdx, slashIdx);
/* 172 */         this.refTokens.add(unescape(token));
/*     */       }
/*     */       else {
/* 175 */         String token = refs.substring(prevSlashIdx);
/* 176 */         this.refTokens.add(unescape(token));
/*     */       }
/* 163 */     } while (
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
/* 178 */       slashIdx >= 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONPointer(List<String> refTokens)
/*     */   {
/* 186 */     this.refTokens = new ArrayList(refTokens);
/*     */   }
/*     */   
/*     */   private String unescape(String token) {
/* 190 */     return 
/*     */     
/* 192 */       token.replace("~1", "/").replace("~0", "~").replace("\\\"", "\"").replace("\\\\", "\\");
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
/*     */   public Object queryFrom(Object document)
/*     */     throws JSONPointerException
/*     */   {
/* 206 */     if (this.refTokens.isEmpty()) {
/* 207 */       return document;
/*     */     }
/* 209 */     Object current = document;
/* 210 */     for (String token : this.refTokens) {
/* 211 */       if ((current instanceof JSONObject)) {
/* 212 */         current = ((JSONObject)current).opt(unescape(token));
/* 213 */       } else if ((current instanceof JSONArray)) {
/* 214 */         current = readByIndexToken(current, token);
/*     */       } else {
/* 216 */         throw new JSONPointerException(String.format(
/* 217 */           "value [%s] is not an array or object therefore its key %s cannot be resolved", new Object[] { current, 
/* 218 */           token }));
/*     */       }
/*     */     }
/* 221 */     return current;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private Object readByIndexToken(Object current, String indexToken)
/*     */     throws JSONPointerException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: invokestatic 186	java/lang/Integer:parseInt	(Ljava/lang/String;)I
/*     */     //   4: istore_3
/*     */     //   5: aload_1
/*     */     //   6: checkcast 159	org/json/JSONArray
/*     */     //   9: astore 4
/*     */     //   11: iload_3
/*     */     //   12: aload 4
/*     */     //   14: invokevirtual 187	org/json/JSONArray:length	()I
/*     */     //   17: if_icmplt +38 -> 55
/*     */     //   20: new 140	org/json/JSONPointerException
/*     */     //   23: dup
/*     */     //   24: ldc -67
/*     */     //   26: iconst_2
/*     */     //   27: anewarray 4	java/lang/Object
/*     */     //   30: dup
/*     */     //   31: iconst_0
/*     */     //   32: iload_3
/*     */     //   33: invokestatic 193	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   36: aastore
/*     */     //   37: dup
/*     */     //   38: iconst_1
/*     */     //   39: aload 4
/*     */     //   41: invokevirtual 187	org/json/JSONArray:length	()I
/*     */     //   44: invokestatic 193	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
/*     */     //   47: aastore
/*     */     //   48: invokestatic 169	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   51: invokespecial 170	org/json/JSONPointerException:<init>	(Ljava/lang/String;)V
/*     */     //   54: athrow
/*     */     //   55: aload 4
/*     */     //   57: iload_3
/*     */     //   58: invokevirtual 197	org/json/JSONArray:get	(I)Ljava/lang/Object;
/*     */     //   61: areturn
/*     */     //   62: astore 5
/*     */     //   64: new 140	org/json/JSONPointerException
/*     */     //   67: dup
/*     */     //   68: new 199	java/lang/StringBuilder
/*     */     //   71: dup
/*     */     //   72: ldc -55
/*     */     //   74: invokespecial 202	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   77: iload_3
/*     */     //   78: invokevirtual 206	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   81: invokevirtual 210	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   84: aload 5
/*     */     //   86: invokespecial 213	org/json/JSONPointerException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   89: athrow
/*     */     //   90: astore_3
/*     */     //   91: new 140	org/json/JSONPointerException
/*     */     //   94: dup
/*     */     //   95: ldc -41
/*     */     //   97: iconst_1
/*     */     //   98: anewarray 4	java/lang/Object
/*     */     //   101: dup
/*     */     //   102: iconst_0
/*     */     //   103: aload_2
/*     */     //   104: aastore
/*     */     //   105: invokestatic 169	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   108: aload_3
/*     */     //   109: invokespecial 213	org/json/JSONPointerException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   112: athrow
/*     */     // Line number table:
/*     */     //   Java source line #233	-> byte code offset #0
/*     */     //   Java source line #234	-> byte code offset #5
/*     */     //   Java source line #235	-> byte code offset #11
/*     */     //   Java source line #236	-> byte code offset #20
/*     */     //   Java source line #237	-> byte code offset #39
/*     */     //   Java source line #236	-> byte code offset #48
/*     */     //   Java source line #240	-> byte code offset #55
/*     */     //   Java source line #241	-> byte code offset #62
/*     */     //   Java source line #242	-> byte code offset #64
/*     */     //   Java source line #244	-> byte code offset #90
/*     */     //   Java source line #245	-> byte code offset #91
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	113	0	this	JSONPointer
/*     */     //   0	113	1	current	Object
/*     */     //   0	113	2	indexToken	String
/*     */     //   4	74	3	index	int
/*     */     //   90	19	3	e	NumberFormatException
/*     */     //   9	47	4	currentArr	JSONArray
/*     */     //   62	23	5	e	JSONException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   55	61	62	org/json/JSONException
/*     */     //   0	61	90	java/lang/NumberFormatException
/*     */     //   62	90	90	java/lang/NumberFormatException
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 255 */     StringBuilder rval = new StringBuilder("");
/* 256 */     for (String token : this.refTokens) {
/* 257 */       rval.append('/').append(escape(token));
/*     */     }
/* 259 */     return rval.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String escape(String token)
/*     */   {
/* 271 */     return 
/*     */     
/*     */ 
/* 274 */       token.replace("~", "~0").replace("/", "~1").replace("\\", "\\\\").replace("\"", "\\\"");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toURIFragment()
/*     */   {
/*     */     try
/*     */     {
/* 283 */       StringBuilder rval = new StringBuilder("#");
/* 284 */       for (String token : this.refTokens) {
/* 285 */         rval.append('/').append(URLEncoder.encode(token, "utf-8"));
/*     */       }
/* 287 */       return rval.toString();
/*     */     } catch (UnsupportedEncodingException e) {
/* 289 */       throw new RuntimeException(e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\JSONPointer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */