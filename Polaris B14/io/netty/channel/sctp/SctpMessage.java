/*     */ package io.netty.channel.sctp;
/*     */ 
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.DefaultByteBufHolder;
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
/*     */ public final class SctpMessage
/*     */   extends DefaultByteBufHolder
/*     */ {
/*     */   private final int streamIdentifier;
/*     */   private final int protocolIdentifier;
/*     */   private final MessageInfo msgInfo;
/*     */   
/*     */   public SctpMessage(int protocolIdentifier, int streamIdentifier, ByteBuf payloadBuffer)
/*     */   {
/*  39 */     super(payloadBuffer);
/*  40 */     this.protocolIdentifier = protocolIdentifier;
/*  41 */     this.streamIdentifier = streamIdentifier;
/*  42 */     this.msgInfo = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SctpMessage(MessageInfo msgInfo, ByteBuf payloadBuffer)
/*     */   {
/*  51 */     super(payloadBuffer);
/*  52 */     if (msgInfo == null) {
/*  53 */       throw new NullPointerException("msgInfo");
/*     */     }
/*  55 */     this.msgInfo = msgInfo;
/*  56 */     this.streamIdentifier = msgInfo.streamNumber();
/*  57 */     this.protocolIdentifier = msgInfo.payloadProtocolID();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int streamIdentifier()
/*     */   {
/*  64 */     return this.streamIdentifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int protocolIdentifier()
/*     */   {
/*  71 */     return this.protocolIdentifier;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MessageInfo messageInfo()
/*     */   {
/*  79 */     return this.msgInfo;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/*  86 */     if (this.msgInfo != null) {
/*  87 */       return this.msgInfo.isComplete();
/*     */     }
/*     */     
/*  90 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  96 */     if (this == o) {
/*  97 */       return true;
/*     */     }
/*     */     
/* 100 */     if ((o == null) || (getClass() != o.getClass())) {
/* 101 */       return false;
/*     */     }
/*     */     
/* 104 */     SctpMessage sctpFrame = (SctpMessage)o;
/*     */     
/* 106 */     if (this.protocolIdentifier != sctpFrame.protocolIdentifier) {
/* 107 */       return false;
/*     */     }
/*     */     
/* 110 */     if (this.streamIdentifier != sctpFrame.streamIdentifier) {
/* 111 */       return false;
/*     */     }
/*     */     
/* 114 */     return content().equals(sctpFrame.content());
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 119 */     int result = this.streamIdentifier;
/* 120 */     result = 31 * result + this.protocolIdentifier;
/* 121 */     result = 31 * result + content().hashCode();
/* 122 */     return result;
/*     */   }
/*     */   
/*     */   public SctpMessage copy()
/*     */   {
/* 127 */     if (this.msgInfo == null) {
/* 128 */       return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, content().copy());
/*     */     }
/* 130 */     return new SctpMessage(this.msgInfo, content().copy());
/*     */   }
/*     */   
/*     */ 
/*     */   public SctpMessage duplicate()
/*     */   {
/* 136 */     if (this.msgInfo == null) {
/* 137 */       return new SctpMessage(this.protocolIdentifier, this.streamIdentifier, content().duplicate());
/*     */     }
/* 139 */     return new SctpMessage(this.msgInfo, content().copy());
/*     */   }
/*     */   
/*     */ 
/*     */   public SctpMessage retain()
/*     */   {
/* 145 */     super.retain();
/* 146 */     return this;
/*     */   }
/*     */   
/*     */   public SctpMessage retain(int increment)
/*     */   {
/* 151 */     super.retain(increment);
/* 152 */     return this;
/*     */   }
/*     */   
/*     */   public SctpMessage touch()
/*     */   {
/* 157 */     super.touch();
/* 158 */     return this;
/*     */   }
/*     */   
/*     */   public SctpMessage touch(Object hint)
/*     */   {
/* 163 */     super.touch(hint);
/* 164 */     return this;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 169 */     if (refCnt() == 0) {
/* 170 */       return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=(FREED)}";
/*     */     }
/*     */     
/*     */ 
/* 174 */     return "SctpFrame{streamIdentifier=" + this.streamIdentifier + ", protocolIdentifier=" + this.protocolIdentifier + ", data=" + ByteBufUtil.hexDump(content()) + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\sctp\SctpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */