/*     */ package io.netty.handler.codec.protobuf;
/*     */ 
/*     */ import com.google.protobuf.ExtensionRegistry;
/*     */ import com.google.protobuf.ExtensionRegistryLite;
/*     */ import com.google.protobuf.MessageLite;
/*     */ import com.google.protobuf.MessageLite.Builder;
/*     */ import com.google.protobuf.Parser;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandler.Sharable;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.MessageToMessageDecoder;
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
/*     */ @ChannelHandler.Sharable
/*     */ public class ProtobufDecoder
/*     */   extends MessageToMessageDecoder<ByteBuf>
/*     */ {
/*     */   private static final boolean HAS_PARSER;
/*     */   private final MessageLite prototype;
/*     */   private final ExtensionRegistryLite extensionRegistry;
/*     */   
/*     */   static
/*     */   {
/*  69 */     boolean hasParser = false;
/*     */     try
/*     */     {
/*  72 */       MessageLite.class.getDeclaredMethod("getParserForType", new Class[0]);
/*  73 */       hasParser = true;
/*     */     }
/*     */     catch (Throwable t) {}
/*     */     
/*     */ 
/*  78 */     HAS_PARSER = hasParser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ProtobufDecoder(MessageLite prototype)
/*     */   {
/*  88 */     this(prototype, null);
/*     */   }
/*     */   
/*     */   public ProtobufDecoder(MessageLite prototype, ExtensionRegistry extensionRegistry) {
/*  92 */     this(prototype, extensionRegistry);
/*     */   }
/*     */   
/*     */   public ProtobufDecoder(MessageLite prototype, ExtensionRegistryLite extensionRegistry) {
/*  96 */     if (prototype == null) {
/*  97 */       throw new NullPointerException("prototype");
/*     */     }
/*  99 */     this.prototype = prototype.getDefaultInstanceForType();
/* 100 */     this.extensionRegistry = extensionRegistry;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 107 */     int length = msg.readableBytes();
/* 108 */     int offset; byte[] array; int offset; if (msg.hasArray()) {
/* 109 */       byte[] array = msg.array();
/* 110 */       offset = msg.arrayOffset() + msg.readerIndex();
/*     */     } else {
/* 112 */       array = new byte[length];
/* 113 */       msg.getBytes(msg.readerIndex(), array, 0, length);
/* 114 */       offset = 0;
/*     */     }
/*     */     
/* 117 */     if (this.extensionRegistry == null) {
/* 118 */       if (HAS_PARSER) {
/* 119 */         out.add(this.prototype.getParserForType().parseFrom(array, offset, length));
/*     */       } else {
/* 121 */         out.add(this.prototype.newBuilderForType().mergeFrom(array, offset, length).build());
/*     */       }
/*     */     }
/* 124 */     else if (HAS_PARSER) {
/* 125 */       out.add(this.prototype.getParserForType().parseFrom(array, offset, length, this.extensionRegistry));
/*     */     } else {
/* 127 */       out.add(this.prototype.newBuilderForType().mergeFrom(array, offset, length, this.extensionRegistry).build());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\protobuf\ProtobufDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */