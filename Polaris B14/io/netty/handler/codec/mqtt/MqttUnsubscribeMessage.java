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
/*    */ 
/*    */ public class MqttUnsubscribeMessage
/*    */   extends MqttMessage
/*    */ {
/*    */   public MqttUnsubscribeMessage(MqttFixedHeader mqttFixedHeader, MqttMessageIdVariableHeader variableHeader, MqttUnsubscribePayload payload)
/*    */   {
/* 29 */     super(mqttFixedHeader, variableHeader, payload);
/*    */   }
/*    */   
/*    */   public MqttMessageIdVariableHeader variableHeader()
/*    */   {
/* 34 */     return (MqttMessageIdVariableHeader)super.variableHeader();
/*    */   }
/*    */   
/*    */   public MqttUnsubscribePayload payload()
/*    */   {
/* 39 */     return (MqttUnsubscribePayload)super.payload();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttUnsubscribeMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */