/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.CompositeByteBuf;
/*     */ import io.netty.channel.ChannelOutboundBuffer.MessageProcessor;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.nio.ByteBuffer;
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
/*     */ final class IovArray
/*     */   implements ChannelOutboundBuffer.MessageProcessor
/*     */ {
/*  45 */   private static final int ADDRESS_SIZE = PlatformDependent.addressSize();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   private static final int IOV_SIZE = 2 * ADDRESS_SIZE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   private static final int CAPACITY = Native.IOV_MAX * IOV_SIZE;
/*     */   private final long memoryAddress;
/*     */   private int count;
/*     */   private long size;
/*     */   
/*     */   IovArray()
/*     */   {
/*  64 */     this.memoryAddress = PlatformDependent.allocateMemory(CAPACITY);
/*     */   }
/*     */   
/*     */   void clear() {
/*  68 */     this.count = 0;
/*  69 */     this.size = 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean add(ByteBuf buf)
/*     */   {
/*  77 */     if (this.count == Native.IOV_MAX)
/*     */     {
/*  79 */       return false;
/*     */     }
/*     */     
/*  82 */     int len = buf.readableBytes();
/*  83 */     if (len == 0)
/*     */     {
/*     */ 
/*     */ 
/*  87 */       return true;
/*     */     }
/*     */     
/*  90 */     long addr = buf.memoryAddress();
/*  91 */     int offset = buf.readerIndex();
/*  92 */     add(addr, offset, len);
/*  93 */     return true;
/*     */   }
/*     */   
/*     */   private void add(long addr, int offset, int len) {
/*  97 */     if (len == 0)
/*     */     {
/*  99 */       return;
/*     */     }
/*     */     
/* 102 */     long baseOffset = memoryAddress(this.count++);
/* 103 */     long lengthOffset = baseOffset + ADDRESS_SIZE;
/*     */     
/* 105 */     if (ADDRESS_SIZE == 8)
/*     */     {
/* 107 */       PlatformDependent.putLong(baseOffset, addr + offset);
/* 108 */       PlatformDependent.putLong(lengthOffset, len);
/*     */     } else {
/* 110 */       assert (ADDRESS_SIZE == 4);
/* 111 */       PlatformDependent.putInt(baseOffset, (int)addr + offset);
/* 112 */       PlatformDependent.putInt(lengthOffset, len);
/*     */     }
/*     */     
/* 115 */     this.size += len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean add(CompositeByteBuf buf)
/*     */   {
/* 123 */     ByteBuffer[] buffers = buf.nioBuffers();
/* 124 */     if (this.count + buffers.length >= Native.IOV_MAX)
/*     */     {
/* 126 */       return false;
/*     */     }
/* 128 */     for (int i = 0; i < buffers.length; i++) {
/* 129 */       ByteBuffer nioBuffer = buffers[i];
/* 130 */       int offset = nioBuffer.position();
/* 131 */       int len = nioBuffer.limit() - nioBuffer.position();
/* 132 */       if (len != 0)
/*     */       {
/*     */ 
/*     */ 
/* 136 */         long addr = PlatformDependent.directBufferAddress(nioBuffer);
/*     */         
/* 138 */         add(addr, offset, len);
/*     */       } }
/* 140 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   long processWritten(int index, long written)
/*     */   {
/* 148 */     long baseOffset = memoryAddress(index);
/* 149 */     long lengthOffset = baseOffset + ADDRESS_SIZE;
/* 150 */     if (ADDRESS_SIZE == 8)
/*     */     {
/* 152 */       long len = PlatformDependent.getLong(lengthOffset);
/* 153 */       if (len > written) {
/* 154 */         long offset = PlatformDependent.getLong(baseOffset);
/* 155 */         PlatformDependent.putLong(baseOffset, offset + written);
/* 156 */         PlatformDependent.putLong(lengthOffset, len - written);
/* 157 */         return -1L;
/*     */       }
/* 159 */       return len;
/*     */     }
/* 161 */     assert (ADDRESS_SIZE == 4);
/* 162 */     long len = PlatformDependent.getInt(lengthOffset);
/* 163 */     if (len > written) {
/* 164 */       int offset = PlatformDependent.getInt(baseOffset);
/* 165 */       PlatformDependent.putInt(baseOffset, (int)(offset + written));
/* 166 */       PlatformDependent.putInt(lengthOffset, (int)(len - written));
/* 167 */       return -1L;
/*     */     }
/* 169 */     return len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int count()
/*     */   {
/* 177 */     return this.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long size()
/*     */   {
/* 184 */     return this.size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long memoryAddress(int offset)
/*     */   {
/* 191 */     return this.memoryAddress + IOV_SIZE * offset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void release()
/*     */   {
/* 198 */     PlatformDependent.freeMemory(this.memoryAddress);
/*     */   }
/*     */   
/*     */   public boolean processMessage(Object msg) throws Exception
/*     */   {
/* 203 */     if ((msg instanceof ByteBuf)) {
/* 204 */       if ((msg instanceof CompositeByteBuf)) {
/* 205 */         return add((CompositeByteBuf)msg);
/*     */       }
/* 207 */       return add((ByteBuf)msg);
/*     */     }
/*     */     
/* 210 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\IovArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */