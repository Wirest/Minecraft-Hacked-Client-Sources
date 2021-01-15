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
/*    */ public class MqttPublishVariableHeader
/*    */ {
/*    */   private final String topicName;
/*    */   private final int messageId;
/*    */   
/*    */   public MqttPublishVariableHeader(String topicName, int messageId)
/*    */   {
/* 30 */     this.topicName = topicName;
/* 31 */     this.messageId = messageId;
/*    */   }
/*    */   
/*    */   public String topicName() {
/* 35 */     return this.topicName;
/*    */   }
/*    */   
/*    */   public int messageId() {
/* 39 */     return this.messageId;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 44 */     return StringUtil.simpleClassName(this) + '[' + "topicName=" + this.topicName + ", messageId=" + this.messageId + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttPublishVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */