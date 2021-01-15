/*    */ package io.netty.handler.codec.mqtt;
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
/*    */ public enum MqttMessageType
/*    */ {
/* 23 */   CONNECT(1), 
/* 24 */   CONNACK(2), 
/* 25 */   PUBLISH(3), 
/* 26 */   PUBACK(4), 
/* 27 */   PUBREC(5), 
/* 28 */   PUBREL(6), 
/* 29 */   PUBCOMP(7), 
/* 30 */   SUBSCRIBE(8), 
/* 31 */   SUBACK(9), 
/* 32 */   UNSUBSCRIBE(10), 
/* 33 */   UNSUBACK(11), 
/* 34 */   PINGREQ(12), 
/* 35 */   PINGRESP(13), 
/* 36 */   DISCONNECT(14);
/*    */   
/*    */   private final int value;
/*    */   
/*    */   private MqttMessageType(int value) {
/* 41 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int value() {
/* 45 */     return this.value;
/*    */   }
/*    */   
/*    */   public static MqttMessageType valueOf(int type) {
/* 49 */     for (MqttMessageType t : ) {
/* 50 */       if (t.value == type) {
/* 51 */         return t;
/*    */       }
/*    */     }
/* 54 */     throw new IllegalArgumentException("unknown message type: " + type);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttMessageType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */