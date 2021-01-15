/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.socket.DatagramPacket;
/*     */ import io.netty.handler.codec.MessageToMessageEncoder;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.nio.charset.Charset;
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
/*     */ public class DnsQueryEncoder
/*     */   extends MessageToMessageEncoder<DnsQuery>
/*     */ {
/*     */   protected void encode(ChannelHandlerContext ctx, DnsQuery query, List<Object> out)
/*     */     throws Exception
/*     */   {
/*  39 */     ByteBuf buf = ctx.alloc().buffer();
/*  40 */     encodeHeader(query.header(), buf);
/*  41 */     List<DnsQuestion> questions = query.questions();
/*  42 */     for (DnsQuestion question : questions) {
/*  43 */       encodeQuestion(question, CharsetUtil.US_ASCII, buf);
/*     */     }
/*  45 */     for (DnsResource resource : query.additionalResources()) {
/*  46 */       encodeResource(resource, CharsetUtil.US_ASCII, buf);
/*     */     }
/*  48 */     out.add(new DatagramPacket(buf, query.recipient(), null));
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
/*     */   private static void encodeHeader(DnsHeader header, ByteBuf buf)
/*     */   {
/*  61 */     buf.writeShort(header.id());
/*  62 */     int flags = 0;
/*  63 */     flags |= header.type() << 15;
/*  64 */     flags |= header.opcode() << 14;
/*  65 */     flags |= (header.isRecursionDesired() ? 256 : 0);
/*  66 */     buf.writeShort(flags);
/*  67 */     buf.writeShort(header.questionCount());
/*  68 */     buf.writeShort(0);
/*  69 */     buf.writeShort(0);
/*  70 */     buf.writeShort(header.additionalResourceCount());
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
/*     */   private static void encodeQuestion(DnsQuestion question, Charset charset, ByteBuf buf)
/*     */   {
/*  85 */     encodeName(question.name(), charset, buf);
/*  86 */     buf.writeShort(question.type().intValue());
/*  87 */     buf.writeShort(question.dnsClass().intValue());
/*     */   }
/*     */   
/*     */   private static void encodeResource(DnsResource resource, Charset charset, ByteBuf buf) {
/*  91 */     encodeName(resource.name(), charset, buf);
/*     */     
/*  93 */     buf.writeShort(resource.type().intValue());
/*  94 */     buf.writeShort(resource.dnsClass().intValue());
/*  95 */     buf.writeInt((int)resource.timeToLive());
/*     */     
/*  97 */     ByteBuf content = resource.content();
/*  98 */     int contentLen = content.readableBytes();
/*     */     
/* 100 */     buf.writeShort(contentLen);
/* 101 */     buf.writeBytes(content, content.readerIndex(), contentLen);
/*     */   }
/*     */   
/*     */   private static void encodeName(String name, Charset charset, ByteBuf buf) {
/* 105 */     String[] parts = StringUtil.split(name, '.');
/* 106 */     for (String part : parts) {
/* 107 */       int partLen = part.length();
/* 108 */       if (partLen != 0)
/*     */       {
/*     */ 
/* 111 */         buf.writeByte(partLen);
/* 112 */         buf.writeBytes(part.getBytes(charset));
/*     */       } }
/* 114 */     buf.writeByte(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsQueryEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */