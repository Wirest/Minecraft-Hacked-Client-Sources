/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class MixedAttribute
/*     */   implements Attribute
/*     */ {
/*     */   private Attribute attribute;
/*     */   private final long limitSize;
/*  33 */   private long maxSize = -1L;
/*     */   
/*     */   public MixedAttribute(String name, long limitSize) {
/*  36 */     this(name, limitSize, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public MixedAttribute(String name, long limitSize, Charset charset) {
/*  40 */     this.limitSize = limitSize;
/*  41 */     this.attribute = new MemoryAttribute(name, charset);
/*     */   }
/*     */   
/*     */   public MixedAttribute(String name, String value, long limitSize) {
/*  45 */     this(name, value, limitSize, HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public MixedAttribute(String name, String value, long limitSize, Charset charset) {
/*  49 */     this.limitSize = limitSize;
/*  50 */     if (value.length() > this.limitSize) {
/*     */       try {
/*  52 */         this.attribute = new DiskAttribute(name, value, charset);
/*     */       }
/*     */       catch (IOException e) {
/*     */         try {
/*  56 */           this.attribute = new MemoryAttribute(name, value, charset);
/*     */         } catch (IOException ignore) {
/*  58 */           throw new IllegalArgumentException(e);
/*     */         }
/*     */       }
/*     */     } else {
/*     */       try {
/*  63 */         this.attribute = new MemoryAttribute(name, value, charset);
/*     */       } catch (IOException e) {
/*  65 */         throw new IllegalArgumentException(e);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public long getMaxSize()
/*     */   {
/*  72 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   public void setMaxSize(long maxSize)
/*     */   {
/*  77 */     this.maxSize = maxSize;
/*  78 */     this.attribute.setMaxSize(maxSize);
/*     */   }
/*     */   
/*     */   public void checkSize(long newSize) throws IOException
/*     */   {
/*  83 */     if ((this.maxSize >= 0L) && (newSize > this.maxSize)) {
/*  84 */       throw new IOException("Size exceed allowed maximum capacity");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last) throws IOException
/*     */   {
/*  90 */     if ((this.attribute instanceof MemoryAttribute)) {
/*  91 */       checkSize(this.attribute.length() + buffer.readableBytes());
/*  92 */       if (this.attribute.length() + buffer.readableBytes() > this.limitSize) {
/*  93 */         DiskAttribute diskAttribute = new DiskAttribute(this.attribute.getName());
/*     */         
/*  95 */         diskAttribute.setMaxSize(this.maxSize);
/*  96 */         if (((MemoryAttribute)this.attribute).getByteBuf() != null) {
/*  97 */           diskAttribute.addContent(((MemoryAttribute)this.attribute).getByteBuf(), false);
/*     */         }
/*     */         
/* 100 */         this.attribute = diskAttribute;
/*     */       }
/*     */     }
/* 103 */     this.attribute.addContent(buffer, last);
/*     */   }
/*     */   
/*     */   public void delete()
/*     */   {
/* 108 */     this.attribute.delete();
/*     */   }
/*     */   
/*     */   public byte[] get() throws IOException
/*     */   {
/* 113 */     return this.attribute.get();
/*     */   }
/*     */   
/*     */   public ByteBuf getByteBuf() throws IOException
/*     */   {
/* 118 */     return this.attribute.getByteBuf();
/*     */   }
/*     */   
/*     */   public Charset getCharset()
/*     */   {
/* 123 */     return this.attribute.getCharset();
/*     */   }
/*     */   
/*     */   public String getString() throws IOException
/*     */   {
/* 128 */     return this.attribute.getString();
/*     */   }
/*     */   
/*     */   public String getString(Charset encoding) throws IOException
/*     */   {
/* 133 */     return this.attribute.getString(encoding);
/*     */   }
/*     */   
/*     */   public boolean isCompleted()
/*     */   {
/* 138 */     return this.attribute.isCompleted();
/*     */   }
/*     */   
/*     */   public boolean isInMemory()
/*     */   {
/* 143 */     return this.attribute.isInMemory();
/*     */   }
/*     */   
/*     */   public long length()
/*     */   {
/* 148 */     return this.attribute.length();
/*     */   }
/*     */   
/*     */   public boolean renameTo(File dest) throws IOException
/*     */   {
/* 153 */     return this.attribute.renameTo(dest);
/*     */   }
/*     */   
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 158 */     this.attribute.setCharset(charset);
/*     */   }
/*     */   
/*     */   public void setContent(ByteBuf buffer) throws IOException
/*     */   {
/* 163 */     checkSize(buffer.readableBytes());
/* 164 */     if ((buffer.readableBytes() > this.limitSize) && 
/* 165 */       ((this.attribute instanceof MemoryAttribute)))
/*     */     {
/* 167 */       this.attribute = new DiskAttribute(this.attribute.getName());
/* 168 */       this.attribute.setMaxSize(this.maxSize);
/*     */     }
/*     */     
/* 171 */     this.attribute.setContent(buffer);
/*     */   }
/*     */   
/*     */   public void setContent(File file) throws IOException
/*     */   {
/* 176 */     checkSize(file.length());
/* 177 */     if ((file.length() > this.limitSize) && 
/* 178 */       ((this.attribute instanceof MemoryAttribute)))
/*     */     {
/* 180 */       this.attribute = new DiskAttribute(this.attribute.getName());
/* 181 */       this.attribute.setMaxSize(this.maxSize);
/*     */     }
/*     */     
/* 184 */     this.attribute.setContent(file);
/*     */   }
/*     */   
/*     */   public void setContent(InputStream inputStream) throws IOException
/*     */   {
/* 189 */     if ((this.attribute instanceof MemoryAttribute))
/*     */     {
/* 191 */       this.attribute = new DiskAttribute(this.attribute.getName());
/* 192 */       this.attribute.setMaxSize(this.maxSize);
/*     */     }
/* 194 */     this.attribute.setContent(inputStream);
/*     */   }
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType()
/*     */   {
/* 199 */     return this.attribute.getHttpDataType();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 204 */     return this.attribute.getName();
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 209 */     return this.attribute.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 214 */     return this.attribute.equals(obj);
/*     */   }
/*     */   
/*     */   public int compareTo(InterfaceHttpData o)
/*     */   {
/* 219 */     return this.attribute.compareTo(o);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 224 */     return "Mixed: " + this.attribute;
/*     */   }
/*     */   
/*     */   public String getValue() throws IOException
/*     */   {
/* 229 */     return this.attribute.getValue();
/*     */   }
/*     */   
/*     */   public void setValue(String value) throws IOException
/*     */   {
/* 234 */     if (value != null) {
/* 235 */       checkSize(value.getBytes().length);
/*     */     }
/* 237 */     this.attribute.setValue(value);
/*     */   }
/*     */   
/*     */   public ByteBuf getChunk(int length) throws IOException
/*     */   {
/* 242 */     return this.attribute.getChunk(length);
/*     */   }
/*     */   
/*     */   public File getFile() throws IOException
/*     */   {
/* 247 */     return this.attribute.getFile();
/*     */   }
/*     */   
/*     */   public Attribute copy()
/*     */   {
/* 252 */     return this.attribute.copy();
/*     */   }
/*     */   
/*     */   public Attribute duplicate()
/*     */   {
/* 257 */     return this.attribute.duplicate();
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/* 262 */     return this.attribute.content();
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/* 267 */     return this.attribute.refCnt();
/*     */   }
/*     */   
/*     */   public Attribute retain()
/*     */   {
/* 272 */     this.attribute.retain();
/* 273 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute retain(int increment)
/*     */   {
/* 278 */     this.attribute.retain(increment);
/* 279 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute touch()
/*     */   {
/* 284 */     this.attribute.touch();
/* 285 */     return this;
/*     */   }
/*     */   
/*     */   public Attribute touch(Object hint)
/*     */   {
/* 290 */     this.attribute.touch(hint);
/* 291 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 296 */     return this.attribute.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 301 */     return this.attribute.release(decrement);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\MixedAttribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */