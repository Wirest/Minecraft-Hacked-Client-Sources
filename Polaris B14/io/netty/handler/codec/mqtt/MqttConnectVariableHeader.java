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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MqttConnectVariableHeader
/*    */ {
/*    */   private final String name;
/*    */   private final int version;
/*    */   private final boolean hasUserName;
/*    */   private final boolean hasPassword;
/*    */   private final boolean isWillRetain;
/*    */   private final int willQos;
/*    */   private final boolean isWillFlag;
/*    */   private final boolean isCleanSession;
/*    */   private final int keepAliveTimeSeconds;
/*    */   
/*    */   public MqttConnectVariableHeader(String name, int version, boolean hasUserName, boolean hasPassword, boolean isWillRetain, int willQos, boolean isWillFlag, boolean isCleanSession, int keepAliveTimeSeconds)
/*    */   {
/* 46 */     this.name = name;
/* 47 */     this.version = version;
/* 48 */     this.hasUserName = hasUserName;
/* 49 */     this.hasPassword = hasPassword;
/* 50 */     this.isWillRetain = isWillRetain;
/* 51 */     this.willQos = willQos;
/* 52 */     this.isWillFlag = isWillFlag;
/* 53 */     this.isCleanSession = isCleanSession;
/* 54 */     this.keepAliveTimeSeconds = keepAliveTimeSeconds;
/*    */   }
/*    */   
/*    */   public String name() {
/* 58 */     return this.name;
/*    */   }
/*    */   
/*    */   public int version() {
/* 62 */     return this.version;
/*    */   }
/*    */   
/*    */   public boolean hasUserName() {
/* 66 */     return this.hasUserName;
/*    */   }
/*    */   
/*    */   public boolean hasPassword() {
/* 70 */     return this.hasPassword;
/*    */   }
/*    */   
/*    */   public boolean isWillRetain() {
/* 74 */     return this.isWillRetain;
/*    */   }
/*    */   
/*    */   public int willQos() {
/* 78 */     return this.willQos;
/*    */   }
/*    */   
/*    */   public boolean isWillFlag() {
/* 82 */     return this.isWillFlag;
/*    */   }
/*    */   
/*    */   public boolean isCleanSession() {
/* 86 */     return this.isCleanSession;
/*    */   }
/*    */   
/*    */   public int keepAliveTimeSeconds() {
/* 90 */     return this.keepAliveTimeSeconds;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 95 */     return StringUtil.simpleClassName(this) + '[' + "name=" + this.name + ", version=" + this.version + ", hasUserName=" + this.hasUserName + ", hasPassword=" + this.hasPassword + ", isWillRetain=" + this.isWillRetain + ", isWillFlag=" + this.isWillFlag + ", isCleanSession=" + this.isCleanSession + ", keepAliveTimeSeconds=" + this.keepAliveTimeSeconds + ']';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\mqtt\MqttConnectVariableHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */