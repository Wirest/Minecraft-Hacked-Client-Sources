/*     */ package io.netty.handler.codec.http.cors;
/*     */ 
/*     */ import io.netty.handler.codec.http.DefaultHttpHeaders;
/*     */ import io.netty.handler.codec.http.EmptyHttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
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
/*     */ public final class CorsConfig
/*     */ {
/*     */   private final Set<String> origins;
/*     */   private final boolean anyOrigin;
/*     */   private final boolean enabled;
/*     */   private final Set<String> exposeHeaders;
/*     */   private final boolean allowCredentials;
/*     */   private final long maxAge;
/*     */   private final Set<HttpMethod> allowedRequestMethods;
/*     */   private final Set<String> allowedRequestHeaders;
/*     */   private final boolean allowNullOrigin;
/*     */   private final Map<CharSequence, Callable<?>> preflightHeaders;
/*     */   private final boolean shortCurcuit;
/*     */   
/*     */   private CorsConfig(Builder builder)
/*     */   {
/*  54 */     this.origins = new LinkedHashSet(builder.origins);
/*  55 */     this.anyOrigin = builder.anyOrigin;
/*  56 */     this.enabled = builder.enabled;
/*  57 */     this.exposeHeaders = builder.exposeHeaders;
/*  58 */     this.allowCredentials = builder.allowCredentials;
/*  59 */     this.maxAge = builder.maxAge;
/*  60 */     this.allowedRequestMethods = builder.requestMethods;
/*  61 */     this.allowedRequestHeaders = builder.requestHeaders;
/*  62 */     this.allowNullOrigin = builder.allowNullOrigin;
/*  63 */     this.preflightHeaders = builder.preflightHeaders;
/*  64 */     this.shortCurcuit = builder.shortCurcuit;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCorsSupportEnabled()
/*     */   {
/*  73 */     return this.enabled;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAnyOriginSupported()
/*     */   {
/*  82 */     return this.anyOrigin;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String origin()
/*     */   {
/*  91 */     return this.origins.isEmpty() ? "*" : (String)this.origins.iterator().next();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> origins()
/*     */   {
/* 100 */     return this.origins;
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
/*     */   public boolean isNullOriginAllowed()
/*     */   {
/* 113 */     return this.allowNullOrigin;
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
/*     */   public Set<String> exposedHeaders()
/*     */   {
/* 139 */     return Collections.unmodifiableSet(this.exposeHeaders);
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
/*     */   public boolean isCredentialsAllowed()
/*     */   {
/* 160 */     return this.allowCredentials;
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
/*     */   public long maxAge()
/*     */   {
/* 174 */     return this.maxAge;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<HttpMethod> allowedRequestMethods()
/*     */   {
/* 184 */     return Collections.unmodifiableSet(this.allowedRequestMethods);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> allowedRequestHeaders()
/*     */   {
/* 196 */     return Collections.unmodifiableSet(this.allowedRequestHeaders);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpHeaders preflightResponseHeaders()
/*     */   {
/* 205 */     if (this.preflightHeaders.isEmpty()) {
/* 206 */       return EmptyHttpHeaders.INSTANCE;
/*     */     }
/* 208 */     HttpHeaders preflightHeaders = new DefaultHttpHeaders();
/* 209 */     for (Map.Entry<CharSequence, Callable<?>> entry : this.preflightHeaders.entrySet()) {
/* 210 */       Object value = getValue((Callable)entry.getValue());
/* 211 */       if ((value instanceof Iterable)) {
/* 212 */         preflightHeaders.addObject((CharSequence)entry.getKey(), (Iterable)value);
/*     */       } else {
/* 214 */         preflightHeaders.addObject((CharSequence)entry.getKey(), value);
/*     */       }
/*     */     }
/* 217 */     return preflightHeaders;
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
/*     */   public boolean isShortCurcuit()
/*     */   {
/* 231 */     return this.shortCurcuit;
/*     */   }
/*     */   
/*     */   private static <T> T getValue(Callable<T> callable) {
/*     */     try {
/* 236 */       return (T)callable.call();
/*     */     } catch (Exception e) {
/* 238 */       throw new IllegalStateException("Could not generate value for callable [" + callable + ']', e);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 244 */     return StringUtil.simpleClassName(this) + "[enabled=" + this.enabled + ", origins=" + this.origins + ", anyOrigin=" + this.anyOrigin + ", exposedHeaders=" + this.exposeHeaders + ", isCredentialsAllowed=" + this.allowCredentials + ", maxAge=" + this.maxAge + ", allowedRequestMethods=" + this.allowedRequestMethods + ", allowedRequestHeaders=" + this.allowedRequestHeaders + ", preflightHeaders=" + this.preflightHeaders + ']';
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
/*     */   public static Builder withAnyOrigin()
/*     */   {
/* 261 */     return new Builder();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder withOrigin(String origin)
/*     */   {
/* 270 */     if ("*".equals(origin)) {
/* 271 */       return new Builder();
/*     */     }
/* 273 */     return new Builder(new String[] { origin });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Builder withOrigins(String... origins)
/*     */   {
/* 282 */     return new Builder(origins);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Builder
/*     */   {
/*     */     private final Set<String> origins;
/*     */     
/*     */     private final boolean anyOrigin;
/*     */     
/*     */     private boolean allowNullOrigin;
/* 293 */     private boolean enabled = true;
/*     */     private boolean allowCredentials;
/* 295 */     private final Set<String> exposeHeaders = new HashSet();
/*     */     private long maxAge;
/* 297 */     private final Set<HttpMethod> requestMethods = new HashSet();
/* 298 */     private final Set<String> requestHeaders = new HashSet();
/* 299 */     private final Map<CharSequence, Callable<?>> preflightHeaders = new HashMap();
/*     */     
/*     */ 
/*     */     private boolean noPreflightHeaders;
/*     */     
/*     */     private boolean shortCurcuit;
/*     */     
/*     */ 
/*     */     public Builder(String... origins)
/*     */     {
/* 309 */       this.origins = new LinkedHashSet(Arrays.asList(origins));
/* 310 */       this.anyOrigin = false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder()
/*     */     {
/* 319 */       this.anyOrigin = true;
/* 320 */       this.origins = Collections.emptySet();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder allowNullOrigin()
/*     */     {
/* 331 */       this.allowNullOrigin = true;
/* 332 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder disable()
/*     */     {
/* 341 */       this.enabled = false;
/* 342 */       return this;
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
/*     */     public Builder exposeHeaders(String... headers)
/*     */     {
/* 371 */       this.exposeHeaders.addAll(Arrays.asList(headers));
/* 372 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder allowCredentials()
/*     */     {
/* 391 */       this.allowCredentials = true;
/* 392 */       return this;
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
/*     */     public Builder maxAge(long max)
/*     */     {
/* 405 */       this.maxAge = max;
/* 406 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder allowedRequestMethods(HttpMethod... methods)
/*     */     {
/* 417 */       this.requestMethods.addAll(Arrays.asList(methods));
/* 418 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder allowedRequestHeaders(String... headers)
/*     */     {
/* 438 */       this.requestHeaders.addAll(Arrays.asList(headers));
/* 439 */       return this;
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
/*     */     public Builder preflightResponseHeader(CharSequence name, Object... values)
/*     */     {
/* 453 */       if (values.length == 1) {
/* 454 */         this.preflightHeaders.put(name, new CorsConfig.ConstantValueGenerator(values[0], null));
/*     */       } else {
/* 456 */         preflightResponseHeader(name, Arrays.asList(values));
/*     */       }
/* 458 */       return this;
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
/*     */     public <T> Builder preflightResponseHeader(CharSequence name, Iterable<T> value)
/*     */     {
/* 473 */       this.preflightHeaders.put(name, new CorsConfig.ConstantValueGenerator(value, null));
/* 474 */       return this;
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
/*     */ 
/*     */ 
/*     */ 
/*     */     public <T> Builder preflightResponseHeader(String name, Callable<T> valueGenerator)
/*     */     {
/* 493 */       this.preflightHeaders.put(name, valueGenerator);
/* 494 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder noPreflightResponseHeaders()
/*     */     {
/* 503 */       this.noPreflightHeaders = true;
/* 504 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CorsConfig build()
/*     */     {
/* 513 */       if ((this.preflightHeaders.isEmpty()) && (!this.noPreflightHeaders)) {
/* 514 */         this.preflightHeaders.put(HttpHeaderNames.DATE, new CorsConfig.DateValueGenerator());
/* 515 */         this.preflightHeaders.put(HttpHeaderNames.CONTENT_LENGTH, new CorsConfig.ConstantValueGenerator("0", null));
/*     */       }
/* 517 */       return new CorsConfig(this, null);
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
/*     */     public Builder shortCurcuit()
/*     */     {
/* 531 */       this.shortCurcuit = true;
/* 532 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class ConstantValueGenerator
/*     */     implements Callable<Object>
/*     */   {
/*     */     private final Object value;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private ConstantValueGenerator(Object value)
/*     */     {
/* 551 */       if (value == null) {
/* 552 */         throw new IllegalArgumentException("value must not be null");
/*     */       }
/* 554 */       this.value = value;
/*     */     }
/*     */     
/*     */     public Object call()
/*     */     {
/* 559 */       return this.value;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class DateValueGenerator
/*     */     implements Callable<Date>
/*     */   {
/*     */     public Date call()
/*     */       throws Exception
/*     */     {
/* 572 */       return new Date();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\cors\CorsConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */