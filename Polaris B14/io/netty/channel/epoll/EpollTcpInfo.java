/*     */ package io.netty.channel.epoll;
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
/*     */ public final class EpollTcpInfo
/*     */ {
/*  64 */   final int[] info = new int[32];
/*     */   
/*     */   public int state() {
/*  67 */     return this.info[0] & 0xFF;
/*     */   }
/*     */   
/*     */   public int caState() {
/*  71 */     return this.info[1] & 0xFF;
/*     */   }
/*     */   
/*     */   public int retransmits() {
/*  75 */     return this.info[2] & 0xFF;
/*     */   }
/*     */   
/*     */   public int probes() {
/*  79 */     return this.info[3] & 0xFF;
/*     */   }
/*     */   
/*     */   public int backoff() {
/*  83 */     return this.info[4] & 0xFF;
/*     */   }
/*     */   
/*     */   public int options() {
/*  87 */     return this.info[5] & 0xFF;
/*     */   }
/*     */   
/*     */   public int sndWscale() {
/*  91 */     return this.info[6] & 0xFF;
/*     */   }
/*     */   
/*     */   public int rcvWscale() {
/*  95 */     return this.info[7] & 0xFF;
/*     */   }
/*     */   
/*     */   public long rto() {
/*  99 */     return this.info[8] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long ato() {
/* 103 */     return this.info[9] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long sndMss() {
/* 107 */     return this.info[10] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long rcvMss() {
/* 111 */     return this.info[11] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long unacked() {
/* 115 */     return this.info[12] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long sacked() {
/* 119 */     return this.info[13] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long lost() {
/* 123 */     return this.info[14] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long retrans() {
/* 127 */     return this.info[15] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long fackets() {
/* 131 */     return this.info[16] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long lastDataSent() {
/* 135 */     return this.info[17] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long lastAckSent() {
/* 139 */     return this.info[18] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long lastDataRecv() {
/* 143 */     return this.info[19] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long lastAckRecv() {
/* 147 */     return this.info[20] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long pmtu() {
/* 151 */     return this.info[21] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long rcvSsthresh() {
/* 155 */     return this.info[22] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long rtt() {
/* 159 */     return this.info[23] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long rttvar() {
/* 163 */     return this.info[24] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long sndSsthresh() {
/* 167 */     return this.info[25] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long sndCwnd() {
/* 171 */     return this.info[26] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long advmss() {
/* 175 */     return this.info[27] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long reordering() {
/* 179 */     return this.info[28] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long rcvRtt() {
/* 183 */     return this.info[29] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long rcvSpace() {
/* 187 */     return this.info[30] & 0xFFFFFFFF;
/*     */   }
/*     */   
/*     */   public long totalRetrans() {
/* 191 */     return this.info[31] & 0xFFFFFFFF;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollTcpInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */