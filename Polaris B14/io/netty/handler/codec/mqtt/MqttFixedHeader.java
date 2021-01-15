/*    */ package io.netty.handler.codec.mqtt;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MqttFixedHeader
/*    */ {
/*    */   private final MqttMessageType messageType;
/*    */   private final boolean isDup;
/*    */   private final MqttQoS qosLevel;
/*    */   private final boolean isRetain;
/*    */   private final int remainingLength;
/*    */   
/*    */   public MqttFixedHeader(MqttMessageType messageType, boolean isDup, MqttQoS qosLevel, boolean isRetain, int remainingLength)
/*    */   {
/* 39 */     this.messageType = messageType;
/* 40 */     this.isDup = isDup;
/* 41 */     this.qosLevel = qosLevel;
/* 42 */     this.isRetain = isRetain;
/* 43 */     this.remainingLength = remainingLength;
/*    */   }
/*    */   
/*    */   public MqttMessageType messageType() {
/* 47 */     return this.messageType;
/*    */   }
/*    */   
/*    */   public boolean isDup() {
/* 51 */     return this.isDup;
/*    */   }
/*    */   
/*    */   public MqttQoS qosLevel() {
/* 55 */     return this.qosLevel;
/*    */   }
/*    */   
/*    */   public boolean isRetain() {
/* 59 */     return this.isRetain;
/*    */   }
/*    */   
/*    */   public int remainingLength() {
/* 63 */     return this.remainingLength;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 68 */     return StringUtil.simpleClassName(this) + '[' + "messageType=" + this.messageType + ", isDup=" + this.isDup + ", qosLevel=" + this.qosLevel + ", isRetain=" + this.isRetain + ", remainingLength=" + this.remainingLength + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttFixedHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */