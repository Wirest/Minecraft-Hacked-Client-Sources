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
/*    */ public class MqttConnAckVariableHeader
/*    */ {
/*    */   private final MqttConnectReturnCode connectReturnCode;
/*    */   
/*    */   public MqttConnAckVariableHeader(MqttConnectReturnCode connectReturnCode)
/*    */   {
/* 29 */     this.connectReturnCode = connectReturnCode;
/*    */   }
/*    */   
/*    */   public MqttConnectReturnCode connectReturnCode() {
/* 33 */     return this.connectReturnCode;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 38 */     return StringUtil.simpleClassName(this) + '[' + "connectReturnCode=" + this.connectReturnCode + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttConnAckVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */