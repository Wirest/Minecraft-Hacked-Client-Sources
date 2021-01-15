/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.handler.codec.AsciiString;
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
/*     */ public final class HttpHeaderNames
/*     */ {
/*  31 */   public static final AsciiString ACCEPT = new AsciiString("accept");
/*     */   
/*     */ 
/*     */ 
/*  35 */   public static final AsciiString ACCEPT_CHARSET = new AsciiString("accept-charset");
/*     */   
/*     */ 
/*     */ 
/*  39 */   public static final AsciiString ACCEPT_ENCODING = new AsciiString("accept-encoding");
/*     */   
/*     */ 
/*     */ 
/*  43 */   public static final AsciiString ACCEPT_LANGUAGE = new AsciiString("accept-language");
/*     */   
/*     */ 
/*     */ 
/*  47 */   public static final AsciiString ACCEPT_RANGES = new AsciiString("accept-ranges");
/*     */   
/*     */ 
/*     */ 
/*  51 */   public static final AsciiString ACCEPT_PATCH = new AsciiString("accept-patch");
/*     */   
/*     */ 
/*     */ 
/*  55 */   public static final AsciiString ACCESS_CONTROL_ALLOW_CREDENTIALS = new AsciiString("access-control-allow-credentials");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   public static final AsciiString ACCESS_CONTROL_ALLOW_HEADERS = new AsciiString("access-control-allow-headers");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   public static final AsciiString ACCESS_CONTROL_ALLOW_METHODS = new AsciiString("access-control-allow-methods");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  70 */   public static final AsciiString ACCESS_CONTROL_ALLOW_ORIGIN = new AsciiString("access-control-allow-origin");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  75 */   public static final AsciiString ACCESS_CONTROL_EXPOSE_HEADERS = new AsciiString("access-control-expose-headers");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  80 */   public static final AsciiString ACCESS_CONTROL_MAX_AGE = new AsciiString("access-control-max-age");
/*     */   
/*     */ 
/*     */ 
/*  84 */   public static final AsciiString ACCESS_CONTROL_REQUEST_HEADERS = new AsciiString("access-control-request-headers");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  89 */   public static final AsciiString ACCESS_CONTROL_REQUEST_METHOD = new AsciiString("access-control-request-method");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   public static final AsciiString AGE = new AsciiString("age");
/*     */   
/*     */ 
/*     */ 
/*  98 */   public static final AsciiString ALLOW = new AsciiString("allow");
/*     */   
/*     */ 
/*     */ 
/* 102 */   public static final AsciiString AUTHORIZATION = new AsciiString("authorization");
/*     */   
/*     */ 
/*     */ 
/* 106 */   public static final AsciiString CACHE_CONTROL = new AsciiString("cache-control");
/*     */   
/*     */ 
/*     */ 
/* 110 */   public static final AsciiString CONNECTION = new AsciiString("connection");
/*     */   
/*     */ 
/*     */ 
/* 114 */   public static final AsciiString CONTENT_BASE = new AsciiString("content-base");
/*     */   
/*     */ 
/*     */ 
/* 118 */   public static final AsciiString CONTENT_ENCODING = new AsciiString("content-encoding");
/*     */   
/*     */ 
/*     */ 
/* 122 */   public static final AsciiString CONTENT_LANGUAGE = new AsciiString("content-language");
/*     */   
/*     */ 
/*     */ 
/* 126 */   public static final AsciiString CONTENT_LENGTH = new AsciiString("content-length");
/*     */   
/*     */ 
/*     */ 
/* 130 */   public static final AsciiString CONTENT_LOCATION = new AsciiString("content-location");
/*     */   
/*     */ 
/*     */ 
/* 134 */   public static final AsciiString CONTENT_TRANSFER_ENCODING = new AsciiString("content-transfer-encoding");
/*     */   
/*     */ 
/*     */ 
/* 138 */   public static final AsciiString CONTENT_DISPOSITION = new AsciiString("content-disposition");
/*     */   
/*     */ 
/*     */ 
/* 142 */   public static final AsciiString CONTENT_MD5 = new AsciiString("content-md5");
/*     */   
/*     */ 
/*     */ 
/* 146 */   public static final AsciiString CONTENT_RANGE = new AsciiString("content-range");
/*     */   
/*     */ 
/*     */ 
/* 150 */   public static final AsciiString CONTENT_TYPE = new AsciiString("content-type");
/*     */   
/*     */ 
/*     */ 
/* 154 */   public static final AsciiString COOKIE = new AsciiString("cookie");
/*     */   
/*     */ 
/*     */ 
/* 158 */   public static final AsciiString DATE = new AsciiString("date");
/*     */   
/*     */ 
/*     */ 
/* 162 */   public static final AsciiString ETAG = new AsciiString("etag");
/*     */   
/*     */ 
/*     */ 
/* 166 */   public static final AsciiString EXPECT = new AsciiString("expect");
/*     */   
/*     */ 
/*     */ 
/* 170 */   public static final AsciiString EXPIRES = new AsciiString("expires");
/*     */   
/*     */ 
/*     */ 
/* 174 */   public static final AsciiString FROM = new AsciiString("from");
/*     */   
/*     */ 
/*     */ 
/* 178 */   public static final AsciiString HOST = new AsciiString("host");
/*     */   
/*     */ 
/*     */ 
/* 182 */   public static final AsciiString IF_MATCH = new AsciiString("if-match");
/*     */   
/*     */ 
/*     */ 
/* 186 */   public static final AsciiString IF_MODIFIED_SINCE = new AsciiString("if-modified-since");
/*     */   
/*     */ 
/*     */ 
/* 190 */   public static final AsciiString IF_NONE_MATCH = new AsciiString("if-none-match");
/*     */   
/*     */ 
/*     */ 
/* 194 */   public static final AsciiString IF_RANGE = new AsciiString("if-range");
/*     */   
/*     */ 
/*     */ 
/* 198 */   public static final AsciiString IF_UNMODIFIED_SINCE = new AsciiString("if-unmodified-since");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/* 205 */   public static final AsciiString KEEP_ALIVE = new AsciiString("keep-alive");
/*     */   
/*     */ 
/*     */ 
/* 209 */   public static final AsciiString LAST_MODIFIED = new AsciiString("last-modified");
/*     */   
/*     */ 
/*     */ 
/* 213 */   public static final AsciiString LOCATION = new AsciiString("location");
/*     */   
/*     */ 
/*     */ 
/* 217 */   public static final AsciiString MAX_FORWARDS = new AsciiString("max-forwards");
/*     */   
/*     */ 
/*     */ 
/* 221 */   public static final AsciiString ORIGIN = new AsciiString("origin");
/*     */   
/*     */ 
/*     */ 
/* 225 */   public static final AsciiString PRAGMA = new AsciiString("pragma");
/*     */   
/*     */ 
/*     */ 
/* 229 */   public static final AsciiString PROXY_AUTHENTICATE = new AsciiString("proxy-authenticate");
/*     */   
/*     */ 
/*     */ 
/* 233 */   public static final AsciiString PROXY_AUTHORIZATION = new AsciiString("proxy-authorization");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/* 240 */   public static final AsciiString PROXY_CONNECTION = new AsciiString("proxy-connection");
/*     */   
/*     */ 
/*     */ 
/* 244 */   public static final AsciiString RANGE = new AsciiString("range");
/*     */   
/*     */ 
/*     */ 
/* 248 */   public static final AsciiString REFERER = new AsciiString("referer");
/*     */   
/*     */ 
/*     */ 
/* 252 */   public static final AsciiString RETRY_AFTER = new AsciiString("retry-after");
/*     */   
/*     */ 
/*     */ 
/* 256 */   public static final AsciiString SEC_WEBSOCKET_KEY1 = new AsciiString("sec-websocket-key1");
/*     */   
/*     */ 
/*     */ 
/* 260 */   public static final AsciiString SEC_WEBSOCKET_KEY2 = new AsciiString("sec-websocket-key2");
/*     */   
/*     */ 
/*     */ 
/* 264 */   public static final AsciiString SEC_WEBSOCKET_LOCATION = new AsciiString("sec-websocket-location");
/*     */   
/*     */ 
/*     */ 
/* 268 */   public static final AsciiString SEC_WEBSOCKET_ORIGIN = new AsciiString("sec-websocket-origin");
/*     */   
/*     */ 
/*     */ 
/* 272 */   public static final AsciiString SEC_WEBSOCKET_PROTOCOL = new AsciiString("sec-websocket-protocol");
/*     */   
/*     */ 
/*     */ 
/* 276 */   public static final AsciiString SEC_WEBSOCKET_VERSION = new AsciiString("sec-websocket-version");
/*     */   
/*     */ 
/*     */ 
/* 280 */   public static final AsciiString SEC_WEBSOCKET_KEY = new AsciiString("sec-websocket-key");
/*     */   
/*     */ 
/*     */ 
/* 284 */   public static final AsciiString SEC_WEBSOCKET_ACCEPT = new AsciiString("sec-websocket-accept");
/*     */   
/*     */ 
/*     */ 
/* 288 */   public static final AsciiString SEC_WEBSOCKET_EXTENSIONS = new AsciiString("sec-websocket-extensions");
/*     */   
/*     */ 
/*     */ 
/* 292 */   public static final AsciiString SERVER = new AsciiString("server");
/*     */   
/*     */ 
/*     */ 
/* 296 */   public static final AsciiString SET_COOKIE = new AsciiString("set-cookie");
/*     */   
/*     */ 
/*     */ 
/* 300 */   public static final AsciiString SET_COOKIE2 = new AsciiString("set-cookie2");
/*     */   
/*     */ 
/*     */ 
/* 304 */   public static final AsciiString TE = new AsciiString("te");
/*     */   
/*     */ 
/*     */ 
/* 308 */   public static final AsciiString TRAILER = new AsciiString("trailer");
/*     */   
/*     */ 
/*     */ 
/* 312 */   public static final AsciiString TRANSFER_ENCODING = new AsciiString("transfer-encoding");
/*     */   
/*     */ 
/*     */ 
/* 316 */   public static final AsciiString UPGRADE = new AsciiString("upgrade");
/*     */   
/*     */ 
/*     */ 
/* 320 */   public static final AsciiString USER_AGENT = new AsciiString("user-agent");
/*     */   
/*     */ 
/*     */ 
/* 324 */   public static final AsciiString VARY = new AsciiString("vary");
/*     */   
/*     */ 
/*     */ 
/* 328 */   public static final AsciiString VIA = new AsciiString("via");
/*     */   
/*     */ 
/*     */ 
/* 332 */   public static final AsciiString WARNING = new AsciiString("warning");
/*     */   
/*     */ 
/*     */ 
/* 336 */   public static final AsciiString WEBSOCKET_LOCATION = new AsciiString("websocket-location");
/*     */   
/*     */ 
/*     */ 
/* 340 */   public static final AsciiString WEBSOCKET_ORIGIN = new AsciiString("websocket-origin");
/*     */   
/*     */ 
/*     */ 
/* 344 */   public static final AsciiString WEBSOCKET_PROTOCOL = new AsciiString("websocket-protocol");
/*     */   
/*     */ 
/*     */ 
/* 348 */   public static final AsciiString WWW_AUTHENTICATE = new AsciiString("www-authenticate");
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpHeaderNames.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */