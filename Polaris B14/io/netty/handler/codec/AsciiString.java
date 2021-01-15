/*      */ package io.netty.handler.codec;
/*      */ 
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.Unpooled;
/*      */ import io.netty.util.internal.EmptyArrays;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class AsciiString
/*      */   implements CharSequence, Comparable<CharSequence>
/*      */ {
/*   39 */   public static final AsciiString EMPTY_STRING = new AsciiString("");
/*   40 */   public static final Comparator<AsciiString> CASE_INSENSITIVE_ORDER = new Comparator()
/*      */   {
/*      */     public int compare(AsciiString o1, AsciiString o2) {
/*   43 */       return AsciiString.CHARSEQUENCE_CASE_INSENSITIVE_ORDER.compare(o1, o2);
/*      */     }
/*      */   };
/*   46 */   public static final Comparator<AsciiString> CASE_SENSITIVE_ORDER = new Comparator()
/*      */   {
/*      */     public int compare(AsciiString o1, AsciiString o2) {
/*   49 */       return AsciiString.CHARSEQUENCE_CASE_SENSITIVE_ORDER.compare(o1, o2);
/*      */     }
/*      */   };
/*      */   
/*   53 */   public static final Comparator<CharSequence> CHARSEQUENCE_CASE_INSENSITIVE_ORDER = new Comparator()
/*      */   {
/*      */     public int compare(CharSequence o1, CharSequence o2) {
/*   56 */       if (o1 == o2) {
/*   57 */         return 0;
/*      */       }
/*      */       
/*   60 */       AsciiString a1 = (o1 instanceof AsciiString) ? (AsciiString)o1 : null;
/*   61 */       AsciiString a2 = (o2 instanceof AsciiString) ? (AsciiString)o2 : null;
/*      */       
/*      */ 
/*   64 */       int length1 = o1.length();
/*   65 */       int length2 = o2.length();
/*   66 */       int minLength = Math.min(length1, length2);
/*   67 */       if ((a1 != null) && (a2 != null)) {
/*   68 */         byte[] thisValue = a1.value;
/*   69 */         byte[] thatValue = a2.value;
/*   70 */         for (int i = 0; i < minLength; i++) {
/*   71 */           byte v1 = thisValue[i];
/*   72 */           byte v2 = thatValue[i];
/*   73 */           if (v1 != v2)
/*      */           {
/*      */ 
/*   76 */             int c1 = AsciiString.toLowerCase(v1) & 0xFF;
/*   77 */             int c2 = AsciiString.toLowerCase(v2) & 0xFF;
/*   78 */             int result = c1 - c2;
/*   79 */             if (result != 0)
/*   80 */               return result;
/*      */           }
/*      */         }
/*   83 */       } else if (a1 != null) {
/*   84 */         byte[] thisValue = a1.value;
/*   85 */         for (int i = 0; i < minLength; i++) {
/*   86 */           int c1 = AsciiString.toLowerCase(thisValue[i]) & 0xFF;
/*   87 */           int c2 = AsciiString.toLowerCase(o2.charAt(i));
/*   88 */           int result = c1 - c2;
/*   89 */           if (result != 0) {
/*   90 */             return result;
/*      */           }
/*      */         }
/*   93 */       } else if (a2 != null) {
/*   94 */         byte[] thatValue = a2.value;
/*   95 */         for (int i = 0; i < minLength; i++) {
/*   96 */           int c1 = AsciiString.toLowerCase(o1.charAt(i));
/*   97 */           int c2 = AsciiString.toLowerCase(thatValue[i]) & 0xFF;
/*   98 */           int result = c1 - c2;
/*   99 */           if (result != 0) {
/*  100 */             return result;
/*      */           }
/*      */         }
/*      */       } else {
/*  104 */         for (int i = 0; i < minLength; i++) {
/*  105 */           int c1 = AsciiString.toLowerCase(o1.charAt(i));
/*  106 */           int c2 = AsciiString.toLowerCase(o2.charAt(i));
/*  107 */           int result = c1 - c2;
/*  108 */           if (result != 0) {
/*  109 */             return result;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  114 */       return length1 - length2;
/*      */     }
/*      */   };
/*      */   
/*  118 */   public static final Comparator<CharSequence> CHARSEQUENCE_CASE_SENSITIVE_ORDER = new Comparator()
/*      */   {
/*      */     public int compare(CharSequence o1, CharSequence o2) {
/*  121 */       if (o1 == o2) {
/*  122 */         return 0;
/*      */       }
/*      */       
/*  125 */       AsciiString a1 = (o1 instanceof AsciiString) ? (AsciiString)o1 : null;
/*  126 */       AsciiString a2 = (o2 instanceof AsciiString) ? (AsciiString)o2 : null;
/*      */       
/*      */ 
/*  129 */       int length1 = o1.length();
/*  130 */       int length2 = o2.length();
/*  131 */       int minLength = Math.min(length1, length2);
/*  132 */       if ((a1 != null) && (a2 != null)) {
/*  133 */         byte[] thisValue = a1.value;
/*  134 */         byte[] thatValue = a2.value;
/*  135 */         for (int i = 0; i < minLength; i++) {
/*  136 */           byte v1 = thisValue[i];
/*  137 */           byte v2 = thatValue[i];
/*  138 */           int result = v1 - v2;
/*  139 */           if (result != 0) {
/*  140 */             return result;
/*      */           }
/*      */         }
/*  143 */       } else if (a1 != null) {
/*  144 */         byte[] thisValue = a1.value;
/*  145 */         for (int i = 0; i < minLength; i++) {
/*  146 */           int c1 = thisValue[i];
/*  147 */           int c2 = o2.charAt(i);
/*  148 */           int result = c1 - c2;
/*  149 */           if (result != 0) {
/*  150 */             return result;
/*      */           }
/*      */         }
/*  153 */       } else if (a2 != null) {
/*  154 */         byte[] thatValue = a2.value;
/*  155 */         for (int i = 0; i < minLength; i++) {
/*  156 */           int c1 = o1.charAt(i);
/*  157 */           int c2 = thatValue[i];
/*  158 */           int result = c1 - c2;
/*  159 */           if (result != 0) {
/*  160 */             return result;
/*      */           }
/*      */         }
/*      */       } else {
/*  164 */         for (int i = 0; i < minLength; i++) {
/*  165 */           int c1 = o1.charAt(i);
/*  166 */           int c2 = o2.charAt(i);
/*  167 */           int result = c1 - c2;
/*  168 */           if (result != 0) {
/*  169 */             return result;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  174 */       return length1 - length2;
/*      */     }
/*      */   };
/*      */   
/*      */   private final byte[] value;
/*      */   private String string;
/*      */   private int hash;
/*      */   
/*      */   public static int caseInsensitiveHashCode(CharSequence value)
/*      */   {
/*  184 */     if ((value instanceof AsciiString)) {
/*  185 */       return value.hashCode();
/*      */     }
/*      */     
/*  188 */     int hash = 0;
/*  189 */     int end = value.length();
/*  190 */     for (int i = 0; i < end; i++) {
/*  191 */       hash = hash * 31 ^ value.charAt(i) & 0x1F;
/*      */     }
/*      */     
/*  194 */     return hash;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean equalsIgnoreCase(CharSequence a, CharSequence b)
/*      */   {
/*  202 */     if (a == b) {
/*  203 */       return true;
/*      */     }
/*      */     
/*  206 */     if ((a instanceof AsciiString)) {
/*  207 */       AsciiString aa = (AsciiString)a;
/*  208 */       return aa.equalsIgnoreCase(b);
/*      */     }
/*      */     
/*  211 */     if ((b instanceof AsciiString)) {
/*  212 */       AsciiString ab = (AsciiString)b;
/*  213 */       return ab.equalsIgnoreCase(a);
/*      */     }
/*      */     
/*  216 */     if ((a == null) || (b == null)) {
/*  217 */       return false;
/*      */     }
/*      */     
/*  220 */     return a.toString().equalsIgnoreCase(b.toString());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean equals(CharSequence a, CharSequence b)
/*      */   {
/*  227 */     if (a == b) {
/*  228 */       return true;
/*      */     }
/*      */     
/*  231 */     if ((a instanceof AsciiString)) {
/*  232 */       AsciiString aa = (AsciiString)a;
/*  233 */       return aa.equals(b);
/*      */     }
/*      */     
/*  236 */     if ((b instanceof AsciiString)) {
/*  237 */       AsciiString ab = (AsciiString)b;
/*  238 */       return ab.equals(a);
/*      */     }
/*      */     
/*  241 */     if ((a == null) || (b == null)) {
/*  242 */       return false;
/*      */     }
/*      */     
/*  245 */     return a.equals(b);
/*      */   }
/*      */   
/*      */   public static byte[] getBytes(CharSequence v, Charset charset) {
/*  249 */     if ((v instanceof AsciiString))
/*  250 */       return ((AsciiString)v).array();
/*  251 */     if ((v instanceof String))
/*  252 */       return ((String)v).getBytes(charset);
/*  253 */     if (v != null) {
/*  254 */       ByteBuf buf = Unpooled.copiedBuffer(v, charset);
/*      */       try {
/*  256 */         if (buf.hasArray()) {
/*  257 */           return buf.array();
/*      */         }
/*  259 */         byte[] result = new byte[buf.readableBytes()];
/*  260 */         buf.readBytes(result);
/*  261 */         return result;
/*      */       }
/*      */       finally {
/*  264 */         buf.release();
/*      */       }
/*      */     }
/*  267 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AsciiString of(CharSequence string)
/*      */   {
/*  275 */     return (string instanceof AsciiString) ? (AsciiString)string : new AsciiString(string);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString(byte[] value)
/*      */   {
/*  283 */     this(value, true);
/*      */   }
/*      */   
/*      */   public AsciiString(byte[] value, boolean copy) {
/*  287 */     checkNull(value);
/*  288 */     if (copy) {
/*  289 */       this.value = ((byte[])value.clone());
/*      */     } else {
/*  291 */       this.value = value;
/*      */     }
/*      */   }
/*      */   
/*      */   public AsciiString(byte[] value, int start, int length) {
/*  296 */     this(value, start, length, true);
/*      */   }
/*      */   
/*      */   public AsciiString(byte[] value, int start, int length, boolean copy) {
/*  300 */     checkNull(value);
/*  301 */     if ((start < 0) || (start > value.length - length)) {
/*  302 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= " + "value.length(" + value.length + ')');
/*      */     }
/*      */     
/*      */ 
/*  306 */     if ((copy) || (start != 0) || (length != value.length)) {
/*  307 */       this.value = Arrays.copyOfRange(value, start, start + length);
/*      */     } else {
/*  309 */       this.value = value;
/*      */     }
/*      */   }
/*      */   
/*      */   public AsciiString(char[] value) {
/*  314 */     this((char[])checkNull(value), 0, value.length);
/*      */   }
/*      */   
/*      */   public AsciiString(char[] value, int start, int length) {
/*  318 */     checkNull(value);
/*  319 */     if ((start < 0) || (start > value.length - length)) {
/*  320 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= " + "value.length(" + value.length + ')');
/*      */     }
/*      */     
/*      */ 
/*  324 */     this.value = new byte[length];
/*  325 */     int i = 0; for (int j = start; i < length; j++) {
/*  326 */       this.value[i] = c2b(value[j]);i++;
/*      */     }
/*      */   }
/*      */   
/*      */   public AsciiString(CharSequence value) {
/*  331 */     this((CharSequence)checkNull(value), 0, value.length());
/*      */   }
/*      */   
/*      */   public AsciiString(CharSequence value, int start, int length) {
/*  335 */     if (value == null) {
/*  336 */       throw new NullPointerException("value");
/*      */     }
/*      */     
/*  339 */     if ((start < 0) || (length < 0) || (length > value.length() - start)) {
/*  340 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= " + "value.length(" + value.length() + ')');
/*      */     }
/*      */     
/*      */ 
/*  344 */     this.value = new byte[length];
/*  345 */     for (int i = 0; i < length; i++) {
/*  346 */       this.value[i] = c2b(value.charAt(start + i));
/*      */     }
/*      */   }
/*      */   
/*      */   public AsciiString(ByteBuffer value) {
/*  351 */     this((ByteBuffer)checkNull(value), value.position(), value.remaining());
/*      */   }
/*      */   
/*      */   public AsciiString(ByteBuffer value, int start, int length) {
/*  355 */     if (value == null) {
/*  356 */       throw new NullPointerException("value");
/*      */     }
/*      */     
/*  359 */     if ((start < 0) || (length > value.capacity() - start)) {
/*  360 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= start + length(" + length + ") <= " + "value.capacity(" + value.capacity() + ')');
/*      */     }
/*      */     
/*      */ 
/*  364 */     if (value.hasArray()) {
/*  365 */       int baseOffset = value.arrayOffset() + start;
/*  366 */       this.value = Arrays.copyOfRange(value.array(), baseOffset, baseOffset + length);
/*      */     } else {
/*  368 */       this.value = new byte[length];
/*  369 */       int oldPos = value.position();
/*  370 */       value.get(this.value, 0, this.value.length);
/*  371 */       value.position(oldPos);
/*      */     }
/*      */   }
/*      */   
/*      */   private static <T> T checkNull(T value) {
/*  376 */     if (value == null) {
/*  377 */       throw new NullPointerException("value");
/*      */     }
/*  379 */     return value;
/*      */   }
/*      */   
/*      */   public int length()
/*      */   {
/*  384 */     return this.value.length;
/*      */   }
/*      */   
/*      */   public char charAt(int index)
/*      */   {
/*  389 */     return (char)(byteAt(index) & 0xFF);
/*      */   }
/*      */   
/*      */   public byte byteAt(int index) {
/*  393 */     return this.value[index];
/*      */   }
/*      */   
/*      */   public byte[] array() {
/*  397 */     return this.value;
/*      */   }
/*      */   
/*      */   public int arrayOffset() {
/*  401 */     return 0;
/*      */   }
/*      */   
/*      */   private static byte c2b(char c) {
/*  405 */     if (c > 'ÿ') {
/*  406 */       return 63;
/*      */     }
/*  408 */     return (byte)c;
/*      */   }
/*      */   
/*      */   private static byte toLowerCase(byte b) {
/*  412 */     if ((65 <= b) && (b <= 90)) {
/*  413 */       return (byte)(b + 32);
/*      */     }
/*  415 */     return b;
/*      */   }
/*      */   
/*      */   private static char toLowerCase(char c) {
/*  419 */     if (('A' <= c) && (c <= 'Z')) {
/*  420 */       return (char)(c + ' ');
/*      */     }
/*  422 */     return c;
/*      */   }
/*      */   
/*      */   private static byte toUpperCase(byte b) {
/*  426 */     if ((97 <= b) && (b <= 122)) {
/*  427 */       return (byte)(b - 32);
/*      */     }
/*  429 */     return b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString subSequence(int start)
/*      */   {
/*  440 */     return subSequence(start, length());
/*      */   }
/*      */   
/*      */   public AsciiString subSequence(int start, int end)
/*      */   {
/*  445 */     if ((start < 0) || (start > end) || (end > length())) {
/*  446 */       throw new IndexOutOfBoundsException("expected: 0 <= start(" + start + ") <= end (" + end + ") <= length(" + length() + ')');
/*      */     }
/*      */     
/*      */ 
/*  450 */     byte[] value = this.value;
/*  451 */     if ((start == 0) && (end == value.length)) {
/*  452 */       return this;
/*      */     }
/*      */     
/*  455 */     if (end == start) {
/*  456 */       return EMPTY_STRING;
/*      */     }
/*      */     
/*  459 */     return new AsciiString(value, start, end - start, false);
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/*  464 */     int hash = this.hash;
/*  465 */     byte[] value = this.value;
/*  466 */     if ((hash != 0) || (value.length == 0)) {
/*  467 */       return hash;
/*      */     }
/*      */     
/*  470 */     for (int i = 0; i < value.length; i++) {
/*  471 */       hash = hash * 31 ^ value[i] & 0x1F;
/*      */     }
/*      */     
/*  474 */     return this.hash = hash;
/*      */   }
/*      */   
/*      */   public boolean equals(Object obj)
/*      */   {
/*  479 */     if (!(obj instanceof AsciiString)) {
/*  480 */       return false;
/*      */     }
/*      */     
/*  483 */     if (this == obj) {
/*  484 */       return true;
/*      */     }
/*      */     
/*  487 */     AsciiString that = (AsciiString)obj;
/*  488 */     int thisHash = hashCode();
/*  489 */     int thatHash = that.hashCode();
/*  490 */     if ((thisHash != thatHash) || (length() != that.length())) {
/*  491 */       return false;
/*      */     }
/*      */     
/*  494 */     byte[] thisValue = this.value;
/*  495 */     byte[] thatValue = that.value;
/*  496 */     int end = thisValue.length;
/*  497 */     int i = 0; for (int j = 0; i < end; j++) {
/*  498 */       if (thisValue[i] != thatValue[j]) {
/*  499 */         return false;
/*      */       }
/*  497 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  503 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public String toString()
/*      */   {
/*  509 */     String string = this.string;
/*  510 */     if (string != null) {
/*  511 */       return string;
/*      */     }
/*      */     
/*  514 */     byte[] value = this.value;
/*  515 */     return this.string = new String(value, 0, 0, value.length);
/*      */   }
/*      */   
/*      */   public String toString(int start, int end)
/*      */   {
/*  520 */     byte[] value = this.value;
/*  521 */     if ((start == 0) && (end == value.length)) {
/*  522 */       return toString();
/*      */     }
/*      */     
/*  525 */     int length = end - start;
/*  526 */     if (length == 0) {
/*  527 */       return "";
/*      */     }
/*      */     
/*  530 */     return new String(value, 0, start, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int compareTo(CharSequence string)
/*      */   {
/*  548 */     if (this == string) {
/*  549 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*  553 */     int length1 = length();
/*  554 */     int length2 = string.length();
/*  555 */     int minLength = Math.min(length1, length2);
/*  556 */     byte[] value = this.value;
/*  557 */     int i = 0; for (int j = 0; j < minLength; j++) {
/*  558 */       int result = (value[i] & 0xFF) - string.charAt(j);
/*  559 */       if (result != 0) {
/*  560 */         return result;
/*      */       }
/*  557 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  564 */     return length1 - length2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int compareToIgnoreCase(CharSequence string)
/*      */   {
/*  582 */     return CHARSEQUENCE_CASE_INSENSITIVE_ORDER.compare(this, string);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString concat(CharSequence string)
/*      */   {
/*  592 */     int thisLen = length();
/*  593 */     int thatLen = string.length();
/*  594 */     if (thatLen == 0) {
/*  595 */       return this;
/*      */     }
/*      */     
/*  598 */     if ((string instanceof AsciiString)) {
/*  599 */       AsciiString that = (AsciiString)string;
/*  600 */       if (isEmpty()) {
/*  601 */         return that;
/*      */       }
/*      */       
/*  604 */       byte[] newValue = Arrays.copyOf(this.value, thisLen + thatLen);
/*  605 */       System.arraycopy(that.value, 0, newValue, thisLen, thatLen);
/*      */       
/*  607 */       return new AsciiString(newValue, false);
/*      */     }
/*      */     
/*  610 */     if (isEmpty()) {
/*  611 */       return new AsciiString(string);
/*      */     }
/*      */     
/*  614 */     int newLen = thisLen + thatLen;
/*  615 */     byte[] newValue = Arrays.copyOf(this.value, newLen);
/*  616 */     int i = thisLen; for (int j = 0; i < newLen; j++) {
/*  617 */       newValue[i] = c2b(string.charAt(j));i++;
/*      */     }
/*      */     
/*  620 */     return new AsciiString(newValue, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean endsWith(CharSequence suffix)
/*      */   {
/*  631 */     int suffixLen = suffix.length();
/*  632 */     return regionMatches(length() - suffixLen, suffix, 0, suffixLen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean equalsIgnoreCase(CharSequence string)
/*      */   {
/*  643 */     if (string == this) {
/*  644 */       return true;
/*      */     }
/*      */     
/*  647 */     if (string == null) {
/*  648 */       return false;
/*      */     }
/*      */     
/*  651 */     byte[] value = this.value;
/*  652 */     int thisLen = value.length;
/*  653 */     int thatLen = string.length();
/*  654 */     if (thisLen != thatLen) {
/*  655 */       return false;
/*      */     }
/*      */     
/*  658 */     for (int i = 0; i < thisLen; i++) {
/*  659 */       char c1 = (char)(value[i] & 0xFF);
/*  660 */       char c2 = string.charAt(i);
/*  661 */       if ((c1 != c2) && (toLowerCase(c1) != toLowerCase(c2))) {
/*  662 */         return false;
/*      */       }
/*      */     }
/*  665 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] toByteArray()
/*      */   {
/*  674 */     return toByteArray(0, length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] toByteArray(int start, int end)
/*      */   {
/*  683 */     return Arrays.copyOfRange(this.value, start, end);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char[] toCharArray()
/*      */   {
/*  692 */     return toCharArray(0, length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public char[] toCharArray(int start, int end)
/*      */   {
/*  701 */     int length = end - start;
/*  702 */     if (length == 0) {
/*  703 */       return EmptyArrays.EMPTY_CHARS;
/*      */     }
/*      */     
/*  706 */     byte[] value = this.value;
/*  707 */     char[] buffer = new char[length];
/*  708 */     int i = 0; for (int j = start; i < length; j++) {
/*  709 */       buffer[i] = ((char)(value[j] & 0xFF));i++;
/*      */     }
/*  711 */     return buffer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copy(int srcIdx, ByteBuf dst, int dstIdx, int length)
/*      */   {
/*  723 */     if (dst == null) {
/*  724 */       throw new NullPointerException("dst");
/*      */     }
/*      */     
/*  727 */     byte[] value = this.value;
/*  728 */     int thisLen = value.length;
/*      */     
/*  730 */     if ((srcIdx < 0) || (length > thisLen - srcIdx)) {
/*  731 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + thisLen + ')');
/*      */     }
/*      */     
/*      */ 
/*  735 */     dst.setBytes(dstIdx, value, srcIdx, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copy(int srcIdx, ByteBuf dst, int length)
/*      */   {
/*  746 */     if (dst == null) {
/*  747 */       throw new NullPointerException("dst");
/*      */     }
/*      */     
/*  750 */     byte[] value = this.value;
/*  751 */     int thisLen = value.length;
/*      */     
/*  753 */     if ((srcIdx < 0) || (length > thisLen - srcIdx)) {
/*  754 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + thisLen + ')');
/*      */     }
/*      */     
/*      */ 
/*  758 */     dst.writeBytes(value, srcIdx, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copy(int srcIdx, byte[] dst, int dstIdx, int length)
/*      */   {
/*  770 */     if (dst == null) {
/*  771 */       throw new NullPointerException("dst");
/*      */     }
/*      */     
/*  774 */     byte[] value = this.value;
/*  775 */     int thisLen = value.length;
/*      */     
/*  777 */     if ((srcIdx < 0) || (length > thisLen - srcIdx)) {
/*  778 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + thisLen + ')');
/*      */     }
/*      */     
/*      */ 
/*  782 */     System.arraycopy(value, srcIdx, dst, dstIdx, length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copy(int srcIdx, char[] dst, int dstIdx, int length)
/*      */   {
/*  794 */     if (dst == null) {
/*  795 */       throw new NullPointerException("dst");
/*      */     }
/*      */     
/*  798 */     byte[] value = this.value;
/*  799 */     int thisLen = value.length;
/*      */     
/*  801 */     if ((srcIdx < 0) || (length > thisLen - srcIdx)) {
/*  802 */       throw new IndexOutOfBoundsException("expected: 0 <= srcIdx(" + srcIdx + ") <= srcIdx + length(" + length + ") <= srcLen(" + thisLen + ')');
/*      */     }
/*      */     
/*      */ 
/*  806 */     int dstEnd = dstIdx + length;
/*  807 */     int i = srcIdx; for (int j = dstIdx; j < dstEnd; j++) {
/*  808 */       dst[j] = ((char)(value[i] & 0xFF));i++;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int indexOf(int c)
/*      */   {
/*  820 */     return indexOf(c, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int indexOf(int c, int start)
/*      */   {
/*  832 */     byte[] value = this.value;
/*  833 */     int length = value.length;
/*  834 */     if (start < length) {
/*  835 */       if (start < 0) {
/*  836 */         start = 0;
/*      */       }
/*      */       
/*  839 */       for (int i = start; i < length; i++) {
/*  840 */         if ((value[i] & 0xFF) == c) {
/*  841 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*  845 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int indexOf(CharSequence string)
/*      */   {
/*  858 */     return indexOf(string, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int indexOf(CharSequence subString, int start)
/*      */   {
/*  872 */     if (start < 0) {
/*  873 */       start = 0;
/*      */     }
/*      */     
/*  876 */     byte[] value = this.value;
/*  877 */     int thisLen = value.length;
/*      */     
/*  879 */     int subCount = subString.length();
/*  880 */     if (subCount <= 0) {
/*  881 */       return start < thisLen ? start : thisLen;
/*      */     }
/*  883 */     if (subCount > thisLen - start) {
/*  884 */       return -1;
/*      */     }
/*      */     
/*  887 */     char firstChar = subString.charAt(0);
/*      */     for (;;) {
/*  889 */       int i = indexOf(firstChar, start);
/*  890 */       if ((i == -1) || (subCount + i > thisLen)) {
/*  891 */         return -1;
/*      */       }
/*  893 */       int o1 = i;int o2 = 0;
/*  894 */       do { o2++; } while ((o2 < subCount) && ((value[(++o1)] & 0xFF) == subString.charAt(o2)));
/*      */       
/*      */ 
/*  897 */       if (o2 == subCount) {
/*  898 */         return i;
/*      */       }
/*  900 */       start = i + 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int lastIndexOf(int c)
/*      */   {
/*  912 */     return lastIndexOf(c, length() - 1);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int lastIndexOf(int c, int start)
/*      */   {
/*  924 */     if (start >= 0) {
/*  925 */       byte[] value = this.value;
/*  926 */       int length = value.length;
/*  927 */       if (start >= length) {
/*  928 */         start = length - 1;
/*      */       }
/*  930 */       for (int i = start; i >= 0; i--) {
/*  931 */         if ((value[i] & 0xFF) == c) {
/*  932 */           return i;
/*      */         }
/*      */       }
/*      */     }
/*  936 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int lastIndexOf(CharSequence string)
/*      */   {
/*  950 */     return lastIndexOf(string, length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int lastIndexOf(CharSequence subString, int start)
/*      */   {
/*  964 */     byte[] value = this.value;
/*  965 */     int thisLen = value.length;
/*  966 */     int subCount = subString.length();
/*      */     
/*  968 */     if ((subCount > thisLen) || (start < 0)) {
/*  969 */       return -1;
/*      */     }
/*      */     
/*  972 */     if (subCount <= 0) {
/*  973 */       return start < thisLen ? start : thisLen;
/*      */     }
/*      */     
/*  976 */     start = Math.min(start, thisLen - subCount);
/*      */     
/*      */ 
/*  979 */     char firstChar = subString.charAt(0);
/*      */     for (;;) {
/*  981 */       int i = lastIndexOf(firstChar, start);
/*  982 */       if (i == -1) {
/*  983 */         return -1;
/*      */       }
/*  985 */       int o1 = i;int o2 = 0;
/*  986 */       do { o2++; } while ((o2 < subCount) && ((value[(++o1)] & 0xFF) == subString.charAt(o2)));
/*      */       
/*      */ 
/*  989 */       if (o2 == subCount) {
/*  990 */         return i;
/*      */       }
/*  992 */       start = i - 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/* 1002 */     return this.value.length == 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean regionMatches(int thisStart, CharSequence string, int start, int length)
/*      */   {
/* 1017 */     if (string == null) {
/* 1018 */       throw new NullPointerException("string");
/*      */     }
/*      */     
/* 1021 */     if ((start < 0) || (string.length() - start < length)) {
/* 1022 */       return false;
/*      */     }
/*      */     
/* 1025 */     byte[] value = this.value;
/* 1026 */     int thisLen = value.length;
/* 1027 */     if ((thisStart < 0) || (thisLen - thisStart < length)) {
/* 1028 */       return false;
/*      */     }
/*      */     
/* 1031 */     if (length <= 0) {
/* 1032 */       return true;
/*      */     }
/*      */     
/* 1035 */     int thisEnd = thisStart + length;
/* 1036 */     int i = thisStart; for (int j = start; i < thisEnd; j++) {
/* 1037 */       if ((value[i] & 0xFF) != string.charAt(j)) {
/* 1038 */         return false;
/*      */       }
/* 1036 */       i++;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1041 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean regionMatches(boolean ignoreCase, int thisStart, CharSequence string, int start, int length)
/*      */   {
/* 1057 */     if (!ignoreCase) {
/* 1058 */       return regionMatches(thisStart, string, start, length);
/*      */     }
/*      */     
/* 1061 */     if (string == null) {
/* 1062 */       throw new NullPointerException("string");
/*      */     }
/*      */     
/* 1065 */     byte[] value = this.value;
/* 1066 */     int thisLen = value.length;
/* 1067 */     if ((thisStart < 0) || (length > thisLen - thisStart)) {
/* 1068 */       return false;
/*      */     }
/* 1070 */     if ((start < 0) || (length > string.length() - start)) {
/* 1071 */       return false;
/*      */     }
/*      */     
/* 1074 */     int thisEnd = thisStart + length;
/* 1075 */     while (thisStart < thisEnd) {
/* 1076 */       char c1 = (char)(value[(thisStart++)] & 0xFF);
/* 1077 */       char c2 = string.charAt(start++);
/* 1078 */       if ((c1 != c2) && (toLowerCase(c1) != toLowerCase(c2))) {
/* 1079 */         return false;
/*      */       }
/*      */     }
/* 1082 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString replace(char oldChar, char newChar)
/*      */   {
/* 1093 */     int index = indexOf(oldChar, 0);
/* 1094 */     if (index == -1) {
/* 1095 */       return this;
/*      */     }
/*      */     
/* 1098 */     byte[] value = this.value;
/* 1099 */     int count = value.length;
/* 1100 */     byte[] buffer = new byte[count];
/* 1101 */     int i = 0; for (int j = 0; i < value.length; j++) {
/* 1102 */       byte b = value[i];
/* 1103 */       if ((char)(b & 0xFF) == oldChar) {
/* 1104 */         b = (byte)newChar;
/*      */       }
/* 1106 */       buffer[j] = b;i++;
/*      */     }
/*      */     
/* 1109 */     return new AsciiString(buffer, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean startsWith(CharSequence prefix)
/*      */   {
/* 1120 */     return startsWith(prefix, 0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean startsWith(CharSequence prefix, int start)
/*      */   {
/* 1134 */     return regionMatches(start, prefix, 0, prefix.length());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString toLowerCase()
/*      */   {
/* 1143 */     boolean lowercased = true;
/* 1144 */     byte[] value = this.value;
/*      */     
/* 1146 */     for (int i = 0; i < value.length; i++) {
/* 1147 */       byte b = value[i];
/* 1148 */       if ((b >= 65) && (b <= 90)) {
/* 1149 */         lowercased = false;
/* 1150 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1155 */     if (lowercased) {
/* 1156 */       return this;
/*      */     }
/*      */     
/* 1159 */     int length = value.length;
/* 1160 */     byte[] newValue = new byte[length];
/* 1161 */     i = 0; for (int j = 0; i < length; j++) {
/* 1162 */       newValue[i] = toLowerCase(value[j]);i++;
/*      */     }
/*      */     
/* 1165 */     return new AsciiString(newValue, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString toUpperCase()
/*      */   {
/* 1174 */     byte[] value = this.value;
/* 1175 */     boolean uppercased = true;
/*      */     
/* 1177 */     for (int i = 0; i < value.length; i++) {
/* 1178 */       byte b = value[i];
/* 1179 */       if ((b >= 97) && (b <= 122)) {
/* 1180 */         uppercased = false;
/* 1181 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1186 */     if (uppercased) {
/* 1187 */       return this;
/*      */     }
/*      */     
/* 1190 */     int length = value.length;
/* 1191 */     byte[] newValue = new byte[length];
/* 1192 */     i = 0; for (int j = 0; i < length; j++) {
/* 1193 */       newValue[i] = toUpperCase(value[j]);i++;
/*      */     }
/*      */     
/* 1196 */     return new AsciiString(newValue, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString trim()
/*      */   {
/* 1205 */     byte[] value = this.value;
/* 1206 */     int start = 0;int last = value.length;
/* 1207 */     int end = last;
/* 1208 */     while ((start <= end) && (value[start] <= 32)) {
/* 1209 */       start++;
/*      */     }
/* 1211 */     while ((end >= start) && (value[end] <= 32)) {
/* 1212 */       end--;
/*      */     }
/* 1214 */     if ((start == 0) && (end == last)) {
/* 1215 */       return this;
/*      */     }
/* 1217 */     return new AsciiString(value, start, end - start + 1, false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean contentEquals(CharSequence cs)
/*      */   {
/* 1227 */     if (cs == null) {
/* 1228 */       throw new NullPointerException();
/*      */     }
/*      */     
/* 1231 */     int length1 = length();
/* 1232 */     int length2 = cs.length();
/* 1233 */     if (length1 != length2) {
/* 1234 */       return false;
/*      */     }
/*      */     
/* 1237 */     if ((length1 == 0) && (length2 == 0)) {
/* 1238 */       return true;
/*      */     }
/*      */     
/* 1241 */     return regionMatches(0, cs, 0, length2);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean matches(String expr)
/*      */   {
/* 1253 */     return Pattern.matches(expr, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AsciiString[] split(String expr, int max)
/*      */   {
/* 1268 */     return toAsciiStringArray(Pattern.compile(expr).split(this, max));
/*      */   }
/*      */   
/*      */   private static AsciiString[] toAsciiStringArray(String[] jdkResult) {
/* 1272 */     AsciiString[] res = new AsciiString[jdkResult.length];
/* 1273 */     for (int i = 0; i < jdkResult.length; i++) {
/* 1274 */       res[i] = new AsciiString(jdkResult[i]);
/*      */     }
/* 1276 */     return res;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AsciiString[] split(char delim)
/*      */   {
/* 1283 */     List<AsciiString> res = new ArrayList();
/*      */     
/* 1285 */     int start = 0;
/* 1286 */     byte[] value = this.value;
/* 1287 */     int length = value.length;
/* 1288 */     for (int i = start; i < length; i++) {
/* 1289 */       if (charAt(i) == delim) {
/* 1290 */         if (start == i) {
/* 1291 */           res.add(EMPTY_STRING);
/*      */         } else {
/* 1293 */           res.add(new AsciiString(value, start, i - start, false));
/*      */         }
/* 1295 */         start = i + 1;
/*      */       }
/*      */     }
/*      */     
/* 1299 */     if (start == 0) {
/* 1300 */       res.add(this);
/*      */     }
/* 1302 */     else if (start != length)
/*      */     {
/* 1304 */       res.add(new AsciiString(value, start, length - start, false));
/*      */     }
/*      */     else {
/* 1307 */       for (int i = res.size() - 1; i >= 0; i--) {
/* 1308 */         if (!((AsciiString)res.get(i)).isEmpty()) break;
/* 1309 */         res.remove(i);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1317 */     return (AsciiString[])res.toArray(new AsciiString[res.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean contains(CharSequence cs)
/*      */   {
/* 1327 */     if (cs == null) {
/* 1328 */       throw new NullPointerException();
/*      */     }
/* 1330 */     return indexOf(cs) >= 0;
/*      */   }
/*      */   
/*      */   public int parseInt() {
/* 1334 */     return parseInt(0, length(), 10);
/*      */   }
/*      */   
/*      */   public int parseInt(int radix) {
/* 1338 */     return parseInt(0, length(), radix);
/*      */   }
/*      */   
/*      */   public int parseInt(int start, int end) {
/* 1342 */     return parseInt(start, end, 10);
/*      */   }
/*      */   
/*      */   public int parseInt(int start, int end, int radix) {
/* 1346 */     if ((radix < 2) || (radix > 36)) {
/* 1347 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1350 */     if (start == end) {
/* 1351 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1354 */     int i = start;
/* 1355 */     boolean negative = charAt(i) == '-';
/* 1356 */     if (negative) { i++; if (i == end) {
/* 1357 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/*      */     }
/* 1360 */     return parseInt(i, end, radix, negative);
/*      */   }
/*      */   
/*      */   private int parseInt(int start, int end, int radix, boolean negative) {
/* 1364 */     byte[] value = this.value;
/* 1365 */     int max = Integer.MIN_VALUE / radix;
/* 1366 */     int result = 0;
/* 1367 */     int offset = start;
/* 1368 */     while (offset < end) {
/* 1369 */       int digit = Character.digit((char)(value[(offset++)] & 0xFF), radix);
/* 1370 */       if (digit == -1) {
/* 1371 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/* 1373 */       if (max > result) {
/* 1374 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/* 1376 */       int next = result * radix - digit;
/* 1377 */       if (next > result) {
/* 1378 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/* 1380 */       result = next;
/*      */     }
/* 1382 */     if (!negative) {
/* 1383 */       result = -result;
/* 1384 */       if (result < 0) {
/* 1385 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/*      */     }
/* 1388 */     return result;
/*      */   }
/*      */   
/*      */   public long parseLong() {
/* 1392 */     return parseLong(0, length(), 10);
/*      */   }
/*      */   
/*      */   public long parseLong(int radix) {
/* 1396 */     return parseLong(0, length(), radix);
/*      */   }
/*      */   
/*      */   public long parseLong(int start, int end) {
/* 1400 */     return parseLong(start, end, 10);
/*      */   }
/*      */   
/*      */   public long parseLong(int start, int end, int radix) {
/* 1404 */     if ((radix < 2) || (radix > 36)) {
/* 1405 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1408 */     if (start == end) {
/* 1409 */       throw new NumberFormatException();
/*      */     }
/*      */     
/* 1412 */     int i = start;
/* 1413 */     boolean negative = charAt(i) == '-';
/* 1414 */     if (negative) { i++; if (i == end) {
/* 1415 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/*      */     }
/* 1418 */     return parseLong(i, end, radix, negative);
/*      */   }
/*      */   
/*      */   private long parseLong(int start, int end, int radix, boolean negative) {
/* 1422 */     byte[] value = this.value;
/* 1423 */     long max = Long.MIN_VALUE / radix;
/* 1424 */     long result = 0L;
/* 1425 */     int offset = start;
/* 1426 */     while (offset < end) {
/* 1427 */       int digit = Character.digit((char)(value[(offset++)] & 0xFF), radix);
/* 1428 */       if (digit == -1) {
/* 1429 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/* 1431 */       if (max > result) {
/* 1432 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/* 1434 */       long next = result * radix - digit;
/* 1435 */       if (next > result) {
/* 1436 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/* 1438 */       result = next;
/*      */     }
/* 1440 */     if (!negative) {
/* 1441 */       result = -result;
/* 1442 */       if (result < 0L) {
/* 1443 */         throw new NumberFormatException(subSequence(start, end).toString());
/*      */       }
/*      */     }
/* 1446 */     return result;
/*      */   }
/*      */   
/*      */   public short parseShort() {
/* 1450 */     return parseShort(0, length(), 10);
/*      */   }
/*      */   
/*      */   public short parseShort(int radix) {
/* 1454 */     return parseShort(0, length(), radix);
/*      */   }
/*      */   
/*      */   public short parseShort(int start, int end) {
/* 1458 */     return parseShort(start, end, 10);
/*      */   }
/*      */   
/*      */   public short parseShort(int start, int end, int radix) {
/* 1462 */     int intValue = parseInt(start, end, radix);
/* 1463 */     short result = (short)intValue;
/* 1464 */     if (result != intValue) {
/* 1465 */       throw new NumberFormatException(subSequence(start, end).toString());
/*      */     }
/* 1467 */     return result;
/*      */   }
/*      */   
/*      */   public float parseFloat() {
/* 1471 */     return parseFloat(0, length());
/*      */   }
/*      */   
/*      */   public float parseFloat(int start, int end) {
/* 1475 */     return Float.parseFloat(toString(start, end));
/*      */   }
/*      */   
/*      */   public double parseDouble() {
/* 1479 */     return parseDouble(0, length());
/*      */   }
/*      */   
/*      */   public double parseDouble(int start, int end) {
/* 1483 */     return Double.parseDouble(toString(start, end));
/*      */   }
/*      */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\AsciiString.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */