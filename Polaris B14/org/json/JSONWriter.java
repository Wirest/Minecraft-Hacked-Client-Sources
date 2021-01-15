/*     */ package org.json;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JSONWriter
/*     */ {
/*     */   private static final int maxdepth = 200;
/*     */   private boolean comma;
/*     */   protected char mode;
/*     */   private final JSONObject[] stack;
/*     */   private int top;
/*     */   protected Appendable writer;
/*     */   
/*     */   public JSONWriter(Appendable w)
/*     */   {
/*  99 */     this.comma = false;
/* 100 */     this.mode = 'i';
/* 101 */     this.stack = new JSONObject['È'];
/* 102 */     this.top = 0;
/* 103 */     this.writer = w;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JSONWriter append(String string)
/*     */     throws JSONException
/*     */   {
/* 113 */     if (string == null) {
/* 114 */       throw new JSONException("Null pointer");
/*     */     }
/* 116 */     if ((this.mode == 'o') || (this.mode == 'a')) {
/*     */       try {
/* 118 */         if ((this.comma) && (this.mode == 'a')) {
/* 119 */           this.writer.append(',');
/*     */         }
/* 121 */         this.writer.append(string);
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 126 */         throw new JSONException(e);
/*     */       }
/* 128 */       if (this.mode == 'o') {
/* 129 */         this.mode = 'k';
/*     */       }
/* 131 */       this.comma = true;
/* 132 */       return this;
/*     */     }
/* 134 */     throw new JSONException("Value out of sequence.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter array()
/*     */     throws JSONException
/*     */   {
/* 147 */     if ((this.mode == 'i') || (this.mode == 'o') || (this.mode == 'a')) {
/* 148 */       push(null);
/* 149 */       append("[");
/* 150 */       this.comma = false;
/* 151 */       return this;
/*     */     }
/* 153 */     throw new JSONException("Misplaced array.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private JSONWriter end(char m, char c)
/*     */     throws JSONException
/*     */   {
/* 164 */     if (this.mode != m) {
/* 165 */       throw new JSONException(m == 'a' ? 
/* 166 */         "Misplaced endArray." : 
/* 167 */         "Misplaced endObject.");
/*     */     }
/* 169 */     pop(m);
/*     */     try {
/* 171 */       this.writer.append(c);
/*     */ 
/*     */     }
/*     */     catch (IOException e)
/*     */     {
/* 176 */       throw new JSONException(e);
/*     */     }
/* 178 */     this.comma = true;
/* 179 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter endArray()
/*     */     throws JSONException
/*     */   {
/* 189 */     return end('a', ']');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter endObject()
/*     */     throws JSONException
/*     */   {
/* 199 */     return end('k', '}');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter key(String string)
/*     */     throws JSONException
/*     */   {
/* 211 */     if (string == null) {
/* 212 */       throw new JSONException("Null key.");
/*     */     }
/* 214 */     if (this.mode == 'k') {
/*     */       try {
/* 216 */         JSONObject topObject = this.stack[(this.top - 1)];
/*     */         
/* 218 */         if (topObject.has(string)) {
/* 219 */           throw new JSONException("Duplicate key \"" + string + "\"");
/*     */         }
/* 221 */         topObject.put(string, true);
/* 222 */         if (this.comma) {
/* 223 */           this.writer.append(',');
/*     */         }
/* 225 */         this.writer.append(JSONObject.quote(string));
/* 226 */         this.writer.append(':');
/* 227 */         this.comma = false;
/* 228 */         this.mode = 'o';
/* 229 */         return this;
/*     */ 
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 234 */         throw new JSONException(e);
/*     */       }
/*     */     }
/* 237 */     throw new JSONException("Misplaced key.");
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
/*     */   public JSONWriter object()
/*     */     throws JSONException
/*     */   {
/* 251 */     if (this.mode == 'i') {
/* 252 */       this.mode = 'o';
/*     */     }
/* 254 */     if ((this.mode == 'o') || (this.mode == 'a')) {
/* 255 */       append("{");
/* 256 */       push(new JSONObject());
/* 257 */       this.comma = false;
/* 258 */       return this;
/*     */     }
/* 260 */     throw new JSONException("Misplaced object.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void pop(char c)
/*     */     throws JSONException
/*     */   {
/* 271 */     if (this.top <= 0) {
/* 272 */       throw new JSONException("Nesting error.");
/*     */     }
/* 274 */     char m = this.stack[(this.top - 1)] == null ? 'a' : 'k';
/* 275 */     if (m != c) {
/* 276 */       throw new JSONException("Nesting error.");
/*     */     }
/* 278 */     this.top -= 1;
/* 279 */     this.mode = 
/*     */     
/* 281 */       (this.stack[(this.top - 1)] == null ? 
/* 282 */       'a' : this.top == 0 ? 'd' : 
/* 283 */       'k');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void push(JSONObject jo)
/*     */     throws JSONException
/*     */   {
/* 292 */     if (this.top >= 200) {
/* 293 */       throw new JSONException("Nesting too deep.");
/*     */     }
/* 295 */     this.stack[this.top] = jo;
/* 296 */     this.mode = (jo == null ? 'a' : 'k');
/* 297 */     this.top += 1;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String valueToString(Object value)
/*     */     throws JSONException
/*     */   {
/* 325 */     if ((value == null) || (value.equals(null))) {
/* 326 */       return "null";
/*     */     }
/* 328 */     if ((value instanceof JSONString))
/*     */     {
/*     */       try {
/* 331 */         object = ((JSONString)value).toJSONString();
/*     */       } catch (Exception e) { Object object;
/* 333 */         throw new JSONException(e); }
/*     */       Object object;
/* 335 */       if ((object instanceof String)) {
/* 336 */         return (String)object;
/*     */       }
/* 338 */       throw new JSONException("Bad value from toJSONString: " + object);
/*     */     }
/* 340 */     if ((value instanceof Number))
/*     */     {
/* 342 */       String numberAsString = JSONObject.numberToString((Number)value);
/*     */       
/*     */       try
/*     */       {
/* 346 */         BigDecimal unused = new BigDecimal(numberAsString);
/*     */         
/* 348 */         return numberAsString;
/*     */       }
/*     */       catch (NumberFormatException ex)
/*     */       {
/* 352 */         return JSONObject.quote(numberAsString);
/*     */       }
/*     */     }
/* 355 */     if (((value instanceof Boolean)) || ((value instanceof JSONObject)) || 
/* 356 */       ((value instanceof JSONArray))) {
/* 357 */       return value.toString();
/*     */     }
/* 359 */     if ((value instanceof Map)) {
/* 360 */       Map<?, ?> map = (Map)value;
/* 361 */       return new JSONObject(map).toString();
/*     */     }
/* 363 */     if ((value instanceof Collection)) {
/* 364 */       Collection<?> coll = (Collection)value;
/* 365 */       return new JSONArray(coll).toString();
/*     */     }
/* 367 */     if (value.getClass().isArray()) {
/* 368 */       return new JSONArray(value).toString();
/*     */     }
/* 370 */     if ((value instanceof Enum)) {
/* 371 */       return JSONObject.quote(((Enum)value).name());
/*     */     }
/* 373 */     return JSONObject.quote(value.toString());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter value(boolean b)
/*     */     throws JSONException
/*     */   {
/* 384 */     return append(b ? "true" : "false");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter value(double d)
/*     */     throws JSONException
/*     */   {
/* 394 */     return value(new Double(d));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter value(long l)
/*     */     throws JSONException
/*     */   {
/* 404 */     return append(Long.toString(l));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JSONWriter value(Object object)
/*     */     throws JSONException
/*     */   {
/* 416 */     return append(valueToString(object));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\JSONWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */