/*     */ package io.netty.buffer;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteBufInputStream
/*     */   extends InputStream
/*     */   implements DataInput
/*     */ {
/*     */   private final ByteBuf buffer;
/*     */   private final int startIndex;
/*     */   private final int endIndex;
/*     */   
/*     */   public ByteBufInputStream(ByteBuf buffer)
/*     */   {
/*  52 */     this(buffer, buffer.readableBytes());
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
/*     */   public ByteBufInputStream(ByteBuf buffer, int length)
/*     */   {
/*  65 */     if (buffer == null) {
/*  66 */       throw new NullPointerException("buffer");
/*     */     }
/*  68 */     if (length < 0) {
/*  69 */       throw new IllegalArgumentException("length: " + length);
/*     */     }
/*  71 */     if (length > buffer.readableBytes()) {
/*  72 */       throw new IndexOutOfBoundsException("Too many bytes to be read - Needs " + length + ", maximum is " + buffer.readableBytes());
/*     */     }
/*     */     
/*     */ 
/*  76 */     this.buffer = buffer;
/*  77 */     this.startIndex = buffer.readerIndex();
/*  78 */     this.endIndex = (this.startIndex + length);
/*  79 */     buffer.markReaderIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int readBytes()
/*     */   {
/*  86 */     return this.buffer.readerIndex() - this.startIndex;
/*     */   }
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/*  91 */     return this.endIndex - this.buffer.readerIndex();
/*     */   }
/*     */   
/*     */   public void mark(int readlimit)
/*     */   {
/*  96 */     this.buffer.markReaderIndex();
/*     */   }
/*     */   
/*     */   public boolean markSupported()
/*     */   {
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/* 106 */     if (!this.buffer.isReadable()) {
/* 107 */       return -1;
/*     */     }
/* 109 */     return this.buffer.readByte() & 0xFF;
/*     */   }
/*     */   
/*     */   public int read(byte[] b, int off, int len) throws IOException
/*     */   {
/* 114 */     int available = available();
/* 115 */     if (available == 0) {
/* 116 */       return -1;
/*     */     }
/*     */     
/* 119 */     len = Math.min(available, len);
/* 120 */     this.buffer.readBytes(b, off, len);
/* 121 */     return len;
/*     */   }
/*     */   
/*     */   public void reset() throws IOException
/*     */   {
/* 126 */     this.buffer.resetReaderIndex();
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException
/*     */   {
/* 131 */     if (n > 2147483647L) {
/* 132 */       return skipBytes(Integer.MAX_VALUE);
/*     */     }
/* 134 */     return skipBytes((int)n);
/*     */   }
/*     */   
/*     */   public boolean readBoolean()
/*     */     throws IOException
/*     */   {
/* 140 */     checkAvailable(1);
/* 141 */     return read() != 0;
/*     */   }
/*     */   
/*     */   public byte readByte() throws IOException
/*     */   {
/* 146 */     if (!this.buffer.isReadable()) {
/* 147 */       throw new EOFException();
/*     */     }
/* 149 */     return this.buffer.readByte();
/*     */   }
/*     */   
/*     */   public char readChar() throws IOException
/*     */   {
/* 154 */     return (char)readShort();
/*     */   }
/*     */   
/*     */   public double readDouble() throws IOException
/*     */   {
/* 159 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */   
/*     */   public float readFloat() throws IOException
/*     */   {
/* 164 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b) throws IOException
/*     */   {
/* 169 */     readFully(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException
/*     */   {
/* 174 */     checkAvailable(len);
/* 175 */     this.buffer.readBytes(b, off, len);
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException
/*     */   {
/* 180 */     checkAvailable(4);
/* 181 */     return this.buffer.readInt();
/*     */   }
/*     */   
/* 184 */   private final StringBuilder lineBuf = new StringBuilder();
/*     */   
/*     */   public String readLine() throws IOException
/*     */   {
/* 188 */     this.lineBuf.setLength(0);
/*     */     for (;;)
/*     */     {
/* 191 */       if (!this.buffer.isReadable()) {
/* 192 */         return this.lineBuf.length() > 0 ? this.lineBuf.toString() : null;
/*     */       }
/*     */       
/* 195 */       int c = this.buffer.readUnsignedByte();
/* 196 */       switch (c)
/*     */       {
/*     */       case 10: 
/*     */         break;
/*     */       case 13: 
/* 201 */         if ((!this.buffer.isReadable()) || ((char)this.buffer.getUnsignedByte(this.buffer.readerIndex()) != '\n')) break;
/* 202 */         this.buffer.skipBytes(1); break;
/*     */       
/*     */ 
/*     */ 
/*     */       default: 
/* 207 */         this.lineBuf.append((char)c);
/*     */       }
/*     */       
/*     */     }
/* 211 */     return this.lineBuf.toString();
/*     */   }
/*     */   
/*     */   public long readLong() throws IOException
/*     */   {
/* 216 */     checkAvailable(8);
/* 217 */     return this.buffer.readLong();
/*     */   }
/*     */   
/*     */   public short readShort() throws IOException
/*     */   {
/* 222 */     checkAvailable(2);
/* 223 */     return this.buffer.readShort();
/*     */   }
/*     */   
/*     */   public String readUTF() throws IOException
/*     */   {
/* 228 */     return DataInputStream.readUTF(this);
/*     */   }
/*     */   
/*     */   public int readUnsignedByte() throws IOException
/*     */   {
/* 233 */     return readByte() & 0xFF;
/*     */   }
/*     */   
/*     */   public int readUnsignedShort() throws IOException
/*     */   {
/* 238 */     return readShort() & 0xFFFF;
/*     */   }
/*     */   
/*     */   public int skipBytes(int n) throws IOException
/*     */   {
/* 243 */     int nBytes = Math.min(available(), n);
/* 244 */     this.buffer.skipBytes(nBytes);
/* 245 */     return nBytes;
/*     */   }
/*     */   
/*     */   private void checkAvailable(int fieldSize) throws IOException {
/* 249 */     if (fieldSize < 0) {
/* 250 */       throw new IndexOutOfBoundsException("fieldSize cannot be a negative number");
/*     */     }
/* 252 */     if (fieldSize > available()) {
/* 253 */       throw new EOFException("fieldSize is too long! Length is " + fieldSize + ", but maximum is " + available());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\ByteBufInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */