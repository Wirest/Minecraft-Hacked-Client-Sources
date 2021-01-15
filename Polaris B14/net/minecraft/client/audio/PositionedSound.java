/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public abstract class PositionedSound implements ISound
/*    */ {
/*    */   protected final ResourceLocation positionedSoundLocation;
/*  8 */   protected float volume = 1.0F;
/*  9 */   protected float pitch = 1.0F;
/*    */   protected float xPosF;
/*    */   protected float yPosF;
/*    */   protected float zPosF;
/* 13 */   protected boolean repeat = false;
/*    */   
/*    */ 
/* 16 */   protected int repeatDelay = 0;
/* 17 */   protected ISound.AttenuationType attenuationType = ISound.AttenuationType.LINEAR;
/*    */   
/*    */   protected PositionedSound(ResourceLocation soundResource)
/*    */   {
/* 21 */     this.positionedSoundLocation = soundResource;
/*    */   }
/*    */   
/*    */   public ResourceLocation getSoundLocation()
/*    */   {
/* 26 */     return this.positionedSoundLocation;
/*    */   }
/*    */   
/*    */   public boolean canRepeat()
/*    */   {
/* 31 */     return this.repeat;
/*    */   }
/*    */   
/*    */   public int getRepeatDelay()
/*    */   {
/* 36 */     return this.repeatDelay;
/*    */   }
/*    */   
/*    */   public float getVolume()
/*    */   {
/* 41 */     return this.volume;
/*    */   }
/*    */   
/*    */   public float getPitch()
/*    */   {
/* 46 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public float getXPosF()
/*    */   {
/* 51 */     return this.xPosF;
/*    */   }
/*    */   
/*    */   public float getYPosF()
/*    */   {
/* 56 */     return this.yPosF;
/*    */   }
/*    */   
/*    */   public float getZPosF()
/*    */   {
/* 61 */     return this.zPosF;
/*    */   }
/*    */   
/*    */   public ISound.AttenuationType getAttenuationType()
/*    */   {
/* 66 */     return this.attenuationType;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\PositionedSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */