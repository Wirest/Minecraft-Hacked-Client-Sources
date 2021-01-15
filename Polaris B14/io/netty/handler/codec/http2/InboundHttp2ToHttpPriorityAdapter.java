/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.TextHeaders.EntryVisitor;
/*     */ import io.netty.handler.codec.http.DefaultHttpHeaders;
/*     */ import io.netty.handler.codec.http.FullHttpMessage;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpMethod;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.util.collection.IntObjectHashMap;
/*     */ import io.netty.util.collection.IntObjectMap;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.Map.Entry;
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
/*     */ public final class InboundHttp2ToHttpPriorityAdapter
/*     */   extends InboundHttp2ToHttpAdapter
/*     */ {
/*  38 */   private static final AsciiString OUT_OF_MESSAGE_SEQUENCE_METHOD = new AsciiString(HttpUtil.OUT_OF_MESSAGE_SEQUENCE_METHOD.toString());
/*     */   
/*  40 */   private static final AsciiString OUT_OF_MESSAGE_SEQUENCE_PATH = new AsciiString("");
/*     */   
/*  42 */   private static final AsciiString OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE = new AsciiString(HttpUtil.OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE.toString());
/*     */   
/*     */ 
/*     */   private final IntObjectMap<HttpHeaders> outOfMessageFlowHeaders;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class Builder
/*     */     extends InboundHttp2ToHttpAdapter.Builder
/*     */   {
/*     */     public Builder(Http2Connection connection)
/*     */     {
/*  54 */       super();
/*     */     }
/*     */     
/*     */     public InboundHttp2ToHttpPriorityAdapter build()
/*     */     {
/*  59 */       InboundHttp2ToHttpPriorityAdapter instance = new InboundHttp2ToHttpPriorityAdapter(this);
/*  60 */       instance.connection.addListener(instance);
/*  61 */       return instance;
/*     */     }
/*     */   }
/*     */   
/*     */   InboundHttp2ToHttpPriorityAdapter(Builder builder) {
/*  66 */     super(builder);
/*  67 */     this.outOfMessageFlowHeaders = new IntObjectHashMap();
/*     */   }
/*     */   
/*     */   protected void removeMessage(int streamId)
/*     */   {
/*  72 */     super.removeMessage(streamId);
/*  73 */     this.outOfMessageFlowHeaders.remove(streamId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static HttpHeaders getActiveHeaders(FullHttpMessage msg)
/*     */   {
/*  82 */     return msg.content().isReadable() ? msg.trailingHeaders() : msg.headers();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void importOutOfMessageFlowHeaders(int streamId, HttpHeaders headers)
/*     */   {
/*  91 */     HttpHeaders outOfMessageFlowHeader = (HttpHeaders)this.outOfMessageFlowHeaders.get(streamId);
/*  92 */     if (outOfMessageFlowHeader == null) {
/*  93 */       this.outOfMessageFlowHeaders.put(streamId, headers);
/*     */     } else {
/*  95 */       outOfMessageFlowHeader.setAll(headers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void exportOutOfMessageFlowHeaders(int streamId, HttpHeaders headers)
/*     */   {
/* 105 */     HttpHeaders outOfMessageFlowHeader = (HttpHeaders)this.outOfMessageFlowHeaders.get(streamId);
/* 106 */     if (outOfMessageFlowHeader != null) {
/* 107 */       headers.setAll(outOfMessageFlowHeader);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void removePriorityRelatedHeaders(HttpHeaders headers)
/*     */   {
/* 116 */     headers.remove(HttpUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text());
/* 117 */     headers.remove(HttpUtil.ExtensionHeaderNames.STREAM_WEIGHT.text());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void initializePseudoHeaders(Http2Headers headers)
/*     */   {
/* 125 */     if (this.connection.isServer()) {
/* 126 */       headers.method(OUT_OF_MESSAGE_SEQUENCE_METHOD).path(OUT_OF_MESSAGE_SEQUENCE_PATH);
/*     */     } else {
/* 128 */       headers.status(OUT_OF_MESSAGE_SEQUENCE_RETURN_CODE);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addHttpHeadersToHttp2Headers(HttpHeaders httpHeaders, Http2Headers http2Headers)
/*     */   {
/*     */     try
/*     */     {
/* 139 */       httpHeaders.forEachEntry(new TextHeaders.EntryVisitor()
/*     */       {
/*     */         public boolean visit(Map.Entry<CharSequence, CharSequence> entry) throws Exception {
/* 142 */           this.val$http2Headers.add(AsciiString.of((CharSequence)entry.getKey()), AsciiString.of((CharSequence)entry.getValue()));
/* 143 */           return true;
/*     */         }
/*     */       });
/*     */     } catch (Exception ex) {
/* 147 */       PlatformDependent.throwException(ex);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void fireChannelRead(ChannelHandlerContext ctx, FullHttpMessage msg, int streamId)
/*     */   {
/* 153 */     exportOutOfMessageFlowHeaders(streamId, getActiveHeaders(msg));
/* 154 */     super.fireChannelRead(ctx, msg, streamId);
/*     */   }
/*     */   
/*     */   protected FullHttpMessage processHeadersBegin(ChannelHandlerContext ctx, int streamId, Http2Headers headers, boolean endOfStream, boolean allowAppend, boolean appendToTrailer)
/*     */     throws Http2Exception
/*     */   {
/* 160 */     FullHttpMessage msg = super.processHeadersBegin(ctx, streamId, headers, endOfStream, allowAppend, appendToTrailer);
/*     */     
/* 162 */     if (msg != null) {
/* 163 */       exportOutOfMessageFlowHeaders(streamId, getActiveHeaders(msg));
/*     */     }
/* 165 */     return msg;
/*     */   }
/*     */   
/*     */   public void priorityTreeParentChanged(Http2Stream stream, Http2Stream oldParent)
/*     */   {
/* 170 */     Http2Stream parent = stream.parent();
/* 171 */     FullHttpMessage msg = (FullHttpMessage)this.messageMap.get(stream.id());
/* 172 */     if (msg == null)
/*     */     {
/*     */ 
/*     */ 
/* 176 */       if ((parent != null) && (!parent.equals(this.connection.connectionStream()))) {
/* 177 */         HttpHeaders headers = new DefaultHttpHeaders();
/* 178 */         headers.setInt(HttpUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text(), parent.id());
/* 179 */         importOutOfMessageFlowHeaders(stream.id(), headers);
/*     */       }
/*     */     }
/* 182 */     else if (parent == null) {
/* 183 */       removePriorityRelatedHeaders(msg.headers());
/* 184 */       removePriorityRelatedHeaders(msg.trailingHeaders());
/* 185 */     } else if (!parent.equals(this.connection.connectionStream())) {
/* 186 */       HttpHeaders headers = getActiveHeaders(msg);
/* 187 */       headers.setInt(HttpUtil.ExtensionHeaderNames.STREAM_DEPENDENCY_ID.text(), parent.id());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void onWeightChanged(Http2Stream stream, short oldWeight)
/*     */   {
/* 194 */     FullHttpMessage msg = (FullHttpMessage)this.messageMap.get(stream.id());
/*     */     HttpHeaders headers;
/* 196 */     if (msg == null)
/*     */     {
/*     */ 
/*     */ 
/* 200 */       HttpHeaders headers = new DefaultHttpHeaders();
/* 201 */       importOutOfMessageFlowHeaders(stream.id(), headers);
/*     */     } else {
/* 203 */       headers = getActiveHeaders(msg);
/*     */     }
/* 205 */     headers.setShort(HttpUtil.ExtensionHeaderNames.STREAM_WEIGHT.text(), stream.weight());
/*     */   }
/*     */   
/*     */   public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */     throws Http2Exception
/*     */   {
/* 211 */     FullHttpMessage msg = (FullHttpMessage)this.messageMap.get(streamId);
/* 212 */     if (msg == null) {
/* 213 */       HttpHeaders httpHeaders = (HttpHeaders)this.outOfMessageFlowHeaders.remove(streamId);
/* 214 */       if (httpHeaders == null) {
/* 215 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Priority Frame recieved for unknown stream id %d", new Object[] { Integer.valueOf(streamId) });
/*     */       }
/*     */       
/* 218 */       Http2Headers http2Headers = new DefaultHttp2Headers();
/* 219 */       initializePseudoHeaders(http2Headers);
/* 220 */       addHttpHeadersToHttp2Headers(httpHeaders, http2Headers);
/* 221 */       msg = newMessage(streamId, http2Headers, this.validateHttpHeaders);
/* 222 */       fireChannelRead(ctx, msg, streamId);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\InboundHttp2ToHttpPriorityAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */