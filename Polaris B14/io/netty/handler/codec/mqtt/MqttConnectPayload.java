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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MqttConnectPayload
/*    */ {
/*    */   private final String clientIdentifier;
/*    */   private final String willTopic;
/*    */   private final String willMessage;
/*    */   private final String userName;
/*    */   private final String password;
/*    */   
/*    */   public MqttConnectPayload(String clientIdentifier, String willTopic, String willMessage, String userName, String password)
/*    */   {
/* 38 */     this.clientIdentifier = clientIdentifier;
/* 39 */     this.willTopic = willTopic;
/* 40 */     this.willMessage = willMessage;
/* 41 */     this.userName = userName;
/* 42 */     this.password = password;
/*    */   }
/*    */   
/*    */   public String clientIdentifier() {
/* 46 */     return this.clientIdentifier;
/*    */   }
/*    */   
/*    */   public String willTopic() {
/* 50 */     return this.willTopic;
/*    */   }
/*    */   
/*    */   public String willMessage() {
/* 54 */     return this.willMessage;
/*    */   }
/*    */   
/*    */   public String userName() {
/* 58 */     return this.userName;
/*    */   }
/*    */   
/*    */   public String password() {
/* 62 */     return this.password;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 67 */     return StringUtil.simpleClassName(this) + '[' + "clientIdentifier=" + this.clientIdentifier + ", willTopic=" + this.willTopic + ", willMessage=" + this.willMessage + ", userName=" + this.userName + ", password=" + this.password + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttConnectPayload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */