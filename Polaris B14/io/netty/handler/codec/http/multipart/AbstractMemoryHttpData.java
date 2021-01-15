/*     */ package io.netty.handler.codec.http.multipart;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.http.HttpConstants;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public abstract class AbstractMemoryHttpData
/*     */   extends AbstractHttpData
/*     */ {
/*     */   private ByteBuf byteBuf;
/*     */   private int chunkPosition;
/*     */   
/*     */   protected AbstractMemoryHttpData(String name, Charset charset, long size)
/*     */   {
/*  42 */     super(name, charset, size);
/*     */   }
/*     */   
/*     */   public void setContent(ByteBuf buffer) throws IOException
/*     */   {
/*  47 */     if (buffer == null) {
/*  48 */       throw new NullPointerException("buffer");
/*     */     }
/*  50 */     long localsize = buffer.readableBytes();
/*  51 */     checkSize(localsize);
/*  52 */     if ((this.definedSize > 0L) && (this.definedSize < localsize)) {
/*  53 */       throw new IOException("Out of size: " + localsize + " > " + this.definedSize);
/*     */     }
/*     */     
/*  56 */     if (this.byteBuf != null) {
/*  57 */       this.byteBuf.release();
/*     */     }
/*  59 */     this.byteBuf = buffer;
/*  60 */     this.size = localsize;
/*  61 */     setCompleted();
/*     */   }
/*     */   
/*     */   public void setContent(InputStream inputStream) throws IOException
/*     */   {
/*  66 */     if (inputStream == null) {
/*  67 */       throw new NullPointerException("inputStream");
/*     */     }
/*  69 */     ByteBuf buffer = Unpooled.buffer();
/*  70 */     byte[] bytes = new byte['䀀'];
/*  71 */     int read = inputStream.read(bytes);
/*  72 */     int written = 0;
/*  73 */     while (read > 0) {
/*  74 */       buffer.writeBytes(bytes, 0, read);
/*  75 */       written += read;
/*  76 */       checkSize(written);
/*  77 */       read = inputStream.read(bytes);
/*     */     }
/*  79 */     this.size = written;
/*  80 */     if ((this.definedSize > 0L) && (this.definedSize < this.size)) {
/*  81 */       throw new IOException("Out of size: " + this.size + " > " + this.definedSize);
/*     */     }
/*  83 */     if (this.byteBuf != null) {
/*  84 */       this.byteBuf.release();
/*     */     }
/*  86 */     this.byteBuf = buffer;
/*  87 */     setCompleted();
/*     */   }
/*     */   
/*     */   public void addContent(ByteBuf buffer, boolean last)
/*     */     throws IOException
/*     */   {
/*  93 */     if (buffer != null) {
/*  94 */       long localsize = buffer.readableBytes();
/*  95 */       checkSize(this.size + localsize);
/*  96 */       if ((this.definedSize > 0L) && (this.definedSize < this.size + localsize)) {
/*  97 */         throw new IOException("Out of size: " + (this.size + localsize) + " > " + this.definedSize);
/*     */       }
/*     */       
/* 100 */       this.size += localsize;
/* 101 */       if (this.byteBuf == null) {
/* 102 */         this.byteBuf = buffer;
/* 103 */       } else if ((this.byteBuf instanceof CompositeByteBuf)) {
/* 104 */         CompositeByteBuf cbb = (CompositeByteBuf)this.byteBuf;
/* 105 */         cbb.addComponent(buffer);
/* 106 */         cbb.writerIndex(cbb.writerIndex() + buffer.readableBytes());
/*     */       } else {
/* 108 */         CompositeByteBuf cbb = Unpooled.compositeBuffer(Integer.MAX_VALUE);
/* 109 */         cbb.addComponents(new ByteBuf[] { this.byteBuf, buffer });
/* 110 */         cbb.writerIndex(this.byteBuf.readableBytes() + buffer.readableBytes());
/* 111 */         this.byteBuf = cbb;
/*     */       }
/*     */     }
/* 114 */     if (last) {
/* 115 */       setCompleted();
/*     */     }
/* 117 */     else if (buffer == null) {
/* 118 */       throw new NullPointerException("buffer");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setContent(File file)
/*     */     throws IOException
/*     */   {
/* 125 */     if (file == null) {
/* 126 */       throw new NullPointerException("file");
/*     */     }
/* 128 */     long newsize = file.length();
/* 129 */     if (newsize > 2147483647L) {
/* 130 */       throw new IllegalArgumentException("File too big to be loaded in memory");
/*     */     }
/*     */     
/* 133 */     checkSize(newsize);
/* 134 */     FileInputStream inputStream = new FileInputStream(file);
/* 135 */     FileChannel fileChannel = inputStream.getChannel();
/* 136 */     byte[] array = new byte[(int)newsize];
/* 137 */     ByteBuffer byteBuffer = ByteBuffer.wrap(array);
/* 138 */     int read = 0;
/* 139 */     while (read < newsize) {
/* 140 */       read += fileChannel.read(byteBuffer);
/*     */     }
/* 142 */     fileChannel.close();
/* 143 */     inputStream.close();
/* 144 */     byteBuffer.flip();
/* 145 */     if (this.byteBuf != null) {
/* 146 */       this.byteBuf.release();
/*     */     }
/* 148 */     this.byteBuf = Unpooled.wrappedBuffer(Integer.MAX_VALUE, new ByteBuffer[] { byteBuffer });
/* 149 */     this.size = newsize;
/* 150 */     setCompleted();
/*     */   }
/*     */   
/*     */   public void delete()
/*     */   {
/* 155 */     if (this.byteBuf != null) {
/* 156 */       this.byteBuf.release();
/* 157 */       this.byteBuf = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public byte[] get()
/*     */   {
/* 163 */     if (this.byteBuf == null) {
/* 164 */       return Unpooled.EMPTY_BUFFER.array();
/*     */     }
/* 166 */     byte[] array = new byte[this.byteBuf.readableBytes()];
/* 167 */     this.byteBuf.getBytes(this.byteBuf.readerIndex(), array);
/* 168 */     return array;
/*     */   }
/*     */   
/*     */   public String getString()
/*     */   {
/* 173 */     return getString(HttpConstants.DEFAULT_CHARSET);
/*     */   }
/*     */   
/*     */   public String getString(Charset encoding)
/*     */   {
/* 178 */     if (this.byteBuf == null) {
/* 179 */       return "";
/*     */     }
/* 181 */     if (encoding == null) {
/* 182 */       encoding = HttpConstants.DEFAULT_CHARSET;
/*     */     }
/* 184 */     return this.byteBuf.toString(encoding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuf getByteBuf()
/*     */   {
/* 194 */     return this.byteBuf;
/*     */   }
/*     */   
/*     */   public ByteBuf getChunk(int length) throws IOException
/*     */   {
/* 199 */     if ((this.byteBuf == null) || (length == 0) || (this.byteBuf.readableBytes() == 0)) {
/* 200 */       this.chunkPosition = 0;
/* 201 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 203 */     int sizeLeft = this.byteBuf.readableBytes() - this.chunkPosition;
/* 204 */     if (sizeLeft == 0) {
/* 205 */       this.chunkPosition = 0;
/* 206 */       return Unpooled.EMPTY_BUFFER;
/*     */     }
/* 208 */     int sliceLength = length;
/* 209 */     if (sizeLeft < length) {
/* 210 */       sliceLength = sizeLeft;
/*     */     }
/* 212 */     ByteBuf chunk = this.byteBuf.slice(this.chunkPosition, sliceLength).retain();
/* 213 */     this.chunkPosition += sliceLength;
/* 214 */     return chunk;
/*     */   }
/*     */   
/*     */   public boolean isInMemory()
/*     */   {
/* 219 */     return true;
/*     */   }
/*     */   
/*     */   public boolean renameTo(File dest) throws IOException
/*     */   {
/* 224 */     if (dest == null) {
/* 225 */       throw new NullPointerException("dest");
/*     */     }
/* 227 */     if (this.byteBuf == null)
/*     */     {
/* 229 */       if (!dest.createNewFile()) {
/* 230 */         throw new IOException("file exists already: " + dest);
/*     */       }
/* 232 */       return true;
/*     */     }
/* 234 */     int length = this.byteBuf.readableBytes();
/* 235 */     FileOutputStream outputStream = new FileOutputStream(dest);
/* 236 */     FileChannel fileChannel = outputStream.getChannel();
/* 237 */     int written = 0;
/* 238 */     if (this.byteBuf.nioBufferCount() == 1) {
/* 239 */       ByteBuffer byteBuffer = this.byteBuf.nioBuffer();
/* 240 */       while (written < length) {
/* 241 */         written += fileChannel.write(byteBuffer);
/*     */       }
/*     */     } else {
/* 244 */       ByteBuffer[] byteBuffers = this.byteBuf.nioBuffers();
/* 245 */       while (written < length) {
/* 246 */         written = (int)(written + fileChannel.write(byteBuffers));
/*     */       }
/*     */     }
/*     */     
/* 250 */     fileChannel.force(false);
/* 251 */     fileChannel.close();
/* 252 */     outputStream.close();
/* 253 */     return written == length;
/*     */   }
/*     */   
/*     */   public File getFile() throws IOException
/*     */   {
/* 258 */     throw new IOException("Not represented by a file");
/*     */   }
/*     */   
/*     */   public HttpData touch()
/*     */   {
/* 263 */     return touch(null);
/*     */   }
/*     */   
/*     */   public HttpData touch(Object hint)
/*     */   {
/* 268 */     if (this.byteBuf != null) {
/* 269 */       this.byteBuf.touch(hint);
/*     */     }
/* 271 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\AbstractMemoryHttpData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */