/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderException;
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
/*    */ public class MqttUnacceptableProtocolVersionException
/*    */   extends DecoderException
/*    */ {
/*    */   private static final long serialVersionUID = 4914652213232455749L;
/*    */   
/*    */   public MqttUnacceptableProtocolVersionException() {}
/*    */   
/*    */   public MqttUnacceptableProtocolVersionException(String message, Throwable cause)
/*    */   {
/* 37 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public MqttUnacceptableProtocolVersionException(String message)
/*    */   {
/* 44 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public MqttUnacceptableProtocolVersionException(Throwable cause)
/*    */   {
/* 51 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttUnacceptableProtocolVersionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */