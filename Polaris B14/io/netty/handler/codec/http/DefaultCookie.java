/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultCookie
/*     */   implements Cookie
/*     */ {
/*     */   private final String name;
/*     */   private String value;
/*     */   private String rawValue;
/*     */   private String domain;
/*     */   private String path;
/*     */   private String comment;
/*     */   private String commentUrl;
/*     */   private boolean discard;
/*  37 */   private Set<Integer> ports = Collections.emptySet();
/*  38 */   private Set<Integer> unmodifiablePorts = this.ports;
/*  39 */   private long maxAge = Long.MIN_VALUE;
/*     */   
/*     */   private int version;
/*     */   
/*     */   private boolean secure;
/*     */   private boolean httpOnly;
/*     */   
/*     */   public DefaultCookie(String name, String value)
/*     */   {
/*  48 */     if (name == null) {
/*  49 */       throw new NullPointerException("name");
/*     */     }
/*  51 */     name = name.trim();
/*  52 */     if (name.isEmpty()) {
/*  53 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/*  56 */     for (int i = 0; i < name.length(); i++) {
/*  57 */       char c = name.charAt(i);
/*  58 */       if (c > '') {
/*  59 */         throw new IllegalArgumentException("name contains non-ascii character: " + name);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  64 */       switch (c) {
/*     */       case '\t': case '\n': case '\013': case '\f': case '\r': 
/*     */       case ' ': case ',': case ';': case '=': 
/*  67 */         throw new IllegalArgumentException("name contains one of the following prohibited characters: =,; \\t\\r\\n\\v\\f: " + name);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*  73 */     if (name.charAt(0) == '$') {
/*  74 */       throw new IllegalArgumentException("name starting with '$' not allowed: " + name);
/*     */     }
/*     */     
/*  77 */     this.name = name;
/*  78 */     setValue(value);
/*     */   }
/*     */   
/*     */   public String name()
/*     */   {
/*  83 */     return this.name;
/*     */   }
/*     */   
/*     */   public String value()
/*     */   {
/*  88 */     return this.value;
/*     */   }
/*     */   
/*     */   public void setValue(String value)
/*     */   {
/*  93 */     if (value == null) {
/*  94 */       throw new NullPointerException("value");
/*     */     }
/*  96 */     this.value = value;
/*     */   }
/*     */   
/*     */   public String rawValue()
/*     */   {
/* 101 */     return this.rawValue;
/*     */   }
/*     */   
/*     */   public void setRawValue(String rawValue)
/*     */   {
/* 106 */     if (this.value == null) {
/* 107 */       throw new NullPointerException("rawValue");
/*     */     }
/* 109 */     this.rawValue = rawValue;
/*     */   }
/*     */   
/*     */   public String domain()
/*     */   {
/* 114 */     return this.domain;
/*     */   }
/*     */   
/*     */   public void setDomain(String domain)
/*     */   {
/* 119 */     this.domain = validateValue("domain", domain);
/*     */   }
/*     */   
/*     */   public String path()
/*     */   {
/* 124 */     return this.path;
/*     */   }
/*     */   
/*     */   public void setPath(String path)
/*     */   {
/* 129 */     this.path = validateValue("path", path);
/*     */   }
/*     */   
/*     */   public String comment()
/*     */   {
/* 134 */     return this.comment;
/*     */   }
/*     */   
/*     */   public void setComment(String comment)
/*     */   {
/* 139 */     this.comment = validateValue("comment", comment);
/*     */   }
/*     */   
/*     */   public String commentUrl()
/*     */   {
/* 144 */     return this.commentUrl;
/*     */   }
/*     */   
/*     */   public void setCommentUrl(String commentUrl)
/*     */   {
/* 149 */     this.commentUrl = validateValue("commentUrl", commentUrl);
/*     */   }
/*     */   
/*     */   public boolean isDiscard()
/*     */   {
/* 154 */     return this.discard;
/*     */   }
/*     */   
/*     */   public void setDiscard(boolean discard)
/*     */   {
/* 159 */     this.discard = discard;
/*     */   }
/*     */   
/*     */   public Set<Integer> ports()
/*     */   {
/* 164 */     if (this.unmodifiablePorts == null) {
/* 165 */       this.unmodifiablePorts = Collections.unmodifiableSet(this.ports);
/*     */     }
/* 167 */     return this.unmodifiablePorts;
/*     */   }
/*     */   
/*     */   public void setPorts(int... ports)
/*     */   {
/* 172 */     if (ports == null) {
/* 173 */       throw new NullPointerException("ports");
/*     */     }
/*     */     
/* 176 */     int[] portsCopy = (int[])ports.clone();
/* 177 */     if (portsCopy.length == 0) {
/* 178 */       this.unmodifiablePorts = (this.ports = Collections.emptySet());
/*     */     } else {
/* 180 */       Set<Integer> newPorts = new TreeSet();
/* 181 */       for (int p : portsCopy) {
/* 182 */         if ((p <= 0) || (p > 65535)) {
/* 183 */           throw new IllegalArgumentException("port out of range: " + p);
/*     */         }
/* 185 */         newPorts.add(Integer.valueOf(p));
/*     */       }
/* 187 */       this.ports = newPorts;
/* 188 */       this.unmodifiablePorts = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPorts(Iterable<Integer> ports)
/*     */   {
/* 194 */     Set<Integer> newPorts = new TreeSet();
/* 195 */     for (Iterator i$ = ports.iterator(); i$.hasNext();) { int p = ((Integer)i$.next()).intValue();
/* 196 */       if ((p <= 0) || (p > 65535)) {
/* 197 */         throw new IllegalArgumentException("port out of range: " + p);
/*     */       }
/* 199 */       newPorts.add(Integer.valueOf(p));
/*     */     }
/* 201 */     if (newPorts.isEmpty()) {
/* 202 */       this.unmodifiablePorts = (this.ports = Collections.emptySet());
/*     */     } else {
/* 204 */       this.ports = newPorts;
/* 205 */       this.unmodifiablePorts = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public long maxAge()
/*     */   {
/* 211 */     return this.maxAge;
/*     */   }
/*     */   
/*     */   public void setMaxAge(long maxAge)
/*     */   {
/* 216 */     this.maxAge = maxAge;
/*     */   }
/*     */   
/*     */   public int version()
/*     */   {
/* 221 */     return this.version;
/*     */   }
/*     */   
/*     */   public void setVersion(int version)
/*     */   {
/* 226 */     this.version = version;
/*     */   }
/*     */   
/*     */   public boolean isSecure()
/*     */   {
/* 231 */     return this.secure;
/*     */   }
/*     */   
/*     */   public void setSecure(boolean secure)
/*     */   {
/* 236 */     this.secure = secure;
/*     */   }
/*     */   
/*     */   public boolean isHttpOnly()
/*     */   {
/* 241 */     return this.httpOnly;
/*     */   }
/*     */   
/*     */   public void setHttpOnly(boolean httpOnly)
/*     */   {
/* 246 */     this.httpOnly = httpOnly;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 251 */     return name().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 256 */     if (!(o instanceof Cookie)) {
/* 257 */       return false;
/*     */     }
/*     */     
/* 260 */     Cookie that = (Cookie)o;
/* 261 */     if (!name().equalsIgnoreCase(that.name())) {
/* 262 */       return false;
/*     */     }
/*     */     
/* 265 */     if (path() == null) {
/* 266 */       if (that.path() != null)
/* 267 */         return false;
/*     */     } else {
/* 269 */       if (that.path() == null)
/* 270 */         return false;
/* 271 */       if (!path().equals(that.path())) {
/* 272 */         return false;
/*     */       }
/*     */     }
/* 275 */     if (domain() == null) {
/* 276 */       if (that.domain() != null)
/* 277 */         return false;
/*     */     } else {
/* 279 */       if (that.domain() == null) {
/* 280 */         return false;
/*     */       }
/* 282 */       return domain().equalsIgnoreCase(that.domain());
/*     */     }
/*     */     
/* 285 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public int compareTo(Cookie c)
/*     */   {
/* 291 */     int v = name().compareToIgnoreCase(c.name());
/* 292 */     if (v != 0) {
/* 293 */       return v;
/*     */     }
/*     */     
/* 296 */     if (path() == null) {
/* 297 */       if (c.path() != null)
/* 298 */         return -1;
/*     */     } else {
/* 300 */       if (c.path() == null) {
/* 301 */         return 1;
/*     */       }
/* 303 */       v = path().compareTo(c.path());
/* 304 */       if (v != 0) {
/* 305 */         return v;
/*     */       }
/*     */     }
/*     */     
/* 309 */     if (domain() == null) {
/* 310 */       if (c.domain() != null)
/* 311 */         return -1;
/*     */     } else {
/* 313 */       if (c.domain() == null) {
/* 314 */         return 1;
/*     */       }
/* 316 */       v = domain().compareToIgnoreCase(c.domain());
/* 317 */       return v;
/*     */     }
/*     */     
/* 320 */     return 0;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 325 */     StringBuilder buf = new StringBuilder().append(name()).append('=').append(value());
/*     */     
/*     */ 
/*     */ 
/* 329 */     if (domain() != null) {
/* 330 */       buf.append(", domain=").append(domain());
/*     */     }
/*     */     
/* 333 */     if (path() != null) {
/* 334 */       buf.append(", path=").append(path());
/*     */     }
/*     */     
/* 337 */     if (comment() != null) {
/* 338 */       buf.append(", comment=").append(comment());
/*     */     }
/*     */     
/* 341 */     if (maxAge() >= 0L) {
/* 342 */       buf.append(", maxAge=").append(maxAge()).append('s');
/*     */     }
/*     */     
/*     */ 
/* 346 */     if (isSecure()) {
/* 347 */       buf.append(", secure");
/*     */     }
/* 349 */     if (isHttpOnly()) {
/* 350 */       buf.append(", HTTPOnly");
/*     */     }
/* 352 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private static String validateValue(String name, String value) {
/* 356 */     if (value == null) {
/* 357 */       return null;
/*     */     }
/* 359 */     value = value.trim();
/* 360 */     if (value.isEmpty()) {
/* 361 */       return null;
/*     */     }
/* 363 */     for (int i = 0; i < value.length(); i++) {
/* 364 */       char c = value.charAt(i);
/* 365 */       switch (c) {
/*     */       case '\n': case '\013': case '\f': case '\r': case ';': 
/* 367 */         throw new IllegalArgumentException(name + " contains one of the following prohibited characters: " + ";\\r\\n\\f\\v (" + value + ')');
/*     */       }
/*     */       
/*     */     }
/*     */     
/* 372 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultCookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */