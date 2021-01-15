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
/*    */ 
/*    */ 
/*    */ public final class MqttConnectMessage
/*    */   extends MqttMessage
/*    */ {
/*    */   public MqttConnectMessage(MqttFixedHeader mqttFixedHeader, MqttConnectVariableHeader variableHeader, MqttConnectPayload payload)
/*    */   {
/* 28 */     super(mqttFixedHeader, variableHeader, payload);
/*    */   }
/*    */   
/*    */   public MqttConnectVariableHeader variableHeader()
/*    */   {
/* 33 */     return (MqttConnectVariableHeader)super.variableHeader();
/*    */   }
/*    */   
/*    */   public MqttConnectPayload payload()
/*    */   {
/* 38 */     return (MqttConnectPayload)super.payload();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttConnectMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */