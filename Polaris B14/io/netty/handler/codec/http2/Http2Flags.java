/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Http2Flags
/*     */ {
/*     */   public static final short END_STREAM = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   public static final short END_HEADERS = 4;
/*     */   
/*     */ 
/*     */   public static final short ACK = 1;
/*     */   
/*     */ 
/*     */   public static final short PADDED = 8;
/*     */   
/*     */ 
/*     */   public static final short PRIORITY = 32;
/*     */   
/*     */ 
/*     */   private short value;
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Flags() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Flags(short value)
/*     */   {
/*  34 */     this.value = value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public short value()
/*     */   {
/*  41 */     return this.value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean endOfStream()
/*     */   {
/*  49 */     return isFlagSet((short)1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean endOfHeaders()
/*     */   {
/*  57 */     return isFlagSet((short)4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean priorityPresent()
/*     */   {
/*  65 */     return isFlagSet((short)32);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean ack()
/*     */   {
/*  73 */     return isFlagSet((short)1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean paddingPresent()
/*     */   {
/*  81 */     return isFlagSet((short)8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getNumPriorityBytes()
/*     */   {
/*  89 */     return priorityPresent() ? 5 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPaddingPresenceFieldLength()
/*     */   {
/*  97 */     return paddingPresent() ? 1 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Flags endOfStream(boolean endOfStream)
/*     */   {
/* 104 */     return setFlag(endOfStream, (short)1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Flags endOfHeaders(boolean endOfHeaders)
/*     */   {
/* 111 */     return setFlag(endOfHeaders, (short)4);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Flags priorityPresent(boolean priorityPresent)
/*     */   {
/* 118 */     return setFlag(priorityPresent, (short)32);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Flags paddingPresent(boolean paddingPresent)
/*     */   {
/* 125 */     return setFlag(paddingPresent, (short)8);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2Flags ack(boolean ack)
/*     */   {
/* 132 */     return setFlag(ack, (short)1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2Flags setFlag(boolean on, short mask)
/*     */   {
/* 142 */     if (on) {
/* 143 */       this.value = ((short)(this.value | mask));
/*     */     } else {
/* 145 */       this.value = ((short)(this.value & (mask ^ 0xFFFFFFFF)));
/*     */     }
/* 147 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFlagSet(short mask)
/*     */   {
/* 156 */     return (this.value & mask) != 0;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 161 */     int prime = 31;
/* 162 */     int result = 1;
/* 163 */     result = 31 * result + this.value;
/* 164 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 169 */     if (this == obj) {
/* 170 */       return true;
/*     */     }
/* 172 */     if (obj == null) {
/* 173 */       return false;
/*     */     }
/* 175 */     if (getClass() != obj.getClass()) {
/* 176 */       return false;
/*     */     }
/*     */     
/* 179 */     return this.value == ((Http2Flags)obj).value;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 184 */     StringBuilder builder = new StringBuilder();
/* 185 */     builder.append("value = ").append(this.value).append(" (");
/* 186 */     if (ack()) {
/* 187 */       builder.append("ACK,");
/*     */     }
/* 189 */     if (endOfHeaders()) {
/* 190 */       builder.append("END_OF_HEADERS,");
/*     */     }
/* 192 */     if (endOfStream()) {
/* 193 */       builder.append("END_OF_STREAM,");
/*     */     }
/* 195 */     if (priorityPresent()) {
/* 196 */       builder.append("PRIORITY_PRESENT,");
/*     */     }
/* 198 */     if (paddingPresent()) {
/* 199 */       builder.append("PADDING_PRESENT,");
/*     */     }
/* 201 */     builder.append(')');
/* 202 */     return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2Flags.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */