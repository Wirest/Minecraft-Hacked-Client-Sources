/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class SoundPoolEntry
/*    */ {
/*    */   private final ResourceLocation location;
/*    */   private final boolean streamingSound;
/*    */   private double pitch;
/*    */   private double volume;
/*    */   
/*    */   public SoundPoolEntry(ResourceLocation locationIn, double pitchIn, double volumeIn, boolean streamingSoundIn)
/*    */   {
/* 14 */     this.location = locationIn;
/* 15 */     this.pitch = pitchIn;
/* 16 */     this.volume = volumeIn;
/* 17 */     this.streamingSound = streamingSoundIn;
/*    */   }
/*    */   
/*    */   public SoundPoolEntry(SoundPoolEntry locationIn)
/*    */   {
/* 22 */     this.location = locationIn.location;
/* 23 */     this.pitch = locationIn.pitch;
/* 24 */     this.volume = locationIn.volume;
/* 25 */     this.streamingSound = locationIn.streamingSound;
/*    */   }
/*    */   
/*    */   public ResourceLocation getSoundPoolEntryLocation()
/*    */   {
/* 30 */     return this.location;
/*    */   }
/*    */   
/*    */   public double getPitch()
/*    */   {
/* 35 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public void setPitch(double pitchIn)
/*    */   {
/* 40 */     this.pitch = pitchIn;
/*    */   }
/*    */   
/*    */   public double getVolume()
/*    */   {
/* 45 */     return this.volume;
/*    */   }
/*    */   
/*    */   public void setVolume(double volumeIn)
/*    */   {
/* 50 */     this.volume = volumeIn;
/*    */   }
/*    */   
/*    */   public boolean isStreamingSound()
/*    */   {
/* 55 */     return this.streamingSound;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundPoolEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */