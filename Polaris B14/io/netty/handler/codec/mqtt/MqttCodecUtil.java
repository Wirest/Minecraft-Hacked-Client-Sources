/*     */ package io.netty.handler.codec.mqtt;
/*     */ 
/*     */ import io.netty.handler.codec.DecoderException;
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
/*     */ final class MqttCodecUtil
/*     */ {
/*  23 */   private static final char[] TOPIC_WILDCARDS = { '#', '+' };
/*     */   private static final int MIN_CLIENT_ID_LENGTH = 1;
/*     */   private static final int MAX_CLIENT_ID_LENGTH = 23;
/*     */   
/*     */   static boolean isValidPublishTopicName(String topicName)
/*     */   {
/*  29 */     for (char c : TOPIC_WILDCARDS) {
/*  30 */       if (topicName.indexOf(c) >= 0) {
/*  31 */         return false;
/*     */       }
/*     */     }
/*  34 */     return true;
/*     */   }
/*     */   
/*     */   static boolean isValidMessageId(int messageId) {
/*  38 */     return messageId != 0;
/*     */   }
/*     */   
/*     */   static boolean isValidClientId(MqttVersion mqttVersion, String clientId) {
/*  42 */     if (mqttVersion == MqttVersion.MQTT_3_1) {
/*  43 */       return (clientId != null) && (clientId.length() >= 1) && (clientId.length() <= 23);
/*     */     }
/*  45 */     if (mqttVersion == MqttVersion.MQTT_3_1_1)
/*     */     {
/*     */ 
/*  48 */       return clientId != null;
/*     */     }
/*  50 */     throw new IllegalArgumentException(mqttVersion + " is unknown mqtt version");
/*     */   }
/*     */   
/*     */   static MqttFixedHeader validateFixedHeader(MqttFixedHeader mqttFixedHeader) {
/*  54 */     switch (mqttFixedHeader.messageType()) {
/*     */     case PUBREL: 
/*     */     case SUBSCRIBE: 
/*     */     case UNSUBSCRIBE: 
/*  58 */       if (mqttFixedHeader.qosLevel() != MqttQoS.AT_LEAST_ONCE)
/*  59 */         throw new DecoderException(mqttFixedHeader.messageType().name() + " message must have QoS 1");
/*     */       break;
/*     */     }
/*  62 */     return mqttFixedHeader;
/*     */   }
/*     */   
/*     */   static MqttFixedHeader resetUnusedFields(MqttFixedHeader mqttFixedHeader)
/*     */   {
/*  67 */     switch (mqttFixedHeader.messageType()) {
/*     */     case CONNECT: 
/*     */     case CONNACK: 
/*     */     case PUBACK: 
/*     */     case PUBREC: 
/*     */     case PUBCOMP: 
/*     */     case SUBACK: 
/*     */     case UNSUBACK: 
/*     */     case PINGREQ: 
/*     */     case PINGRESP: 
/*     */     case DISCONNECT: 
/*  78 */       if ((mqttFixedHeader.isDup()) || (mqttFixedHeader.qosLevel() != MqttQoS.AT_MOST_ONCE) || (mqttFixedHeader.isRetain()))
/*     */       {
/*     */ 
/*  81 */         return new MqttFixedHeader(mqttFixedHeader.messageType(), false, MqttQoS.AT_MOST_ONCE, false, mqttFixedHeader.remainingLength());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */       return mqttFixedHeader;
/*     */     case PUBREL: 
/*     */     case SUBSCRIBE: 
/*     */     case UNSUBSCRIBE: 
/*  92 */       if (mqttFixedHeader.isRetain()) {
/*  93 */         return new MqttFixedHeader(mqttFixedHeader.messageType(), mqttFixedHeader.isDup(), mqttFixedHeader.qosLevel(), false, mqttFixedHeader.remainingLength());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */       return mqttFixedHeader;
/*     */     }
/* 102 */     return mqttFixedHeader;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttCodecUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */