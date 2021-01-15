/*    */ package io.netty.handler.codec.haproxy;
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
/*    */ public enum HAProxyCommand
/*    */ {
/* 26 */   LOCAL((byte)0), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 31 */   PROXY((byte)1);
/*    */   
/*    */ 
/*    */ 
/*    */   private static final byte COMMAND_MASK = 15;
/*    */   
/*    */ 
/*    */   private final byte byteValue;
/*    */   
/*    */ 
/*    */ 
/*    */   private HAProxyCommand(byte byteValue)
/*    */   {
/* 44 */     this.byteValue = byteValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static HAProxyCommand valueOf(byte verCmdByte)
/*    */   {
/* 53 */     int cmd = verCmdByte & 0xF;
/* 54 */     switch ((byte)cmd) {
/*    */     case 1: 
/* 56 */       return PROXY;
/*    */     case 0: 
/* 58 */       return LOCAL;
/*    */     }
/* 60 */     throw new IllegalArgumentException("unknown command: " + cmd);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public byte byteValue()
/*    */   {
/* 68 */     return this.byteValue;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\haproxy\HAProxyCommand.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */