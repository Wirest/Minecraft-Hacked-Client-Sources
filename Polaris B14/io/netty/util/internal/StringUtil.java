/*     */ package io.netty.util.internal;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Formatter;
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
/*     */ public final class StringUtil
/*     */ {
/*     */   public static final String NEWLINE;
/*     */   public static final char DOUBLE_QUOTE = '"';
/*     */   public static final char COMMA = ',';
/*     */   public static final char LINE_FEED = '\n';
/*     */   public static final char CARRIAGE_RETURN = '\r';
/*     */   private static final String[] BYTE2HEX_PAD;
/*     */   private static final String[] BYTE2HEX_NOPAD;
/*     */   private static final String EMPTY_STRING = "";
/*     */   private static final int CSV_NUMBER_ESCAPE_CHARACTERS = 7;
/*     */   
/*     */   static
/*     */   {
/*  36 */     BYTE2HEX_PAD = new String['Ā'];
/*  37 */     BYTE2HEX_NOPAD = new String['Ā'];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     String newLine;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  50 */       newLine = new Formatter().format("%n", new Object[0]).toString();
/*     */     }
/*     */     catch (Exception e) {
/*  53 */       newLine = "\n";
/*     */     }
/*     */     
/*  56 */     NEWLINE = newLine;
/*     */     
/*     */ 
/*     */ 
/*  60 */     for (int i = 0; i < 10; i++) {
/*  61 */       StringBuilder buf = new StringBuilder(2);
/*  62 */       buf.append('0');
/*  63 */       buf.append(i);
/*  64 */       BYTE2HEX_PAD[i] = buf.toString();
/*  65 */       BYTE2HEX_NOPAD[i] = String.valueOf(i);
/*     */     }
/*  67 */     for (; i < 16; i++) {
/*  68 */       StringBuilder buf = new StringBuilder(2);
/*  69 */       char c = (char)(97 + i - 10);
/*  70 */       buf.append('0');
/*  71 */       buf.append(c);
/*  72 */       BYTE2HEX_PAD[i] = buf.toString();
/*  73 */       BYTE2HEX_NOPAD[i] = String.valueOf(c);
/*     */     }
/*  75 */     for (; i < BYTE2HEX_PAD.length; i++) {
/*  76 */       StringBuilder buf = new StringBuilder(2);
/*  77 */       buf.append(Integer.toHexString(i));
/*  78 */       String str = buf.toString();
/*  79 */       BYTE2HEX_PAD[i] = str;
/*  80 */       BYTE2HEX_NOPAD[i] = str;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] split(String value, char delim)
/*     */   {
/*  89 */     int end = value.length();
/*  90 */     List<String> res = new ArrayList();
/*     */     
/*  92 */     int start = 0;
/*  93 */     for (int i = 0; i < end; i++) {
/*  94 */       if (value.charAt(i) == delim) {
/*  95 */         if (start == i) {
/*  96 */           res.add("");
/*     */         } else {
/*  98 */           res.add(value.substring(start, i));
/*     */         }
/* 100 */         start = i + 1;
/*     */       }
/*     */     }
/*     */     
/* 104 */     if (start == 0) {
/* 105 */       res.add(value);
/*     */     }
/* 107 */     else if (start != end)
/*     */     {
/* 109 */       res.add(value.substring(start, end));
/*     */     }
/*     */     else {
/* 112 */       for (int i = res.size() - 1; i >= 0; i--) {
/* 113 */         if (!((String)res.get(i)).isEmpty()) break;
/* 114 */         res.remove(i);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 122 */     return (String[])res.toArray(new String[res.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] split(String value, char delim, int maxParts)
/*     */   {
/* 131 */     int end = value.length();
/* 132 */     List<String> res = new ArrayList();
/*     */     
/* 134 */     int start = 0;
/* 135 */     int cpt = 1;
/* 136 */     for (int i = 0; (i < end) && (cpt < maxParts); i++) {
/* 137 */       if (value.charAt(i) == delim) {
/* 138 */         if (start == i) {
/* 139 */           res.add("");
/*     */         } else {
/* 141 */           res.add(value.substring(start, i));
/*     */         }
/* 143 */         start = i + 1;
/* 144 */         cpt++;
/*     */       }
/*     */     }
/*     */     
/* 148 */     if (start == 0) {
/* 149 */       res.add(value);
/*     */     }
/* 151 */     else if (start != end)
/*     */     {
/* 153 */       res.add(value.substring(start, end));
/*     */     }
/*     */     else {
/* 156 */       for (int i = res.size() - 1; i >= 0; i--) {
/* 157 */         if (!((String)res.get(i)).isEmpty()) break;
/* 158 */         res.remove(i);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 166 */     return (String[])res.toArray(new String[res.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String substringAfter(String value, char delim)
/*     */   {
/* 175 */     int pos = value.indexOf(delim);
/* 176 */     if (pos >= 0) {
/* 177 */       return value.substring(pos + 1);
/*     */     }
/* 179 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String byteToHexStringPadded(int value)
/*     */   {
/* 186 */     return BYTE2HEX_PAD[(value & 0xFF)];
/*     */   }
/*     */   
/*     */ 
/*     */   public static <T extends Appendable> T byteToHexStringPadded(T buf, int value)
/*     */   {
/*     */     try
/*     */     {
/* 194 */       buf.append(byteToHexStringPadded(value));
/*     */     } catch (IOException e) {
/* 196 */       PlatformDependent.throwException(e);
/*     */     }
/* 198 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toHexStringPadded(byte[] src)
/*     */   {
/* 205 */     return toHexStringPadded(src, 0, src.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toHexStringPadded(byte[] src, int offset, int length)
/*     */   {
/* 212 */     return ((StringBuilder)toHexStringPadded(new StringBuilder(length << 1), src, offset, length)).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src)
/*     */   {
/* 219 */     return toHexStringPadded(dst, src, 0, src.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <T extends Appendable> T toHexStringPadded(T dst, byte[] src, int offset, int length)
/*     */   {
/* 226 */     int end = offset + length;
/* 227 */     for (int i = offset; i < end; i++) {
/* 228 */       byteToHexStringPadded(dst, src[i]);
/*     */     }
/* 230 */     return dst;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String byteToHexString(int value)
/*     */   {
/* 237 */     return BYTE2HEX_NOPAD[(value & 0xFF)];
/*     */   }
/*     */   
/*     */ 
/*     */   public static <T extends Appendable> T byteToHexString(T buf, int value)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       buf.append(byteToHexString(value));
/*     */     } catch (IOException e) {
/* 247 */       PlatformDependent.throwException(e);
/*     */     }
/* 249 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toHexString(byte[] src)
/*     */   {
/* 256 */     return toHexString(src, 0, src.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String toHexString(byte[] src, int offset, int length)
/*     */   {
/* 263 */     return ((StringBuilder)toHexString(new StringBuilder(length << 1), src, offset, length)).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <T extends Appendable> T toHexString(T dst, byte[] src)
/*     */   {
/* 270 */     return toHexString(dst, src, 0, src.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static <T extends Appendable> T toHexString(T dst, byte[] src, int offset, int length)
/*     */   {
/* 277 */     assert (length >= 0);
/* 278 */     if (length == 0) {
/* 279 */       return dst;
/*     */     }
/*     */     
/* 282 */     int end = offset + length;
/* 283 */     int endMinusOne = end - 1;
/*     */     
/*     */ 
/*     */ 
/* 287 */     for (int i = offset; i < endMinusOne; i++) {
/* 288 */       if (src[i] != 0) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 293 */     byteToHexString(dst, src[(i++)]);
/* 294 */     int remaining = end - i;
/* 295 */     toHexStringPadded(dst, src, i, remaining);
/*     */     
/* 297 */     return dst;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String simpleClassName(Object o)
/*     */   {
/* 304 */     if (o == null) {
/* 305 */       return "null_object";
/*     */     }
/* 307 */     return simpleClassName(o.getClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String simpleClassName(Class<?> clazz)
/*     */   {
/* 316 */     if (clazz == null) {
/* 317 */       return "null_class";
/*     */     }
/*     */     
/* 320 */     Package pkg = clazz.getPackage();
/* 321 */     if (pkg != null) {
/* 322 */       return clazz.getName().substring(pkg.getName().length() + 1);
/*     */     }
/* 324 */     return clazz.getName();
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
/*     */   public static CharSequence escapeCsv(CharSequence value)
/*     */   {
/* 337 */     int length = ((CharSequence)ObjectUtil.checkNotNull(value, "value")).length();
/* 338 */     if (length == 0) {
/* 339 */       return value;
/*     */     }
/* 341 */     int last = length - 1;
/* 342 */     boolean quoted = (isDoubleQuote(value.charAt(0))) && (isDoubleQuote(value.charAt(last))) && (length != 1);
/* 343 */     boolean foundSpecialCharacter = false;
/* 344 */     boolean escapedDoubleQuote = false;
/* 345 */     StringBuilder escaped = new StringBuilder(length + 7).append('"');
/* 346 */     for (int i = 0; i < length; i++) {
/* 347 */       char current = value.charAt(i);
/* 348 */       switch (current) {
/*     */       case '"': 
/* 350 */         if ((i == 0) || (i == last)) {
/* 351 */           if (quoted) continue;
/* 352 */           escaped.append('"');
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 357 */           boolean isNextCharDoubleQuote = isDoubleQuote(value.charAt(i + 1));
/* 358 */           if ((isDoubleQuote(value.charAt(i - 1))) || ((isNextCharDoubleQuote) && ((!isNextCharDoubleQuote) || (i + 1 != last))))
/*     */             break label240;
/* 360 */           escaped.append('"');
/* 361 */           escapedDoubleQuote = true;
/*     */         }
/*     */         
/*     */         break;
/*     */       case '\n': 
/*     */       case '\r': 
/*     */       case ',': 
/* 368 */         foundSpecialCharacter = true; }
/*     */       label240:
/* 370 */       escaped.append(current);
/*     */     }
/* 372 */     return (escapedDoubleQuote) || ((foundSpecialCharacter) && (!quoted)) ? escaped.append('"') : value;
/*     */   }
/*     */   
/*     */   private static boolean isDoubleQuote(char c)
/*     */   {
/* 377 */     return c == '"';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\StringUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */