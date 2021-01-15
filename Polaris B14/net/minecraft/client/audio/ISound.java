/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public abstract interface ISound
/*    */ {
/*    */   public abstract ResourceLocation getSoundLocation();
/*    */   
/*    */   public abstract boolean canRepeat();
/*    */   
/*    */   public abstract int getRepeatDelay();
/*    */   
/*    */   public abstract float getVolume();
/*    */   
/*    */   public abstract float getPitch();
/*    */   
/*    */   public abstract float getXPosF();
/*    */   
/*    */   public abstract float getYPosF();
/*    */   
/*    */   public abstract float getZPosF();
/*    */   
/*    */   public abstract AttenuationType getAttenuationType();
/*    */   
/*    */   public static enum AttenuationType
/*    */   {
/* 27 */     NONE(0), 
/* 28 */     LINEAR(2);
/*    */     
/*    */     private final int type;
/*    */     
/*    */     private AttenuationType(int typeIn)
/*    */     {
/* 34 */       this.type = typeIn;
/*    */     }
/*    */     
/*    */     public int getTypeInt()
/*    */     {
/* 39 */       return this.type;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\ISound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */