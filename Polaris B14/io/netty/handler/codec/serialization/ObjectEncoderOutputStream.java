/*     */ package io.netty.handler.codec.serialization;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufOutputStream;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.ObjectOutputStream;
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
/*     */ public class ObjectEncoderOutputStream
/*     */   extends OutputStream
/*     */   implements ObjectOutput
/*     */ {
/*     */   private final DataOutputStream out;
/*     */   private final int estimatedLength;
/*     */   
/*     */   public ObjectEncoderOutputStream(OutputStream out)
/*     */   {
/*  47 */     this(out, 512);
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
/*     */ 
/*     */   public ObjectEncoderOutputStream(OutputStream out, int estimatedLength)
/*     */   {
/*  66 */     if (out == null) {
/*  67 */       throw new NullPointerException("out");
/*     */     }
/*  69 */     if (estimatedLength < 0) {
/*  70 */       throw new IllegalArgumentException("estimatedLength: " + estimatedLength);
/*     */     }
/*     */     
/*  73 */     if ((out instanceof DataOutputStream)) {
/*  74 */       this.out = ((DataOutputStream)out);
/*     */     } else {
/*  76 */       this.out = new DataOutputStream(out);
/*     */     }
/*  78 */     this.estimatedLength = estimatedLength;
/*     */   }
/*     */   
/*     */   public void writeObject(Object obj) throws IOException
/*     */   {
/*  83 */     ByteBufOutputStream bout = new ByteBufOutputStream(Unpooled.buffer(this.estimatedLength));
/*  84 */     ObjectOutputStream oout = new CompactObjectOutputStream(bout);
/*  85 */     oout.writeObject(obj);
/*  86 */     oout.flush();
/*  87 */     oout.close();
/*     */     
/*  89 */     ByteBuf buffer = bout.buffer();
/*  90 */     int objectSize = buffer.readableBytes();
/*  91 */     writeInt(objectSize);
/*  92 */     buffer.getBytes(0, this, objectSize);
/*     */   }
/*     */   
/*     */   public void write(int b) throws IOException
/*     */   {
/*  97 */     this.out.write(b);
/*     */   }
/*     */   
/*     */   public void close() throws IOException
/*     */   {
/* 102 */     this.out.close();
/*     */   }
/*     */   
/*     */   public void flush() throws IOException
/*     */   {
/* 107 */     this.out.flush();
/*     */   }
/*     */   
/*     */   public final int size() {
/* 111 */     return this.out.size();
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException
/*     */   {
/* 116 */     this.out.write(b, off, len);
/*     */   }
/*     */   
/*     */   public void write(byte[] b) throws IOException
/*     */   {
/* 121 */     this.out.write(b);
/*     */   }
/*     */   
/*     */   public final void writeBoolean(boolean v) throws IOException
/*     */   {
/* 126 */     this.out.writeBoolean(v);
/*     */   }
/*     */   
/*     */   public final void writeByte(int v) throws IOException
/*     */   {
/* 131 */     this.out.writeByte(v);
/*     */   }
/*     */   
/*     */   public final void writeBytes(String s) throws IOException
/*     */   {
/* 136 */     this.out.writeBytes(s);
/*     */   }
/*     */   
/*     */   public final void writeChar(int v) throws IOException
/*     */   {
/* 141 */     this.out.writeChar(v);
/*     */   }
/*     */   
/*     */   public final void writeChars(String s) throws IOException
/*     */   {
/* 146 */     this.out.writeChars(s);
/*     */   }
/*     */   
/*     */   public final void writeDouble(double v) throws IOException
/*     */   {
/* 151 */     this.out.writeDouble(v);
/*     */   }
/*     */   
/*     */   public final void writeFloat(float v) throws IOException
/*     */   {
/* 156 */     this.out.writeFloat(v);
/*     */   }
/*     */   
/*     */   public final void writeInt(int v) throws IOException
/*     */   {
/* 161 */     this.out.writeInt(v);
/*     */   }
/*     */   
/*     */   public final void writeLong(long v) throws IOException
/*     */   {
/* 166 */     this.out.writeLong(v);
/*     */   }
/*     */   
/*     */   public final void writeShort(int v) throws IOException
/*     */   {
/* 171 */     this.out.writeShort(v);
/*     */   }
/*     */   
/*     */   public final void writeUTF(String str) throws IOException
/*     */   {
/* 176 */     this.out.writeUTF(str);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\ObjectEncoderOutputStream.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */