/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.handler.codec.CorruptedFrameException;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.List;
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
/*     */ @ChannelHandler.Sharable
/*     */ public class DnsResponseDecoder
/*     */   extends MessageToMessageDecoder<DatagramPacket>
/*     */ {
/*     */   protected void decode(ChannelHandlerContext ctx, DatagramPacket packet, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  38 */     ByteBuf buf = (ByteBuf)packet.content();
/*     */     
/*  40 */     int id = buf.readUnsignedShort();
/*     */     
/*  42 */     DnsResponse response = new DnsResponse(id, (InetSocketAddress)packet.sender());
/*  43 */     DnsResponseHeader header = response.header();
/*  44 */     int flags = buf.readUnsignedShort();
/*  45 */     header.setType(flags >> 15);
/*  46 */     header.setOpcode(flags >> 11 & 0xF);
/*  47 */     header.setRecursionDesired((flags >> 8 & 0x1) == 1);
/*  48 */     header.setAuthoritativeAnswer((flags >> 10 & 0x1) == 1);
/*  49 */     header.setTruncated((flags >> 9 & 0x1) == 1);
/*  50 */     header.setRecursionAvailable((flags >> 7 & 0x1) == 1);
/*  51 */     header.setZ(flags >> 4 & 0x7);
/*  52 */     header.setResponseCode(DnsResponseCode.valueOf(flags & 0xF));
/*     */     
/*  54 */     int questions = buf.readUnsignedShort();
/*  55 */     int answers = buf.readUnsignedShort();
/*  56 */     int authorities = buf.readUnsignedShort();
/*  57 */     int additionals = buf.readUnsignedShort();
/*     */     
/*  59 */     for (int i = 0; i < questions; i++) {
/*  60 */       response.addQuestion(decodeQuestion(buf));
/*     */     }
/*  62 */     if (header.responseCode() != DnsResponseCode.NOERROR)
/*     */     {
/*  64 */       out.add(response);
/*  65 */       return;
/*     */     }
/*  67 */     boolean release = true;
/*     */     try {
/*  69 */       for (int i = 0; i < answers; i++) {
/*  70 */         response.addAnswer(decodeResource(buf));
/*     */       }
/*  72 */       for (int i = 0; i < authorities; i++) {
/*  73 */         response.addAuthorityResource(decodeResource(buf));
/*     */       }
/*  75 */       for (int i = 0; i < additionals; i++) {
/*  76 */         response.addAdditionalResource(decodeResource(buf));
/*     */       }
/*  78 */       out.add(response);
/*  79 */       release = false;
/*     */     } finally {
/*  81 */       if (release)
/*     */       {
/*  83 */         releaseDnsResources(response.answers());
/*  84 */         releaseDnsResources(response.authorityResources());
/*  85 */         releaseDnsResources(response.additionalResources());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void releaseDnsResources(List<DnsResource> resources) {
/*  91 */     int size = resources.size();
/*  92 */     for (int i = 0; i < size; i++) {
/*  93 */       DnsResource resource = (DnsResource)resources.get(i);
/*  94 */       resource.release();
/*     */     }
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
/*     */   private static String readName(ByteBuf buf)
/*     */   {
/* 108 */     int position = -1;
/* 109 */     int checked = 0;
/* 110 */     int length = buf.writerIndex();
/* 111 */     StringBuilder name = new StringBuilder();
/* 112 */     for (int len = buf.readUnsignedByte(); (buf.isReadable()) && (len != 0); len = buf.readUnsignedByte()) {
/* 113 */       boolean pointer = (len & 0xC0) == 192;
/* 114 */       if (pointer) {
/* 115 */         if (position == -1) {
/* 116 */           position = buf.readerIndex() + 1;
/*     */         }
/* 118 */         buf.readerIndex((len & 0x3F) << 8 | buf.readUnsignedByte());
/*     */         
/* 120 */         checked += 2;
/* 121 */         if (checked >= length) {
/* 122 */           throw new CorruptedFrameException("name contains a loop.");
/*     */         }
/*     */       } else {
/* 125 */         name.append(buf.toString(buf.readerIndex(), len, CharsetUtil.UTF_8)).append('.');
/* 126 */         buf.skipBytes(len);
/*     */       }
/*     */     }
/* 129 */     if (position != -1) {
/* 130 */       buf.readerIndex(position);
/*     */     }
/* 132 */     if (name.length() == 0) {
/* 133 */       return "";
/*     */     }
/*     */     
/* 136 */     return name.substring(0, name.length() - 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static DnsQuestion decodeQuestion(ByteBuf buf)
/*     */   {
/* 147 */     String name = readName(buf);
/* 148 */     DnsType type = DnsType.valueOf(buf.readUnsignedShort());
/* 149 */     DnsClass qClass = DnsClass.valueOf(buf.readUnsignedShort());
/* 150 */     return new DnsQuestion(name, type, qClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static DnsResource decodeResource(ByteBuf buf)
/*     */   {
/* 161 */     String name = readName(buf);
/* 162 */     DnsType type = DnsType.valueOf(buf.readUnsignedShort());
/* 163 */     DnsClass aClass = DnsClass.valueOf(buf.readUnsignedShort());
/* 164 */     long ttl = buf.readUnsignedInt();
/* 165 */     int len = buf.readUnsignedShort();
/*     */     
/* 167 */     int readerIndex = buf.readerIndex();
/* 168 */     ByteBuf payload = buf.duplicate().setIndex(readerIndex, readerIndex + len).retain();
/* 169 */     buf.readerIndex(readerIndex + len);
/* 170 */     return new DnsResource(name, type, aClass, ttl, payload);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */