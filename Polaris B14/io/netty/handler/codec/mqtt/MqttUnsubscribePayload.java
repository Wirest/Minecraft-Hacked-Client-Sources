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
/*    */ public class MqttUnsubscribePayload
/*    */ {
/*    */   private final List<String> topics;
/*    */   
/*    */   public MqttUnsubscribePayload(List<String> topics)
/*    */   {
/* 32 */     this.topics = Collections.unmodifiableList(topics);
/*    */   }
/*    */   
/*    */   public List<String> topics() {
/* 36 */     return this.topics;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 41 */     StringBuilder builder = new StringBuilder(StringUtil.simpleClassName(this)).append('[');
/* 42 */     for (int i = 0; i < this.topics.size() - 1; i++) {
/* 43 */       builder.append("topicName = ").append((String)this.topics.get(i)).append(", ");
/*    */     }
/* 45 */     builder.append("topicName = ").append((String)this.topics.get(this.topics.size() - 1)).append(']');
/*    */     
/* 47 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttUnsubscribePayload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */