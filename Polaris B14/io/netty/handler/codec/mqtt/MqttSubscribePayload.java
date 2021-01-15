/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public class MqttSubscribePayload
/*    */ {
/*    */   private final List<MqttTopicSubscription> topicSubscriptions;
/*    */   
/*    */   public MqttSubscribePayload(List<MqttTopicSubscription> topicSubscriptions)
/*    */   {
/* 32 */     this.topicSubscriptions = Collections.unmodifiableList(topicSubscriptions);
/*    */   }
/*    */   
/*    */   public List<MqttTopicSubscription> topicSubscriptions() {
/* 36 */     return this.topicSubscriptions;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 41 */     StringBuilder builder = new StringBuilder(StringUtil.simpleClassName(this)).append('[');
/* 42 */     for (int i = 0; i < this.topicSubscriptions.size() - 1; i++) {
/* 43 */       builder.append(this.topicSubscriptions.get(i)).append(", ");
/*    */     }
/* 45 */     builder.append(this.topicSubscriptions.get(this.topicSubscriptions.size() - 1));
/* 46 */     builder.append(']');
/* 47 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttSubscribePayload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */