/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class DefaultSpdySynReplyFrame
/*    */   extends DefaultSpdyHeadersFrame
/*    */   implements SpdySynReplyFrame
/*    */ {
/*    */   public DefaultSpdySynReplyFrame(int streamId)
/*    */   {
/* 32 */     super(streamId);
/*    */   }
/*    */   
/*    */   public SpdySynReplyFrame setStreamId(int streamId)
/*    */   {
/* 37 */     super.setStreamId(streamId);
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public SpdySynReplyFrame setLast(boolean last)
/*    */   {
/* 43 */     super.setLast(last);
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public SpdySynReplyFrame setInvalid()
/*    */   {
/* 49 */     super.setInvalid();
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 55 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE).append("--> Headers:").append(StringUtil.NEWLINE);
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
/* 66 */     appendHeaders(buf);
/*    */     
/*    */ 
/* 69 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/* 70 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdySynReplyFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */