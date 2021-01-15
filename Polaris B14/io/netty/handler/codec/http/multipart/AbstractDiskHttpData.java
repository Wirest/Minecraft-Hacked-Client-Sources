/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.channels.FileChannel;
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
/*     */ public abstract class AbstractDiskHttpData
/*     */   extends AbstractHttpData
/*     */ {
/*  40 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(AbstractDiskHttpData.class);
/*     */   private File file;
/*     */   private boolean isRenamed;
/*     */   private FileChannel fileChannel;
/*     */   
/*     */   protected AbstractDiskHttpData(String name, Charset charset, long size)
/*     */   {
/*  47 */     super(name, charset, size);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String getDiskFilename();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String getPrefix();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String getBaseDirectory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract String getPostfix();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean deleteOnExit();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private File tempFile()
/*     */     throws IOException
/*     */   {
/*  81 */     String diskFilename = getDiskFilename();
/*  82 */     String newpostfix; String newpostfix; if (diskFilename != null) {
/*  83 */       newpostfix = '_' + diskFilename;
/*     */     } else
/*  85 */       newpostfix = getPostfix();
/*     */     File tmpFile;
/*     */     File tmpFile;
/*  88 */     if (getBaseDirectory() == null)
/*     */     {
/*  90 */       tmpFile = File.createTempFile(getPrefix(), newpostfix);
/*     */     } else {
/*  92 */       tmpFile = File.createTempFile(getPrefix(), newpostfix, new File(getBaseDirectory()));
/*     */     }
/*     */     
/*  95 */     if (deleteOnExit()) {
/*  96 */       tmpFile.deleteOnExit();
/*     */     }
/*  98 */     return tmpFile;
/*     */   }
/*     */   
/*     */   public void setContent(ByteBuf buffer) throws IOException
/*     */   {
/* 103 */     if (buffer == null) {
/* 104 */       throw new NullPointerException("buffer");
/*     */     }
/*     */     try {
/* 107 */       this.size = buffer.readableBytes();
/* 108 */       checkSize(this.size);
/* 109 */       if ((this.definedSize > 0L) && (this.definedSize < this.size)) {
/* 110 */         throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
/*     */       }
/* 112 */       if (this.file == null) {
/* 113 */         this.file = tempFile();
/*     */       }
/* 115 */       if (buffer.readableBytes() == 0)
/*     */       {
/* 117 */         if (!this.file.createNewFile()) {
/* 118 */           throw new IOException("file exists already: " + this.file);
/*     */         }
/*     */       }
/*     */       else {
/* 122 */         FileOutputStream outputStream = new FileOutputStream(this.file);
/*     */         try {
/* 124 */           FileChannel localfileChannel = outputStream.getChannel();
/* 125 */           ByteBuffer byteBuffer = buffer.nioBuffer();
/* 126 */           int written = 0;
/* 127 */           while (written < this.size) {
/* 128 */             written += localfileChannel.write(byteBuffer);
/*     */           }
/* 130 */           buffer.readerIndex(buffer.readerIndex() + written);
/* 131 */           localfileChannel.force(false);
/*     */         } finally {
/* 133 */           outputStream.close();
/*     */         }
/* 135 */         setCompleted();
/*     */       }
/*     */     }
/*     */     finally {
/* 139 */       buffer.release();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last)
/*     */     throws IOException
/*     */   {
/* 146 */     if (buffer != null) {
/*     */       try {
/* 148 */         int localsize = buffer.readableBytes();
/* 149 */         checkSize(this.size + localsize);
/* 150 */         if ((this.definedSize > 0L) && (this.definedSize < this.size + localsize)) {
/* 151 */           throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
/*     */         }
/*     */         
/* 154 */         ByteBuffer byteBuffer = buffer.nioBufferCount() == 1 ? buffer.nioBuffer() : buffer.copy().nioBuffer();
/* 155 */         int written = 0;
/* 156 */         if (this.file == null) {
/* 157 */           this.file = tempFile();
/*     */         }
/* 159 */         if (this.fileChannel == null) {
/* 160 */           FileOutputStream outputStream = new FileOutputStream(this.file);
/* 161 */           this.fileChannel = outputStream.getChannel();
/*     */         }
/* 163 */         while (written < localsize) {
/* 164 */           written += this.fileChannel.write(byteBuffer);
/*     */         }
/* 166 */         this.size += localsize;
/* 167 */         buffer.readerIndex(buffer.readerIndex() + written);
/*     */       }
/*     */       finally
/*     */       {
/* 171 */         buffer.release();
/*     */       }
/*     */     }
/* 174 */     if (last) {
/* 175 */       if (this.file == null) {
/* 176 */         this.file = tempFile();
/*     */       }
/* 178 */       if (this.fileChannel == null) {
/* 179 */         FileOutputStream outputStream = new FileOutputStream(this.file);
/* 180 */         this.fileChannel = outputStream.getChannel();
/*     */       }
/* 182 */       this.fileChannel.force(false);
/* 183 */       this.fileChannel.close();
/* 184 */       this.fileChannel = null;
/* 185 */       setCompleted();
/*     */     }
/* 187 */     else if (buffer == null) {
/* 188 */       throw new NullPointerException("buffer");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setContent(File file)
/*     */     throws IOException
/*     */   {
/* 195 */     if (this.file != null) {
/* 196 */       delete();
/*     */     }
/* 198 */     this.file = file;
/* 199 */     this.size = file.length();
/* 200 */     checkSize(this.size);
/* 201 */     this.isRenamed = true;
/* 202 */     setCompleted();
/*     */   }
/*     */   
/*     */   public void setContent(InputStream inputStream) throws IOException
/*     */   {
/* 207 */     if (inputStream == null) {
/* 208 */       throw new NullPointerException("inputStream");
/*     */     }
/* 210 */     if (this.file != null) {
/* 211 */       delete();
/*     */     }
/* 213 */     this.file = tempFile();
/* 214 */     FileOutputStream outputStream = new FileOutputStream(this.file);
/* 215 */     int written = 0;
/*     */     try {
/* 217 */       FileChannel localfileChannel = outputStream.getChannel();
/* 218 */       byte[] bytes = new byte['䀀'];
/* 219 */       ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
/* 220 */       int read = inputStream.read(bytes);
/* 221 */       while (read > 0) {
/* 222 */         byteBuffer.position(read).flip();
/* 223 */         written += localfileChannel.write(byteBuffer);
/* 224 */         checkSize(written);
/* 225 */         read = inputStream.read(bytes);
/*     */       }
/* 227 */       localfileChannel.force(false);
/*     */     } finally {
/* 229 */       outputStream.close();
/*     */     }
/* 231 */     this.size = written;
/* 232 */     if ((this.definedSize > 0L) && (this.definedSize < this.size)) {
/* 233 */       if (!this.file.delete()) {
/* 234 */         logger.warn("Failed to delete: {}", this.file);
/*     */       }
/* 236 */       this.file = null;
/* 237 */       throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
/*     */     }
/* 239 */     this.isRenamed = true;
/* 240 */     setCompleted();
/*     */   }
/*     */   
/*     */   public void delete()
/*     */   {
/* 245 */     if (this.fileChannel != null) {
/*     */       try {
/* 247 */         this.fileChannel.force(false);
/* 248 */         this.fileChannel.close();
/*     */       } catch (IOException e) {
/* 250 */         logger.warn("Failed to close a file.", e);
/*     */       }
/* 252 */       this.fileChannel = null;
/*     */     }
/* 254 */     if (!this.isRenamed) {
/* 255 */       if ((this.file != null) && (this.file.exists()) && 
/* 256 */         (!this.file.delete())) {
/* 257 */         logger.warn("Failed to delete: {}", this.file);
/*     */       }
/*     */       
/* 260 */       this.file = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public byte[] get() throws IOException
/*     */   {
/* 266 */     if (this.file == null) {
/* 267 */       return EmptyArrays.EMPTY_BYTES;
/*     */     }
/* 269 */     return readFrom(this.file);
/*     */   }
/*     */   
/*     */   public ByteBuf getByteBuf() throws IOException
/*     */   {
/* 274 */     if (this.file == null) {
/* 275 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 277 */     byte[] array = readFrom(this.file);
/* 278 */     return Unpooled.wrappedBuffer(array);
/*     */   }
/*     */   
/*     */   public ByteBuf getChunk(int length) throws IOException
/*     */   {
/* 283 */     if ((this.file == null) || (length == 0)) {
/* 284 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 286 */     if (this.fileChannel == null) {
/* 287 */       FileInputStream inputStream = new FileInputStream(this.file);
/* 288 */       this.fileChannel = inputStream.getChannel();
/*     */     }
/* 290 */     int read = 0;
/* 291 */     ByteBuffer byteBuffer = ByteBuffer.allocate(length);
/* 292 */     while (read < length) {
/* 293 */       int readnow = this.fileChannel.read(byteBuffer);
/* 294 */       if (readnow == -1) {
/* 295 */         this.fileChannel.close();
/* 296 */         this.fileChannel = null;
/* 297 */         break;
/*     */       }
/* 299 */       read += readnow;
/*     */     }
/*     */     
/* 302 */     if (read == 0) {
/* 303 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 305 */     byteBuffer.flip();
/* 306 */     ByteBuf buffer = Unpooled.wrappedBuffer(byteBuffer);
/* 307 */     buffer.readerIndex(0);
/* 308 */     buffer.writerIndex(read);
/* 309 */     return buffer;
/*     */   }
/*     */   
/*     */   public String getString() throws IOException
/*     */   {
/* 314 */     return getString(HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public String getString(Charset encoding) throws IOException
/*     */   {
/* 319 */     if (this.file == null) {
/* 320 */       return "";
/*     */     }
/* 322 */     if (encoding == null) {
/* 323 */       byte[] array = readFrom(this.file);
/* 324 */       return new String(array, HttpConstants.DEFAULT_CHARSET.name());
/*     */     }
/* 326 */     byte[] array = readFrom(this.file);
/* 327 */     return new String(array, encoding.name());
/*     */   }
/*     */   
/*     */   public boolean isInMemory()
/*     */   {
/* 332 */     return false;
/*     */   }
/*     */   
/*     */   public boolean renameTo(File dest) throws IOException
/*     */   {
/* 337 */     if (dest == null) {
/* 338 */       throw new NullPointerException("dest");
/*     */     }
/* 340 */     if (this.file == null) {
/* 341 */       throw new IOException("No file defined so cannot be renamed");
/*     */     }
/* 343 */     if (!this.file.renameTo(dest))
/*     */     {
/* 345 */       IOException exception = null;
/* 346 */       FileInputStream inputStream = null;
/* 347 */       FileOutputStream outputStream = null;
/* 348 */       long chunkSize = 8196L;
/* 349 */       long position = 0L;
/*     */       try {
/* 351 */         inputStream = new FileInputStream(this.file);
/* 352 */         outputStream = new FileOutputStream(dest);
/* 353 */         FileChannel in = inputStream.getChannel();
/* 354 */         FileChannel out = outputStream.getChannel();
/* 355 */         while (position < this.size) {
/* 356 */           if (chunkSize < this.size - position) {
/* 357 */             chunkSize = this.size - position;
/*     */           }
/* 359 */           position += in.transferTo(position, chunkSize, out);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 364 */         if (inputStream != null) {
/*     */           try {
/* 366 */             inputStream.close();
/*     */           } catch (IOException e) {
/* 368 */             if (exception == null) {
/* 369 */               exception = e;
/*     */             } else {
/* 371 */               logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
/*     */             }
/*     */           }
/*     */         }
/* 375 */         if (outputStream != null) {
/*     */           try {
/* 377 */             outputStream.close();
/*     */           } catch (IOException e) {
/* 379 */             if (exception == null) {
/* 380 */               exception = e;
/*     */             } else {
/* 382 */               logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 387 */         if (exception == null) {
/*     */           break label389;
/*     */         }
/*     */       }
/*     */       catch (IOException e)
/*     */       {
/* 362 */         exception = e;
/*     */       } finally {
/* 364 */         if (inputStream != null) {
/*     */           try {
/* 366 */             inputStream.close();
/*     */           } catch (IOException e) {
/* 368 */             if (exception == null) {
/* 369 */               exception = e;
/*     */             } else {
/* 371 */               logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
/*     */             }
/*     */           }
/*     */         }
/* 375 */         if (outputStream != null) {
/*     */           try {
/* 377 */             outputStream.close();
/*     */           } catch (IOException e) {
/* 379 */             if (exception == null) {
/* 380 */               exception = e;
/*     */             } else {
/* 382 */               logger.warn("Multiple exceptions detected, the following will be suppressed {}", e);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 388 */       throw exception;
/*     */       label389:
/* 390 */       if (position == this.size) {
/* 391 */         if (!this.file.delete()) {
/* 392 */           logger.warn("Failed to delete: {}", this.file);
/*     */         }
/* 394 */         this.file = dest;
/* 395 */         this.isRenamed = true;
/* 396 */         return true;
/*     */       }
/* 398 */       if (!dest.delete()) {
/* 399 */         logger.warn("Failed to delete: {}", dest);
/*     */       }
/* 401 */       return false;
/*     */     }
/*     */     
/* 404 */     this.file = dest;
/* 405 */     this.isRenamed = true;
/* 406 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static byte[] readFrom(File src)
/*     */     throws IOException
/*     */   {
/* 414 */     long srcsize = src.length();
/* 415 */     if (srcsize > 2147483647L) {
/* 416 */       throw new IllegalArgumentException("File too big to be loaded in memory");
/*     */     }
/*     */     
/* 419 */     FileInputStream inputStream = new FileInputStream(src);
/* 420 */     byte[] array = new byte[(int)srcsize];
/*     */     try {
/* 422 */       FileChannel fileChannel = inputStream.getChannel();
/* 423 */       ByteBuffer byteBuffer = ByteBuffer.wrap(array);
/* 424 */       int read = 0;
/* 425 */       while (read < srcsize) {
/* 426 */         read += fileChannel.read(byteBuffer);
/*     */       }
/*     */     } finally {
/* 429 */       inputStream.close();
/*     */     }
/* 431 */     return array;
/*     */   }
/*     */   
/*     */   public File getFile() throws IOException
/*     */   {
/* 436 */     return this.file;
/*     */   }
/*     */   
/*     */   public HttpData touch()
/*     */   {
/* 441 */     return this;
/*     */   }
/*     */   
/*     */   public HttpData touch(Object hint)
/*     */   {
/* 446 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\AbstractDiskHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */