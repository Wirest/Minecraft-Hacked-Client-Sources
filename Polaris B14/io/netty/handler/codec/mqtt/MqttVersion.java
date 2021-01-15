/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.util.CharsetUtil;
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
/*    */ public enum MqttVersion
/*    */ {
/* 25 */   MQTT_3_1("MQIsdp", (byte)3), 
/* 26 */   MQTT_3_1_1("MQTT", (byte)4);
/*    */   
/*    */   private String name;
/*    */   private byte level;
/*    */   
/*    */   private MqttVersion(String protocolName, byte protocolLevel) {
/* 32 */     this.name = protocolName;
/* 33 */     this.level = protocolLevel;
/*    */   }
/*    */   
/*    */   public String protocolName() {
/* 37 */     return this.name;
/*    */   }
/*    */   
/*    */   public byte[] protocolNameBytes() {
/* 41 */     return this.name.getBytes(CharsetUtil.UTF_8);
/*    */   }
/*    */   
/*    */   public byte protocolLevel() {
/* 45 */     return this.level;
/*    */   }
/*    */   
/*    */   public static MqttVersion fromProtocolNameAndLevel(String protocolName, byte protocolLevel) {
/* 49 */     for (MqttVersion mv : ) {
/* 50 */       if (mv.name.equals(protocolName)) {
/* 51 */         if (mv.level == protocolLevel) {
/* 52 */           return mv;
/*    */         }
/* 54 */         throw new MqttUnacceptableProtocolVersionException(protocolName + " and " + protocolLevel + " are not match");
/*    */       }
/*    */     }
/*    */     
/*    */ 
/* 59 */     throw new MqttUnacceptableProtocolVersionException(protocolName + "is unknown protocol name");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */