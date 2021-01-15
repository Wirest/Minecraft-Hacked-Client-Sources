/*     */ package io.netty.channel.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.FileRegion;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.Channels;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.nio.channels.NotYetConnectedException;
/*     */ import java.nio.channels.WritableByteChannel;
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
/*     */ public abstract class OioByteStreamChannel
/*     */   extends AbstractOioByteChannel
/*     */ {
/*  36 */   private static final InputStream CLOSED_IN = new InputStream()
/*     */   {
/*     */     public int read() {
/*  39 */       return -1;
/*     */     }
/*     */   };
/*     */   
/*  43 */   private static final OutputStream CLOSED_OUT = new OutputStream()
/*     */   {
/*     */     public void write(int b) throws IOException {
/*  46 */       throw new ClosedChannelException();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */   private InputStream is;
/*     */   
/*     */ 
/*     */   private OutputStream os;
/*     */   
/*     */   private WritableByteChannel outChannel;
/*     */   
/*     */ 
/*     */   protected OioByteStreamChannel(Channel parent)
/*     */   {
/*  61 */     super(parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void activate(InputStream is, OutputStream os)
/*     */   {
/*  68 */     if (this.is != null) {
/*  69 */       throw new IllegalStateException("input was set already");
/*     */     }
/*  71 */     if (this.os != null) {
/*  72 */       throw new IllegalStateException("output was set already");
/*     */     }
/*  74 */     if (is == null) {
/*  75 */       throw new NullPointerException("is");
/*     */     }
/*  77 */     if (os == null) {
/*  78 */       throw new NullPointerException("os");
/*     */     }
/*  80 */     this.is = is;
/*  81 */     this.os = os;
/*     */   }
/*     */   
/*     */   public boolean isActive()
/*     */   {
/*  86 */     InputStream is = this.is;
/*  87 */     if ((is == null) || (is == CLOSED_IN)) {
/*  88 */       return false;
/*     */     }
/*     */     
/*  91 */     OutputStream os = this.os;
/*  92 */     return (os != null) && (os != CLOSED_OUT);
/*     */   }
/*     */   
/*     */   protected int available()
/*     */   {
/*     */     try {
/*  98 */       return this.is.available();
/*     */     } catch (IOException ignored) {}
/* 100 */     return 0;
/*     */   }
/*     */   
/*     */   protected int doReadBytes(ByteBuf buf)
/*     */     throws Exception
/*     */   {
/* 106 */     int length = Math.max(1, Math.min(available(), buf.maxWritableBytes()));
/* 107 */     return buf.writeBytes(this.is, length);
/*     */   }
/*     */   
/*     */   protected void doWriteBytes(ByteBuf buf) throws Exception
/*     */   {
/* 112 */     OutputStream os = this.os;
/* 113 */     if (os == null) {
/* 114 */       throw new NotYetConnectedException();
/*     */     }
/* 116 */     buf.readBytes(os, buf.readableBytes());
/*     */   }
/*     */   
/*     */   protected void doWriteFileRegion(FileRegion region) throws Exception
/*     */   {
/* 121 */     OutputStream os = this.os;
/* 122 */     if (os == null) {
/* 123 */       throw new NotYetConnectedException();
/*     */     }
/* 125 */     if (this.outChannel == null) {
/* 126 */       this.outChannel = Channels.newChannel(os);
/*     */     }
/*     */     
/* 129 */     long written = 0L;
/*     */     for (;;) {
/* 131 */       long localWritten = region.transferTo(this.outChannel, written);
/* 132 */       if (localWritten == -1L) {
/* 133 */         checkEOF(region);
/* 134 */         return;
/*     */       }
/* 136 */       written += localWritten;
/*     */       
/* 138 */       if (written >= region.count()) {
/* 139 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void checkEOF(FileRegion region) throws IOException {
/* 145 */     if (region.transfered() < region.count()) {
/* 146 */       throw new EOFException("Expected to be able to write " + region.count() + " bytes, " + "but only wrote " + region.transfered());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doClose()
/*     */     throws Exception
/*     */   {
/* 153 */     InputStream is = this.is;
/* 154 */     OutputStream os = this.os;
/* 155 */     this.is = CLOSED_IN;
/* 156 */     this.os = CLOSED_OUT;
/*     */     try
/*     */     {
/* 159 */       if (is != null) {
/* 160 */         is.close();
/*     */       }
/*     */     } finally {
/* 163 */       if (os != null) {
/* 164 */         os.close();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\oio\OioByteStreamChannel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */