/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.util.internal.StringUtil;
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
/*     */ public class DefaultSpdySynStreamFrame
/*     */   extends DefaultSpdyHeadersFrame
/*     */   implements SpdySynStreamFrame
/*     */ {
/*     */   private int associatedStreamId;
/*     */   private byte priority;
/*     */   private boolean unidirectional;
/*     */   
/*     */   public DefaultSpdySynStreamFrame(int streamId, int associatedStreamId, byte priority)
/*     */   {
/*  38 */     super(streamId);
/*  39 */     setAssociatedStreamId(associatedStreamId);
/*  40 */     setPriority(priority);
/*     */   }
/*     */   
/*     */   public SpdySynStreamFrame setStreamId(int streamId)
/*     */   {
/*  45 */     super.setStreamId(streamId);
/*  46 */     return this;
/*     */   }
/*     */   
/*     */   public SpdySynStreamFrame setLast(boolean last)
/*     */   {
/*  51 */     super.setLast(last);
/*  52 */     return this;
/*     */   }
/*     */   
/*     */   public SpdySynStreamFrame setInvalid()
/*     */   {
/*  57 */     super.setInvalid();
/*  58 */     return this;
/*     */   }
/*     */   
/*     */   public int associatedStreamId()
/*     */   {
/*  63 */     return this.associatedStreamId;
/*     */   }
/*     */   
/*     */   public SpdySynStreamFrame setAssociatedStreamId(int associatedStreamId)
/*     */   {
/*  68 */     if (associatedStreamId < 0) {
/*  69 */       throw new IllegalArgumentException("Associated-To-Stream-ID cannot be negative: " + associatedStreamId);
/*     */     }
/*     */     
/*     */ 
/*  73 */     this.associatedStreamId = associatedStreamId;
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public byte priority()
/*     */   {
/*  79 */     return this.priority;
/*     */   }
/*     */   
/*     */   public SpdySynStreamFrame setPriority(byte priority)
/*     */   {
/*  84 */     if ((priority < 0) || (priority > 7)) {
/*  85 */       throw new IllegalArgumentException("Priority must be between 0 and 7 inclusive: " + priority);
/*     */     }
/*     */     
/*  88 */     this.priority = priority;
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isUnidirectional()
/*     */   {
/*  94 */     return this.unidirectional;
/*     */   }
/*     */   
/*     */   public SpdySynStreamFrame setUnidirectional(boolean unidirectional)
/*     */   {
/*  99 */     this.unidirectional = unidirectional;
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 105 */     StringBuilder buf = new StringBuilder().append(StringUtil.simpleClassName(this)).append("(last: ").append(isLast()).append("; unidirectional: ").append(isUnidirectional()).append(')').append(StringUtil.NEWLINE).append("--> Stream-ID = ").append(streamId()).append(StringUtil.NEWLINE);
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
/* 116 */     if (this.associatedStreamId != 0) {
/* 117 */       buf.append("--> Associated-To-Stream-ID = ").append(associatedStreamId()).append(StringUtil.NEWLINE);
/*     */     }
/*     */     
/*     */ 
/* 121 */     buf.append("--> Priority = ").append(priority()).append(StringUtil.NEWLINE).append("--> Headers:").append(StringUtil.NEWLINE);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 126 */     appendHeaders(buf);
/*     */     
/*     */ 
/* 129 */     buf.setLength(buf.length() - StringUtil.NEWLINE.length());
/* 130 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdySynStreamFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */