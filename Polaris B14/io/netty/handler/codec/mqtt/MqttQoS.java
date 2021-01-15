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
/*    */ public enum MqttQoS
/*    */ {
/* 19 */   AT_MOST_ONCE(0), 
/* 20 */   AT_LEAST_ONCE(1), 
/* 21 */   EXACTLY_ONCE(2), 
/* 22 */   FAILURE(128);
/*    */   
/*    */   private final int value;
/*    */   
/*    */   private MqttQoS(int value) {
/* 27 */     this.value = value;
/*    */   }
/*    */   
/*    */   public int value() {
/* 31 */     return this.value;
/*    */   }
/*    */   
/*    */   public static MqttQoS valueOf(int value) {
/* 35 */     for (MqttQoS q : ) {
/* 36 */       if (q.value == value) {
/* 37 */         return q;
/*    */       }
/*    */     }
/* 40 */     throw new IllegalArgumentException("invalid QoS: " + value);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttQoS.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */