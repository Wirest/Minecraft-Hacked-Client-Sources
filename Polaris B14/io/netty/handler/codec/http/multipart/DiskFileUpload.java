/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaderValues;
/*     */ import java.io.File;
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
/*     */ public class DiskFileUpload
/*     */   extends AbstractDiskHttpData
/*     */   implements FileUpload
/*     */ {
/*     */   public static String baseDirectory;
/*  33 */   public static boolean deleteOnExitTemporaryFile = true;
/*     */   
/*     */   public static final String prefix = "FUp_";
/*     */   
/*     */   public static final String postfix = ".tmp";
/*     */   
/*     */   private String filename;
/*     */   
/*     */   private String contentType;
/*     */   
/*     */   private String contentTransferEncoding;
/*     */   
/*     */   public DiskFileUpload(String name, String filename, String contentType, String contentTransferEncoding, Charset charset, long size)
/*     */   {
/*  47 */     super(name, charset, size);
/*  48 */     setFilename(filename);
/*  49 */     setContentType(contentType);
/*  50 */     setContentTransferEncoding(contentTransferEncoding);
/*     */   }
/*     */   
/*     */   public InterfaceHttpData.HttpDataType getHttpDataType()
/*     */   {
/*  55 */     return InterfaceHttpData.HttpDataType.FileUpload;
/*     */   }
/*     */   
/*     */   public String getFilename()
/*     */   {
/*  60 */     return this.filename;
/*     */   }
/*     */   
/*     */   public void setFilename(String filename)
/*     */   {
/*  65 */     if (filename == null) {
/*  66 */       throw new NullPointerException("filename");
/*     */     }
/*  68 */     this.filename = filename;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/*  73 */     return getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  78 */     if (!(o instanceof Attribute)) {
/*  79 */       return false;
/*     */     }
/*  81 */     Attribute attribute = (Attribute)o;
/*  82 */     return getName().equalsIgnoreCase(attribute.getName());
/*     */   }
/*     */   
/*     */   public int compareTo(InterfaceHttpData o)
/*     */   {
/*  87 */     if (!(o instanceof FileUpload)) {
/*  88 */       throw new ClassCastException("Cannot compare " + getHttpDataType() + " with " + o.getHttpDataType());
/*     */     }
/*     */     
/*  91 */     return compareTo((FileUpload)o);
/*     */   }
/*     */   
/*     */   public int compareTo(FileUpload o)
/*     */   {
/*  96 */     int v = getName().compareToIgnoreCase(o.getName());
/*  97 */     if (v != 0) {
/*  98 */       return v;
/*     */     }
/*     */     
/* 101 */     return v;
/*     */   }
/*     */   
/*     */   public void setContentType(String contentType)
/*     */   {
/* 106 */     if (contentType == null) {
/* 107 */       throw new NullPointerException("contentType");
/*     */     }
/* 109 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */   public String getContentType()
/*     */   {
/* 114 */     return this.contentType;
/*     */   }
/*     */   
/*     */   public String getContentTransferEncoding()
/*     */   {
/* 119 */     return this.contentTransferEncoding;
/*     */   }
/*     */   
/*     */   public void setContentTransferEncoding(String contentTransferEncoding)
/*     */   {
/* 124 */     this.contentTransferEncoding = contentTransferEncoding;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 129 */     File file = null;
/*     */     try {
/* 131 */       file = getFile();
/*     */     }
/*     */     catch (IOException e) {}
/*     */     
/*     */ 
/* 136 */     return HttpHeaderNames.CONTENT_DISPOSITION + ": " + HttpHeaderValues.FORM_DATA + "; " + HttpHeaderValues.NAME + "=\"" + getName() + "\"; " + HttpHeaderValues.FILENAME + "=\"" + this.filename + "\"\r\n" + HttpHeaderNames.CONTENT_TYPE + ": " + this.contentType + (getCharset() != null ? "; " + HttpHeaderValues.CHARSET + '=' + getCharset() + "\r\n" : "\r\n") + HttpHeaderNames.CONTENT_LENGTH + ": " + length() + "\r\n" + "Completed: " + isCompleted() + "\r\nIsInMemory: " + isInMemory() + "\r\nRealFile: " + (file != null ? file.getAbsolutePath() : "null") + " DefaultDeleteAfter: " + deleteOnExitTemporaryFile;
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
/*     */   protected boolean deleteOnExit()
/*     */   {
/* 150 */     return deleteOnExitTemporaryFile;
/*     */   }
/*     */   
/*     */   protected String getBaseDirectory()
/*     */   {
/* 155 */     return baseDirectory;
/*     */   }
/*     */   
/*     */   protected String getDiskFilename()
/*     */   {
/* 160 */     File file = new File(this.filename);
/* 161 */     return file.getName();
/*     */   }
/*     */   
/*     */   protected String getPostfix()
/*     */   {
/* 166 */     return ".tmp";
/*     */   }
/*     */   
/*     */   protected String getPrefix()
/*     */   {
/* 171 */     return "FUp_";
/*     */   }
/*     */   
/*     */   public FileUpload copy()
/*     */   {
/* 176 */     DiskFileUpload upload = new DiskFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
/*     */     
/* 178 */     ByteBuf buf = content();
/* 179 */     if (buf != null) {
/*     */       try {
/* 181 */         upload.setContent(buf.copy());
/*     */       } catch (IOException e) {
/* 183 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 186 */     return upload;
/*     */   }
/*     */   
/*     */   public FileUpload duplicate()
/*     */   {
/* 191 */     DiskFileUpload upload = new DiskFileUpload(getName(), getFilename(), getContentType(), getContentTransferEncoding(), getCharset(), this.size);
/*     */     
/* 193 */     ByteBuf buf = content();
/* 194 */     if (buf != null) {
/*     */       try {
/* 196 */         upload.setContent(buf.duplicate());
/*     */       } catch (IOException e) {
/* 198 */         throw new ChannelException(e);
/*     */       }
/*     */     }
/* 201 */     return upload;
/*     */   }
/*     */   
/*     */   public FileUpload retain(int increment)
/*     */   {
/* 206 */     super.retain(increment);
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public FileUpload retain()
/*     */   {
/* 212 */     super.retain();
/* 213 */     return this;
/*     */   }
/*     */   
/*     */   public FileUpload touch()
/*     */   {
/* 218 */     super.touch();
/* 219 */     return this;
/*     */   }
/*     */   
/*     */   public FileUpload touch(Object hint)
/*     */   {
/* 224 */     super.touch(hint);
/* 225 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\DiskFileUpload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */