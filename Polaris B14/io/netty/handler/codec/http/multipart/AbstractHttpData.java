/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.util.AbstractReferenceCounted;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.Charset;
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
/*     */ public abstract class AbstractHttpData
/*     */   extends AbstractReferenceCounted
/*     */   implements HttpData
/*     */ {
/*  32 */   private static final Pattern STRIP_PATTERN = Pattern.compile("(?:^\\s+|\\s+$|\\n)");
/*  33 */   private static final Pattern REPLACE_PATTERN = Pattern.compile("[\\r\\t]");
/*     */   
/*     */   private final String name;
/*     */   protected long definedSize;
/*     */   protected long size;
/*  38 */   private Charset charset = HttpConstants.DEFAULT_CHARSET;
/*     */   private boolean completed;
/*  40 */   private long maxSize = -1L;
/*     */   
/*     */   protected AbstractHttpData(String name, Charset charset, long size) {
/*  43 */     if (name == null) {
/*  44 */       throw new NullPointerException("name");
/*     */     }
/*     */     
/*  47 */     name = REPLACE_PATTERN.matcher(name).replaceAll(" ");
/*  48 */     name = STRIP_PATTERN.matcher(name).replaceAll("");
/*     */     
/*  50 */     if (name.isEmpty()) {
/*  51 */       throw new IllegalArgumentException("empty name");
/*     */     }
/*     */     
/*  54 */     this.name = name;
/*  55 */     if (charset != null) {
/*  56 */       setCharset(charset);
/*     */     }
/*  58 */     this.definedSize = size;
/*     */   }
/*     */   
/*     */   public long getMaxSize() {
/*  62 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   public void setMaxSize(long maxSize) {
/*  66 */     this.maxSize = maxSize;
/*     */   }
/*     */   
/*     */   public void checkSize(long newSize) throws IOException
/*     */   {
/*  71 */     if ((this.maxSize >= 0L) && (newSize > this.maxSize)) {
/*  72 */       throw new IOException("Size exceed allowed maximum capacity");
/*     */     }
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  78 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isCompleted()
/*     */   {
/*  83 */     return this.completed;
/*     */   }
/*     */   
/*     */   protected void setCompleted() {
/*  87 */     this.completed = true;
/*     */   }
/*     */   
/*     */   public Charset getCharset()
/*     */   {
/*  92 */     return this.charset;
/*     */   }
/*     */   
/*     */   public void setCharset(Charset charset)
/*     */   {
/*  97 */     if (charset == null) {
/*  98 */       throw new NullPointerException("charset");
/*     */     }
/* 100 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public long length()
/*     */   {
/* 105 */     return this.size;
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/*     */     try {
/* 111 */       return getByteBuf();
/*     */     } catch (IOException e) {
/* 113 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void deallocate()
/*     */   {
/* 119 */     delete();
/*     */   }
/*     */   
/*     */   public HttpData retain()
/*     */   {
/* 124 */     super.retain();
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public HttpData retain(int increment)
/*     */   {
/* 130 */     super.retain(increment);
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public abstract HttpData touch();
/*     */   
/*     */   public abstract HttpData touch(Object paramObject);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\AbstractHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */