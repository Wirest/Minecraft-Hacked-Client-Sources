/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelOutboundBuffer;
/*     */ import io.netty.channel.ChannelOutboundBuffer.MessageProcessor;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
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
/*     */ final class NativeDatagramPacketArray
/*     */   implements ChannelOutboundBuffer.MessageProcessor
/*     */ {
/*  32 */   private static final FastThreadLocal<NativeDatagramPacketArray> ARRAY = new FastThreadLocal()
/*     */   {
/*     */     protected NativeDatagramPacketArray initialValue() throws Exception
/*     */     {
/*  36 */       return new NativeDatagramPacketArray(null);
/*     */     }
/*     */     
/*     */     protected void onRemoval(NativeDatagramPacketArray value) throws Exception
/*     */     {
/*  41 */       NativeDatagramPacketArray.NativeDatagramPacket[] array = value.packets;
/*     */       
/*  43 */       for (int i = 0; i < array.length; i++) {
/*  44 */         array[i].release();
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*  50 */   private final NativeDatagramPacket[] packets = new NativeDatagramPacket[Native.UIO_MAX_IOV];
/*     */   private int count;
/*     */   
/*     */   private NativeDatagramPacketArray() {
/*  54 */     for (int i = 0; i < this.packets.length; i++) {
/*  55 */       this.packets[i] = new NativeDatagramPacket();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean add(DatagramPacket packet)
/*     */   {
/*  64 */     if (this.count == this.packets.length) {
/*  65 */       return false;
/*     */     }
/*  67 */     ByteBuf content = (ByteBuf)packet.content();
/*  68 */     int len = content.readableBytes();
/*  69 */     if (len == 0) {
/*  70 */       return true;
/*     */     }
/*  72 */     NativeDatagramPacket p = this.packets[this.count];
/*  73 */     InetSocketAddress recipient = (InetSocketAddress)packet.recipient();
/*  74 */     if (!p.init(content, recipient)) {
/*  75 */       return false;
/*     */     }
/*     */     
/*  78 */     this.count += 1;
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public boolean processMessage(Object msg) throws Exception
/*     */   {
/*  84 */     return ((msg instanceof DatagramPacket)) && (add((DatagramPacket)msg));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int count()
/*     */   {
/*  91 */     return this.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   NativeDatagramPacket[] packets()
/*     */   {
/*  98 */     return this.packets;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static NativeDatagramPacketArray getInstance(ChannelOutboundBuffer buffer)
/*     */     throws Exception
/*     */   {
/* 106 */     NativeDatagramPacketArray array = (NativeDatagramPacketArray)ARRAY.get();
/* 107 */     array.count = 0;
/* 108 */     buffer.forEachFlushedMessage(array);
/* 109 */     return array;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class NativeDatagramPacket
/*     */   {
/* 120 */     private final IovArray array = new IovArray();
/*     */     
/*     */     private long memoryAddress;
/*     */     
/*     */     private int count;
/*     */     private byte[] addr;
/*     */     private int scopeId;
/*     */     private int port;
/*     */     
/*     */     private void release()
/*     */     {
/* 131 */       this.array.release();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean init(ByteBuf buf, InetSocketAddress recipient)
/*     */     {
/* 138 */       this.array.clear();
/* 139 */       if (!this.array.add(buf)) {
/* 140 */         return false;
/*     */       }
/*     */       
/* 143 */       this.memoryAddress = this.array.memoryAddress(0);
/* 144 */       this.count = this.array.count();
/*     */       
/* 146 */       InetAddress address = recipient.getAddress();
/* 147 */       if ((address instanceof Inet6Address)) {
/* 148 */         this.addr = address.getAddress();
/* 149 */         this.scopeId = ((Inet6Address)address).getScopeId();
/*     */       } else {
/* 151 */         this.addr = Native.ipv4MappedIpv6Address(address.getAddress());
/* 152 */         this.scopeId = 0;
/*     */       }
/* 154 */       this.port = recipient.getPort();
/* 155 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\NativeDatagramPacketArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */