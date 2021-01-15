/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.ITickable;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class MusicTicker implements ITickable
/*    */ {
/* 11 */   private final Random rand = new Random();
/*    */   private final Minecraft mc;
/*    */   private ISound currentMusic;
/* 14 */   private int timeUntilNextMusic = 100;
/*    */   
/*    */   public MusicTicker(Minecraft mcIn)
/*    */   {
/* 18 */     this.mc = mcIn;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void update()
/*    */   {
/* 26 */     MusicType musicticker$musictype = this.mc.getAmbientMusicType();
/*    */     
/* 28 */     if (this.currentMusic != null)
/*    */     {
/* 30 */       if (!musicticker$musictype.getMusicLocation().equals(this.currentMusic.getSoundLocation()))
/*    */       {
/* 32 */         this.mc.getSoundHandler().stopSound(this.currentMusic);
/* 33 */         this.timeUntilNextMusic = MathHelper.getRandomIntegerInRange(this.rand, 0, musicticker$musictype.getMinDelay() / 2);
/*    */       }
/*    */       
/* 36 */       if (!this.mc.getSoundHandler().isSoundPlaying(this.currentMusic))
/*    */       {
/* 38 */         this.currentMusic = null;
/* 39 */         this.timeUntilNextMusic = Math.min(MathHelper.getRandomIntegerInRange(this.rand, musicticker$musictype.getMinDelay(), musicticker$musictype.getMaxDelay()), this.timeUntilNextMusic);
/*    */       }
/*    */     }
/*    */     
/* 43 */     if ((this.currentMusic == null) && (this.timeUntilNextMusic-- <= 0))
/*    */     {
/* 45 */       func_181558_a(musicticker$musictype);
/*    */     }
/*    */   }
/*    */   
/*    */   public void func_181558_a(MusicType p_181558_1_)
/*    */   {
/* 51 */     this.currentMusic = PositionedSoundRecord.create(p_181558_1_.getMusicLocation());
/* 52 */     this.mc.getSoundHandler().playSound(this.currentMusic);
/* 53 */     this.timeUntilNextMusic = Integer.MAX_VALUE;
/*    */   }
/*    */   
/*    */   public void func_181557_a()
/*    */   {
/* 58 */     if (this.currentMusic != null)
/*    */     {
/* 60 */       this.mc.getSoundHandler().stopSound(this.currentMusic);
/* 61 */       this.currentMusic = null;
/* 62 */       this.timeUntilNextMusic = 0;
/*    */     }
/*    */   }
/*    */   
/*    */   public static enum MusicType
/*    */   {
/* 68 */     MENU(new ResourceLocation("minecraft:music.menu"), 20, 600), 
/* 69 */     GAME(new ResourceLocation("minecraft:music.game"), 12000, 24000), 
/* 70 */     CREATIVE(new ResourceLocation("minecraft:music.game.creative"), 1200, 3600), 
/* 71 */     CREDITS(new ResourceLocation("minecraft:music.game.end.credits"), Integer.MAX_VALUE, Integer.MAX_VALUE), 
/* 72 */     NETHER(new ResourceLocation("minecraft:music.game.nether"), 1200, 3600), 
/* 73 */     END_BOSS(new ResourceLocation("minecraft:music.game.end.dragon"), 0, 0), 
/* 74 */     END(new ResourceLocation("minecraft:music.game.end"), 6000, 24000);
/*    */     
/*    */     private final ResourceLocation musicLocation;
/*    */     private final int minDelay;
/*    */     private final int maxDelay;
/*    */     
/*    */     private MusicType(ResourceLocation location, int minDelayIn, int maxDelayIn)
/*    */     {
/* 82 */       this.musicLocation = location;
/* 83 */       this.minDelay = minDelayIn;
/* 84 */       this.maxDelay = maxDelayIn;
/*    */     }
/*    */     
/*    */     public ResourceLocation getMusicLocation()
/*    */     {
/* 89 */       return this.musicLocation;
/*    */     }
/*    */     
/*    */     public int getMinDelay()
/*    */     {
/* 94 */       return this.minDelay;
/*    */     }
/*    */     
/*    */     public int getMaxDelay()
/*    */     {
/* 99 */       return this.maxDelay;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\MusicTicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */