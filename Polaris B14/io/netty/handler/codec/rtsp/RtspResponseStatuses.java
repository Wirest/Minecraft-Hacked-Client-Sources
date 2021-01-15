/*     */ package io.netty.handler.codec.rtsp;
/*     */ 
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
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
/*     */ public final class RtspResponseStatuses
/*     */ {
/*  28 */   public static final HttpResponseStatus CONTINUE = HttpResponseStatus.CONTINUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  33 */   public static final HttpResponseStatus OK = HttpResponseStatus.OK;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  38 */   public static final HttpResponseStatus CREATED = HttpResponseStatus.CREATED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  43 */   public static final HttpResponseStatus LOW_STORAGE_SPACE = new HttpResponseStatus(250, "Low on Storage Space");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   public static final HttpResponseStatus MULTIPLE_CHOICES = HttpResponseStatus.MULTIPLE_CHOICES;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  54 */   public static final HttpResponseStatus MOVED_PERMANENTLY = HttpResponseStatus.MOVED_PERMANENTLY;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  59 */   public static final HttpResponseStatus MOVED_TEMPORARILY = new HttpResponseStatus(302, "Moved Temporarily");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  64 */   public static final HttpResponseStatus NOT_MODIFIED = HttpResponseStatus.NOT_MODIFIED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  69 */   public static final HttpResponseStatus USE_PROXY = HttpResponseStatus.USE_PROXY;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  74 */   public static final HttpResponseStatus BAD_REQUEST = HttpResponseStatus.BAD_REQUEST;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  79 */   public static final HttpResponseStatus UNAUTHORIZED = HttpResponseStatus.UNAUTHORIZED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  84 */   public static final HttpResponseStatus PAYMENT_REQUIRED = HttpResponseStatus.PAYMENT_REQUIRED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  89 */   public static final HttpResponseStatus FORBIDDEN = HttpResponseStatus.FORBIDDEN;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  94 */   public static final HttpResponseStatus NOT_FOUND = HttpResponseStatus.NOT_FOUND;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  99 */   public static final HttpResponseStatus METHOD_NOT_ALLOWED = HttpResponseStatus.METHOD_NOT_ALLOWED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 104 */   public static final HttpResponseStatus NOT_ACCEPTABLE = HttpResponseStatus.NOT_ACCEPTABLE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 109 */   public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED = HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */   public static final HttpResponseStatus REQUEST_TIMEOUT = HttpResponseStatus.REQUEST_TIMEOUT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 120 */   public static final HttpResponseStatus GONE = HttpResponseStatus.GONE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 125 */   public static final HttpResponseStatus LENGTH_REQUIRED = HttpResponseStatus.LENGTH_REQUIRED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 130 */   public static final HttpResponseStatus PRECONDITION_FAILED = HttpResponseStatus.PRECONDITION_FAILED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 135 */   public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE = HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 140 */   public static final HttpResponseStatus REQUEST_URI_TOO_LONG = HttpResponseStatus.REQUEST_URI_TOO_LONG;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 145 */   public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE = HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 150 */   public static final HttpResponseStatus PARAMETER_NOT_UNDERSTOOD = new HttpResponseStatus(451, "Parameter Not Understood");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */   public static final HttpResponseStatus CONFERENCE_NOT_FOUND = new HttpResponseStatus(452, "Conference Not Found");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 162 */   public static final HttpResponseStatus NOT_ENOUGH_BANDWIDTH = new HttpResponseStatus(453, "Not Enough Bandwidth");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 168 */   public static final HttpResponseStatus SESSION_NOT_FOUND = new HttpResponseStatus(454, "Session Not Found");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 174 */   public static final HttpResponseStatus METHOD_NOT_VALID = new HttpResponseStatus(455, "Method Not Valid in This State");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 180 */   public static final HttpResponseStatus HEADER_FIELD_NOT_VALID = new HttpResponseStatus(456, "Header Field Not Valid for Resource");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 186 */   public static final HttpResponseStatus INVALID_RANGE = new HttpResponseStatus(457, "Invalid Range");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */   public static final HttpResponseStatus PARAMETER_IS_READONLY = new HttpResponseStatus(458, "Parameter Is Read-Only");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 198 */   public static final HttpResponseStatus AGGREGATE_OPERATION_NOT_ALLOWED = new HttpResponseStatus(459, "Aggregate operation not allowed");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 204 */   public static final HttpResponseStatus ONLY_AGGREGATE_OPERATION_ALLOWED = new HttpResponseStatus(460, "Only Aggregate operation allowed");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */   public static final HttpResponseStatus UNSUPPORTED_TRANSPORT = new HttpResponseStatus(461, "Unsupported transport");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 216 */   public static final HttpResponseStatus DESTINATION_UNREACHABLE = new HttpResponseStatus(462, "Destination unreachable");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 222 */   public static final HttpResponseStatus KEY_MANAGEMENT_FAILURE = new HttpResponseStatus(463, "Key management failure");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */   public static final HttpResponseStatus INTERNAL_SERVER_ERROR = HttpResponseStatus.INTERNAL_SERVER_ERROR;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 233 */   public static final HttpResponseStatus NOT_IMPLEMENTED = HttpResponseStatus.NOT_IMPLEMENTED;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 238 */   public static final HttpResponseStatus BAD_GATEWAY = HttpResponseStatus.BAD_GATEWAY;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 243 */   public static final HttpResponseStatus SERVICE_UNAVAILABLE = HttpResponseStatus.SERVICE_UNAVAILABLE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 248 */   public static final HttpResponseStatus GATEWAY_TIMEOUT = HttpResponseStatus.GATEWAY_TIMEOUT;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 253 */   public static final HttpResponseStatus RTSP_VERSION_NOT_SUPPORTED = new HttpResponseStatus(505, "RTSP Version not supported");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 259 */   public static final HttpResponseStatus OPTION_NOT_SUPPORTED = new HttpResponseStatus(551, "Option not supported");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static HttpResponseStatus valueOf(int code)
/*     */   {
/* 268 */     switch (code) {
/* 269 */     case 250:  return LOW_STORAGE_SPACE;
/* 270 */     case 302:  return MOVED_TEMPORARILY;
/* 271 */     case 451:  return PARAMETER_NOT_UNDERSTOOD;
/* 272 */     case 452:  return CONFERENCE_NOT_FOUND;
/* 273 */     case 453:  return NOT_ENOUGH_BANDWIDTH;
/* 274 */     case 454:  return SESSION_NOT_FOUND;
/* 275 */     case 455:  return METHOD_NOT_VALID;
/* 276 */     case 456:  return HEADER_FIELD_NOT_VALID;
/* 277 */     case 457:  return INVALID_RANGE;
/* 278 */     case 458:  return PARAMETER_IS_READONLY;
/* 279 */     case 459:  return AGGREGATE_OPERATION_NOT_ALLOWED;
/* 280 */     case 460:  return ONLY_AGGREGATE_OPERATION_ALLOWED;
/* 281 */     case 461:  return UNSUPPORTED_TRANSPORT;
/* 282 */     case 462:  return DESTINATION_UNREACHABLE;
/* 283 */     case 463:  return KEY_MANAGEMENT_FAILURE;
/* 284 */     case 505:  return RTSP_VERSION_NOT_SUPPORTED;
/* 285 */     case 551:  return OPTION_NOT_SUPPORTED; }
/* 286 */     return HttpResponseStatus.valueOf(code);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\rtsp\RtspResponseStatuses.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */