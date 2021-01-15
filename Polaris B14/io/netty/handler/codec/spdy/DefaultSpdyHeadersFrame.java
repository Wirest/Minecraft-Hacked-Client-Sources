/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public class DefaultSpdyHeadersFrame
/*     */   extends DefaultSpdyStreamFrame
/*     */   implements SpdyHeadersFrame
/*     */ {
/*     */   private boolean invalid;
/*     */   private boolean truncated;
/*  30 */   private final SpdyHeaders headers = new DefaultSpdyHeaders();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultSpdyHeadersFrame(int streamId)
/*     */   {
/*  38 */     super(streamId);
/*     */   }
/*     */   
/*     */   public SpdyHeadersFrame setStreamId(int streamId)
/*     */   {
/*  43 */     super.setStreamId(streamId);
/*  44 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeadersFrame setLast(boolean last)
/*     */   {
/*  49 */     super.setLast(last);
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isInvalid()
/*     */   {
/*  55 */     return this.invalid;
/*     */   }
/*     */   
/*     */   public SpdyHeadersFrame setInvalid()
/*     */   {
/*  60 */     this.invalid = true;
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isTruncated()
/*     */   {
/*  66 */     return this.truncated;
/*     */   }
/*     */   
/*     */   public SpdyHeadersFrame setTruncated()
/*     */   {
/*  71 */     this.truncated = true;
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public SpdyHeaders headers()
/*     */   {
/*  77 */     return this.headers;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  82 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE).append("--> Headers:").append(StringUtil.NEWLINE);
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
/*  93 */     appendHeaders(buf);
/*     */     
/*     */ 
/*  96 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/*  97 */     return buf.toString();
/*     */   }
/*     */   
/*     */   protected void appendHeaders(StringBuilder buf) {
/* 101 */     for (Map.Entry<CharSequence, CharSequence> e : headers()) {
/* 102 */       buf.append("    ");
/* 103 */       buf.append((CharSequence)e.getKey());
/* 104 */       buf.append(": ");
/* 105 */       buf.append((CharSequence)e.getValue());
/* 106 */       buf.append(StringUtil.NEWLINE);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdyHeadersFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */