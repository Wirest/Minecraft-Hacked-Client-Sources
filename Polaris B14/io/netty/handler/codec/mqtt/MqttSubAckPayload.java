/*    */ package io.netty.handler.codec.mqtt;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
/*    */ import java.util.ArrayList;
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
/*    */ public class MqttSubAckPayload
/*    */ {
/*    */   private final List<Integer> grantedQoSLevels;
/*    */   
/*    */   public MqttSubAckPayload(int... grantedQoSLevels)
/*    */   {
/* 33 */     if (grantedQoSLevels == null) {
/* 34 */       throw new NullPointerException("grantedQoSLevels");
/*    */     }
/*    */     
/* 37 */     List<Integer> list = new ArrayList(grantedQoSLevels.length);
/* 38 */     for (int v : grantedQoSLevels) {
/* 39 */       list.add(Integer.valueOf(v));
/*    */     }
/* 41 */     this.grantedQoSLevels = Collections.unmodifiableList(list);
/*    */   }
/*    */   
/*    */   public MqttSubAckPayload(Iterable<Integer> grantedQoSLevels) {
/* 45 */     if (grantedQoSLevels == null) {
/* 46 */       throw new NullPointerException("grantedQoSLevels");
/*    */     }
/* 48 */     List<Integer> list = new ArrayList();
/* 49 */     for (Integer v : grantedQoSLevels) {
/* 50 */       if (v == null) {
/*    */         break;
/*    */       }
/* 53 */       list.add(v);
/*    */     }
/* 55 */     this.grantedQoSLevels = Collections.unmodifiableList(list);
/*    */   }
/*    */   
/*    */   public List<Integer> grantedQoSLevels() {
/* 59 */     return this.grantedQoSLevels;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 64 */     return StringUtil.simpleClassName(this) + '[' + "grantedQoSLevels=" + this.grantedQoSLevels + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttSubAckPayload.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */