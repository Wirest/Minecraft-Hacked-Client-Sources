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
/*    */ public class MqttSubAckMessage
/*    */   extends MqttMessage
/*    */ {
/*    */   public MqttSubAckMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader, MqttSubAckPayload payload)
/*    */   {
/* 28 */     super(mqttFixedHeader, variableHeader, payload);
/*    */   }
/*    */   
/*    */   public MqttMessageIdVariableHeader variableHeader()
/*    */   {
/* 33 */     return (MqttMessageIdVariableHeader)super.variableHeader();
/*    */   }
/*    */   
/*    */   public MqttSubAckPayload payload()
/*    */   {
/* 38 */     return (MqttSubAckPayload)super.payload();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttSubAckMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */