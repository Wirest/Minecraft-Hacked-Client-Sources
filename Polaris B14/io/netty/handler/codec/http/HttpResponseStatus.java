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
/*     */ public class HttpResponseStatus
/*     */   implements Comparable<HttpResponseStatus>
/*     */ {
/*  30 */   public static final HttpResponseStatus CONTINUE = newStatus(100, "Continue");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   public static final HttpResponseStatus SWITCHING_PROTOCOLS = newStatus(101, "Switching Protocols");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  40 */   public static final HttpResponseStatus PROCESSING = newStatus(102, "Processing");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  45 */   public static final HttpResponseStatus OK = newStatus(200, "OK");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public static final HttpResponseStatus CREATED = newStatus(201, "Created");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  55 */   public static final HttpResponseStatus ACCEPTED = newStatus(202, "Accepted");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   public static final HttpResponseStatus NON_AUTHORITATIVE_INFORMATION = newStatus(203, "Non-Authoritative Information");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */   public static final HttpResponseStatus NO_CONTENT = newStatus(204, "No Content");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  71 */   public static final HttpResponseStatus RESET_CONTENT = newStatus(205, "Reset Content");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   public static final HttpResponseStatus PARTIAL_CONTENT = newStatus(206, "Partial Content");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   public static final HttpResponseStatus MULTI_STATUS = newStatus(207, "Multi-Status");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  86 */   public static final HttpResponseStatus MULTIPLE_CHOICES = newStatus(300, "Multiple Choices");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  91 */   public static final HttpResponseStatus MOVED_PERMANENTLY = newStatus(301, "Moved Permanently");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  96 */   public static final HttpResponseStatus FOUND = newStatus(302, "Found");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 101 */   public static final HttpResponseStatus SEE_OTHER = newStatus(303, "See Other");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 106 */   public static final HttpResponseStatus NOT_MODIFIED = newStatus(304, "Not Modified");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 111 */   public static final HttpResponseStatus USE_PROXY = newStatus(305, "Use Proxy");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 116 */   public static final HttpResponseStatus TEMPORARY_REDIRECT = newStatus(307, "Temporary Redirect");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 121 */   public static final HttpResponseStatus BAD_REQUEST = newStatus(400, "Bad Request");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 126 */   public static final HttpResponseStatus UNAUTHORIZED = newStatus(401, "Unauthorized");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 131 */   public static final HttpResponseStatus PAYMENT_REQUIRED = newStatus(402, "Payment Required");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 136 */   public static final HttpResponseStatus FORBIDDEN = newStatus(403, "Forbidden");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 141 */   public static final HttpResponseStatus NOT_FOUND = newStatus(404, "Not Found");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 146 */   public static final HttpResponseStatus METHOD_NOT_ALLOWED = newStatus(405, "Method Not Allowed");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 151 */   public static final HttpResponseStatus NOT_ACCEPTABLE = newStatus(406, "Not Acceptable");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 156 */   public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED = newStatus(407, "Proxy Authentication Required");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 162 */   public static final HttpResponseStatus REQUEST_TIMEOUT = newStatus(408, "Request Timeout");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 167 */   public static final HttpResponseStatus CONFLICT = newStatus(409, "Conflict");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 172 */   public static final HttpResponseStatus GONE = newStatus(410, "Gone");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 177 */   public static final HttpResponseStatus LENGTH_REQUIRED = newStatus(411, "Length Required");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 182 */   public static final HttpResponseStatus PRECONDITION_FAILED = newStatus(412, "Precondition Failed");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 187 */   public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE = newStatus(413, "Request Entity Too Large");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 193 */   public static final HttpResponseStatus REQUEST_URI_TOO_LONG = newStatus(414, "Request-URI Too Long");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 198 */   public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE = newStatus(415, "Unsupported Media Type");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 203 */   public static final HttpResponseStatus REQUESTED_RANGE_NOT_SATISFIABLE = newStatus(416, "Requested Range Not Satisfiable");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 209 */   public static final HttpResponseStatus EXPECTATION_FAILED = newStatus(417, "Expectation Failed");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */   public static final HttpResponseStatus MISDIRECTED_REQUEST = newStatus(421, "Misdirected Request");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 221 */   public static final HttpResponseStatus UNPROCESSABLE_ENTITY = newStatus(422, "Unprocessable Entity");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 226 */   public static final HttpResponseStatus LOCKED = newStatus(423, "Locked");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 231 */   public static final HttpResponseStatus FAILED_DEPENDENCY = newStatus(424, "Failed Dependency");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 236 */   public static final HttpResponseStatus UNORDERED_COLLECTION = newStatus(425, "Unordered Collection");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 241 */   public static final HttpResponseStatus UPGRADE_REQUIRED = newStatus(426, "Upgrade Required");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 246 */   public static final HttpResponseStatus PRECONDITION_REQUIRED = newStatus(428, "Precondition Required");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 251 */   public static final HttpResponseStatus TOO_MANY_REQUESTS = newStatus(429, "Too Many Requests");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 256 */   public static final HttpResponseStatus REQUEST_HEADER_FIELDS_TOO_LARGE = newStatus(431, "Request Header Fields Too Large");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 262 */   public static final HttpResponseStatus INTERNAL_SERVER_ERROR = newStatus(500, "Internal Server Error");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 267 */   public static final HttpResponseStatus NOT_IMPLEMENTED = newStatus(501, "Not Implemented");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 272 */   public static final HttpResponseStatus BAD_GATEWAY = newStatus(502, "Bad Gateway");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 277 */   public static final HttpResponseStatus SERVICE_UNAVAILABLE = newStatus(503, "Service Unavailable");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 282 */   public static final HttpResponseStatus GATEWAY_TIMEOUT = newStatus(504, "Gateway Timeout");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 287 */   public static final HttpResponseStatus HTTP_VERSION_NOT_SUPPORTED = newStatus(505, "HTTP Version Not Supported");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 293 */   public static final HttpResponseStatus VARIANT_ALSO_NEGOTIATES = newStatus(506, "Variant Also Negotiates");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 298 */   public static final HttpResponseStatus INSUFFICIENT_STORAGE = newStatus(507, "Insufficient Storage");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 303 */   public static final HttpResponseStatus NOT_EXTENDED = newStatus(510, "Not Extended");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 308 */   public static final HttpResponseStatus NETWORK_AUTHENTICATION_REQUIRED = newStatus(511, "Network Authentication Required");
/*     */   private final int code;
/*     */   private final AsciiString codeAsText;
/*     */   
/* 312 */   private static HttpResponseStatus newStatus(int statusCode, String reasonPhrase) { return new HttpResponseStatus(statusCode, reasonPhrase, true); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpResponseStatus valueOf(int code)
/*     */   {
/* 321 */     switch (code) {
/*     */     case 100: 
/* 323 */       return CONTINUE;
/*     */     case 101: 
/* 325 */       return SWITCHING_PROTOCOLS;
/*     */     case 102: 
/* 327 */       return PROCESSING;
/*     */     case 200: 
/* 329 */       return OK;
/*     */     case 201: 
/* 331 */       return CREATED;
/*     */     case 202: 
/* 333 */       return ACCEPTED;
/*     */     case 203: 
/* 335 */       return NON_AUTHORITATIVE_INFORMATION;
/*     */     case 204: 
/* 337 */       return NO_CONTENT;
/*     */     case 205: 
/* 339 */       return RESET_CONTENT;
/*     */     case 206: 
/* 341 */       return PARTIAL_CONTENT;
/*     */     case 207: 
/* 343 */       return MULTI_STATUS;
/*     */     case 300: 
/* 345 */       return MULTIPLE_CHOICES;
/*     */     case 301: 
/* 347 */       return MOVED_PERMANENTLY;
/*     */     case 302: 
/* 349 */       return FOUND;
/*     */     case 303: 
/* 351 */       return SEE_OTHER;
/*     */     case 304: 
/* 353 */       return NOT_MODIFIED;
/*     */     case 305: 
/* 355 */       return USE_PROXY;
/*     */     case 307: 
/* 357 */       return TEMPORARY_REDIRECT;
/*     */     case 400: 
/* 359 */       return BAD_REQUEST;
/*     */     case 401: 
/* 361 */       return UNAUTHORIZED;
/*     */     case 402: 
/* 363 */       return PAYMENT_REQUIRED;
/*     */     case 403: 
/* 365 */       return FORBIDDEN;
/*     */     case 404: 
/* 367 */       return NOT_FOUND;
/*     */     case 405: 
/* 369 */       return METHOD_NOT_ALLOWED;
/*     */     case 406: 
/* 371 */       return NOT_ACCEPTABLE;
/*     */     case 407: 
/* 373 */       return PROXY_AUTHENTICATION_REQUIRED;
/*     */     case 408: 
/* 375 */       return REQUEST_TIMEOUT;
/*     */     case 409: 
/* 377 */       return CONFLICT;
/*     */     case 410: 
/* 379 */       return GONE;
/*     */     case 411: 
/* 381 */       return LENGTH_REQUIRED;
/*     */     case 412: 
/* 383 */       return PRECONDITION_FAILED;
/*     */     case 413: 
/* 385 */       return REQUEST_ENTITY_TOO_LARGE;
/*     */     case 414: 
/* 387 */       return REQUEST_URI_TOO_LONG;
/*     */     case 415: 
/* 389 */       return UNSUPPORTED_MEDIA_TYPE;
/*     */     case 416: 
/* 391 */       return REQUESTED_RANGE_NOT_SATISFIABLE;
/*     */     case 417: 
/* 393 */       return EXPECTATION_FAILED;
/*     */     case 421: 
/* 395 */       return MISDIRECTED_REQUEST;
/*     */     case 422: 
/* 397 */       return UNPROCESSABLE_ENTITY;
/*     */     case 423: 
/* 399 */       return LOCKED;
/*     */     case 424: 
/* 401 */       return FAILED_DEPENDENCY;
/*     */     case 425: 
/* 403 */       return UNORDERED_COLLECTION;
/*     */     case 426: 
/* 405 */       return UPGRADE_REQUIRED;
/*     */     case 428: 
/* 407 */       return PRECONDITION_REQUIRED;
/*     */     case 429: 
/* 409 */       return TOO_MANY_REQUESTS;
/*     */     case 431: 
/* 411 */       return REQUEST_HEADER_FIELDS_TOO_LARGE;
/*     */     case 500: 
/* 413 */       return INTERNAL_SERVER_ERROR;
/*     */     case 501: 
/* 415 */       return NOT_IMPLEMENTED;
/*     */     case 502: 
/* 417 */       return BAD_GATEWAY;
/*     */     case 503: 
/* 419 */       return SERVICE_UNAVAILABLE;
/*     */     case 504: 
/* 421 */       return GATEWAY_TIMEOUT;
/*     */     case 505: 
/* 423 */       return HTTP_VERSION_NOT_SUPPORTED;
/*     */     case 506: 
/* 425 */       return VARIANT_ALSO_NEGOTIATES;
/*     */     case 507: 
/* 427 */       return INSUFFICIENT_STORAGE;
/*     */     case 510: 
/* 429 */       return NOT_EXTENDED;
/*     */     case 511: 
/* 431 */       return NETWORK_AUTHENTICATION_REQUIRED;
/*     */     }
/*     */     
/* 434 */     return new HttpResponseStatus(code);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private HttpStatusClass codeClass;
/*     */   
/*     */ 
/*     */   private final AsciiString reasonPhrase;
/*     */   
/*     */ 
/*     */   public static HttpResponseStatus parseLine(CharSequence line)
/*     */   {
/* 447 */     String status = line.toString();
/*     */     try {
/* 449 */       int space = status.indexOf(' ');
/* 450 */       if (space == -1) {
/* 451 */         return valueOf(Integer.parseInt(status));
/*     */       }
/* 453 */       int code = Integer.parseInt(status.substring(0, space));
/* 454 */       String reasonPhrase = status.substring(space + 1);
/* 455 */       HttpResponseStatus responseStatus = valueOf(code);
/* 456 */       if (responseStatus.reasonPhrase().toString().equals(reasonPhrase)) {
/* 457 */         return responseStatus;
/*     */       }
/* 459 */       return new HttpResponseStatus(code, reasonPhrase);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 463 */       throw new IllegalArgumentException("malformed status line: " + status, e);
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
/*     */   private HttpResponseStatus(int code)
/*     */   {
/* 477 */     this(code, HttpStatusClass.valueOf(code).defaultReasonPhrase() + " (" + code + ')', false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpResponseStatus(int code, String reasonPhrase)
/*     */   {
/* 484 */     this(code, reasonPhrase, false);
/*     */   }
/*     */   
/*     */   private HttpResponseStatus(int code, String reasonPhrase, boolean bytes) {
/* 488 */     if (code < 0) {
/* 489 */       throw new IllegalArgumentException("code: " + code + " (expected: 0+)");
/*     */     }
/*     */     
/*     */ 
/* 493 */     if (reasonPhrase == null) {
/* 494 */       throw new NullPointerException("reasonPhrase");
/*     */     }
/*     */     
/* 497 */     for (int i = 0; i < reasonPhrase.length(); i++) {
/* 498 */       char c = reasonPhrase.charAt(i);
/*     */       
/* 500 */       switch (c) {
/*     */       case '\n': case '\r': 
/* 502 */         throw new IllegalArgumentException("reasonPhrase contains one of the following prohibited characters: \\r\\n: " + reasonPhrase);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/* 508 */     this.code = code;
/* 509 */     this.codeAsText = new AsciiString(Integer.toString(code));
/* 510 */     this.reasonPhrase = new AsciiString(reasonPhrase);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int code()
/*     */   {
/* 517 */     return this.code;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AsciiString codeAsText()
/*     */   {
/* 524 */     return this.codeAsText;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AsciiString reasonPhrase()
/*     */   {
/* 531 */     return this.reasonPhrase;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HttpStatusClass codeClass()
/*     */   {
/* 538 */     HttpStatusClass codeClass = this.codeClass;
/* 539 */     if (codeClass == null) {
/* 540 */       this.codeClass = (codeClass = HttpStatusClass.valueOf(this.code));
/*     */     }
/* 542 */     return codeClass;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 547 */     return code();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 556 */     if (!(o instanceof HttpResponseStatus)) {
/* 557 */       return false;
/*     */     }
/*     */     
/* 560 */     return code() == ((HttpResponseStatus)o).code();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int compareTo(HttpResponseStatus o)
/*     */   {
/* 569 */     return code() - o.code();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 574 */     return this.reasonPhrase.length() + 5 + this.code + ' ' + this.reasonPhrase;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpResponseStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */