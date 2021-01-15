/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class HttpVersion
/*     */   implements Comparable<HttpVersion>
/*     */ {
/*  30 */   private static final Pattern VERSION_PATTERN = Pattern.compile("(\\S+)/(\\d+)\\.(\\d+)");
/*     */   
/*     */ 
/*     */   private static final String HTTP_1_0_STRING = "HTTP/1.0";
/*     */   
/*     */ 
/*     */   private static final String HTTP_1_1_STRING = "HTTP/1.1";
/*     */   
/*     */ 
/*  39 */   public static final HttpVersion HTTP_1_0 = new HttpVersion("HTTP", 1, 0, false);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   public static final HttpVersion HTTP_1_1 = new HttpVersion("HTTP", 1, 1, true);
/*     */   
/*     */   private final AsciiString protocolName;
/*     */   private final int majorVersion;
/*     */   private final int minorVersion;
/*     */   private final AsciiString text;
/*     */   private final String textAsString;
/*     */   private final boolean keepAliveDefault;
/*     */   
/*     */   public static HttpVersion valueOf(String text)
/*     */   {
/*  55 */     if (text == null) {
/*  56 */       throw new NullPointerException("text");
/*     */     }
/*     */     
/*  59 */     text = text.trim();
/*     */     
/*  61 */     if (text.isEmpty()) {
/*  62 */       throw new IllegalArgumentException("text is empty");
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
/*  73 */     HttpVersion version = version0(text);
/*  74 */     if (version == null) {
/*  75 */       version = new HttpVersion(text, true);
/*     */     }
/*  77 */     return version;
/*     */   }
/*     */   
/*     */   private static HttpVersion version0(String text) {
/*  81 */     if ("HTTP/1.1".equals(text)) {
/*  82 */       return HTTP_1_1;
/*     */     }
/*  84 */     if ("HTTP/1.0".equals(text)) {
/*  85 */       return HTTP_1_0;
/*     */     }
/*  87 */     return null;
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
/*     */   public HttpVersion(String text, boolean keepAliveDefault)
/*     */   {
/* 109 */     if (text == null) {
/* 110 */       throw new NullPointerException("text");
/*     */     }
/*     */     
/* 113 */     text = text.trim().toUpperCase();
/* 114 */     if (text.isEmpty()) {
/* 115 */       throw new IllegalArgumentException("empty text");
/*     */     }
/*     */     
/* 118 */     Matcher m = VERSION_PATTERN.matcher(text);
/* 119 */     if (!m.matches()) {
/* 120 */       throw new IllegalArgumentException("invalid version format: " + text);
/*     */     }
/*     */     
/* 123 */     this.protocolName = new AsciiString(m.group(1));
/* 124 */     this.majorVersion = Integer.parseInt(m.group(2));
/* 125 */     this.minorVersion = Integer.parseInt(m.group(3));
/* 126 */     this.textAsString = (this.protocolName + "/" + this.majorVersion + '.' + this.minorVersion);
/* 127 */     this.text = new AsciiString(this.textAsString);
/* 128 */     this.keepAliveDefault = keepAliveDefault;
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
/*     */   public HttpVersion(String protocolName, int majorVersion, int minorVersion, boolean keepAliveDefault)
/*     */   {
/* 143 */     if (protocolName == null) {
/* 144 */       throw new NullPointerException("protocolName");
/*     */     }
/*     */     
/* 147 */     protocolName = protocolName.trim().toUpperCase();
/* 148 */     if (protocolName.isEmpty()) {
/* 149 */       throw new IllegalArgumentException("empty protocolName");
/*     */     }
/*     */     
/* 152 */     for (int i = 0; i < protocolName.length(); i++) {
/* 153 */       if ((Character.isISOControl(protocolName.charAt(i))) || (Character.isWhitespace(protocolName.charAt(i))))
/*     */       {
/* 155 */         throw new IllegalArgumentException("invalid character in protocolName");
/*     */       }
/*     */     }
/*     */     
/* 159 */     if (majorVersion < 0) {
/* 160 */       throw new IllegalArgumentException("negative majorVersion");
/*     */     }
/* 162 */     if (minorVersion < 0) {
/* 163 */       throw new IllegalArgumentException("negative minorVersion");
/*     */     }
/*     */     
/* 166 */     this.protocolName = new AsciiString(protocolName);
/* 167 */     this.majorVersion = majorVersion;
/* 168 */     this.minorVersion = minorVersion;
/* 169 */     this.textAsString = (protocolName + '/' + majorVersion + '.' + minorVersion);
/* 170 */     this.text = new AsciiString(this.textAsString);
/* 171 */     this.keepAliveDefault = keepAliveDefault;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AsciiString protocolName()
/*     */   {
/* 178 */     return this.protocolName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int majorVersion()
/*     */   {
/* 185 */     return this.majorVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int minorVersion()
/*     */   {
/* 192 */     return this.minorVersion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AsciiString text()
/*     */   {
/* 199 */     return this.text;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isKeepAliveDefault()
/*     */   {
/* 207 */     return this.keepAliveDefault;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 215 */     return this.textAsString;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 220 */     return (protocolName().hashCode() * 31 + majorVersion()) * 31 + minorVersion();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 226 */     if (!(o instanceof HttpVersion)) {
/* 227 */       return false;
/*     */     }
/*     */     
/* 230 */     HttpVersion that = (HttpVersion)o;
/* 231 */     return (minorVersion() == that.minorVersion()) && (majorVersion() == that.majorVersion()) && (protocolName().equals(that.protocolName()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int compareTo(HttpVersion o)
/*     */   {
/* 238 */     int v = protocolName().compareTo(o.protocolName());
/* 239 */     if (v != 0) {
/* 240 */       return v;
/*     */     }
/*     */     
/* 243 */     v = majorVersion() - o.majorVersion();
/* 244 */     if (v != 0) {
/* 245 */       return v;
/*     */     }
/*     */     
/* 248 */     return minorVersion() - o.minorVersion();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */