/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.channel.ChannelHandler;
/*    */ import io.netty.channel.embedded.EmbeddedChannel;
/*    */ import io.netty.handler.codec.AsciiString;
/*    */ import io.netty.handler.codec.compression.ZlibCodecFactory;
/*    */ import io.netty.handler.codec.compression.ZlibWrapper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpContentDecompressor
/*    */   extends HttpContentDecoder
/*    */ {
/*    */   private final boolean strict;
/*    */   
/*    */   public HttpContentDecompressor()
/*    */   {
/* 38 */     this(false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public HttpContentDecompressor(boolean strict)
/*    */   {
/* 48 */     this.strict = strict;
/*    */   }
/*    */   
/*    */   protected EmbeddedChannel newContentDecoder(String contentEncoding) throws Exception
/*    */   {
/* 53 */     if ((HttpHeaderValues.GZIP.equalsIgnoreCase(contentEncoding)) || (HttpHeaderValues.X_GZIP.equalsIgnoreCase(contentEncoding)))
/*    */     {
/* 55 */       return new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(ZlibWrapper.GZIP) });
/*    */     }
/* 57 */     if ((HttpHeaderValues.DEFLATE.equalsIgnoreCase(contentEncoding)) || (HttpHeaderValues.X_DEFLATE.equalsIgnoreCase(contentEncoding)))
/*    */     {
/* 59 */       ZlibWrapper wrapper = this.strict ? ZlibWrapper.ZLIB : ZlibWrapper.ZLIB_OR_NONE;
/*    */       
/* 61 */       return new EmbeddedChannel(new ChannelHandler[] { ZlibCodecFactory.newZlibDecoder(wrapper) });
/*    */     }
/*    */     
/*    */ 
/* 65 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpContentDecompressor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */