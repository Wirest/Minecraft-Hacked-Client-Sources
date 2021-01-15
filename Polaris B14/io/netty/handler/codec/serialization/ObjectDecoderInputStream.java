/*     */ package io.netty.handler.codec.serialization;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.StreamCorruptedException;
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
/*     */ public class ObjectDecoderInputStream
/*     */   extends InputStream
/*     */   implements ObjectInput
/*     */ {
/*     */   private final DataInputStream in;
/*     */   private final int maxObjectSize;
/*     */   private final ClassResolver classResolver;
/*     */   
/*     */   public ObjectDecoderInputStream(InputStream in)
/*     */   {
/*  44 */     this(in, null);
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
/*     */   public ObjectDecoderInputStream(InputStream in, ClassLoader classLoader)
/*     */   {
/*  58 */     this(in, classLoader, 1048576);
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
/*     */   public ObjectDecoderInputStream(InputStream in, int maxObjectSize)
/*     */   {
/*  73 */     this(in, null, maxObjectSize);
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
/*     */   public ObjectDecoderInputStream(InputStream in, ClassLoader classLoader, int maxObjectSize)
/*     */   {
/*  91 */     if (in == null) {
/*  92 */       throw new NullPointerException("in");
/*     */     }
/*  94 */     if (maxObjectSize <= 0) {
/*  95 */       throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
/*     */     }
/*  97 */     if ((in instanceof DataInputStream)) {
/*  98 */       this.in = ((DataInputStream)in);
/*     */     } else {
/* 100 */       this.in = new DataInputStream(in);
/*     */     }
/* 102 */     this.classResolver = ClassResolvers.weakCachingResolver(classLoader);
/* 103 */     this.maxObjectSize = maxObjectSize;
/*     */   }
/*     */   
/*     */   public Object readObject() throws ClassNotFoundException, IOException
/*     */   {
/* 108 */     int dataLen = readInt();
/* 109 */     if (dataLen <= 0) {
/* 110 */       throw new StreamCorruptedException("invalid data length: " + dataLen);
/*     */     }
/* 112 */     if (dataLen > this.maxObjectSize) {
/* 113 */       throw new StreamCorruptedException("data length too big: " + dataLen + " (max: " + this.maxObjectSize + ')');
/*     */     }
/*     */     
/*     */ 
/* 117 */     return new CompactObjectInputStream(this.in, this.classResolver).readObject();
/*     */   }
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/* 122 */     return this.in.available();
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 127 */     this.in.close();
/*     */   }
/*     */   
/*     */   public void mark(int readlimit)
/*     */   {
/* 132 */     this.in.mark(readlimit);
/*     */   }
/*     */   
/*     */   public boolean markSupported()
/*     */   {
/* 137 */     return this.in.markSupported();
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/* 142 */     return this.in.read();
/*     */   }
/*     */   
/*     */   public final int read(byte[] b, int off, int len) throws IOException
/*     */   {
/* 147 */     return this.in.read(b, off, len);
/*     */   }
/*     */   
/*     */   public final int read(byte[] b) throws IOException
/*     */   {
/* 152 */     return this.in.read(b);
/*     */   }
/*     */   
/*     */   public final boolean readBoolean() throws IOException
/*     */   {
/* 157 */     return this.in.readBoolean();
/*     */   }
/*     */   
/*     */   public final byte readByte() throws IOException
/*     */   {
/* 162 */     return this.in.readByte();
/*     */   }
/*     */   
/*     */   public final char readChar() throws IOException
/*     */   {
/* 167 */     return this.in.readChar();
/*     */   }
/*     */   
/*     */   public final double readDouble() throws IOException
/*     */   {
/* 172 */     return this.in.readDouble();
/*     */   }
/*     */   
/*     */   public final float readFloat() throws IOException
/*     */   {
/* 177 */     return this.in.readFloat();
/*     */   }
/*     */   
/*     */   public final void readFully(byte[] b, int off, int len) throws IOException
/*     */   {
/* 182 */     this.in.readFully(b, off, len);
/*     */   }
/*     */   
/*     */   public final void readFully(byte[] b) throws IOException
/*     */   {
/* 187 */     this.in.readFully(b);
/*     */   }
/*     */   
/*     */   public final int readInt() throws IOException
/*     */   {
/* 192 */     return this.in.readInt();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final String readLine()
/*     */     throws IOException
/*     */   {
/* 201 */     return this.in.readLine();
/*     */   }
/*     */   
/*     */   public final long readLong() throws IOException
/*     */   {
/* 206 */     return this.in.readLong();
/*     */   }
/*     */   
/*     */   public final short readShort() throws IOException
/*     */   {
/* 211 */     return this.in.readShort();
/*     */   }
/*     */   
/*     */   public final int readUnsignedByte() throws IOException
/*     */   {
/* 216 */     return this.in.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public final int readUnsignedShort() throws IOException
/*     */   {
/* 221 */     return this.in.readUnsignedShort();
/*     */   }
/*     */   
/*     */   public final String readUTF() throws IOException
/*     */   {
/* 226 */     return this.in.readUTF();
/*     */   }
/*     */   
/*     */   public void reset() throws IOException
/*     */   {
/* 231 */     this.in.reset();
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException
/*     */   {
/* 236 */     return this.in.skip(n);
/*     */   }
/*     */   
/*     */   public final int skipBytes(int n) throws IOException
/*     */   {
/* 241 */     return this.in.skipBytes(n);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\ObjectDecoderInputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */