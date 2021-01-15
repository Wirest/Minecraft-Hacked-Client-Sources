/*     */ package io.netty.handler.codec.rtsp;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
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
/*     */ public final class RtspHeaderNames
/*     */ {
/*  32 */   public static final AsciiString ACCEPT = HttpHeaderNames.ACCEPT;
/*     */   
/*     */ 
/*     */ 
/*  36 */   public static final AsciiString ACCEPT_ENCODING = HttpHeaderNames.ACCEPT_ENCODING;
/*     */   
/*     */ 
/*     */ 
/*  40 */   public static final AsciiString ACCEPT_LANGUAGE = HttpHeaderNames.ACCEPT_LANGUAGE;
/*     */   
/*     */ 
/*     */ 
/*  44 */   public static final AsciiString ALLOW = new AsciiString("allow");
/*     */   
/*     */ 
/*     */ 
/*  48 */   public static final AsciiString AUTHORIZATION = HttpHeaderNames.AUTHORIZATION;
/*     */   
/*     */ 
/*     */ 
/*  52 */   public static final AsciiString BANDWIDTH = new AsciiString("bandwidth");
/*     */   
/*     */ 
/*     */ 
/*  56 */   public static final AsciiString BLOCKSIZE = new AsciiString("blocksize");
/*     */   
/*     */ 
/*     */ 
/*  60 */   public static final AsciiString CACHE_CONTROL = HttpHeaderNames.CACHE_CONTROL;
/*     */   
/*     */ 
/*     */ 
/*  64 */   public static final AsciiString CONFERENCE = new AsciiString("conference");
/*     */   
/*     */ 
/*     */ 
/*  68 */   public static final AsciiString CONNECTION = HttpHeaderNames.CONNECTION;
/*     */   
/*     */ 
/*     */ 
/*  72 */   public static final AsciiString CONTENT_BASE = HttpHeaderNames.CONTENT_BASE;
/*     */   
/*     */ 
/*     */ 
/*  76 */   public static final AsciiString CONTENT_ENCODING = HttpHeaderNames.CONTENT_ENCODING;
/*     */   
/*     */ 
/*     */ 
/*  80 */   public static final AsciiString CONTENT_LANGUAGE = HttpHeaderNames.CONTENT_LANGUAGE;
/*     */   
/*     */ 
/*     */ 
/*  84 */   public static final AsciiString CONTENT_LENGTH = HttpHeaderNames.CONTENT_LENGTH;
/*     */   
/*     */ 
/*     */ 
/*  88 */   public static final AsciiString CONTENT_LOCATION = HttpHeaderNames.CONTENT_LOCATION;
/*     */   
/*     */ 
/*     */ 
/*  92 */   public static final AsciiString CONTENT_TYPE = HttpHeaderNames.CONTENT_TYPE;
/*     */   
/*     */ 
/*     */ 
/*  96 */   public static final AsciiString CSEQ = new AsciiString("cseq");
/*     */   
/*     */ 
/*     */ 
/* 100 */   public static final AsciiString DATE = HttpHeaderNames.DATE;
/*     */   
/*     */ 
/*     */ 
/* 104 */   public static final AsciiString EXPIRES = HttpHeaderNames.EXPIRES;
/*     */   
/*     */ 
/*     */ 
/* 108 */   public static final AsciiString FROM = HttpHeaderNames.FROM;
/*     */   
/*     */ 
/*     */ 
/* 112 */   public static final AsciiString HOST = HttpHeaderNames.HOST;
/*     */   
/*     */ 
/*     */ 
/* 116 */   public static final AsciiString IF_MATCH = HttpHeaderNames.IF_MATCH;
/*     */   
/*     */ 
/*     */ 
/* 120 */   public static final AsciiString IF_MODIFIED_SINCE = HttpHeaderNames.IF_MODIFIED_SINCE;
/*     */   
/*     */ 
/*     */ 
/* 124 */   public static final AsciiString KEYMGMT = new AsciiString("keymgmt");
/*     */   
/*     */ 
/*     */ 
/* 128 */   public static final AsciiString LAST_MODIFIED = HttpHeaderNames.LAST_MODIFIED;
/*     */   
/*     */ 
/*     */ 
/* 132 */   public static final AsciiString PROXY_AUTHENTICATE = HttpHeaderNames.PROXY_AUTHENTICATE;
/*     */   
/*     */ 
/*     */ 
/* 136 */   public static final AsciiString PROXY_REQUIRE = new AsciiString("proxy-require");
/*     */   
/*     */ 
/*     */ 
/* 140 */   public static final AsciiString PUBLIC = new AsciiString("public");
/*     */   
/*     */ 
/*     */ 
/* 144 */   public static final AsciiString RANGE = HttpHeaderNames.RANGE;
/*     */   
/*     */ 
/*     */ 
/* 148 */   public static final AsciiString REFERER = HttpHeaderNames.REFERER;
/*     */   
/*     */ 
/*     */ 
/* 152 */   public static final AsciiString REQUIRE = new AsciiString("require");
/*     */   
/*     */ 
/*     */ 
/* 156 */   public static final AsciiString RETRT_AFTER = HttpHeaderNames.RETRY_AFTER;
/*     */   
/*     */ 
/*     */ 
/* 160 */   public static final AsciiString RTP_INFO = new AsciiString("rtp-info");
/*     */   
/*     */ 
/*     */ 
/* 164 */   public static final AsciiString SCALE = new AsciiString("scale");
/*     */   
/*     */ 
/*     */ 
/* 168 */   public static final AsciiString SESSION = new AsciiString("session");
/*     */   
/*     */ 
/*     */ 
/* 172 */   public static final AsciiString SERVER = HttpHeaderNames.SERVER;
/*     */   
/*     */ 
/*     */ 
/* 176 */   public static final AsciiString SPEED = new AsciiString("speed");
/*     */   
/*     */ 
/*     */ 
/* 180 */   public static final AsciiString TIMESTAMP = new AsciiString("timestamp");
/*     */   
/*     */ 
/*     */ 
/* 184 */   public static final AsciiString TRANSPORT = new AsciiString("transport");
/*     */   
/*     */ 
/*     */ 
/* 188 */   public static final AsciiString UNSUPPORTED = new AsciiString("unsupported");
/*     */   
/*     */ 
/*     */ 
/* 192 */   public static final AsciiString USER_AGENT = HttpHeaderNames.USER_AGENT;
/*     */   
/*     */ 
/*     */ 
/* 196 */   public static final AsciiString VARY = HttpHeaderNames.VARY;
/*     */   
/*     */ 
/*     */ 
/* 200 */   public static final AsciiString VIA = HttpHeaderNames.VIA;
/*     */   
/*     */ 
/*     */ 
/* 204 */   public static final AsciiString WWW_AUTHENTICATE = HttpHeaderNames.WWW_AUTHENTICATE;
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\rtsp\RtspHeaderNames.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */