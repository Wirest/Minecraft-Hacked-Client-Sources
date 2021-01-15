/*    */ package net.minecraft.client.stream;
/*    */ 
/*    */ import tv.twitch.ErrorCode;
/*    */ import tv.twitch.broadcast.IngestServer;
/*    */ import tv.twitch.chat.ChatUserInfo;
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
/*    */ public abstract interface IStream
/*    */ {
/*    */   public abstract void shutdownStream();
/*    */   
/*    */   public abstract void func_152935_j();
/*    */   
/*    */   public abstract void func_152922_k();
/*    */   
/*    */   public abstract boolean func_152936_l();
/*    */   
/*    */   public abstract boolean isReadyToBroadcast();
/*    */   
/*    */   public abstract boolean isBroadcasting();
/*    */   
/*    */   public abstract void func_152911_a(Metadata paramMetadata, long paramLong);
/*    */   
/*    */   public abstract void func_176026_a(Metadata paramMetadata, long paramLong1, long paramLong2);
/*    */   
/*    */   public abstract boolean isPaused();
/*    */   
/*    */   public abstract void requestCommercial();
/*    */   
/*    */   public abstract void pause();
/*    */   
/*    */   public abstract void unpause();
/*    */   
/*    */   public abstract void updateStreamVolume();
/*    */   
/*    */   public abstract void func_152930_t();
/*    */   
/*    */   public abstract void stopBroadcasting();
/*    */   
/*    */   public abstract IngestServer[] func_152925_v();
/*    */   
/*    */   public abstract void func_152909_x();
/*    */   
/*    */   public abstract IngestServerTester func_152932_y();
/*    */   
/*    */   public abstract boolean func_152908_z();
/*    */   
/*    */   public abstract int func_152920_A();
/*    */   
/*    */   public abstract boolean func_152927_B();
/*    */   
/*    */   public abstract String func_152921_C();
/*    */   
/*    */   public abstract ChatUserInfo func_152926_a(String paramString);
/*    */   
/*    */   public abstract void func_152917_b(String paramString);
/*    */   
/*    */   public abstract boolean func_152928_D();
/*    */   
/*    */   public abstract ErrorCode func_152912_E();
/*    */   
/*    */   public abstract boolean func_152913_F();
/*    */   
/*    */   public abstract void muteMicrophone(boolean paramBoolean);
/*    */   
/*    */   public abstract boolean func_152929_G();
/*    */   
/*    */   public abstract AuthFailureReason func_152918_H();
/*    */   
/*    */   public static enum AuthFailureReason
/*    */   {
/* 83 */     ERROR, 
/* 84 */     INVALID_TOKEN;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\stream\IStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */