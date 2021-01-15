/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class SoundEventAccessorComposite implements ISoundEventAccessor<SoundPoolEntry>
/*    */ {
/* 10 */   private final List<ISoundEventAccessor<SoundPoolEntry>> soundPool = Lists.newArrayList();
/* 11 */   private final Random rnd = new Random();
/*    */   private final ResourceLocation soundLocation;
/*    */   private final SoundCategory category;
/*    */   private double eventPitch;
/*    */   private double eventVolume;
/*    */   
/*    */   public SoundEventAccessorComposite(ResourceLocation soundLocation, double pitch, double volume, SoundCategory category)
/*    */   {
/* 19 */     this.soundLocation = soundLocation;
/* 20 */     this.eventVolume = volume;
/* 21 */     this.eventPitch = pitch;
/* 22 */     this.category = category;
/*    */   }
/*    */   
/*    */   public int getWeight()
/*    */   {
/* 27 */     int i = 0;
/*    */     
/* 29 */     for (ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor : this.soundPool)
/*    */     {
/* 31 */       i += isoundeventaccessor.getWeight();
/*    */     }
/*    */     
/* 34 */     return i;
/*    */   }
/*    */   
/*    */   public SoundPoolEntry cloneEntry()
/*    */   {
/* 39 */     int i = getWeight();
/*    */     
/* 41 */     if ((!this.soundPool.isEmpty()) && (i != 0))
/*    */     {
/* 43 */       int j = this.rnd.nextInt(i);
/*    */       
/* 45 */       for (ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor : this.soundPool)
/*    */       {
/* 47 */         j -= isoundeventaccessor.getWeight();
/*    */         
/* 49 */         if (j < 0)
/*    */         {
/* 51 */           SoundPoolEntry soundpoolentry = (SoundPoolEntry)isoundeventaccessor.cloneEntry();
/* 52 */           soundpoolentry.setPitch(soundpoolentry.getPitch() * this.eventPitch);
/* 53 */           soundpoolentry.setVolume(soundpoolentry.getVolume() * this.eventVolume);
/* 54 */           return soundpoolentry;
/*    */         }
/*    */       }
/*    */       
/* 58 */       return SoundHandler.missing_sound;
/*    */     }
/*    */     
/*    */ 
/* 62 */     return SoundHandler.missing_sound;
/*    */   }
/*    */   
/*    */ 
/*    */   public void addSoundToEventPool(ISoundEventAccessor<SoundPoolEntry> sound)
/*    */   {
/* 68 */     this.soundPool.add(sound);
/*    */   }
/*    */   
/*    */   public ResourceLocation getSoundEventLocation()
/*    */   {
/* 73 */     return this.soundLocation;
/*    */   }
/*    */   
/*    */   public SoundCategory getSoundCategory()
/*    */   {
/* 78 */     return this.category;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundEventAccessorComposite.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */