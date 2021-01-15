/*    */ package optfine;
/*    */ 
/*    */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*    */ import net.minecraft.profiler.Profiler;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.server.management.ServerConfigurationManager;
/*    */ import net.minecraft.world.WorldServer;
/*    */ import net.minecraft.world.storage.WorldInfo;
/*    */ 
/*    */ public class WorldServerOF extends WorldServer
/*    */ {
/*    */   private MinecraftServer mcServer;
/*    */   
/*    */   public WorldServerOF(MinecraftServer p_i64_1_, net.minecraft.world.storage.ISaveHandler p_i64_2_, WorldInfo p_i64_3_, int p_i64_4_, Profiler p_i64_5_)
/*    */   {
/* 16 */     super(p_i64_1_, p_i64_2_, p_i64_3_, p_i64_4_, p_i64_5_);
/* 17 */     this.mcServer = p_i64_1_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void tick()
/*    */   {
/* 25 */     super.tick();
/*    */     
/* 27 */     if (!Config.isTimeDefault())
/*    */     {
/* 29 */       fixWorldTime();
/*    */     }
/*    */     
/* 32 */     if (Config.waterOpacityChanged)
/*    */     {
/* 34 */       Config.waterOpacityChanged = false;
/* 35 */       ClearWater.updateWaterOpacity(Config.getGameSettings(), this);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void updateWeather()
/*    */   {
/* 44 */     if (!Config.isWeatherEnabled())
/*    */     {
/* 46 */       fixWorldWeather();
/*    */     }
/*    */     
/* 49 */     super.updateWeather();
/*    */   }
/*    */   
/*    */   private void fixWorldWeather()
/*    */   {
/* 54 */     if ((this.worldInfo.isRaining()) || (this.worldInfo.isThundering()))
/*    */     {
/* 56 */       this.worldInfo.setRainTime(0);
/* 57 */       this.worldInfo.setRaining(false);
/* 58 */       setRainStrength(0.0F);
/* 59 */       this.worldInfo.setThunderTime(0);
/* 60 */       this.worldInfo.setThundering(false);
/* 61 */       setThunderStrength(0.0F);
/* 62 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(2, 0.0F));
/* 63 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(7, 0.0F));
/* 64 */       this.mcServer.getConfigurationManager().sendPacketToAllPlayers(new S2BPacketChangeGameState(8, 0.0F));
/*    */     }
/*    */   }
/*    */   
/*    */   private void fixWorldTime()
/*    */   {
/* 70 */     if (this.worldInfo.getGameType().getID() == 1)
/*    */     {
/* 72 */       long i = getWorldTime();
/* 73 */       long j = i % 24000L;
/*    */       
/* 75 */       if (Config.isTimeDayOnly())
/*    */       {
/* 77 */         if (j <= 1000L)
/*    */         {
/* 79 */           setWorldTime(i - j + 1001L);
/*    */         }
/*    */         
/* 82 */         if (j >= 11000L)
/*    */         {
/* 84 */           setWorldTime(i - j + 24001L);
/*    */         }
/*    */       }
/*    */       
/* 88 */       if (Config.isTimeNightOnly())
/*    */       {
/* 90 */         if (j <= 14000L)
/*    */         {
/* 92 */           setWorldTime(i - j + 14001L);
/*    */         }
/*    */         
/* 95 */         if (j >= 22000L)
/*    */         {
/* 97 */           setWorldTime(i - j + 24000L + 14001L);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\WorldServerOF.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */