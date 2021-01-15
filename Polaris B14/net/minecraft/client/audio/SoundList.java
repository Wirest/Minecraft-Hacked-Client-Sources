/*     */ package net.minecraft.client.audio;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SoundList
/*     */ {
/*   8 */   private final List<SoundEntry> soundList = Lists.newArrayList();
/*     */   
/*     */ 
/*     */   private boolean replaceExisting;
/*     */   
/*     */   private SoundCategory category;
/*     */   
/*     */ 
/*     */   public List<SoundEntry> getSoundList()
/*     */   {
/*  18 */     return this.soundList;
/*     */   }
/*     */   
/*     */   public boolean canReplaceExisting()
/*     */   {
/*  23 */     return this.replaceExisting;
/*     */   }
/*     */   
/*     */   public void setReplaceExisting(boolean p_148572_1_)
/*     */   {
/*  28 */     this.replaceExisting = p_148572_1_;
/*     */   }
/*     */   
/*     */   public SoundCategory getSoundCategory()
/*     */   {
/*  33 */     return this.category;
/*     */   }
/*     */   
/*     */   public void setSoundCategory(SoundCategory soundCat)
/*     */   {
/*  38 */     this.category = soundCat;
/*     */   }
/*     */   
/*     */   public static class SoundEntry
/*     */   {
/*     */     private String name;
/*  44 */     private float volume = 1.0F;
/*  45 */     private float pitch = 1.0F;
/*  46 */     private int weight = 1;
/*  47 */     private Type type = Type.FILE;
/*  48 */     private boolean streaming = false;
/*     */     
/*     */     public String getSoundEntryName()
/*     */     {
/*  52 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setSoundEntryName(String nameIn)
/*     */     {
/*  57 */       this.name = nameIn;
/*     */     }
/*     */     
/*     */     public float getSoundEntryVolume()
/*     */     {
/*  62 */       return this.volume;
/*     */     }
/*     */     
/*     */     public void setSoundEntryVolume(float volumeIn)
/*     */     {
/*  67 */       this.volume = volumeIn;
/*     */     }
/*     */     
/*     */     public float getSoundEntryPitch()
/*     */     {
/*  72 */       return this.pitch;
/*     */     }
/*     */     
/*     */     public void setSoundEntryPitch(float pitchIn)
/*     */     {
/*  77 */       this.pitch = pitchIn;
/*     */     }
/*     */     
/*     */     public int getSoundEntryWeight()
/*     */     {
/*  82 */       return this.weight;
/*     */     }
/*     */     
/*     */     public void setSoundEntryWeight(int weightIn)
/*     */     {
/*  87 */       this.weight = weightIn;
/*     */     }
/*     */     
/*     */     public Type getSoundEntryType()
/*     */     {
/*  92 */       return this.type;
/*     */     }
/*     */     
/*     */     public void setSoundEntryType(Type typeIn)
/*     */     {
/*  97 */       this.type = typeIn;
/*     */     }
/*     */     
/*     */     public boolean isStreaming()
/*     */     {
/* 102 */       return this.streaming;
/*     */     }
/*     */     
/*     */     public void setStreaming(boolean isStreaming)
/*     */     {
/* 107 */       this.streaming = isStreaming;
/*     */     }
/*     */     
/*     */     public static enum Type
/*     */     {
/* 112 */       FILE("file"), 
/* 113 */       SOUND_EVENT("event");
/*     */       
/*     */       private final String field_148583_c;
/*     */       
/*     */       private Type(String p_i45109_3_)
/*     */       {
/* 119 */         this.field_148583_c = p_i45109_3_;
/*     */       }
/*     */       
/*     */       public static Type getType(String p_148580_0_) {
/*     */         Type[] arrayOfType;
/* 124 */         int j = (arrayOfType = values()).length; for (int i = 0; i < j; i++) { Type soundlist$soundentry$type = arrayOfType[i];
/*     */           
/* 126 */           if (soundlist$soundentry$type.field_148583_c.equals(p_148580_0_))
/*     */           {
/* 128 */             return soundlist$soundentry$type;
/*     */           }
/*     */         }
/*     */         
/* 132 */         return null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */