/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import java.io.IOException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MemoryFileUpload
/*     */   extends AbstractMemoryHttpData
/*     */   implements FileUpload
/*     */ {
/*     */   private String filename;
/*     */   private String contentType;
/*     */   private String contentTransferEncoding;
/*     */   
/*     */   public MemoryFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size)
/*     */   {
/*  41 */     super(name, charset, size);
/*  42 */     setFilename(filename);
/*  43 */     setContentType(contentType);
/*  44 */     setContentTransferEncoding(contentTransferEncoding);
/*     */   }
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType()
/*     */   {
/*  49 */     return InterfaceHttpData.HttpDataType.FileUpload;
/*     */   }
/*     */   
/*     */   public String getFilename()
/*     */   {
/*  54 */     return this.filename;
/*     */   }
/*     */   
/*     */   public void setFilename(String filename)
/*     */   {
/*  59 */     if (filename == null) {
/*  60 */       throw new NullPointerException("filename");
/*     */     }
/*  62 */     this.filename = filename;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  67 */     return getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  72 */     if (!(o instanceof Attribute)) {
/*  73 */       return false;
/*     */     }
/*  75 */     Attribute attribute = (Attribute)o;
/*  76 */     return getName().equalsIgnoreCase(attribute.getName());
/*     */   }
/*     */   
/*     */   public int compareTo(InterfaceHttpData o)
/*     */   {
/*  81 */     if (!(o instanceof FileUpload)) {
/*  82 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType());
/*     */     }
/*     */     
/*  85 */     return compareTo((FileUpload)o);
/*     */   }
/*     */   
/*     */   public int compareTo(FileUpload o)
/*     */   {
/*  90 */     int v = getName().compareToIgnoreCase(o.getName());
/*  91 */     if (v != 0) {
/*  92 */       return v;
/*     */     }
/*     */     
/*  95 */     return v;
/*     */   }
/*     */   
/*     */   public void setContentType(String contentType)
/*     */   {
/* 100 */     if (contentType == null) {
/* 101 */       throw new NullPointerException("contentType");
/*     */     }
/* 103 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/* 108 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public String getContentTransferEncoding()
/*     */   {
/* 113 */     return this.contentTransferEncoding;
/*     */   }
/*     */   
/*     */   public void setContentTransferEncoding(String contentTransferEncoding)
/*     */   {
/* 118 */     this.contentTransferEncoding = contentTransferEncoding;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 123 */     return HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + getName() + "\"; " + HttpHeaderValues.FILENAME + "=\"" + this.filename + "\"\r\n" + HttpHeaderNames.CONTENT_TYPE + ": " + this.contentType + (getCharset() != null ? "; " + HttpHeaderValues.CHARSET + '=' + getCharset() + "\r\n" : "\r\n") + HttpHeaderNames.CONTENT_LENGTH + ": " + length() + "\r\n" + "Completed: " + isCompleted() + "\r\nIsInMemory: " + isInMemory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FileUpload copy()
/*     */   {
/* 135 */     MemoryFileUpload upload = new MemoryFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
/*     */     
/* 137 */     ByteBuf buf = content();
/* 138 */     if (buf != null) {
/*     */       try {
/* 140 */         upload.setContent(buf.copy());
/* 141 */         return upload;
/*     */       } catch (IOException e) {
/* 143 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 146 */     return upload;
/*     */   }
/*     */   
/*     */   public FileUpload duplicate()
/*     */   {
/* 151 */     MemoryFileUpload upload = new MemoryFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
/*     */     
/* 153 */     ByteBuf buf = content();
/* 154 */     if (buf != null) {
/*     */       try {
/* 156 */         upload.setContent(buf.duplicate());
/* 157 */         return upload;
/*     */       } catch (IOException e) {
/* 159 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 162 */     return upload;
/*     */   }
/*     */   
/*     */   public FileUpload retain() {
/* 166 */     super.retain();
/* 167 */     return this;
/*     */   }
/*     */   
/*     */   public FileUpload retain(int increment)
/*     */   {
/* 172 */     super.retain(increment);
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public FileUpload touch()
/*     */   {
/* 178 */     super.touch();
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   public FileUpload touch(Object hint)
/*     */   {
/* 184 */     super.touch(hint);
/* 185 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\MemoryFileUpload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */