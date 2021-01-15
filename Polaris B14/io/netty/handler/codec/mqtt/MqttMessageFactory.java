/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public final class MqttMessageFactory
/*    */ {
/*    */   public static MqttMessage newMessage(MqttFixedHeader mqttFixedHeader, Object variableHeader, Object payload)
/*    */   {
/* 28 */     switch (mqttFixedHeader.messageType()) {
/*    */     case CONNECT: 
/* 30 */       return new MqttConnectMessage(mqttFixedHeader, (MqttConnectVariableHeader)variableHeader, (MqttConnectPayload)payload);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     case CONNACK: 
/* 36 */       return new MqttConnAckMessage(mqttFixedHeader, (MqttConnAckVariableHeader)variableHeader);
/*    */     
/*    */     case SUBSCRIBE: 
/* 39 */       return new MqttSubscribeMessage(mqttFixedHeader, (MqttMessageIdVariableHeader)variableHeader, (MqttSubscribePayload)payload);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     case SUBACK: 
/* 45 */       return new MqttSubAckMessage(mqttFixedHeader, (MqttMessageIdVariableHeader)variableHeader, (MqttSubAckPayload)payload);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     case UNSUBACK: 
/* 51 */       return new MqttUnsubAckMessage(mqttFixedHeader, (MqttMessageIdVariableHeader)variableHeader);
/*    */     
/*    */ 
/*    */ 
/*    */     case UNSUBSCRIBE: 
/* 56 */       return new MqttUnsubscribeMessage(mqttFixedHeader, (MqttMessageIdVariableHeader)variableHeader, (MqttUnsubscribePayload)payload);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     case PUBLISH: 
/* 62 */       return new MqttPublishMessage(mqttFixedHeader, (MqttPublishVariableHeader)variableHeader, (ByteBuf)payload);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */     case PUBACK: 
/* 68 */       return new MqttPubAckMessage(mqttFixedHeader, (MqttMessageIdVariableHeader)variableHeader);
/*    */     case PUBREC: 
/*    */     case PUBREL: 
/*    */     case PUBCOMP: 
/* 72 */       return new MqttMessage(mqttFixedHeader, variableHeader);
/*    */     
/*    */     case PINGREQ: 
/*    */     case PINGRESP: 
/*    */     case DISCONNECT: 
/* 77 */       return new MqttMessage(mqttFixedHeader);
/*    */     }
/*    */     
/* 80 */     throw new IllegalArgumentException("unknown message type: " + mqttFixedHeader.messageType());
/*    */   }
/*    */   
/*    */   public static MqttMessage newInvalidMessage(Throwable cause)
/*    */   {
/* 85 */     return new MqttMessage(null, null, null, DecoderResult.failure(cause));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttMessageFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */