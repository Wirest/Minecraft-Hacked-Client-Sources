/*     */ package io.netty.buffer;
/*     */ 
/*     */ import java.io.DataOutput;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public class ByteBufOutputStream
/*     */   extends OutputStream
/*     */   implements DataOutput
/*     */ {
/*     */   private final ByteBuf buffer;
/*     */   private final int startIndex;
/*  40 */   private final DataOutputStream utf8out = new DataOutputStream(this);
/*     */   
/*     */ 
/*     */ 
/*     */   public ByteBufOutputStream(ByteBuf buffer)
/*     */   {
/*  46 */     if (buffer == null) {
/*  47 */       throw new NullPointerException("buffer");
/*     */     }
/*  49 */     this.buffer = buffer;
/*  50 */     this.startIndex = buffer.writerIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int writtenBytes()
/*     */   {
/*  57 */     return this.buffer.writerIndex() - this.startIndex;
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException
/*     */   {
/*  62 */     if (len == 0) {
/*  63 */       return;
/*     */     }
/*     */     
/*  66 */     this.buffer.writeBytes(b, off, len);
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException
/*     */   {
/*  71 */     this.buffer.writeBytes(b);
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException
/*     */   {
/*  76 */     this.buffer.writeByte((byte)b);
/*     */   }
/*     */   
/*     */   public void writeBoolean(boolean v) throws IOException
/*     */   {
/*  81 */     write(v ? 1 : 0);
/*     */   }
/*     */   
/*     */   public void writeByte(int v) throws IOException
/*     */   {
/*  86 */     write(v);
/*     */   }
/*     */   
/*     */   public void writeBytes(String s) throws IOException
/*     */   {
/*  91 */     int len = s.length();
/*  92 */     for (int i = 0; i < len; i++) {
/*  93 */       write((byte)s.charAt(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeChar(int v) throws IOException
/*     */   {
/*  99 */     writeShort((short)v);
/*     */   }
/*     */   
/*     */   public void writeChars(String s) throws IOException
/*     */   {
/* 104 */     int len = s.length();
/* 105 */     for (int i = 0; i < len; i++) {
/* 106 */       writeChar(s.charAt(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeDouble(double v) throws IOException
/*     */   {
/* 112 */     writeLong(Double.doubleToLongBits(v));
/*     */   }
/*     */   
/*     */   public void writeFloat(float v) throws IOException
/*     */   {
/* 117 */     writeInt(Float.floatToIntBits(v));
/*     */   }
/*     */   
/*     */   public void writeInt(int v) throws IOException
/*     */   {
/* 122 */     this.buffer.writeInt(v);
/*     */   }
/*     */   
/*     */   public void writeLong(long v) throws IOException
/*     */   {
/* 127 */     this.buffer.writeLong(v);
/*     */   }
/*     */   
/*     */   public void writeShort(int v) throws IOException
/*     */   {
/* 132 */     this.buffer.writeShort((short)v);
/*     */   }
/*     */   
/*     */   public void writeUTF(String s) throws IOException
/*     */   {
/* 137 */     this.utf8out.writeUTF(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ByteBuf buffer()
/*     */   {
/* 144 */     return this.buffer;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\buffer\ByteBufOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */